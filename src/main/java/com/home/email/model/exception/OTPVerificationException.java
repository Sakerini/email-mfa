package com.home.email.model.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class OTPVerificationException extends Exception {
  private HttpStatus code;
  private String message;
}
