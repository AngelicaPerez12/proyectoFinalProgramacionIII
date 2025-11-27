package com.example.proyectoprogra.models;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailUtils {
    public static void enviarCorreo(String destino, String asunto, String mensaje) {
        final String remitente = "ml23011@ues.edu.sv"; // TU CORREO
        final String clave = "zqrcxkztpbkiwlja"; // CLAVE DE APP (NO TU PASSWORD)

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, clave);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remitente));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destino));
            message.setSubject(asunto);
            message.setText(mensaje);

            Transport.send(message);

            System.out.println("Correo enviado correctamente");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
