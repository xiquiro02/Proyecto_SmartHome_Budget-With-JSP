package com.smarthome.smarthome_budget.utils;

import org.mindrot.jbcrypt.BCrypt;

/* Clase: Encriptador
   Propósito: Proveer métodos estáticos de utilidad para encriptar y verificar contraseñas
   usando el algoritmo BCrypt con factor de costo 12. Se usa en UsuarioDao al registrar
   un usuario y al cambiar su contraseña, y en el proceso de login para comparar la clave
   ingresada contra el hash almacenado. Incluye compatibilidad con contraseñas en texto plano
   insertadas manualmente en la base de datos durante pruebas o migraciones iniciales.
*/
public class Encriptador {

    /* Método: encriptar
       Propósito: Generar un hash BCrypt de la contraseña proporcionada usando un salt aleatorio
       con factor de costo 12. El resultado siempre comienza con "$2a$12$" y es distinto
       cada vez aunque la contraseña sea la misma, lo que impide ataques por tablas arcoíris.
       @param password → Texto con la contraseña en texto plano a encriptar
       @return String → Hash BCrypt de la contraseña listo para almacenar en la base de datos
    */
    public static String encriptar(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    /* Método: verificar
       Propósito: Comparar una contraseña en texto plano contra un hash almacenado. Si el hash
       comienza con "$2a$" se usa BCrypt.checkpw para la comparación segura; de lo contrario
       se compara directamente como texto plano para mantener compatibilidad con datos insertados
       manualmente durante pruebas o migraciones iniciales sin encriptación.
       @param password → Texto con la contraseña en texto plano ingresada por el usuario
       @param hash     → Texto con el hash almacenado en la base de datos (BCrypt o texto plano)
       @return boolean → true si la contraseña coincide con el hash, false si no coincide o hay error
    */
    public static boolean verificar(String password, String hash) {
        try {
            // Si el hash almacenado es BCrypt (empieza con $2a$), se usa la verificación segura
            if (hash != null && hash.startsWith("$2a$")) {
                return BCrypt.checkpw(password, hash);
            } else {
                // Si no es BCrypt (texto plano de inserts manuales), se compara directamente
                return password.equals(hash);
            }
        } catch (Exception e) {
            System.err.println("[Encriptador] Error verificando contraseña: " + e.getMessage());
            return false;
        }
    }
}
