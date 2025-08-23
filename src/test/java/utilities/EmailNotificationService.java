package utilities;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;

public class EmailNotificationService {
    
    public static Map<String, Object> sendEmail(String smtpHost, int smtpPort, String username, String password,
                                               String from, String[] to, String[] cc, String[] bcc,
                                               String subject, String body, boolean isHtml) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", smtpHost);
            props.put("mail.smtp.port", smtpPort);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.starttls.required", "true");
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");
            props.put("mail.smtp.ssl.trust", smtpHost);
            props.put("mail.debug", "false");
            
            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });
            
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            
            if (to != null) {
                InternetAddress[] toAddresses = new InternetAddress[to.length];
                for (int i = 0; i < to.length; i++) {
                    toAddresses[i] = new InternetAddress(to[i]);
                }
                message.setRecipients(Message.RecipientType.TO, toAddresses);
            }
            
            if (cc != null) {
                InternetAddress[] ccAddresses = new InternetAddress[cc.length];
                for (int i = 0; i < cc.length; i++) {
                    ccAddresses[i] = new InternetAddress(cc[i]);
                }
                message.setRecipients(Message.RecipientType.CC, ccAddresses);
            }
            
            if (bcc != null) {
                InternetAddress[] bccAddresses = new InternetAddress[bcc.length];
                for (int i = 0; i < bcc.length; i++) {
                    bccAddresses[i] = new InternetAddress(bcc[i]);
                }
                message.setRecipients(Message.RecipientType.BCC, bccAddresses);
            }
            
            message.setSubject(subject);
            message.setContent(body, isHtml ? "text/html" : "text/plain");
            message.setSentDate(new Date());
            
            Transport.send(message);
            
            result.put("success", true);
            result.put("message", "Email sent successfully");
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
            result.put("errorType", e.getClass().getSimpleName());
            System.err.println("Email sending failed: " + e.getMessage());
            e.printStackTrace();
        }
        
        return result;
    }
    
    public static Map<String, Object> sendSimpleEmail(String smtpHost, int smtpPort, String username, String password,
                                                     String from, String to, String subject, String body) {
        return sendEmail(smtpHost, smtpPort, username, password, from, new String[]{to}, null, null, subject, body, false);
    }
}