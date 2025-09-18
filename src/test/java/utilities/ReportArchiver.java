package utilities;

import java.io.*;
import java.nio.file.*;
import java.util.zip.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReportArchiver {
    
    private static final String KARATE_REPORTS_DIR = "target/karate-reports";
    private static final String ALLURE_REPORTS_DIR = "target/allure-report";
    private static final String ARCHIVE_DIR = "target/report-archives";
    
    public static String createReportsArchive(String projectName) {
        try {
            // Create archive directory if it doesn't exist
            Files.createDirectories(Paths.get(ARCHIVE_DIR));
            
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String zipFileName = String.format("%s/%s_TestReports_%s.zip", ARCHIVE_DIR, projectName, timestamp);
            
            try (FileOutputStream fos = new FileOutputStream(zipFileName);
                 ZipOutputStream zos = new ZipOutputStream(fos)) {
                
                // Add Karate reports
                addDirectoryToZip(zos, KARATE_REPORTS_DIR, "karate-reports/");
                
                // Add Allure reports
                addDirectoryToZip(zos, ALLURE_REPORTS_DIR, "allure-report/");
            }
            
            return zipFileName;
        } catch (Exception e) {
            System.err.println("Failed to create reports archive: " + e.getMessage());
            return null;
        }
    }
    
    private static void addDirectoryToZip(ZipOutputStream zos, String sourceDir, String zipPath) throws IOException {
        Path sourcePath = Paths.get(sourceDir);
        if (!Files.exists(sourcePath)) {
            return;
        }
        
        Files.walk(sourcePath)
             .filter(Files::isRegularFile)
             .forEach(file -> {
                 try {
                     String relativePath = zipPath + sourcePath.relativize(file).toString().replace("\\", "/");
                     ZipEntry zipEntry = new ZipEntry(relativePath);
                     zos.putNextEntry(zipEntry);
                     Files.copy(file, zos);
                     zos.closeEntry();
                 } catch (IOException e) {
                     System.err.println("Failed to add file to zip: " + file);
                 }
             });
    }
    
    public static void main(String[] args) {
        String projectName = args.length > 0 ? args[0] : "TestProject";
        String archivePath = createReportsArchive(projectName);
        if (archivePath != null) {
            System.out.println("Reports archive created: " + archivePath);
        } else {
            System.err.println("Failed to create reports archive");
        }
    }
}