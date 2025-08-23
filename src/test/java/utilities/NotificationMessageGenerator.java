package utilities;

import com.intuit.karate.Results;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NotificationMessageGenerator {
    
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public static String generateSubject(Results results, String projectName) {
        String status = results.getFailCount() > 0 ? "FAILED" : "PASSED";
        return String.format("Test Execution %s - %s", status, projectName != null ? projectName : "Karate Tests");
    }
    
    public static String generateMessage(Results results, String projectName) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        String status = results.getFailCount() > 0 ? "❌ FAILED" : "✅ PASSED";
        
        StringBuilder message = new StringBuilder();
        message.append("🚀 **Test Execution Report**\\n\\n");
        message.append("**Project:** ").append(projectName != null ? projectName : "Karate Framework").append("\\n");
        message.append("**Status:** ").append(status).append("\\n");
        message.append("**Timestamp:** ").append(timestamp).append("\\n\\n");
        message.append("📊 **Results Summary:**\\n");
        message.append("• Total Features: ").append(results.getFeaturesPassed() + results.getFeaturesFailed()).append("\\n");
        message.append("• Total Scenarios: ").append(results.getScenariosPassed() + results.getScenariosFailed()).append("\\n");
        message.append("• Passed: ").append(results.getScenariosPassed()).append("\\n");
        message.append("• Failed: ").append(results.getFailCount()).append("\\n");
        message.append("• Duration: ").append(String.format("%.2f", results.getElapsedTime() / 1000.0)).append(" seconds\\n\\n");
        
        if (results.getFailCount() > 0) {
            message.append("⚠️ **Action Required:** Please check the failed test cases and resolve issues.\\n");
        } else {
            message.append("🎉 **All tests passed successfully!**\\n");
        }
        
        return message.toString();
    }
    
    public static String getSlackColor(Results results) {
        return results.getFailCount() > 0 ? "danger" : "good";
    }
    
    public static String getHtmlReportPath() {
        return "file:///" + System.getProperty("user.dir").replace("\\\\", "/") + "/target/karate-reports/karate-summary.html";
    }
    
    public static String getEscapedHtmlReportPath() {
        return System.getProperty("user.dir").replace("\\\\", "/") + "/target/karate-reports/karate-summary.html";
    }
}