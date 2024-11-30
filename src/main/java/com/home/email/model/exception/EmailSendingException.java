package com.home.email.model.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailSendingException extends Exception {
  private String message;
}
