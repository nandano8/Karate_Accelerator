package utilities;

public class NotificationLogger {
    
    private static final String PREFIX = "[NOTIFICATION] ";
    
    public static void info(String message) {
        System.out.println(PREFIX + "ℹ️ " + message);
    }
    
    public static void success(String message) {
        System.out.println(PREFIX + "✅ " + message);
    }
    
    public static void warning(String message) {
        System.out.println(PREFIX + "⚠️ " + message);
    }
    
    public static void error(String message) {
        System.err.println(PREFIX + "❌ " + message);
    }
    
    public static void error(String message, Throwable throwable) {
        System.err.println(PREFIX + "❌ " + message);
        if (throwable != null) {
            throwable.printStackTrace();
        }
    }
    
    public static void debug(String message) {
        if (isDebugEnabled()) {
            System.out.println(PREFIX + "🔍 " + message);
        }
    }
    
    private static boolean isDebugEnabled() {
        return "true".equalsIgnoreCase(System.getProperty("notification.debug", "false"));
    }
}