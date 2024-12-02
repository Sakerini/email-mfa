package com.home.email;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication
public class EmailMfaApplication {

  public static void main(final String[] args) {
    SpringApplication.run(EmailMfaApplication.class, args);
  }
}
