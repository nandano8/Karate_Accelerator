package projects.projectB;

import com.intuit.karate.junit5.Karate;

public class ProjectB_Runner {

    @Karate.Test
    Karate testSystemProperty(){
        return Karate.run("classpath:projects/projectB")
        .tags("demo")
        .karateEnv("dev")
        .systemProperty("project","projectB");
    }
    
}
