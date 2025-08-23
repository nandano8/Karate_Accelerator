import com.intuit.karate.Results;
import com.intuit.karate.Runner;
import utilities.TestLifecycleHook;

public class AutoNotificationRunner {
    
    public static void main(String[] args) {
        // Example: Run all tests with automatic notifications
        runAllTestsWithNotifications();
    }
    
    public static Results runAllTestsWithNotifications() {
        Results results = null;
        
        try {
            System.out.println("Starting test execution with automatic notifications...");
            
            // Run all tests
            results = Runner.path("classpath:")
                .outputCucumberJson(true)
                .outputJunitXml(true)
                .parallel(1);
            
            // Send notification
            TestLifecycleHook.sendTestCompletionNotification(results, "All Projects");
            
            System.out.println("Test execution completed. Notification sent.");
            
        } catch (Exception e) {
            System.err.println("Error during test execution: " + e.getMessage());
            e.printStackTrace();
        }
        
        return results;
    }
    
    public static Results runProjectWithNotifications(String projectName) {
        Results results = null;
        
        try {
            System.out.println("Starting " + projectName + " test execution with notifications...");
            
            // Set project system property
            System.setProperty("project", projectName);
            
            // Run project tests
            String projectPath = "classpath:projects/" + projectName;
            results = Runner.path(projectPath)
                .outputCucumberJson(true)
                .outputJunitXml(true)
                .parallel(1);
            
            // Send notification
            TestLifecycleHook.sendTestCompletionNotification(results, projectName);
            
            System.out.println(projectName + " test execution completed. Notification sent.");
            
        } catch (Exception e) {
            System.err.println("Error during " + projectName + " test execution: " + e.getMessage());
            e.printStackTrace();
        }
        
        return results;
    }
}