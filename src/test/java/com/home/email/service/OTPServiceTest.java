package com.home.email.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.home.email.model.domain.OTPContext;
import com.home.email.model.exception.OTPStoringException;
import com.home.email.model.exception.OTPVerificationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class OTPServiceTest {

  private final String TEST_EMAIL = "test@example.com";

  @Mock
  private RedisTemplate<String, Object> redisTemplate;

  @Mock
  private ValueOperations<String, Object> valueOperations;

  private OTPService otpService;

  @BeforeEach
  public void setup() {
    otpService = new OTPService(redisTemplate);
    ReflectionTestUtils.setField(otpService, "secret", "testsecret");
    otpService.initTotp();

    when(redisTemplate.opsForValue()).thenReturn(valueOperations);
  }

  @Test
  void generateOTP_shouldBeOk() throws OTPStoringException {
    //Arrange
    doNothing().when(valueOperations).set(anyString(), any(), anyLong(), any());
    //Act
    OTPContext context = otpService.generateOTP(TEST_EMAIL);
    //Assert
    assertEquals(context.getEmail(), TEST_EMAIL);
    assertNotNull(context.getCode());
  }

  @Test
  void generateOTP_shouldThrowException_whenRedisFails() {
    //Arrange
    doThrow(RuntimeException.class).when(valueOperations).set(anyString(), any(), anyLong(), any());
    //Act
    OTPStoringException exception = assertThrows(OTPStoringException.class, () -> otpService.generateOTP(TEST_EMAIL));
    //Assert
    assertEquals("An error occured while trying to create the code!", exception.getMessage());
  }

  @Test
  void verifyOTP_shouldBeTrue() throws OTPVerificationException, OTPStoringException {
    //Arrange
    doNothing().when(valueOperations).set(anyString(), any(), anyLong(), any());
    OTPContext otp = otpService.generateOTP(TEST_EMAIL);
    when(valueOperations.get(any())).thenReturn(otp);
    //Act
    boolean result = otpService.verifyOTP(otp);
    //Assert
    assertTrue(result);
  }

  @Test
  void verifyOTP_shouldBeFalse_whenNotFoundInRedis() throws OTPVerificationException, OTPStoringException {
    //Arrange
    doNothing().when(valueOperations).set(anyString(), any(), anyLong(), any());
    OTPContext otp = otpService.generateOTP(TEST_EMAIL);
    when(valueOperations.get(any())).thenReturn(null);
    //Act
    boolean result = otpService.verifyOTP(otp);
    //Assert
    assertFalse(result);
  }

  @Test
  void verifyOTP_shouldBeFalse_whenCodeIsInvalid() throws OTPVerificationException, OTPStoringException {
    //Arrange
    doNothing().when(valueOperations).set(anyString(), any(), anyLong(), any());
    OTPContext otp = otpService.generateOTP(TEST_EMAIL);
    when(valueOperations.get(any())).thenReturn(new OTPContext(TEST_EMAIL, "111222"));
    //Act
    boolean result = otpService.verifyOTP(otp);
    //Assert
    assertFalse(result);
  }

  @Test
  void verifyOTP_shouldBeFalse_whenEmailIsInvalid() throws OTPVerificationException, OTPStoringException {
    //Arrange
    doNothing().when(valueOperations).set(anyString(), any(), anyLong(), any());
    OTPContext otp = otpService.generateOTP(TEST_EMAIL);
    when(valueOperations.get(any())).thenReturn(new OTPContext("another@email.com", otp.getCode()));
    //Act
    boolean result = otpService.verifyOTP(otp);
    //Assert
    assertFalse(result);
  }

  @Test
  void verifyOTP_shouldThrowException_whenRedisFails() throws OTPStoringException {
    //Arrange
    doNothing().when(valueOperations).set(anyString(), any(), anyLong(), any());
    OTPContext otp = otpService.generateOTP(TEST_EMAIL);
    doThrow(RuntimeException.class).when(valueOperations).get(any());
    //Act
    OTPVerificationException exception = assertThrows(OTPVerificationException.class, () -> otpService.verifyOTP(otp));
    //Assert
    assertEquals("An error occured while trying to validate the code!", exception.getMessage());
  }
}
