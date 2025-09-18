package utilities;

import java.util.*;

/**
 * Notification Utility for Karate Framework
 * Provides static methods for sending notifications that can be used in any feature file
 */
public class NotificationUtility {
    
    /**
     * Send email notification
     * Usage in feature: * def emailResult = Java.type('utilities.NotificationUtility').sendEmail(config, subject, message)
     */
    public static Map<String, Object> sendEmail(Map<String, Object> emailConfig, String subject, String message) {
        try {
            return EmailNotificationService.sendSimpleEmail(
                emailConfig.get("smtpHost").toString(),
                Integer.parseInt(emailConfig.get("smtpPort").toString()),
                emailConfig.get("username").toString(),
                emailConfig.get("password").toString(),
                emailConfig.get("from").toString(),
                emailConfig.get("to").toString(),
                subject,
                message
            );
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("error", e.getMessage());
            return result;
        }
    }
    
    /**
     * Send email with attachment
     * Usage in feature: * def emailResult = Java.type('utilities.NotificationUtility').sendEmailWithAttachment(config, subject, message, filePath)
     */
    public static Map<String, Object> sendEmailWithAttachment(Map<String, Object> emailConfig, String subject, String message, String attachmentPath) {
        try {
            String[] toArray = {emailConfig.get("to").toString()};
            return EmailNotificationService.sendEmailWithAttachment(
                emailConfig.get("smtpHost").toString(),
                Integer.parseInt(emailConfig.get("smtpPort").toString()),
                emailConfig.get("username").toString(),
                emailConfig.get("password").toString(),
                emailConfig.get("from").toString(),
                toArray, null, null,
                subject, message, false, attachmentPath
            );
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("error", e.getMessage());
            return result;
        }
    }
    
    /**
     * Send Slack notification
     * Usage in feature: * def slackResult = Java.type('utilities.NotificationUtility').sendSlack(webhookUrl, message)
     */
    public static Map<String, Object> sendSlack(String webhookUrl, String message) {
        try {
            return SlackNotificationService.sendSimpleSlackMessage(webhookUrl, message);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("error", e.getMessage());
            return result;
        }
    }
    
    /**
     * Send Teams notification
     * Usage in feature: * def teamsResult = Java.type('utilities.NotificationUtility').sendTeams(webhookUrl, title, message)
     */
    public static Map<String, Object> sendTeams(String webhookUrl, String title, String message) {
        try {
            return TeamsNotificationService.sendSimpleTeamsMessage(webhookUrl, title, message);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("error", e.getMessage());
            return result;
        }
    }
    
    /**
     * Send enhanced Teams notification with test results
     * Usage in feature: * def teamsResult = Java.type('utilities.NotificationUtility').sendTeamsTestResults(webhookUrl, title, subtitle, status, total, passed, failed)
     */
    public static Map<String, Object> sendTeamsTestResults(String webhookUrl, String title, String subtitle, String status, int totalTests, int passed, int failed) {
        try {
            String timestamp = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            return TeamsNotificationService.sendTeamsMessage(webhookUrl, title, subtitle, status, totalTests, passed, failed, timestamp, null, null);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("error", e.getMessage());
            return result;
        }
    }
    
    /**
     * Send multi-channel notification
     * Usage in feature: * def multiResult = Java.type('utilities.NotificationUtility').sendMultiChannel(emailConfig, slackUrl, teamsUrl, subject, message)
     */
    public static Map<String, Object> sendMultiChannel(Map<String, Object> emailConfig, String slackWebhookUrl, String teamsWebhookUrl, String subject, String message) {
        Map<String, Object> results = new HashMap<>();
        
        // Send email
        if (emailConfig != null) {
            Map<String, Object> emailResult = sendEmail(emailConfig, subject, message);
            results.put("email", emailResult);
        }
        
        // Send Slack
        if (slackWebhookUrl != null && !slackWebhookUrl.isEmpty()) {
            Map<String, Object> slackResult = sendSlack(slackWebhookUrl, message);
            results.put("slack", slackResult);
        }
        
        // Send Teams
        if (teamsWebhookUrl != null && !teamsWebhookUrl.isEmpty()) {
            Map<String, Object> teamsResult = sendTeams(teamsWebhookUrl, subject, message);
            results.put("teams", teamsResult);
        }
        
        return results;
    }
    
    /**
     * Create email configuration map
     * Usage in feature: * def emailConfig = Java.type('utilities.NotificationUtility').createEmailConfig(host, port, username, password, from, to)
     */
    public static Map<String, Object> createEmailConfig(String smtpHost, int smtpPort, String username, String password, String from, String to) {
        Map<String, Object> config = new HashMap<>();
        config.put("smtpHost", smtpHost);
        config.put("smtpPort", smtpPort);
        config.put("username", username);
        config.put("password", password);
        config.put("from", from);
        config.put("to", to);
        return config;
    }
    
    /**
     * Send test completion notification with reports
     * Usage in feature: * def notificationResult = Java.type('utilities.NotificationUtility').sendTestCompletion(projectName, passed, failed, duration)
     */
    public static Map<String, Object> sendTestCompletion(String projectName, int passed, int failed, long durationMs) {
        try {
            String subject = String.format("Test Results - %s - %s", projectName, failed == 0 ? "PASSED" : "FAILED");
            String message = String.format(
                "Test Execution Summary:\\n" +
                "Project: %s\\n" +
                "Total Tests: %d\\n" +
                "Passed: %d\\n" +
                "Failed: %d\\n" +
                "Duration: %d ms\\n" +
                "Status: %s",
                projectName, passed + failed, passed, failed, durationMs,
                failed == 0 ? "SUCCESS" : "FAILURE"
            );
            
            Map<String, Object> config = NotificationConfigManager.getNotificationConfig();
            if (config != null) {
                Map<String, Object> emailConfig = (Map<String, Object>) config.get("email");
                String slackUrl = config.containsKey("slack") ? ((Map<String, Object>) config.get("slack")).get("webhookUrl").toString() : null;
                String teamsUrl = config.containsKey("teams") ? ((Map<String, Object>) config.get("teams")).get("webhookUrl").toString() : null;
                
                return sendMultiChannel(emailConfig, slackUrl, teamsUrl, subject, message);
            } else {
                Map<String, Object> result = new HashMap<>();
                result.put("success", false);
                result.put("error", "No notification configuration found");
                return result;
            }
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("error", e.getMessage());
            return result;
        }
    }
}