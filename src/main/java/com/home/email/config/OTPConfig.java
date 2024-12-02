package com.home.email.config;

import com.bastiaanjansen.otp.HMACAlgorithm;
import com.bastiaanjansen.otp.TOTPGenerator;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OTPConfig {
  @Bean
  public TOTPGenerator totpGenerator(
      @Value("${OTP.secret}") final String secret,
      @Value("${OTP.digits}") final int digits,
      @Value("${OTP.durationInSeconds}") final int durationInSeconds) {

    return new TOTPGenerator.Builder(secret.getBytes())
        .withHOTPGenerator(
            builder -> {
              builder.withPasswordLength(digits);
              builder.withAlgorithm(HMACAlgorithm.SHA1);
            })
        .withPeriod(Duration.ofSeconds(durationInSeconds))
        .build();
  }
}
