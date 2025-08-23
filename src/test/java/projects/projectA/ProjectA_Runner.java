package projects.projectA;

import com.intuit.karate.junit5.Karate;

public class ProjectA_Runner {

       @Karate.Test
    Karate testSystemProperty(){
        return Karate.run("classpath:projects/projectA")
        .tags("login")
        .karateEnv("dev")
        .systemProperty("project","projectA");
    }
    
}
