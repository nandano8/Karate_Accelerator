package utilities;

import com.intuit.karate.Results;
import java.util.Map;

public class TestLifecycleHook {
    
    public static void sendTestCompletionNotification(Results results, String projectName) {
        try {
            Map<String, Object> config = NotificationConfigManager.getNotificationConfig();
            
            if (config != null) {
                // Create notification payloads
                String slackColor = NotificationMessageGenerator.getSlackColor(results);
                Map<String, Object> emailConfig = NotificationFactory.createEmailPayload(config);
                Map<String, Object> slackConfig = NotificationFactory.createSlackPayload(config, slackColor);
                Map<String, Object> teamsConfig = NotificationFactory.createTeamsPayload(config);
                
                // Generate notification content
                String subject = NotificationMessageGenerator.generateSubject(results, projectName);
                String message = NotificationMessageGenerator.generateMessage(results, projectName);
                
                // Send enhanced Teams notification with detailed test results
                sendEnhancedTeamsNotification(results, projectName, (Map<String, Object>) config.get("teams"));
                
                // Send notification to all channels
                NotificationService.sendMultiChannelNotification(emailConfig, slackConfig, teamsConfig, subject, message);
                
                NotificationLogger.success("Test completion notification sent successfully");
            } else {
                NotificationLogger.error("Failed to load notification configuration");
            }
        } catch (Exception e) {
            NotificationLogger.error("Failed to send test completion notification: " + e.getMessage(), e);
        }
    }
    

    
    private static void sendEnhancedTeamsNotification(Results results, String projectName, Map<String, Object> teamsConfig) {
        try {
            String timestamp = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String htmlReportPath = NotificationMessageGenerator.getEscapedHtmlReportPath();
            int totalTests = results.getScenariosPassed() + results.getScenariosFailed();
            
            Map<String, Object> teamsResult = TeamsNotificationService.sendTeamsMessage(
                (String) teamsConfig.get("webhookUrl"),
                (String) teamsConfig.get("title"),
                (String) teamsConfig.get("subtitle"),
                results.getFailCount() > 0 ? "FAILED" : "PASSED",
                totalTests,
                results.getScenariosPassed(),
                results.getFailCount(),
                timestamp,
                htmlReportPath
            );
            
            if ((Boolean) teamsResult.get("success")) {
                NotificationLogger.success("Enhanced Teams notification sent successfully");
            } else {
                NotificationLogger.error("Failed to send enhanced Teams notification: " + teamsResult.get("error"));
            }
        } catch (Exception e) {
            NotificationLogger.error("Error sending enhanced Teams notification: " + e.getMessage(), e);
        }
    }
}