package projects.projectSQL;

import com.intuit.karate.junit5.Karate;
import org.junit.jupiter.api.AfterAll;
import utilities.TestLifecycleHook;

class ProjectSQL_Runner {
    
    @Karate.Test
    Karate testAll() {
        return Karate.run("classpath:projects/projectSQL/features")
                .systemProperty("project", "projectSQL");
    }
    
    @AfterAll
    static void sendNotifications() {
        try {
            // Create basic results for notification
            com.intuit.karate.Results results = com.intuit.karate.Runner
                .path("classpath:projects/projectSQL/features")
                .parallel(1);
            TestLifecycleHook.sendTestCompletionNotification(results, "projectSQL");
        } catch (Exception e) {
            System.err.println("Failed to send notifications: " + e.getMessage());
        }
    }
}