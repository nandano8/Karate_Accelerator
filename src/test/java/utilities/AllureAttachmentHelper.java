package utilities;

import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AllureAttachmentHelper {
    
    public static void addTextAttachment(String name, String content) {
        Allure.addAttachment(name, "text/plain", content);
    }
    
    public static void addJsonAttachment(String name, String jsonContent) {
        Allure.addAttachment(name, "application/json", jsonContent);
    }
    
    public static void addHtmlAttachment(String name, String htmlContent) {
        Allure.addAttachment(name, "text/html", htmlContent);
    }
    
    public static void addCsvAttachment(String name, String csvContent) {
        Allure.addAttachment(name, "text/csv", csvContent);
    }
    
    public static void addRequestDetails(String method, String url, String requestBody, String responseBody, int statusCode) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        String requestDetails = String.format(
            "=== API Request Details ===\n" +
            "Timestamp: %s\n" +
            "Method: %s\n" +
            "URL: %s\n" +
            "Status Code: %d\n" +
            "Request Body:\n%s\n\n" +
            "Response Body:\n%s",
            timestamp, method, url, statusCode, 
            requestBody != null ? requestBody : "N/A",
            responseBody != null ? responseBody : "N/A"
        );
        
        addTextAttachment("API Request Details", requestDetails);
        
        if (requestBody != null) {
            addJsonAttachment("Request Body", requestBody);
        }
        if (responseBody != null) {
            addJsonAttachment("Response Body", responseBody);
        }
    }
    
    public static void addDatabaseQueryDetails(String query, Object[] params, Object result) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        String queryDetails = String.format(
            "=== Database Query Details ===\n" +
            "Timestamp: %s\n" +
            "Query: %s\n" +
            "Parameters: %s\n" +
            "Result: %s",
            timestamp, query,
            params != null ? java.util.Arrays.toString(params) : "None",
            result != null ? result.toString() : "N/A"
        );
        
        addTextAttachment("Database Query", queryDetails);
    }
    
    public static void addTestContext(String featureName, String scenarioName, String stepName) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        String context = String.format(
            "=== Test Context ===\n" +
            "Feature: %s\n" +
            "Scenario: %s\n" +
            "Step: %s\n" +
            "Timestamp: %s\n" +
            "Thread: %s",
            featureName, scenarioName, stepName, timestamp,
            Thread.currentThread().getName()
        );
        
        addTextAttachment("Test Context", context);
    }
    
    public static void addErrorDetails(String errorMessage, String stackTrace) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        String errorDetails = String.format(
            "=== Error Details ===\n" +
            "Timestamp: %s\n" +
            "Error Message: %s\n\n" +
            "Stack Trace:\n%s",
            timestamp, errorMessage, stackTrace
        );
        
        addTextAttachment("Error Details", errorDetails);
    }
    
    public static void addPerformanceMetrics(String operation, long duration, boolean success) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        String metrics = String.format(
            "=== Performance Metrics ===\n" +
            "Operation: %s\n" +
            "Duration: %d ms\n" +
            "Status: %s\n" +
            "Performance: %s\n" +
            "Timestamp: %s",
            operation, duration, 
            success ? "SUCCESS" : "FAILED",
            duration < 1000 ? "FAST" : duration < 3000 ? "MODERATE" : "SLOW",
            timestamp
        );
        
        addTextAttachment("Performance Metrics", metrics);
    }
}