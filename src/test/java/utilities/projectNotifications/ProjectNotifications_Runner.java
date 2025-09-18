package utilities.projectNotifications;

import com.intuit.karate.junit5.Karate;

class ProjectNotifications_Runner {
    
    @Karate.Test
    Karate testNotifications() {
        return Karate.run("classpath:utilities/projectNotifications/features")
                .systemProperty("project", "projectNotifications")
                .relativeTo(getClass());
    }
}