package com.smarthome.smarthome_budget.utils;

import org.mindrot.jbcrypt.BCrypt;

public class Encriptador {

    public static String encriptar(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    public static boolean verificar(String password, String hash) {
        try {
            return BCrypt.checkpw(password, hash);
        } catch (Exception e) {
            return false;
        }
    }
}
