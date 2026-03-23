package com.smarthome.smarthome_budget.utils;

import org.mindrot.jbcrypt.BCrypt;

public class Encriptador {

    public static String encriptar(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

   public static boolean verificar(String password, String hash) {
        try {
            // Si el hash de la DB empieza con $2a$, es BCrypt
            if (hash != null && hash.startsWith("$2a$")) {
                return BCrypt.checkpw(password, hash);
            } else {
                // Si NO empieza con $2a$, es texto plano (tus inserts manuales)
                // Comparamos directamente
                return password.equals(hash);
            }
        } catch (Exception e) {
            System.err.println("Error verificando: " + e.getMessage());
            return false;
        }
    }
}
