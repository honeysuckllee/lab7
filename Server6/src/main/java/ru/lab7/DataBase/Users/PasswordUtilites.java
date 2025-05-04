package ru.lab7.DataBase.Users;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtilites {
    public static String hash(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encoded = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : encoded) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 не поддерживается", e);
        }
    }
}
