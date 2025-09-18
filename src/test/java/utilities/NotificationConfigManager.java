package utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.List;

public class NotificationConfigManager {
    
    private static final String CONFIG_PATH = "/projects/projectNotifications/projectNotifications-config.json";
    private static Map<String, Object> cachedConfig = null;
    
    public static Map<String, Object> getNotificationConfig() {
        if (cachedConfig == null) {
            cachedConfig = loadConfigFromFile();
        }
        return cachedConfig != null ? resolveParameters(cachedConfig) : getDefaultConfig();
    }
    
    private static Map<String, Object> loadConfigFromFile() {
        try (InputStream is = NotificationConfigManager.class.getResourceAsStream(CONFIG_PATH)) {
            if (is != null) {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> fullConfig = mapper.readValue(is, Map.class);
                return (Map<String, Object>) ((Map<String, Object>) fullConfig.get("environments")).get("qa");
            }
        } catch (Exception e) {
            NotificationLogger.error("Failed to load notification config from file: " + e.getMessage(), e);
        }
        return null;
    }
    
    private static Map<String, Object> resolveParameters(Map<String, Object> config) {
        Map<String, Object> resolved = new HashMap<>();
        
        for (Map.Entry<String, Object> entry : config.entrySet()) {
            if (entry.getValue() instanceof Map) {
                resolved.put(entry.getKey(), resolveParameters((Map<String, Object>) entry.getValue()));
            } else if (entry.getValue() instanceof String) {
                String value = (String) entry.getValue();
                if (value.startsWith("${") && value.endsWith("}")) {
                    resolved.put(entry.getKey(), resolveParameter(value));
                } else {
                    resolved.put(entry.getKey(), value);
                }
            } else {
                resolved.put(entry.getKey(), entry.getValue());
            }
        }
        
        // Handle special cases for recipients (convert comma-separated strings to arrays)
        if (resolved.containsKey("recipients")) {
            Map<String, Object> recipients = (Map<String, Object>) resolved.get("recipients");
            Map<String, Object> processedRecipients = new HashMap<>();
            
            for (Map.Entry<String, Object> recipientEntry : recipients.entrySet()) {
                if (recipientEntry.getValue() instanceof String) {
                    String value = (String) recipientEntry.getValue();
                    if (value.contains(",")) {
                        List<String> emailList = Arrays.asList(value.split(","));
                        processedRecipients.put(recipientEntry.getKey(), emailList.toArray(new String[0]));
                    } else {
                        processedRecipients.put(recipientEntry.getKey(), new String[]{value});
                    }
                } else {
                    processedRecipients.put(recipientEntry.getKey(), recipientEntry.getValue());
                }
            }
            resolved.put("recipients", processedRecipients);
        }
        
        return resolved;
    }
    
    private static Object resolveParameter(String parameterExpression) {
        // Extract parameter name and default value from ${PARAM_NAME:defaultValue}
        String content = parameterExpression.substring(2, parameterExpression.length() - 1);
        String[] parts = content.split(":", 2);
        String paramName = parts[0];
        String defaultValue = parts.length > 1 ? parts[1] : "";
        
        // Try system property first, then environment variable, then default
        String value = System.getProperty(paramName);
        if (value == null) {
            value = System.getenv(paramName);
        }
        if (value == null) {
            value = defaultValue;
        }
        
        // Convert string values to appropriate types
        if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
            return Boolean.parseBoolean(value);
        }
        
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return value;
        }
    }
    
    private static Map<String, Object> getDefaultConfig() {
        Map<String, Object> config = new HashMap<>();
        
        // Email config
        Map<String, Object> email = new HashMap<>();
        email.put("smtpHost", "smtp.gmail.com");
        email.put("smtpPort", 587);
        email.put("username", "exavalu.karate@gmail.com");
        email.put("password", "uzkb hwib medc lagp");
        email.put("from", "exavalu.karate@gmail.com");
        
        // Slack config
        Map<String, Object> slack = new HashMap<>();
        slack.put("webhookUrl", "https://hooks.slack.com/services/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");
        slack.put("channel", "#all-karatetest");
        slack.put("username", "nandan");
        slack.put("iconEmoji", ":robot_face:");
        
        // Teams config
        Map<String, Object> teams = new HashMap<>();
        teams.put("webhookUrl", "https://exavalu.webhook.office.com/webhookb2/9d18104c-03c1-4298-ac50-d2c9b5b72314@ee19a561-5b69-430d-9e17-a12af3c2b3bc/IncomingWebhook/f1c643cae7d7470bbed305bcf44aac27/4f3c32c1-5387-4552-ac0f-28dbaf75a354/V2-s4SuPeXSZffPGJ3Q4Millo8tIkrvb0YZ0cxjRtXpSo1");
        teams.put("title", "🧪 Karate Test Results");
        teams.put("subtitle", "PostgreSQL Integration Tests");
        
        // Recipients
        Map<String, Object> recipients = new HashMap<>();
        recipients.put("to", new String[]{"nandu.sudarsan@exavalu.com"});
        recipients.put("cc", new String[]{"Sivaraj.Subramanian@Exavalu.com"});
        recipients.put("bcc", new String[]{"Nagarjuna.Yeruva@Exavalu.com", "Meghna.Chakraborty@Exavalu.com"});
        
        config.put("email", email);
        config.put("slack", slack);
        config.put("teams", teams);
        config.put("recipients", recipients);
        
        return config;
    }
    
    public static void clearCache() {
        cachedConfig = null;
    }
    
    // Utility method to get pipeline context
    public static Map<String, String> getPipelineContext() {
        Map<String, String> context = new HashMap<>();
        Map<String, Object> config = getNotificationConfig();
        
        if (config.containsKey("pipeline")) {
            Map<String, Object> pipeline = (Map<String, Object>) config.get("pipeline");
            for (Map.Entry<String, Object> entry : pipeline.entrySet()) {
                context.put(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        
        return context;
    }
}