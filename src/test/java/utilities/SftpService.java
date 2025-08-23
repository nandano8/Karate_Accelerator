package utilities;

import com.jcraft.jsch.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SftpService {
    
    public static Map<String, Object> transferFile(String sourceHost, int sourcePort, String sourceUser, String sourcePassword,
                                                  String sourcePath, String targetHost, int targetPort, String targetUser, 
                                                  String targetPassword, String targetPath) {
        Map<String, Object> result = new HashMap<>();
        Session sourceSession = null;
        Session targetSession = null;
        ChannelSftp sourceSftp = null;
        ChannelSftp targetSftp = null;
        
        try {
            JSch jsch = new JSch();
            
            // Connect to source SFTP
            sourceSession = jsch.getSession(sourceUser, sourceHost, sourcePort);
            sourceSession.setPassword(sourcePassword);
            sourceSession.setConfig("StrictHostKeyChecking", "no");
            sourceSession.connect();
            sourceSftp = (ChannelSftp) sourceSession.openChannel("sftp");
            sourceSftp.connect();
            
            // Connect to target SFTP
            targetSession = jsch.getSession(targetUser, targetHost, targetPort);
            targetSession.setPassword(targetPassword);
            targetSession.setConfig("StrictHostKeyChecking", "no");
            targetSession.connect();
            targetSftp = (ChannelSftp) targetSession.openChannel("sftp");
            targetSftp.connect();
            
            // Transfer file
            InputStream inputStream = sourceSftp.get(sourcePath);
            targetSftp.put(inputStream, targetPath);
            
            result.put("success", true);
            result.put("message", "File transferred successfully");
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
        } finally {
            if (sourceSftp != null) sourceSftp.disconnect();
            if (targetSftp != null) targetSftp.disconnect();
            if (sourceSession != null) sourceSession.disconnect();
            if (targetSession != null) targetSession.disconnect();
        }
        
        return result;
    }
    
    public static Map<String, Object> uploadFile(String host, int port, String user, String password, 
                                                String localPath, String remotePath) {
        Map<String, Object> result = new HashMap<>();
        Session session = null;
        ChannelSftp sftp = null;
        
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(user, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            
            sftp = (ChannelSftp) session.openChannel("sftp");
            sftp.connect();
            
            sftp.put(localPath, remotePath);
            
            result.put("success", true);
            result.put("message", "File uploaded successfully");
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
        } finally {
            if (sftp != null) sftp.disconnect();
            if (session != null) session.disconnect();
        }
        
        return result;
    }
    
    public static Map<String, Object> downloadFile(String host, int port, String user, String password, 
                                                  String remotePath, String localPath) {
        Map<String, Object> result = new HashMap<>();
        Session session = null;
        ChannelSftp sftp = null;
        
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(user, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            
            sftp = (ChannelSftp) session.openChannel("sftp");
            sftp.connect();
            
            sftp.get(remotePath, localPath);
            
            result.put("success", true);
            result.put("message", "File downloaded successfully");
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
        } finally {
            if (sftp != null) sftp.disconnect();
            if (session != null) session.disconnect();
        }
        
        return result;
    }
}