package utilities;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReportNotifier {
    
    public static void sendTeamsReport(String webhookUrl, String projectName, int total, int passed, int failed) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(webhookUrl);
            
            String status = failed == 0 ? "✅ PASSED" : "❌ FAILED";
            String color = failed == 0 ? "00FF00" : "FF0000";
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            
            String json = String.format(
                "{\"@type\":\"MessageCard\",\"@context\":\"http://schema.org/extensions\"," +
                "\"themeColor\":\"%s\",\"summary\":\"ExaAPI Test Report\"," +
                "\"sections\":[{\"activityTitle\":\"🚀 ExaAPI Test Execution Report\"," +
                "\"activitySubtitle\":\"%s\",\"facts\":[" +
                "{\"name\":\"Project\",\"value\":\"%s\"}," +
                "{\"name\":\"Status\",\"value\":\"%s\"}," +
                "{\"name\":\"Total Tests\",\"value\":\"%d\"}," +
                "{\"name\":\"Passed\",\"value\":\"%d\"}," +
                "{\"name\":\"Failed\",\"value\":\"%d\"}," +
                "{\"name\":\"Execution Time\",\"value\":\"%s\"}" +
                "]}]}", 
                color, status, projectName, status, total, passed, failed, timestamp
            );
            
            post.setEntity(new StringEntity(json, "UTF-8"));
            post.setHeader("Content-Type", "application/json");
            
            client.execute(post);
        } catch (Exception e) {
            System.err.println("Teams notification failed: " + e.getMessage());
        }
    }
    
    public static void sendEmailReport(String smtpHost, String username, String password,
                                     String from, String to, String projectName,
                                     int total, int passed, int failed) {
        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        
        try {
            String status = failed == 0 ? "PASSED" : "FAILED";
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("ExaAPI Test Report - " + projectName + " - " + status);
            
            String body = String.format(
                "ExaAPI Test Execution Report\\n\\n" +
                "Project: %s\\n" +
                "Status: %s\\n" +
                "Execution Time: %s\\n\\n" +
                "Test Results:\\n" +
                "- Total Tests: %d\\n" +
                "- Passed: %d\\n" +
                "- Failed: %d\\n\\n" +
                "Success Rate: %.1f%%\\n\\n" +
                "This is an automated report from ExaAPI framework.",
                projectName, status, timestamp, total, passed, failed,
                total > 0 ? (passed * 100.0 / total) : 0.0
            );
            
            message.setText(body);
            Transport.send(message);
        } catch (MessagingException e) {
            System.err.println("Email notification failed: " + e.getMessage());
        }
    }
}