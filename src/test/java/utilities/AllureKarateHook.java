package utilities;

import com.intuit.karate.Results;
import com.intuit.karate.core.ScenarioResult;
import io.qameta.allure.Allure;

public class AllureKarateHook {
    
    public static void addAllureResults(Results results) {
        results.getScenarioResults().forEach(AllureKarateHook::processScenario);
    }
    
    private static void processScenario(ScenarioResult scenarioResult) {
        String scenarioName = scenarioResult.getScenario().getName();
        String featureName = scenarioResult.getScenario().getFeature().getName();
        
        Allure.feature(featureName);
        Allure.story(scenarioName);
        
        // Add test context
        AllureAttachmentHelper.addTestContext(featureName, scenarioName, "Scenario Execution");
        
        // Add performance metrics
        long duration = scenarioResult.getDurationNanos() / 1_000_000;
        AllureAttachmentHelper.addPerformanceMetrics(scenarioName, duration, !scenarioResult.isFailed());
        
        if (scenarioResult.isFailed()) {
            if (scenarioResult.getError() != null) {
                AllureAttachmentHelper.addErrorDetails(
                    scenarioResult.getError().getMessage(),
                    getStackTrace(scenarioResult.getError())
                );
            }
        }
    }
    
    private static String getStackTrace(Throwable throwable) {
        java.io.StringWriter sw = new java.io.StringWriter();
        java.io.PrintWriter pw = new java.io.PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }
}