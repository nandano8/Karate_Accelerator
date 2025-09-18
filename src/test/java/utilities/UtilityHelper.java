package utilities;

import java.util.*;

/**
 * Combined Utility Helper for Karate Framework
 * Provides easy access to all utility functions in one place
 */
public class UtilityHelper {
    
    // Database Operations
    public static class Database {
        public static boolean connect(String url, String username, String password) {
            return DatabaseUtility.connect(url, username, password);
        }
        
        public static List<Map<String, Object>> query(String sql, Object[] params) {
            return DatabaseUtility.executeQuery(sql, params);
        }
        
        public static int update(String sql, Object[] params) {
            return DatabaseUtility.executeUpdate(sql, params);
        }
        
        public static boolean disconnect() {
            return DatabaseUtility.disconnect();
        }
        
        public static boolean isConnected() {
            return DatabaseUtility.isConnected();
        }
        
        public static int getRowCount(String tableName) {
            return DatabaseUtility.getRowCount(tableName);
        }
    }
    
    // Notification Operations
    public static class Notification {
        public static Map<String, Object> email(Map<String, Object> config, String subject, String message) {
            return NotificationUtility.sendEmail(config, subject, message);
        }
        
        public static Map<String, Object> emailWithAttachment(Map<String, Object> config, String subject, String message, String filePath) {
            return NotificationUtility.sendEmailWithAttachment(config, subject, message, filePath);
        }
        
        public static Map<String, Object> slack(String webhookUrl, String message) {
            return NotificationUtility.sendSlack(webhookUrl, message);
        }
        
        public static Map<String, Object> teams(String webhookUrl, String title, String message) {
            return NotificationUtility.sendTeams(webhookUrl, title, message);
        }
        
        public static Map<String, Object> multiChannel(Map<String, Object> emailConfig, String slackUrl, String teamsUrl, String subject, String message) {
            return NotificationUtility.sendMultiChannel(emailConfig, slackUrl, teamsUrl, subject, message);
        }
        
        public static Map<String, Object> createEmailConfig(String host, int port, String username, String password, String from, String to) {
            return NotificationUtility.createEmailConfig(host, port, username, password, from, to);
        }
    }
    
    // Encryption Operations
    public static class Encryption {
        public static Map<String, Object> encrypt(String data, String key) {
            return EncryptionService.encryptAES(data, key);
        }
        
        public static Map<String, Object> decrypt(String encryptedData, String key) {
            return EncryptionService.decryptAES(encryptedData, key);
        }
        
        public static String generateKey() {
            return EncryptionService.generateAESKey();
        }
    }
    
    // SFTP Operations
    public static class SFTP {
        public static Map<String, Object> upload(String host, int port, String username, String password, String localPath, String remotePath) {
            return SftpService.uploadFile(host, port, username, password, localPath, remotePath);
        }
        
        public static Map<String, Object> download(String host, int port, String username, String password, String remotePath, String localPath) {
            return SftpService.downloadFile(host, port, username, password, remotePath, localPath);
        }
        
        public static Map<String, Object> transfer(String sourceHost, int sourcePort, String sourceUser, String sourcePassword, String sourcePath, String targetHost, int targetPort, String targetUser, String targetPassword, String targetPath) {
            return SftpService.transferFile(sourceHost, sourcePort, sourceUser, sourcePassword, sourcePath, targetHost, targetPort, targetUser, targetPassword, targetPath);
        }
    }
    
    // Okta Operations
    public static class Okta {
        public static Map<String, Object> getClientCredentialsToken(String oktaDomain, String clientId, String clientSecret, String scope) {
            return OktaTokenService.generateClientCredentialsToken(oktaDomain, clientId, clientSecret, scope);
        }
        
        public static Map<String, Object> getAuthorizationCodeToken(String oktaDomain, String clientId, String clientSecret, String authCode, String redirectUri) {
            return OktaTokenService.generateAuthorizationCodeToken(oktaDomain, clientId, clientSecret, authCode, redirectUri);
        }
    }
    
    // Allure Operations
    public static class Allure {
        public static void addText(String name, String content) {
            AllureAttachmentHelper.addTextAttachment(name, content);
        }
        
        public static void addJson(String name, String jsonContent) {
            AllureAttachmentHelper.addJsonAttachment(name, jsonContent);
        }
        
        public static void addApiDetails(String method, String url, String requestBody, String responseBody, int statusCode) {
            AllureAttachmentHelper.addRequestDetails(method, url, requestBody, responseBody, statusCode);
        }
        
        public static void addTestContext(String featureName, String scenarioName, String stepName) {
            AllureAttachmentHelper.addTestContext(featureName, scenarioName, stepName);
        }
        
        public static void addPerformance(String operation, long duration, boolean success) {
            AllureAttachmentHelper.addPerformanceMetrics(operation, duration, success);
        }
    }
    
    // Utility Operations
    public static class Utils {
        public static String getCurrentTimestamp() {
            return java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        
        public static long getCurrentTimeMillis() {
            return System.currentTimeMillis();
        }
        
        public static String generateUUID() {
            return java.util.UUID.randomUUID().toString();
        }
        
        public static Map<String, Object> createResult(boolean success, String message) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", success);
            result.put("message", message);
            result.put("timestamp", getCurrentTimestamp());
            return result;
        }
    }
}