package com.home.email.model.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MFAEmail {
  private final String email;
  private final String subject;
  private final String context;
}
