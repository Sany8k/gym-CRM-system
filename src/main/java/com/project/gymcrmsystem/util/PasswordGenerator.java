package com.project.gymcrmsystem.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class PasswordGenerator {

  private static final String PASSWORD_SYMBOLS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
      "abcdefghijklmnopqrstuvwxyz" +
      "0123456789!@#$%^&*()_+-=[]{}|;:,.<>?";
  private static final SecureRandom RANDOM = new SecureRandom();

  public String generateRandomPassword() {
    StringBuilder password = new StringBuilder(10);

    for (int i = 0; i < 10; i++) {
      int index = RANDOM.nextInt(PASSWORD_SYMBOLS.length());
      password.append(PASSWORD_SYMBOLS.charAt(index));
    }

    return password.toString();
  }
}
