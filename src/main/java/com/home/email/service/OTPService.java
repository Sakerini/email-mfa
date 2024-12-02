package com.home.email.service;

import com.bastiaanjansen.otp.TOTPGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.email.model.domain.OTPContext;
import com.home.email.model.exception.OTPStoringException;
import com.home.email.model.exception.OTPVerificationException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OTPService {
  private final String OTP_KEY = "OTP:%s";
  private final RedisTemplate<String, Object> redisTemplate;
  private final TOTPGenerator totp;

  private final ObjectMapper objectMapper = new ObjectMapper();

  public OTPContext generateOTP(final String email) throws OTPStoringException {

    final OTPContext otp = new OTPContext(email, totp.now());
    storeOTP(email, otp);
    return otp;
  }

  public boolean verifyOTP(final OTPContext otp) throws OTPVerificationException {

    try {

      final String key = String.format(OTP_KEY, otp.email());
      final Object storedValue = redisTemplate.opsForValue().get(key);
      final OTPContext storedOTP = objectMapper.convertValue(storedValue, OTPContext.class);

      if (Objects.isNull(storedOTP)) {

        return false;
      }

      if (!storedOTP.code().equals(otp.code()) || !storedOTP.email().equals(otp.email())) {

        return false;
      }

      if (!totp.verify(otp.code())) {

        return false;
      }

      redisTemplate.delete(key);

      log.info("Code verified !!");
      return true;
    } catch (final Exception e) {
      log.error("An error occured while trying to validate the code", e);
      throw new OTPVerificationException(
          HttpStatus.INTERNAL_SERVER_ERROR, "An error occured while trying to validate the code!");
    }
  }

  private void storeOTP(final String email, final OTPContext otp) throws OTPStoringException {

    try {

      redisTemplate.opsForValue().set(String.format(OTP_KEY, email), otp, 5, TimeUnit.MINUTES);

      log.info("OTP Generated and stored");
    } catch (final Exception e) {
      log.error("Error occurred while storing the generated OTP", e);
      throw new OTPStoringException("An error occured while trying to create the code!");
    }
  }
}
