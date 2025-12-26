package com.shiva.mailService;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * EmailService is a utility class responsible for sending HTML-formatted emails
 * using the Jakarta Mail API with Gmail SMTP.
 *
 * It reads the SMTP application password securely from the SMTP_PASS environment
 * variable to avoid hardcoding credentials.
 *
 * The class configures SMTP properties such as host, port, authentication, and
 * STARTTLS encryption to ensure secure email delivery.
 *
 * The sendHtmlMail method accepts an HTML template string and delivers it as
 * a MIME email message with UTF-8 encoding.
 */

public class EmailService {

    private static final String FROM_EMAIL = "palleshiva2007@gmail.com";
    private static final String TO_EMAIL   = "pallesumathi18@gmail.com";

    public static void sendHtmlMail(String mailTemplate) throws Exception {

        String smtpPass = System.getenv("SMTP_PASS");
        if (smtpPass == null || smtpPass.isBlank()) {
            throw new RuntimeException("SMTP_PASS environment variable not set");
        }

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(FROM_EMAIL, smtpPass);
                    }
                });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(TO_EMAIL));
        message.setSubject("Today's Top News Summary");
        message.setContent(mailTemplate, "text/html; charset=UTF-8");
        Transport.send(message);
    }
}
