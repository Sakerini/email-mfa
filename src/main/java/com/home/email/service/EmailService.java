package com.home.email.service;

import com.home.email.model.domain.MFAEmail;
import com.home.email.model.exception.EmailSendingException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
  private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
  private final String EMAIL_FROM = "mfa-app@test.com";
  private final String EMAIL_SENT_OK = "Email sent to %s, successfully !!";
  private final String EMAIL_SENT_FAIL = "Error sending an email to %s !!";

  private final JavaMailSender emailSender;

  public void sendEmail(MFAEmail email) throws EmailSendingException {
    if (Objects.isNull(email)) {
      throw new EmailSendingException("Email not provided");
    }

    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(EMAIL_FROM);
    message.setTo(email.getEmail());
    message.setSubject(email.getSubject());
    message.setText(email.getContext());
    sendEmail(message);
  }

  private void sendEmail(SimpleMailMessage mailMessage) throws EmailSendingException {
    try {
      emailSender.send(mailMessage);
      logger.info(String.format(EMAIL_SENT_OK, mailMessage.getTo()[0]));
    } catch (MailException e) {
      logger.info(String.format(EMAIL_SENT_FAIL, mailMessage.getTo()[0]));
      throw new EmailSendingException("There was error while trying to send an Email");
    }
  }
}
