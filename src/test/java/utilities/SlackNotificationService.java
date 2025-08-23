package utilities;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.util.HashMap;
import java.util.Map;

public class SlackNotificationService {
    
    public static Map<String, Object> sendSlackMessage(String webhookUrl, String channel, String username, 
                                                      String text, String iconEmoji, String color) {
        Map<String, Object> result = new HashMap<>();
        
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(webhookUrl);
            post.setHeader("Content-Type", "application/json");
            
            StringBuilder payload = new StringBuilder();
            payload.append("{");
            if (channel != null) payload.append("\"channel\":\"").append(channel).append("\",");
            if (username != null) payload.append("\"username\":\"").append(username).append("\",");
            if (iconEmoji != null) payload.append("\"icon_emoji\":\"").append(iconEmoji).append("\",");
            payload.append("\"text\":\"").append(text.replace("\"", "\\\"")).append("\"");
            if (color != null) {
                payload.append(",\"attachments\":[{\"color\":\"").append(color).append("\",\"text\":\"").append(text.replace("\"", "\\\"")).append("\"}]");
            }
            payload.append("}");
            
            post.setEntity(new StringEntity(payload.toString()));
            
            try (CloseableHttpResponse response = client.execute(post)) {
                int statusCode = response.getStatusLine().getStatusCode();
                String responseBody = EntityUtils.toString(response.getEntity());
                
                if (statusCode == 200) {
                    result.put("success", true);
                    result.put("message", "Slack message sent successfully");
                } else {
                    result.put("success", false);
                    result.put("error", "HTTP " + statusCode + ": " + responseBody);
                }
            }
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }
    
    public static Map<String, Object> sendSimpleSlackMessage(String webhookUrl, String text) {
        return sendSlackMessage(webhookUrl, null, null, text, null, null);
    }
    
    public static Map<String, Object> sendSlackAlert(String webhookUrl, String channel, String title, String message, String severity) {
        String color = "good";
        if ("warning".equalsIgnoreCase(severity)) color = "warning";
        else if ("error".equalsIgnoreCase(severity) || "critical".equalsIgnoreCase(severity)) color = "danger";
        
        String fullMessage = title + "\n" + message;
        return sendSlackMessage(webhookUrl, channel, "Alert Bot", fullMessage, ":warning:", color);
    }
}