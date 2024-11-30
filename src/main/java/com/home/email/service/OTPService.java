package com.home.email.service;

import com.bastiaanjansen.otp.HMACAlgorithm;
import com.bastiaanjansen.otp.TOTPGenerator;
import com.home.email.model.domain.OTPContext;
import com.home.email.model.exception.OTPStoringException;
import com.home.email.model.exception.OTPVerificationException;
import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OTPService {
  private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
  private final String OTP_KEY = "OTP:%s";
  private final String GENERATION_CODE_OK = "Generated a code %s for email %s";
  private final String VERIFICATION_CODE_OK = "Code %s for email %s, verified !!";


  @Value("OTP.secret")
  private String secret;

  private final RedisTemplate<String, Object> redisTemplate;
  private TOTPGenerator totp;

  @PostConstruct
  public void initTotp() {
    this.totp = new TOTPGenerator.Builder(secret.getBytes())
        .withHOTPGenerator(builder -> {
          builder.withPasswordLength(6);
          builder.withAlgorithm(HMACAlgorithm.SHA1);
        })
        .withPeriod(Duration.ofSeconds(300))
        .build();
  }

  public OTPContext generateOTP(String email) throws OTPStoringException {
    OTPContext otp = new OTPContext(email, totp.now());
    storeOTP(email, otp);
    logger.info(String.format(GENERATION_CODE_OK, otp.getCode(), otp.getEmail()));
    return otp;
  }


  public boolean verifyOTP(OTPContext otp) throws OTPVerificationException {
    try {
      String key = String.format(OTP_KEY, otp.getEmail());
      OTPContext storedOTP = (OTPContext) redisTemplate.opsForValue().get(key);

      if (Objects.isNull(storedOTP)) {
        logger.info(otp + " was not found !!");
        return false;
      }

      if (!storedOTP.getCode().equals(otp.getCode()) || !storedOTP.getEmail().equals(otp.getEmail())) {
        logger.info(otp + " is not equal !!");
        return false;
      }

      if (!totp.verify(otp.getCode())) {
        logger.info(otp + "is invalid !!");
        return false;
      }

      redisTemplate.delete(key);
      logger.info(String.format(VERIFICATION_CODE_OK, otp.getCode(), otp.getEmail()));
      return true;
    } catch (Exception e) {
      throw new OTPVerificationException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occured while trying to validate the code!");
    }
  }

  private void storeOTP(String email, OTPContext otp) throws OTPStoringException {
    try {
      redisTemplate.opsForValue().set(String.format(OTP_KEY, email), otp, 5, TimeUnit.MINUTES);
    } catch (Exception e) {
      e.printStackTrace();
      throw new OTPStoringException("An error occured while trying to create the code!");
    }
  }

}
