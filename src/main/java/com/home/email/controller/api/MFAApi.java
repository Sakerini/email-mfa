package com.home.email.controller.api;

import com.home.email.model.dto.ApiErrorResponse;
import com.home.email.model.dto.EmailCodeRequest;
import com.home.email.model.dto.VerifyCodeRequest;
import com.home.email.model.exception.EmailSendingException;
import com.home.email.model.exception.OTPStoringException;
import com.home.email.model.exception.OTPVerificationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface MFAApi {

  @Operation(summary = "Email a new OTP code to a given email.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Email was sent !"),
        @ApiResponse(
            responseCode = "400",
            description = "Bad request",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ApiErrorResponse.class))
            }),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error! Error while sending email, or storing OTP.",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ApiErrorResponse.class))
            }),
      })
  ResponseEntity<?> emailCode(@Valid @RequestBody EmailCodeRequest request)
      throws EmailSendingException, OTPStoringException;

  @Operation(summary = "Verify a code and email.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Code and Email verified!"),
        @ApiResponse(
            responseCode = "400",
            description = "Bad request",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ApiErrorResponse.class))
            }),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized! Invalid code or email",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ApiErrorResponse.class))
            }),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error! Error while verifying code",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ApiErrorResponse.class))
            })
      })
  ResponseEntity<?> verifyMfaCode(@Valid @RequestBody VerifyCodeRequest request)
      throws OTPVerificationException;
}
