package projects.projectUtilities;

import com.intuit.karate.junit5.Karate;

class ProjectUtilities_Runner {
    
    @Karate.Test
    Karate testUtilities() {
        return Karate.run("classpath:projects/projectUtilities/features").relativeTo(getClass());
    }
}