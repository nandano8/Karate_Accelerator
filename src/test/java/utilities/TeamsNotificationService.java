package utilities;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.util.HashMap;
import java.util.Map;

public class TeamsNotificationService {
    
    public static Map<String, Object> sendTeamsMessage(String webhookUrl, String title, String subtitle, 
                                                      String status, int totalTests, int passed, int failed, 
                                                      String timestamp, String htmlReportPath, String allureReportPath) {
        Map<String, Object> result = new HashMap<>();
        
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(webhookUrl);
            post.setHeader("Content-Type", "application/json");
            
            String color = failed == 0 ? "Good" : "Attention";
            String statusIcon = failed == 0 ? "✅ PASSED" : "❌ FAILED";
            
            StringBuilder payload = new StringBuilder();
            payload.append("{");
            payload.append("\"@type\":\"MessageCard\",");
            payload.append("\"@context\":\"https://schema.org/extensions\",");
            payload.append("\"summary\":\"").append(title).append("\",");
            payload.append("\"themeColor\":\"").append(color).append("\",");
            payload.append("\"sections\":[{");
            payload.append("\"activityTitle\":\"").append(title).append("\",");
            payload.append("\"activitySubtitle\":\"").append(subtitle).append("\",");
            payload.append("\"facts\":[");
            payload.append("{\"name\":\"Status\",\"value\":\"").append(statusIcon).append("\"},");
            payload.append("{\"name\":\"Total Tests\",\"value\":\"").append(totalTests).append("\"},");
            payload.append("{\"name\":\"Passed\",\"value\":\"").append(passed).append("\"},");
            payload.append("{\"name\":\"Failed\",\"value\":\"").append(failed).append("\"},");
            payload.append("{\"name\":\"Timestamp\",\"value\":\"").append(timestamp).append("\"}");
            if (htmlReportPath != null) {
                payload.append(",{\"name\":\"Karate Report\",\"value\":\"[View Report](").append(htmlReportPath).append(")\"}");
            }
            if (allureReportPath != null) {
                payload.append(",{\"name\":\"Allure Report\",\"value\":\"[View Allure Report](").append(allureReportPath).append(")\"}");
            }
            payload.append("]}]");
            
            // Add action buttons for reports
            boolean hasActions = htmlReportPath != null || allureReportPath != null;
            if (hasActions) {
                payload.append(",\"potentialAction\":[");
                boolean firstAction = true;
                
                if (htmlReportPath != null) {
                    if (!firstAction) payload.append(",");
                    payload.append("{");
                    payload.append("\"@type\":\"OpenUri\",");
                    payload.append("\"name\":\"📋 View Karate Report\",");
                    payload.append("\"targets\":[{\"os\":\"default\",\"uri\":\"").append(htmlReportPath).append("\"}]");
                    payload.append("}");
                    firstAction = false;
                }
                
                if (allureReportPath != null) {
                    if (!firstAction) payload.append(",");
                    payload.append("{");
                    payload.append("\"@type\":\"OpenUri\",");
                    payload.append("\"name\":\"📊 View Allure Report\",");
                    payload.append("\"targets\":[{\"os\":\"default\",\"uri\":\"").append(allureReportPath).append("\"}]");
                    payload.append("}");
                }
                
                payload.append("]");
            }
            payload.append("}");
            
            post.setEntity(new StringEntity(payload.toString()));
            
            try (CloseableHttpResponse response = client.execute(post)) {
                int statusCode = response.getStatusLine().getStatusCode();
                String responseBody = EntityUtils.toString(response.getEntity());
                
                if (statusCode == 200) {
                    result.put("success", true);
                    result.put("message", "Teams message sent successfully");
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
    
    public static Map<String, Object> sendSimpleTeamsMessage(String webhookUrl, String title, String message) {
        Map<String, Object> result = new HashMap<>();
        
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(webhookUrl);
            post.setHeader("Content-Type", "application/json");
            
            StringBuilder payload = new StringBuilder();
            payload.append("{");
            payload.append("\"@type\":\"MessageCard\",");
            payload.append("\"@context\":\"https://schema.org/extensions\",");
            payload.append("\"summary\":\"").append(title).append("\",");
            payload.append("\"text\":\"").append(message.replace("\"", "\\\"")).append("\"");
            payload.append("}");
            
            post.setEntity(new StringEntity(payload.toString()));
            
            try (CloseableHttpResponse response = client.execute(post)) {
                int statusCode = response.getStatusLine().getStatusCode();
                
                if (statusCode == 200) {
                    result.put("success", true);
                    result.put("message", "Teams message sent successfully");
                } else {
                    result.put("success", false);
                    result.put("error", "HTTP " + statusCode);
                }
            }
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }
}