package com.home.email.service;

import com.home.email.model.domain.MFAEmail;
import com.home.email.model.exception.EmailSendingException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
  private final String EMAIL_FROM = "mfa-app@test.com";

  private final JavaMailSender emailSender;

  public void sendEmail(final MFAEmail email) throws EmailSendingException {

    if (Objects.isNull(email)) {

      throw new EmailSendingException("Email not provided");
    }

    final SimpleMailMessage message = new SimpleMailMessage();

    message.setFrom(EMAIL_FROM);
    message.setTo(email.email());
    message.setSubject(email.subject());
    message.setText(email.context());

    doSendEmail(message);
  }

  @Retryable(value = MailException.class, backoff = @Backoff(delay = 1000))
  private void doSendEmail(final SimpleMailMessage mailMessage) throws EmailSendingException {
    log.info("Sending email to {}", mailMessage.getTo());
    try {

      emailSender.send(mailMessage);
    } catch (final MailException e) {
      log.error("Failed to send an email to {}", mailMessage.getTo(), e);
      throw new EmailSendingException("There was error while trying to send an Email");
    }
  }
}
