package utilities;

import java.util.*;

public class NotificationService {
    
    public static Map<String, Object> sendMultiChannelNotification(Map<String, Object> emailConfig, 
                                                                  Map<String, Object> slackConfig, 
                                                                  Map<String, Object> teamsConfig,
                                                                  String subject, String message) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> results = new ArrayList<>();
        
        // Send email if config provided
        if (emailConfig != null) {
            Map<String, Object> emailResult = EmailNotificationService.sendEmail(
                (String) emailConfig.get("smtpHost"),
                (Integer) emailConfig.get("smtpPort"),
                (String) emailConfig.get("username"),
                (String) emailConfig.get("password"),
                (String) emailConfig.get("from"),
                (String[]) emailConfig.get("to"),
                (String[]) emailConfig.get("cc"),
                (String[]) emailConfig.get("bcc"),
                subject,
                message,
                (Boolean) emailConfig.getOrDefault("isHtml", false)
            );
            emailResult.put("channel", "email");
            results.add(emailResult);
        }
        
        // Send Slack if config provided
        if (slackConfig != null) {
            Map<String, Object> slackResult = SlackNotificationService.sendSlackMessage(
                (String) slackConfig.get("webhookUrl"),
                (String) slackConfig.get("channel"),
                (String) slackConfig.get("username"),
                subject + "\n" + message,
                (String) slackConfig.get("iconEmoji"),
                (String) slackConfig.get("color")
            );
            slackResult.put("channel", "slack");
            results.add(slackResult);
        }
        
        // Send Teams if config provided
        if (teamsConfig != null) {
            Map<String, Object> teamsResult = TeamsNotificationService.sendSimpleTeamsMessage(
                (String) teamsConfig.get("webhookUrl"),
                subject,
                message
            );
            teamsResult.put("channel", "teams");
            results.add(teamsResult);
        }
        
        boolean allSuccess = results.stream().allMatch(r -> (Boolean) r.get("success"));
        result.put("success", allSuccess);
        result.put("results", results);
        result.put("message", allSuccess ? "All notifications sent successfully" : "Some notifications failed");
        
        return result;
    }
    
    // Backward compatibility method
    public static Map<String, Object> sendMultiChannelNotification(Map<String, Object> emailConfig, 
                                                                  Map<String, Object> slackConfig, 
                                                                  String subject, String message) {
        return sendMultiChannelNotification(emailConfig, slackConfig, null, subject, message);
    }
    
    public static Map<String, Object> createEmailPayload(String smtpHost, int smtpPort, String username, String password,
                                                        String from, String[] to, String[] cc, String[] bcc, boolean isHtml) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("smtpHost", smtpHost);
        payload.put("smtpPort", smtpPort);
        payload.put("username", username);
        payload.put("password", password);
        payload.put("from", from);
        payload.put("to", to);
        payload.put("cc", cc);
        payload.put("bcc", bcc);
        payload.put("isHtml", isHtml);
        return payload;
    }
    
    public static Map<String, Object> createSlackPayload(String webhookUrl, String channel, String username, 
                                                        String iconEmoji, String color) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("webhookUrl", webhookUrl);
        payload.put("channel", channel);
        payload.put("username", username);
        payload.put("iconEmoji", iconEmoji);
        payload.put("color", color);
        return payload;
    }
    
    public static Map<String, Object> createTeamsPayload(String webhookUrl, String title, String subtitle) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("webhookUrl", webhookUrl);
        payload.put("title", title);
        payload.put("subtitle", subtitle);
        return payload;
    }
    
    public static Map<String, Object> sendEmail(String subject, String message, String[] to, String[] cc, String[] bcc) {
        Map<String, Object> config = NotificationConfigManager.getNotificationConfig();
        Map<String, Object> emailConfig = NotificationFactory.createEmailPayload(config);
        
        return EmailNotificationService.sendEmail(
            (String) emailConfig.get("smtpHost"),
            (Integer) emailConfig.get("smtpPort"),
            (String) emailConfig.get("username"),
            (String) emailConfig.get("password"),
            (String) emailConfig.get("from"),
            to, cc, bcc, subject, message, false
        );
    }
    
    public static Map<String, Object> sendSlack(String message, String channel) {
        Map<String, Object> config = NotificationConfigManager.getNotificationConfig();
        Map<String, Object> slackConfig = NotificationFactory.createSlackPayload(config, null);
        
        return SlackNotificationService.sendSlackMessage(
            (String) slackConfig.get("webhookUrl"),
            channel,
            (String) slackConfig.get("username"),
            message,
            (String) slackConfig.get("iconEmoji"),
            null
        );
    }
    
    public static Map<String, Object> sendTeams(String title, String message) {
        Map<String, Object> config = NotificationConfigManager.getNotificationConfig();
        Map<String, Object> teamsConfig = NotificationFactory.createTeamsPayload(config);
        
        return TeamsNotificationService.sendSimpleTeamsMessage(
            (String) teamsConfig.get("webhookUrl"),
            title,
            message
        );
    }
    
    public static Map<String, Object> sendBoth(String subject, String message, String[] to, String[] cc, String[] bcc, String channel) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> results = new ArrayList<>();
        
        // Send email
        Map<String, Object> emailResult = sendEmail(subject, message, to, cc, bcc);
        emailResult.put("channel", "email");
        results.add(emailResult);
        
        // Send Slack
        Map<String, Object> slackResult = sendSlack(subject + "\n" + message, channel);
        slackResult.put("channel", "slack");
        results.add(slackResult);
        
        boolean allSuccess = results.stream().allMatch(r -> (Boolean) r.get("success"));
        result.put("success", allSuccess);
        result.put("results", results);
        result.put("message", allSuccess ? "All notifications sent successfully" : "Some notifications failed");
        
        return result;
    }
    
    public static Map<String, Object> sendAll(String subject, String message, String[] to, String[] cc, String[] bcc, String slackChannel, String teamsWebhook) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> results = new ArrayList<>();
        
        // Send email
        Map<String, Object> emailResult = sendEmail(subject, message, to, cc, bcc);
        emailResult.put("channel", "email");
        results.add(emailResult);
        
        // Send Slack
        Map<String, Object> slackResult = sendSlack(subject + "\n" + message, slackChannel);
        slackResult.put("channel", "slack");
        results.add(slackResult);
        
        // Send Teams
        Map<String, Object> teamsResult = sendTeams(subject, message);
        teamsResult.put("channel", "teams");
        results.add(teamsResult);
        
        boolean allSuccess = results.stream().allMatch(r -> (Boolean) r.get("success"));
        result.put("success", allSuccess);
        result.put("results", results);
        result.put("message", allSuccess ? "All notifications sent successfully" : "Some notifications failed");
        
        return result;
    }
}