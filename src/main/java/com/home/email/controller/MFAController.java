package com.home.email.controller;


import com.home.email.controller.api.MFAApi;
import com.home.email.model.dto.EmailCodeRequest;
import com.home.email.model.exception.EmailSendingException;
import com.home.email.model.exception.OTPStoringException;
import com.home.email.model.exception.OTPVerificationException;
import com.home.email.service.MFAService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/mfa")
@RequiredArgsConstructor
public class MFAController implements MFAApi {
  private final String SUCCESS_EMAIL_MESSAGE = "Email with MFA code sent successfully!";
  private final String SUCCESS_VERIFY_TOKEN = "MFA code verified successfully!";

  private final MFAService mfaService;

  @PostMapping("/email-code")
  public ResponseEntity<?> emailCode(
      EmailCodeRequest request)
      throws EmailSendingException, OTPStoringException {
    mfaService.emailCode(request.getEmail());
    return ResponseEntity.ok(SUCCESS_EMAIL_MESSAGE);
  }

  @GetMapping("/email-code/verify/{email}/{code}")
  public ResponseEntity<?> verifyMfaCode(
      String email,
      String code) throws OTPVerificationException {
    mfaService.verifyEmailedCode(email, code);
    return ResponseEntity.ok(SUCCESS_VERIFY_TOKEN);
  }
}
