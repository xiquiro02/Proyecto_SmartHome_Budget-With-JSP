package com.smarthome.smarthome_budget.controlador;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailService {
    public boolean enviarLinkRecuperacion(String destinatario, String token, String contextPath) {
        System.out.println("=== EMAIL SERVICE DEBUG ===");
        System.out.println("Destinatario: " + destinatario);
        System.out.println("Token: " + token);
        
        final String remitente = "ximenaquiro02@gmail.com";
        final String claveApp = "mfyy bive ufeo tujc"; 
        
        // Validar que la clave no sea el placeholder
        if (claveApp.equals("tu_clave_app_aqui") || claveApp.trim().isEmpty()) {
            System.err.println("ERROR: Debes configurar tu clave de aplicación de Gmail en EmailService.java línea 10");
            return false;
        }
        
        System.out.println("Remitente: " + remitente);
        System.out.println("Clave configurada: " + (claveApp.length() > 0 ? "SÍ" : "NO"));

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.debug", "true");

        System.out.println("Propiedades SMTP configuradas");

        try {
            System.out.println("Creando sesión SMTP...");
            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(remitente, claveApp);
                }
            });
            
            session.setDebug(true);
            System.out.println("Sesión SMTP creada exitosamente");

            System.out.println("Creando mensaje de correo...");
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remitente));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject("Restablecer Contraseña - SmartHome Budget");
            
            // Este link es el que el usuario abrirá desde su correo
            String link = "http://localhost:8080" + contextPath + "/NuevaClave?token=" + token;
            
            // Crear contenido HTML para mejor visualización
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
            
            System.out.println("Enviando correo...");
            Transport.send(message);
            System.out.println("Correo enviado exitosamente a: " + destinatario);
            
            return true;
            
        } catch (AddressException e) {
            System.err.println("ERROR: Dirección de correo inválida - " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (AuthenticationFailedException e) {
            System.err.println("ERROR: Autenticación fallida - Verifica tu clave de aplicación de Gmail");
            System.err.println("Detalles: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (SendFailedException e) {
            System.err.println("ERROR: Fallo al enviar correo - " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (MessagingException e) {
            System.err.println("ERROR: Error general de mensajería - " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("ERROR: Error inesperado al enviar correo - " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
