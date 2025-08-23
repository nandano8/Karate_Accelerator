package projects.projectEmailAPI;

import com.intuit.karate.junit5.Karate;

class EmailTestRunner {
    
    @Karate.Test
    Karate testEmail() {
        return Karate.run("features/email-test").relativeTo(getClass());
    }
}