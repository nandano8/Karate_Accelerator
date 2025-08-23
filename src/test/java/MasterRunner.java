import com.intuit.karate.junit5.Karate;
import com.intuit.karate.Results;
import utilities.ReportNotifier;
import java.io.InputStream;
import java.util.Properties;

public class MasterRunner {
    
    @Karate.Test
    Results testSystemProperty(){
        Results results = Karate.run("classpath:projects")
        .tags("")
        .karateEnv("dev")
        .systemProperty("project","projectA")
        .parallel(1);
        
        sendReports(results);
        return results;
    }
    
    private void sendReports(Results results) {
        try {
            Properties config = new Properties();
            try (InputStream input = getClass().getClassLoader().getResourceAsStream("utilities/config.properties")) {
                if (input != null) {
                    config.load(input);
                    
                    int failed = results.getFailCount();
                    int total = (int) results.getSuite().getFeatureResults().count();
                    int passed = total - failed;
                
                String teamsWebhook = config.getProperty("teams.webhook.url");
                if (teamsWebhook != null && !teamsWebhook.contains("https://exavalu.webhook.office.com/webhookb2/9d18104c-03c1-4298-ac50-d2c9b5b72314@ee19a561-5b69-430d-9e17-a12af3c2b3bc/IncomingWebhook/4a8c974dbab94e9d85fe39360d118087/4f3c32c1-5387-4552-ac0f-28dbaf75a354/V21EOMg4a6tyvVLvY_Q6nBTK1vDRtSC7-Sy9FCY0hgE2Q1")) {
                    ReportNotifier.sendTeamsReport(teamsWebhook, "ExaAPI Framework", total, passed, failed);
                }
                
                String smtpHost = config.getProperty("email.smtp.host");
                String username = config.getProperty("email.username");
                String password = config.getProperty("email.password");
                String from = config.getProperty("email.from");
                String to = config.getProperty("email.to");
                
                    if (username != null && !username.contains("your-email")) {
                        ReportNotifier.sendEmailReport(smtpHost, username, password, from, to, "ExaAPI Framework", total, passed, failed);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Report notification failed: " + e.getMessage());
        }
    }

}
