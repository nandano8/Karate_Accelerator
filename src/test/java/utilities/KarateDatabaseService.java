package utilities;

import java.sql.*;
import java.util.*;

public class KarateDatabaseService {
    private static Connection connection;
    private static PreparedStatement statement;
    private static ResultSet resultSet;
    
    public static void connect(String url, String username, String password) {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            throw new RuntimeException("Database connection failed: " + e.getMessage());
        }
    }
    
    public static List<Map<String, Object>> select(String query, Object[] params) {
        try {
            statement = connection.prepareStatement(query);
            setParameters(params);
            resultSet = statement.executeQuery();
            
            List<Map<String, Object>> results = new ArrayList<>();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object value = resultSet.getObject(i);
                    row.put(columnName, value);
                }
                results.add(row);
            }
            
            resultSet.close();
            statement.close();
            return results;
        } catch (SQLException e) {
            throw new RuntimeException("SELECT query failed: " + e.getMessage());
        }
    }
    
    public static int insert(String query, Object[] params) {
        return executeUpdate(query, params);
    }
    
    public static int update(String query, Object[] params) {
        return executeUpdate(query, params);
    }
    
    public static int delete(String query, Object[] params) {
        return executeUpdate(query, params);
    }
    
    private static int executeUpdate(String query, Object[] params) {
        try {
            statement = connection.prepareStatement(query);
            setParameters(params);
            int result = statement.executeUpdate();
            statement.close();
            return result;
        } catch (SQLException e) {
            throw new RuntimeException("Update query failed: " + e.getMessage());
        }
    }
    
    private static void setParameters(Object[] params) throws SQLException {
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
        }
    }
    
    public static void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Disconnect failed: " + e.getMessage());
        }
    }
    
    public static boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}