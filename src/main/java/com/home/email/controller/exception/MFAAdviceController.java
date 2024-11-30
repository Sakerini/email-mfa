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

@ControllerAdvice
public class MFAAdviceController {

  @ExceptionHandler({EmailSendingException.class, OTPStoringException.class, OTPVerificationException.class, MethodArgumentNotValidException.class})
  public final ResponseEntity<ApiErrorResponse> handleException(Exception ex) {
    HttpHeaders headers = new HttpHeaders();

    if (ex instanceof EmailSendingException) {

      HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
      EmailSendingException emailSendingException = (EmailSendingException) ex;
      return handleEmailSendingException(emailSendingException, headers, status);

    } else if (ex instanceof OTPStoringException) {

      HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
      OTPStoringException otpStoringException = (OTPStoringException) ex;
      return handleOTPStoringException(otpStoringException, headers, status);

    } else if (ex instanceof OTPVerificationException) {

      OTPVerificationException otpVerificationException = (OTPVerificationException) ex;
      HttpStatus status = otpVerificationException.getCode();
      return handleOTPVerificationException(otpVerificationException, headers, status);

    } else if(ex instanceof MethodArgumentNotValidException) {

      return handleException(ex, headers, HttpStatus.BAD_REQUEST);

    } else {

      return handleException(ex,  headers, HttpStatus.INTERNAL_SERVER_ERROR);

    }
  }

  private ResponseEntity<ApiErrorResponse> handleEmailSendingException(
      EmailSendingException exception,
      HttpHeaders headers,
      HttpStatus status) {

    return new ResponseEntity<>(createApiErrorResponse(exception, status), headers, status);
  }

  private ResponseEntity<ApiErrorResponse> handleOTPStoringException(
      OTPStoringException exception,
      HttpHeaders headers,
      HttpStatus status) {

    return new ResponseEntity<>(createApiErrorResponse(exception, status), headers, status);
  }

  private ResponseEntity<ApiErrorResponse> handleOTPVerificationException(
      OTPVerificationException exception,
      HttpHeaders headers,
      HttpStatus status) {

    return new ResponseEntity<>(createApiErrorResponse(exception, status), headers, status);
  }

  private ResponseEntity<ApiErrorResponse> handleException(
      Exception exception,
      HttpHeaders headers,
      HttpStatus status) {

    return new ResponseEntity<>(createApiErrorResponse(exception, status), headers, status);
  }


  private ApiErrorResponse createApiErrorResponse(Exception exception, HttpStatus status) {
    return new ApiErrorResponse(status,
        exception.getMessage());
  }
}
