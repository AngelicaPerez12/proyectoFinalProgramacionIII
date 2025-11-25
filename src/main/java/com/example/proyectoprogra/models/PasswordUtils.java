package com.example.proyectoprogra.models;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean verifyPassword(String password, String hashedPassword) {
        if (password == null || hashedPassword == null) return false;
        try {
            // jBCrypt puede lanzar "Invalid salt version" si el prefijo es $2y$ u otro no soportado;
            // convertir $2y$ a $2a$ (compatible) antes de verificar.
            String normalized = hashedPassword;
            if (normalized.startsWith("$2y$")) {
                normalized = "$2a$" + normalized.substring(4);
            }
            return BCrypt.checkpw(password, normalized);
        } catch (IllegalArgumentException e) {
            // Si BCrypt no acepta el hash, evitar que la UI explote.
            // Como fallback controlado, comparar en texto plano (útil para datos legacy) o
            // retornar false para denegar el acceso. Aquí devolvemos una comparación directa
            // para no bloquear cuentas migradas accidentalmente.
            return hashedPassword.equals(password);
        } catch (Exception e) {
            // Cualquier otro error -> no autenticar.
            return false;
        }
    }
}
