package utilities;

import com.intuit.karate.Results;
import com.intuit.karate.Runner;
import java.util.List;

public class TestNotificationRunner {
    
    public static Results runWithNotifications(String... tags) {
        return runWithNotifications(null, tags);
    }
    
    public static Results runWithNotifications(String projectName, String... tags) {
        Results results = null;
        
        try {
            // Run tests
            if (tags != null && tags.length > 0) {
                results = Runner.path("classpath:")
                    .tags(tags)
                    .parallel(1);
            } else {
                results = Runner.path("classpath:")
                    .parallel(1);
            }
            
            // Send notification after test completion
            TestLifecycleHook.sendTestCompletionNotification(results, projectName);
            
        } catch (Exception e) {
            NotificationLogger.error("Error during test execution: " + e.getMessage(), e);
        }
        
        return results;
    }
    
    public static Results runProjectWithNotifications(String projectName) {
        Results results = null;
        
        try {
            // Run specific project tests
            String projectPath = "classpath:projects/" + projectName;
            results = Runner.path(projectPath)
                .parallel(1);
            
            // Send notification after test completion
            TestLifecycleHook.sendTestCompletionNotification(results, projectName);
            
        } catch (Exception e) {
            NotificationLogger.error("Error during project test execution: " + e.getMessage(), e);
        }
        
        return results;
    }
}