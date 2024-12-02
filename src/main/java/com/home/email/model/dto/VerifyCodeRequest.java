package com.home.email.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record VerifyCodeRequest(
    @Schema(name = "email", example = "user@mail.com", required = true)
        @NotBlank(message = "Email is mandatory")
        @Email(message = "Email should be valid")
        String email,
    @Schema(name = "code", example = "512261", required = true)
        @NotBlank(message = "Code is mandatory")
        String code) {}
