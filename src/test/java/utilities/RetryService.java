package utilities;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class RetryService {
    
    public static Map<String, Object> executeWithRetry(Supplier<Map<String, Object>> operation, int maxRetries, long delayMs) {
        Map<String, Object> result = new HashMap<>();
        Exception lastException = null;
        
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                Map<String, Object> operationResult = operation.get();
                if ((Boolean) operationResult.getOrDefault("success", false)) {
                    operationResult.put("attempt", attempt);
                    return operationResult;
                }
                lastException = new Exception((String) operationResult.get("error"));
            } catch (Exception e) {
                lastException = e;
            }
            
            if (attempt < maxRetries) {
                try {
                    Thread.sleep(delayMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        
        result.put("success", false);
        result.put("error", "Failed after " + maxRetries + " attempts: " + (lastException != null ? lastException.getMessage() : "Unknown error"));
        result.put("attempts", maxRetries);
        return result;
    }
    
    public static Map<String, Object> retryOktaToken(String oktaDomain, String clientId, String clientSecret, String scope, int maxRetries) {
        return executeWithRetry(() -> {
            try {
                Map<String, Object> tokenData = OktaTokenService.generateClientCredentialsToken(oktaDomain, clientId, clientSecret, scope);
                if (!(Boolean) tokenData.get("success")) {
                    return tokenData;
                }
                
                // Simulate HTTP call result
                Map<String, Object> result = new HashMap<>();
                result.put("success", true);
                result.put("tokenRequest", tokenData.get("tokenRequest"));
                return result;
            } catch (Exception e) {
                Map<String, Object> result = new HashMap<>();
                result.put("success", false);
                result.put("error", e.getMessage());
                return result;
            }
        }, maxRetries, 2000);
    }
    
    public static Map<String, Object> retryNotification(Map<String, Object> emailConfig, Map<String, Object> slackConfig, String subject, String message, int maxRetries) {
        return executeWithRetry(() -> NotificationService.sendMultiChannelNotification(emailConfig, slackConfig, subject, message), maxRetries, 1000);
    }
    
    public static Map<String, Object> retrySftpTransfer(String sourceHost, int sourcePort, String sourceUser, String sourcePassword, String sourcePath, String targetHost, int targetPort, String targetUser, String targetPassword, String targetPath, int maxRetries) {
        return executeWithRetry(() -> SftpService.transferFile(sourceHost, sourcePort, sourceUser, sourcePassword, sourcePath, targetHost, targetPort, targetUser, targetPassword, targetPath), maxRetries, 3000);
    }
}