package asu.foe.wagba8805.services;

import androidx.annotation.Nullable;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class HashingService {

  private static final Random RANDOM = new SecureRandom();

  private static String convertToHex(final byte[] messageDigest) {
    BigInteger bigint = new BigInteger(1, messageDigest);
    String hexText = bigint.toString(16);
    while (hexText.length() < 32) {
      hexText = "0".concat(hexText);
    }
    return hexText;
  }

  @Nullable
  public static String createSHA256Hash(String input) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      byte[] messageDigest = md.digest(input.getBytes(StandardCharsets.UTF_8));
      return convertToHex(messageDigest);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static String createSalt() {
    byte[] salt = new byte[16];
    RANDOM.nextBytes(salt);
    return convertToHex(salt);
  }
}
