package com.smarthome.smarthome_budget.controlador;
// Importación de librerías necesarias para el envío de correos electrónicos
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

/*Clase: EmailService
 * Propósito:
 * Gestionar el envío de correos electrónicos desde la aplicación,
 * específicamente para el proceso de recuperación de contraseña.
 */

public class EmailService {
    
    // Dirección de correo desde la cual se enviarán los mensajes
    private static final String REMITENTE = "ximenaquiro02@gmail.com";
    // Clave de aplicación generada en Gmail (no es la contraseña normal)
    private static final String CLAVE_APP = "mfyy bive ufeo tujc";
    
     /*Método: enviarLinkRecuperacion
     * Propósito:
     * Enviar un correo electrónico al usuario con un enlace para restablecer su contraseña.
     * @param destinatario Correo electrónico del usuario que recibirá el mensaje
     * @param token Código único de seguridad para validar la solicitud
     * @param contextPath Ruta base de la aplicación web
     * @return boolean Retorna true si el correo fue enviado correctamente, false si ocurrió algún error
     */
    
    public boolean enviarLinkRecuperacion(String destinatario, String token, String contextPath) {
        
        // Verifica que el correo y el token no sean nulos ni estén vacíos
        if (destinatario == null || destinatario.trim().isEmpty() || token == null || token.trim().isEmpty()) {
            return false;
        }

        // Verifica que el correo y el token no sean nulos ni estén vacíos
        if (CLAVE_APP.equals("tu_clave_app_aqui") || CLAVE_APP.trim().isEmpty()) {
            System.err.println("ERROR: Debes configurar tu clave de aplicación de Gmail en EmailService.java");
            return false;
        }

         // Se definen las propiedades necesarias para conectarse al servidor de Gmail
        Properties props = new Properties();
        // Habilita autenticación
        props.put("mail.smtp.auth", "true");
        // Habilita el uso de TLS (seguridad en la conexión)
        props.put("mail.smtp.starttls.enable", "true");
        // Servidor SMTP de Gmail
        props.put("mail.smtp.host", "smtp.gmail.com");
        // Puerto utilizado por Gmail para envío de correos
        props.put("mail.smtp.port", "587");
        // Configuración de confianza SSL
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        // Protocolo de seguridad utilizado
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        try {
            // Se crea una sesión con autenticación usando el correo y la clave de aplicación.
            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(REMITENTE, CLAVE_APP);
                }
            });

            Message message = new MimeMessage(session);
            // Se define el remitente del correo
            message.setFrom(new InternetAddress(REMITENTE));
            // Se define el destinatario
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            // Asunto del correo
            message.setSubject("Restablecer Contraseña - SmartHome Budget");
            // Se genera la URL que el usuario utilizará para cambiar su contraseña.
            String link = "http://localhost:8080" + contextPath + "/NuevaClave?token=" + token;
            // Se construye el cuerpo del mensaje en formato HTML con estilos básicos
            String contenidoHtml = "<div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px;\">" +
                "<h2 style=\"color: #1E88E5;\">Restablecer Contraseña - SmartHome Budget</h2>" +
                "<p>Hola,</p>" +
                "<p>Hemos recibido una solicitud para restablecer tu contraseña. Haz clic en el siguiente enlace:</p>" +
                "<div style=\"text-align: center; margin: 30px 0;\">" +
                "<a href=\"" + link + "\" style=\"background-color: #1E88E5; color: white; padding: 12px 30px; text-decoration: none; border-radius: 5px; display: inline-block;\">" +
                "Restablecer Contraseña" +
                "</a>" +
                "</div>" +
                "<p>O copia y pega este enlace en tu navegador:</p>" +
                "<p style=\"background-color: #f5f5f5; padding: 10px; word-break: break-all;\">" + link + "</p>" +
                "<p style=\"color: #666; font-size: 12px;\">Este enlace expirará en 20 minutos. Si no solicitaste este cambio, ignora este correo.</p>" +
                "</div>";
             // Se asigna el contenido HTML al mensaje con codificación UTF-8
            message.setContent(contenidoHtml, "text/html; charset=utf-8");
            // Se envía el mensaje a través del servidor SMTP configurado
            Transport.send(message);
            // Retorna true indicando que el correo fue enviado correctamente
            return true;
            
        } catch (AddressException e) {
            // Error si la dirección de correo es inválida
            System.err.println("Error: Dirección de correo inválida");
            return false;
        } catch (AuthenticationFailedException e) {
            // Error si fallan las credenciales de autenticación
            System.err.println("Error: Autenticación fallida - Verifica tu clave de aplicación de Gmail");
            return false;
        } catch (SendFailedException e) {
            // Error si no se pudo enviar el correo
            System.err.println("Error: Fallo al enviar correo");
            return false;
        } catch (MessagingException e) {
            // Error general relacionado con el envío de mensajes
            System.err.println("Error: Error general de mensajería");
            return false;
        } catch (Exception e) {
            // Captura cualquier otro error inesperado
            System.err.println("Error: Error inesperado al enviar correo");
            return false;
        }
    }
}
