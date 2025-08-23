package utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class NotificationConfigManager {
    
    private static final String CONFIG_PATH = "/projects/projectNotifications/projectNotifications-config.json";
    private static Map<String, Object> cachedConfig = null;
    
    public static Map<String, Object> getNotificationConfig() {
        if (cachedConfig == null) {
            cachedConfig = loadConfigFromFile();
        }
        return cachedConfig != null ? cachedConfig : getDefaultConfig();
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
}