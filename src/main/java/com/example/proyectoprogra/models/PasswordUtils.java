package com.example.proyectoprogra.models;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean verifyPassword(String password, String hashedPassword) {
        if (password == null || hashedPassword == null) return false;
        try {
            String normalized = hashedPassword;
            if (normalized.startsWith("$2y$")) {
                normalized = "$2a$" + normalized.substring(4);
            }
            return BCrypt.checkpw(password, normalized);
        } catch (IllegalArgumentException e) {
            return hashedPassword.equals(password);
        } catch (Exception e) {
            return false;
        }
    }
}
