package utilities;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class SecureTransferService {
    
    public static Map<String, Object> encryptAndTransfer(String sourceHost, int sourcePort, String sourceUser, String sourcePassword,
                                                        String sourcePath, String targetHost, int targetPort, String targetUser, 
                                                        String targetPassword, String targetPath, String encryptionKey) {
        Map<String, Object> result = new HashMap<>();
        String tempDir = System.getProperty("java.io.tmpdir");
        String tempFile = tempDir + "/encrypted_" + System.currentTimeMillis() + ".tmp";
        
        try {
            // Download from source
            Map<String, Object> downloadResult = SftpService.downloadFile(sourceHost, sourcePort, sourceUser, sourcePassword, sourcePath, tempFile);
            if (!(Boolean) downloadResult.get("success")) {
                return downloadResult;
            }
            
            // Read and encrypt file
            String fileContent = new String(Files.readAllBytes(Paths.get(tempFile)));
            Map<String, Object> encryptResult = EncryptionService.encryptAES(fileContent, encryptionKey);
            if (!(Boolean) encryptResult.get("success")) {
                return encryptResult;
            }
            
            // Write encrypted content to temp file
            String encryptedTempFile = tempDir + "/encrypted_final_" + System.currentTimeMillis() + ".tmp";
            Files.write(Paths.get(encryptedTempFile), ((String) encryptResult.get("encrypted")).getBytes());
            
            // Upload encrypted file to target
            Map<String, Object> uploadResult = SftpService.uploadFile(targetHost, targetPort, targetUser, targetPassword, encryptedTempFile, targetPath);
            
            // Cleanup
            Files.deleteIfExists(Paths.get(tempFile));
            Files.deleteIfExists(Paths.get(encryptedTempFile));
            
            result.put("success", uploadResult.get("success"));
            result.put("message", "File encrypted and transferred successfully");
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }
    
    public static Map<String, Object> downloadAndDecrypt(String host, int port, String user, String password, 
                                                        String remotePath, String localPath, String encryptionKey) {
        Map<String, Object> result = new HashMap<>();
        String tempFile = System.getProperty("java.io.tmpdir") + "/encrypted_download_" + System.currentTimeMillis() + ".tmp";
        
        try {
            // Download encrypted file
            Map<String, Object> downloadResult = SftpService.downloadFile(host, port, user, password, remotePath, tempFile);
            if (!(Boolean) downloadResult.get("success")) {
                return downloadResult;
            }
            
            // Read and decrypt
            String encryptedContent = new String(Files.readAllBytes(Paths.get(tempFile)));
            Map<String, Object> decryptResult = EncryptionService.decryptAES(encryptedContent, encryptionKey);
            if (!(Boolean) decryptResult.get("success")) {
                return decryptResult;
            }
            
            // Write decrypted content
            Files.write(Paths.get(localPath), ((String) decryptResult.get("decrypted")).getBytes());
            
            // Cleanup
            Files.deleteIfExists(Paths.get(tempFile));
            
            result.put("success", true);
            result.put("message", "File downloaded and decrypted successfully");
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }
}