package com.home.email.controller.api;

import com.home.email.model.dto.ApiErrorResponse;
import com.home.email.model.dto.EmailCodeRequest;
import com.home.email.model.exception.EmailSendingException;
import com.home.email.model.exception.OTPStoringException;
import com.home.email.model.exception.OTPVerificationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface MFAApi {
  @Operation(summary = "Verify a code and email.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Code and Email verified!",
          content = @Content(mediaType = "text/plain;charset=UTF-8",
              examples = @ExampleObject(value = "MFA code verified successfully!"))),
      @ApiResponse(responseCode = "401", description = "Unauthorized! Invalid code or email",
          content = {
              @Content(mediaType = "application/json",
                  schema = @Schema(implementation = ApiErrorResponse.class)) }),
      @ApiResponse(responseCode = "500", description = "Internal server error! Error while verifying code",
          content = {
              @Content(mediaType = "application/json",
                  schema = @Schema(implementation = ApiErrorResponse.class)) })
  })
  ResponseEntity<?> verifyMfaCode(
      @Parameter(name = "email", description = "an email", example = "user@mail.com")
      @Valid @Email @PathVariable String email,
      @Parameter(name = "code", description = "an otp code", example = "551262")
      @Valid @PathVariable String code) throws OTPVerificationException;

  @Operation(summary = "Email a new OTP code to a given email.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Email was sent !",
          content = @Content(mediaType = "text/plain;charset=UTF-8",
              examples = @ExampleObject(value = "Email with MFA code sent successfully!"))),
      @ApiResponse(responseCode = "500", description = "Internal server error! Error while sending email, or storing OTP.",
          content = {
              @Content(mediaType = "application/json",
                  schema = @Schema(implementation = ApiErrorResponse.class)) }),
  })
  ResponseEntity<?> emailCode(@Valid @RequestBody EmailCodeRequest request)
      throws EmailSendingException, OTPStoringException;

}
