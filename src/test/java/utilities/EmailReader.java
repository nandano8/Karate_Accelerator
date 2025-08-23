package utilities;

import javax.mail.*;
import javax.mail.search.*;
import java.util.*;

public class EmailReader {
    
    public static Map<String, Object> readEmails(String host, String username, String password, String folder, int maxEmails) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> emails = new ArrayList<>();
        
        try {
            Properties props = new Properties();
            props.put("mail.store.protocol", "imaps");
            props.put("mail.imaps.host", host);
            props.put("mail.imaps.port", "993");
            props.put("mail.imaps.ssl.enable", "true");
            
            Session session = Session.getDefaultInstance(props);
            Store store = session.getStore("imaps");
            store.connect(host, username, password);
            
            Folder emailFolder = store.getFolder(folder);
            emailFolder.open(Folder.READ_ONLY);
            
            Message[] messages = emailFolder.getMessages();
            int count = Math.min(messages.length, maxEmails);
            
            for (int i = messages.length - count; i < messages.length; i++) {
                Message message = messages[i];
                Map<String, Object> emailData = new HashMap<>();
                
                emailData.put("subject", message.getSubject());
                emailData.put("from", message.getFrom()[0].toString());
                emailData.put("receivedDate", message.getReceivedDate().toString());
                emailData.put("content", getTextContent(message));
                
                emails.add(emailData);
            }
            
            emailFolder.close(false);
            store.close();
            
            result.put("success", true);
            result.put("emails", emails);
            result.put("count", emails.size());
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }
    
    public static Map<String, Object> searchEmails(String host, String username, String password, String folder, String subject, int minutes) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> emails = new ArrayList<>();
        
        try {
            Properties props = new Properties();
            props.put("mail.store.protocol", "imaps");
            props.put("mail.imaps.host", host);
            props.put("mail.imaps.port", "993");
            props.put("mail.imaps.ssl.enable", "true");
            
            Session session = Session.getDefaultInstance(props);
            Store store = session.getStore("imaps");
            store.connect(host, username, password);
            
            Folder emailFolder = store.getFolder(folder);
            emailFolder.open(Folder.READ_ONLY);
            
            // Search for emails received in last X minutes with specific subject
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, -minutes);
            Date since = cal.getTime();
            
            SearchTerm searchTerm = new AndTerm(
                new ReceivedDateTerm(ComparisonTerm.GE, since),
                new SubjectTerm(subject)
            );
            
            Message[] messages = emailFolder.search(searchTerm);
            
            for (Message message : messages) {
                Map<String, Object> emailData = new HashMap<>();
                
                emailData.put("subject", message.getSubject());
                emailData.put("from", message.getFrom()[0].toString());
                emailData.put("receivedDate", message.getReceivedDate().toString());
                emailData.put("content", getTextContent(message));
                
                emails.add(emailData);
            }
            
            emailFolder.close(false);
            store.close();
            
            result.put("success", true);
            result.put("emails", emails);
            result.put("count", emails.size());
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }
    
    private static String getTextContent(Message message) throws Exception {
        if (message.isMimeType("text/plain")) {
            return (String) message.getContent();
        } else if (message.isMimeType("text/html")) {
            return (String) message.getContent();
        } else if (message.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) message.getContent();
            return getTextFromMultipart(multipart);
        } else {
            return "Unsupported content type";
        }
    }
    
    private static String getTextFromMultipart(Multipart multipart) throws Exception {
        StringBuilder result = new StringBuilder();
        int count = multipart.getCount();
        
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = multipart.getBodyPart(i);
            
            if (bodyPart.isMimeType("text/plain")) {
                result.append(bodyPart.getContent().toString());
            } else if (bodyPart.isMimeType("text/html")) {
                result.append(bodyPart.getContent().toString());
            } else if (bodyPart.isMimeType("multipart/*")) {
                result.append(getTextFromMultipart((Multipart) bodyPart.getContent()));
            }
        }
        
        return result.toString();
    }
}