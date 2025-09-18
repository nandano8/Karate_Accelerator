package utilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AllureReportService {
    
    private static final String ALLURE_RESULTS_DIR = "target/allure-results";
    private static final String ALLURE_REPORT_DIR = "target/allure-report";
    
    public static boolean generateAllureReport() {
        try {
            // Generate Allure report using Maven plugin
            ProcessBuilder pb = new ProcessBuilder("mvn", "allure:report");
            pb.directory(new File(System.getProperty("user.dir")));
            Process process = pb.start();
            int exitCode = process.waitFor();
            
            return exitCode == 0;
        } catch (Exception e) {
            System.err.println("Failed to generate Allure report: " + e.getMessage());
            return false;
        }
    }
    
    public static String getAllureReportPath() {
        String baseDir = System.getProperty("user.dir");
        return baseDir + File.separator + ALLURE_REPORT_DIR + File.separator + "index.html";
    }
    
    public static String getAllureReportUrl() {
        return "file:///" + getAllureReportPath().replace("\\", "/");
    }
    
    public static boolean hasAllureResults() {
        Path resultsPath = Paths.get(ALLURE_RESULTS_DIR);
        try {
            return Files.exists(resultsPath) && 
                   Files.list(resultsPath).anyMatch(path -> path.toString().endsWith(".json"));
        } catch (IOException e) {
            return false;
        }
    }
    
    public static void cleanAllureResults() {
        try {
            Path resultsPath = Paths.get(ALLURE_RESULTS_DIR);
            if (Files.exists(resultsPath)) {
                Files.walk(resultsPath)
                     .filter(Files::isRegularFile)
                     .forEach(path -> {
                         try {
                             Files.delete(path);
                         } catch (IOException e) {
                             System.err.println("Failed to delete: " + path);
                         }
                     });
            }
        } catch (IOException e) {
            System.err.println("Failed to clean Allure results: " + e.getMessage());
        }
    }
}