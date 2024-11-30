package com.home.email.model.domain;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OTPContext implements Serializable {
  private String email;
  private String code;

  @Override
  public String toString() {
    return "OTPContext{" +
        "email='" + email + '\'' +
        ", code='" + code + '\'' +
        '}';
  }
}
