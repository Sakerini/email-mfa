package com.home.email.model.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OTPStoringException extends Exception {
  private String message;
}
