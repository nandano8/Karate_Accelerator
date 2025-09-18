package utilities.projectOkta;

import com.intuit.karate.junit5.Karate;

class ProjectOkta_Runner {
    
    @Karate.Test
    Karate testOkta() {
        return Karate.run("classpath:projects/projectOkta/features").relativeTo(getClass());
    }
}