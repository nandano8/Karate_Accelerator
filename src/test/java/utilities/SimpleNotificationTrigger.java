package utilities;

import com.intuit.karate.Results;
import com.intuit.karate.Runner;

public class SimpleNotificationTrigger {
    
    public static void main(String[] args) {
        sendNotifications();
    }
    
    public static void sendNotifications() {
        try {
            // Generate Allure report
            AllureReportService.generateAllureReport();
            
            // Create mock results using Runner (bypasses constructor issue)
            Results results = createMockResults();
            
            // Send notifications
            TestLifecycleHook.sendTestCompletionNotification(results, "projectSQL");
            
            System.out.println("✅ Notifications sent successfully!");
            
        } catch (Exception e) {
            System.err.println("❌ Failed to send notifications: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static Results createMockResults() {
        // Use Runner with empty classpath to get a valid Results object
        return Runner.path("classpath:").parallel(1);
    }
}