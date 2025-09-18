package utilities;

import com.intuit.karate.Results;
import java.util.Map;

public class TestLifecycleHook {
    
    public static void sendTestCompletionNotification(Results results, String projectName) {
        try {
            Map<String, Object> config = NotificationConfigManager.getNotificationConfig();
            
            if (config != null) {
                // Create reports archive
                String archivePath = ReportArchiver.createReportsArchive(projectName);
                
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
                
                // Send email with zipped reports attachment
                if (archivePath != null) {
                    sendEmailWithReports(emailConfig, subject, message, archivePath);
                }
                
                // Send notification to other channels (Slack, Teams)
                NotificationService.sendMultiChannelNotification(null, slackConfig, teamsConfig, subject, message);
                
                NotificationLogger.success("Test completion notification sent successfully");
            } else {
                NotificationLogger.error("Failed to load notification configuration");
            }
        } catch (Exception e) {
            NotificationLogger.error("Failed to send test completion notification: " + e.getMessage(), e);
        }
    }
    

    
    private static void sendEmailWithReports(Map<String, Object> emailConfig, String subject, String message, String archivePath) {
        try {
            Map<String, Object> config = (Map<String, Object>) emailConfig.get("config");
            Map<String, Object> recipients = (Map<String, Object>) emailConfig.get("recipients");
            
            String[] toArray = recipients.get("to").toString().split(",");
            String[] ccArray = recipients.get("cc") != null ? recipients.get("cc").toString().split(",") : null;
            String[] bccArray = recipients.get("bcc") != null ? recipients.get("bcc").toString().split(",") : null;
            
            Map<String, Object> result = EmailNotificationService.sendEmailWithAttachment(
                config.get("smtpHost").toString(),
                Integer.parseInt(config.get("smtpPort").toString()),
                config.get("username").toString(),
                config.get("password").toString(),
                config.get("from").toString(),
                toArray, ccArray, bccArray,
                subject + " - Reports Attached",
                message + "\n\nTest reports are attached as a zip file.",
                true, archivePath
            );
            
            if ((Boolean) result.get("success")) {
                NotificationLogger.success("Email with reports sent successfully");
            } else {
                NotificationLogger.error("Failed to send email with reports: " + result.get("error"));
            }
        } catch (Exception e) {
            NotificationLogger.error("Error sending email with reports: " + e.getMessage(), e);
        }
    }
    
    private static void sendEnhancedTeamsNotification(Results results, String projectName, Map<String, Object> teamsConfig) {
        try {
            String timestamp = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String htmlReportPath = NotificationMessageGenerator.getEscapedHtmlReportPath();
            String allureReportPath = AllureReportService.getAllureReportUrl();
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
                htmlReportPath,
                allureReportPath
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