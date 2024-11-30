package com.home.email.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.home.email.model.domain.MFAEmail;
import com.home.email.model.exception.EmailSendingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

  @Mock
  private JavaMailSender javaMailSender;

  @InjectMocks
  private EmailService emailService;

  private MFAEmail constructExampleEmail() {
    return new MFAEmail("test@example.com","Test Subject", "Test Body");

  }

  @Test
  void sendEmail_shouldBeOk() throws EmailSendingException {
    //Arrange:
    doNothing().when(javaMailSender).send(ArgumentMatchers.isA(SimpleMailMessage.class));
    //Act
    emailService.sendEmail(constructExampleEmail());
    //Assert
    verify(javaMailSender, times(1)).send(ArgumentMatchers.isA(SimpleMailMessage.class));
  }

  @Test
  void sendEmail_shouldThrowException_whenEmailNotProvided() {
    //Act
    EmailSendingException exception = assertThrows(EmailSendingException.class, () -> {
      emailService.sendEmail(null);
    });
    //Assert
    assertEquals("Email not provided", exception.getMessage());
  }

  @Test
  void sendEmail_shouldThrowException_SmtpGivesError() {
    //Arrange
    doThrow(new MailException("SMTP server error") {}).when(javaMailSender).send(ArgumentMatchers.isA(SimpleMailMessage.class));

    //Act
    EmailSendingException exception = assertThrows(EmailSendingException.class, () -> {
      emailService.sendEmail(constructExampleEmail());
    });
    //Assert
    assertEquals("There was error while trying to send an Email", exception.getMessage());
  }

}
