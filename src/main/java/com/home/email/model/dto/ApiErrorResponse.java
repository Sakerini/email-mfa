package com.home.email.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ApiErrorResponse {
  @Schema(name = "code", example = "code", required = true)
  private HttpStatus code;
  @Schema(name = "message", example = "message", required = true)
  private String message;
}
