package com.home.email.controller;

import com.home.email.controller.api.MFAApi;
import com.home.email.model.dto.EmailCodeRequest;
import com.home.email.model.dto.VerifyCodeRequest;
import com.home.email.model.exception.EmailSendingException;
import com.home.email.model.exception.OTPStoringException;
import com.home.email.model.exception.OTPVerificationException;
import com.home.email.service.MFAService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/mfa")
@RequiredArgsConstructor
public class MFAController implements MFAApi {

  private final MFAService mfaService;

  @PostMapping("/email-code")
  public ResponseEntity<?> emailCode(final EmailCodeRequest request)
      throws EmailSendingException, OTPStoringException {

    mfaService.emailCode(request.email());
    return ResponseEntity.ok().build();
  }

  @PostMapping("/email-code/verify")
  public ResponseEntity<?> verifyMfaCode(final VerifyCodeRequest request)
      throws OTPVerificationException {

    mfaService.verifyEmailedCode(request.email(), request.code());
    return ResponseEntity.ok().build();
  }
}
