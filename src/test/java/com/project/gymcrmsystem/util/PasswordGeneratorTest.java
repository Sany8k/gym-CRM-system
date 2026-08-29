package com.project.gymcrmsystem.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordGeneratorTest {

  private static final String ALLOWED_SYMBOLS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
      + "abcdefghijklmnopqrstuvwxyz"
      + "0123456789!@#$%^&*()_+-=[]{}|;:,.<>?";

  private final PasswordGenerator passwordGenerator = new PasswordGenerator();

  @Test
  void shouldGeneratePasswordWithExpectedLengthAndAllowedSymbols() {
    String password = passwordGenerator.generateRandomPassword();

    assertNotNull(password);
    assertEquals(10, password.length());
    assertTrue(password.chars().allMatch(symbol -> ALLOWED_SYMBOLS.indexOf(symbol) >= 0));
  }

  @Test
  void shouldNotGenerateEmptyOrInvalidPassword() {
    String password = passwordGenerator.generateRandomPassword();

    assertFalse(password.isEmpty());
    assertFalse(password.chars().anyMatch(symbol -> ALLOWED_SYMBOLS.indexOf(symbol) < 0));
  }
}
