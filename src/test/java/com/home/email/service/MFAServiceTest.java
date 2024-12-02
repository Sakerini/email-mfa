package com.home.email.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.home.email.model.domain.OTPContext;
import com.home.email.model.exception.EmailSendingException;
import com.home.email.model.exception.OTPStoringException;
import com.home.email.model.exception.OTPVerificationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
public class MFAServiceTest {

  private final String TEST_EMAIL = "test@example.com";
  private final String TEST_CODE = "555111";

  @Mock private EmailService emailService;
  @Mock private OTPService otpService;
  @InjectMocks private MFAService mfaService;

  private OTPContext constructOtpContext() {
    return new OTPContext(TEST_EMAIL, TEST_CODE);
  }

  @Test
  void emailCode_shouldBeOk() throws OTPStoringException, EmailSendingException {
    // Arrange
    doReturn(constructOtpContext()).when(otpService).generateOTP(anyString());
    doNothing().when(emailService).sendEmail(any());
    // Act
    mfaService.emailCode(TEST_EMAIL);
    // Assert
    verify(otpService, times(1)).generateOTP(anyString());
    verify(emailService, times(1)).sendEmail(any());
  }

  @Test
  void verifyEmailedCode_shouldBeOk() throws OTPVerificationException {
    // Arrange
    doReturn(true).when(otpService).verifyOTP(any());
    // Act
    mfaService.verifyEmailedCode(TEST_EMAIL, TEST_CODE);
    // Assert
    verify(otpService, times(1)).verifyOTP(any());
  }

  @Test
  void verifyEmailedCode_shouldBeInvalidCode() throws OTPVerificationException {
    // Arrange
    doReturn(false).when(otpService).verifyOTP(any());
    // Act
    final OTPVerificationException exception =
        assertThrows(
            OTPVerificationException.class,
            () -> mfaService.verifyEmailedCode(TEST_EMAIL, TEST_CODE));

    // Assert
    assertEquals("Access denied: Invalid code", exception.getMessage());
    assertEquals(HttpStatus.UNAUTHORIZED, exception.getCode());
  }
}
