package com.home.email.service;

import com.home.email.model.domain.MFAEmail;
import com.home.email.model.domain.OTPContext;
import com.home.email.model.exception.EmailSendingException;
import com.home.email.model.exception.OTPStoringException;
import com.home.email.model.exception.OTPVerificationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MFAService {
  private final String EMAIL_SUBJECT = "MFA AUTHORIZATION";
  private final String EMAIL_MSG = "This is yours code: %s";

  private final OTPService otpService;
  private final EmailService emailService;

  public void emailCode(final String email) throws OTPStoringException, EmailSendingException {
    final OTPContext otp = otpService.generateOTP(email);
    emailService.sendEmail(
        new MFAEmail(email, EMAIL_SUBJECT, String.format(EMAIL_MSG, otp.code())));
  }

  public void verifyEmailedCode(final String email, final String code)
      throws OTPVerificationException {
    final boolean isVerified = otpService.verifyOTP(new OTPContext(email, code));

    if (!isVerified) {
      throw new OTPVerificationException(HttpStatus.UNAUTHORIZED, "Access denied: Invalid code");
    }
  }
}
