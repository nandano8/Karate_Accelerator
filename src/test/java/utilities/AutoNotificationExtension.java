package utilities;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import com.intuit.karate.Results;
import com.intuit.karate.Runner;

public class AutoNotificationExtension implements AfterAllCallback {
    
    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        String projectName = System.getProperty("project");
        if (projectName != null) {
            NotificationLogger.info("AutoNotificationExtension triggered for project: " + projectName);
            
            // Get results from the latest execution
            Results results = getLatestResults(context);
            if (results != null) {
                NotificationLogger.info("Sending notifications with results: " + results.getScenariosPassed() + " passed, " + results.getFailCount() + " failed");
                TestLifecycleHook.sendTestCompletionNotification(results, projectName);
            } else {
                NotificationLogger.warning("No results found, creating mock results for notification");
                // Create basic results for notification
                Results mockResults = createBasicResults();
                if (mockResults != null) {
                    TestLifecycleHook.sendTestCompletionNotification(mockResults, projectName);
                } else {
                    NotificationLogger.error("Failed to create mock results for notification");
                }
            }
        } else {
            NotificationLogger.info("No project name found, skipping notifications");
        }
    }
    
    private Results getLatestResults(ExtensionContext context) {
        try {
            // Read from karate-summary.json if available
            String summaryPath = "target/karate-reports/karate-summary.json";
            java.io.File summaryFile = new java.io.File(summaryPath);
            if (summaryFile.exists()) {
                // Create a mock Results object with basic info
                return createMockResults();
            }
        } catch (Exception e) {
            NotificationLogger.error("Could not read test results: " + e.getMessage(), e);
        }
        return null;
    }
    
    private Results createMockResults() {
        // Run a quick execution to get actual results
        try {
            String projectName = System.getProperty("project");
            String featuresPath = "classpath:projects/" + projectName + "/features";
            return Runner.path(featuresPath).parallel(1);
        } catch (Exception e) {
            NotificationLogger.error("Could not get results: " + e.getMessage(), e);
            return null;
        }
    }
    
    private Results createBasicResults() {
        // Run a simple test to get actual results
        try {
            return Runner.path("classpath:").parallel(1);
        } catch (Exception e) {
            NotificationLogger.error("Could not create results: " + e.getMessage(), e);
            return null;
        }
    }
}