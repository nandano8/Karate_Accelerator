package utilities;

import java.util.Map;

public class NotificationFactory {
    
    public static Map<String, Object> createEmailPayload(Map<String, Object> config) {
        Map<String, Object> email = (Map<String, Object>) config.get("email");
        Map<String, Object> recipients = (Map<String, Object>) config.get("recipients");
        
        return NotificationService.createEmailPayload(
            (String) email.get("smtpHost"),
            (Integer) email.get("smtpPort"),
            (String) email.get("username"),
            (String) email.get("password"),
            (String) email.get("from"),
            convertToStringArray(recipients.get("to")),
            convertToStringArray(recipients.get("cc")),
            convertToStringArray(recipients.get("bcc")),
            false
        );
    }
    
    private static String[] convertToStringArray(Object obj) {
        if (obj == null) return null;
        if (obj instanceof String[]) return (String[]) obj;
        if (obj instanceof java.util.List) {
            java.util.List<String> list = (java.util.List<String>) obj;
            return list.toArray(new String[0]);
        }
        return new String[]{obj.toString()};
    }
    
    public static Map<String, Object> createSlackPayload(Map<String, Object> config, String color) {
        Map<String, Object> slack = (Map<String, Object>) config.get("slack");
        return NotificationService.createSlackPayload(
            (String) slack.get("webhookUrl"),
            (String) slack.get("channel"),
            (String) slack.get("username"),
            (String) slack.get("iconEmoji"),
            color != null ? color : "good"
        );
    }
    
    public static Map<String, Object> createTeamsPayload(Map<String, Object> config) {
        Map<String, Object> teams = (Map<String, Object>) config.get("teams");
        return NotificationService.createTeamsPayload(
            (String) teams.get("webhookUrl"),
            (String) teams.get("title"),
            (String) teams.get("subtitle")
        );
    }
}