package com.example.proyectoprogra.models;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailSender {
    private static final String REMITENTE = "ml23011@ues.edu.sv";
    private static final String PASSWORD = "zqrcxkztpbkiwlja";

    public static void enviarCodigo(String destinatario, String codigo) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(REMITENTE, PASSWORD);
            }
        });

        try {
            Message mensaje = new MimeMessage(session);
            mensaje.setFrom(new InternetAddress(REMITENTE));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            mensaje.setSubject("Código de recuperación de PIN");
            mensaje.setText("Hola,\n\nTu código de recuperación de PIN es: " + codigo + "\n\nNo compartas este código con nadie.");

            Transport.send(mensaje);

            System.out.println("Correo enviado correctamente a " + destinatario);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
