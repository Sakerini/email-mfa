package com.home.email.controller.exception;

import com.home.email.model.dto.ApiErrorResponse;
import com.home.email.model.exception.EmailSendingException;
import com.home.email.model.exception.OTPStoringException;
import com.home.email.model.exception.OTPVerificationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class MFAAdviceController {

  @ExceptionHandler({
    EmailSendingException.class,
    OTPStoringException.class,
    OTPVerificationException.class,
    MethodArgumentNotValidException.class
  })
  @ResponseBody
  public final ResponseEntity<ApiErrorResponse> handleException(final Exception ex) {
    final HttpHeaders headers = new HttpHeaders();

    if (ex instanceof final EmailSendingException ese) {

      final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
      return handleEmailSendingException(ese, headers, status);

    } else if (ex instanceof final OTPStoringException ose) {

      final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
      return handleOTPStoringException(ose, headers, status);

    } else if (ex instanceof final OTPVerificationException ove) {

      final HttpStatus status = ove.getCode();
      return handleOTPVerificationException(ove, headers, status);

    } else if (ex instanceof final MethodArgumentNotValidException mve) {

      return handleException(mve, headers, HttpStatus.BAD_REQUEST);

    } else {

      return handleException(ex, headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private ResponseEntity<ApiErrorResponse> handleEmailSendingException(
      final EmailSendingException exception, final HttpHeaders headers, final HttpStatus status) {

    return new ResponseEntity<>(createApiErrorResponse(exception), headers, status);
  }

  private ResponseEntity<ApiErrorResponse> handleOTPStoringException(
      final OTPStoringException exception, final HttpHeaders headers, final HttpStatus status) {

    return new ResponseEntity<>(createApiErrorResponse(exception), headers, status);
  }

  private ResponseEntity<ApiErrorResponse> handleOTPVerificationException(
      final OTPVerificationException exception,
      final HttpHeaders headers,
      final HttpStatus status) {

    return new ResponseEntity<>(createApiErrorResponse(exception), headers, status);
  }

  private ResponseEntity<ApiErrorResponse> handleException(
      final Exception exception, final HttpHeaders headers, final HttpStatus status) {

    return new ResponseEntity<>(createApiErrorResponse(exception), headers, status);
  }

  private ApiErrorResponse createApiErrorResponse(final Exception exception) {
    return new ApiErrorResponse(exception.getMessage());
  }
}
