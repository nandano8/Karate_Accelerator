package utilities;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class OktaTokenService {
    
    public static Map<String, Object> generateClientCredentialsToken(String oktaDomain, String clientId, String clientSecret, String scope) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // Encode client credentials
            String credentials = clientId + ":" + clientSecret;
            String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
            
            // Prepare token request data
            Map<String, Object> tokenRequest = new HashMap<>();
            tokenRequest.put("url", oktaDomain + "/oauth2/default/v1/token");
            tokenRequest.put("method", "POST");
            
            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Basic " + encodedCredentials);
            headers.put("Content-Type", "application/x-www-form-urlencoded");
            tokenRequest.put("headers", headers);
            
            String formData = "grant_type=client_credentials&scope=" + (scope != null ? scope : "");
            tokenRequest.put("form", formData);
            
            result.put("success", true);
            result.put("tokenRequest", tokenRequest);
            result.put("encodedCredentials", encodedCredentials);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }
    
    public static Map<String, Object> generateAuthorizationCodeToken(String oktaDomain, String clientId, String clientSecret, String authCode, String redirectUri) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            String credentials = clientId + ":" + clientSecret;
            String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
            
            Map<String, Object> tokenRequest = new HashMap<>();
            tokenRequest.put("url", oktaDomain + "/oauth2/default/v1/token");
            tokenRequest.put("method", "POST");
            
            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Basic " + encodedCredentials);
            headers.put("Content-Type", "application/x-www-form-urlencoded");
            tokenRequest.put("headers", headers);
            
            String formData = "grant_type=authorization_code&code=" + authCode + "&redirect_uri=" + redirectUri;
            tokenRequest.put("form", formData);
            
            result.put("success", true);
            result.put("tokenRequest", tokenRequest);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }
}