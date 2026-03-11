package com.smarthome.smarthome_budget.controlador;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailService {
    
    private static final String REMITENTE = "ximenaquiro02@gmail.com";
    private static final String CLAVE_APP = "mfyy bive ufeo tujc";
    
    public boolean enviarLinkRecuperacion(String destinatario, String token, String contextPath) {
        
        if (destinatario == null || destinatario.trim().isEmpty() || token == null || token.trim().isEmpty()) {
            return false;
        }

        if (CLAVE_APP.equals("tu_clave_app_aqui") || CLAVE_APP.trim().isEmpty()) {
            System.err.println("ERROR: Debes configurar tu clave de aplicación de Gmail en EmailService.java");
            return false;
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        try {
            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(REMITENTE, CLAVE_APP);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(REMITENTE));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject("Restablecer Contraseña - SmartHome Budget");

            String link = "http://localhost:8080" + contextPath + "/NuevaClave?token=" + token;
            
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
            
            message.setContent(contenidoHtml, "text/html; charset=utf-8");
            
            Transport.send(message);
            return true;
            
        } catch (AddressException e) {
            System.err.println("Error: Dirección de correo inválida");
            return false;
        } catch (AuthenticationFailedException e) {
            System.err.println("Error: Autenticación fallida - Verifica tu clave de aplicación de Gmail");
            return false;
        } catch (SendFailedException e) {
            System.err.println("Error: Fallo al enviar correo");
            return false;
        } catch (MessagingException e) {
            System.err.println("Error: Error general de mensajería");
            return false;
        } catch (Exception e) {
            System.err.println("Error: Error inesperado al enviar correo");
            return false;
        }
    }
}
