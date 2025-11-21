package com.example.proyectoprogra.models;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    // Encriptar contraseña
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Verificar contraseña
    public static boolean verifyPassword(String password, String hashedPassword) {
        if (password == null || hashedPassword == null) return false;
        return BCrypt.checkpw(password, hashedPassword);
    }
}
