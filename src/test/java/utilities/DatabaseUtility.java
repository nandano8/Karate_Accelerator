package utilities;

import java.sql.*;
import java.util.*;

/**
 * Database Utility for Karate Framework
 * Provides static methods for database operations that can be used in any feature file
 */
public class DatabaseUtility {
    
    private static Connection connection;
    
    /**
     * Connect to database
     * Usage in feature: * def dbConnection = Java.type('utilities.DatabaseUtility').connect(url, user, password)
     */
    public static boolean connect(String url, String username, String password) {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, username, password);
            return true;
        } catch (Exception e) {
            System.err.println("Database connection failed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Execute SELECT query
     * Usage in feature: * def results = Java.type('utilities.DatabaseUtility').executeQuery('SELECT * FROM table WHERE id = ?', [1])
     */
    public static List<Map<String, Object>> executeQuery(String query, Object[] params) {
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            setParameters(statement, params);
            ResultSet resultSet = statement.executeQuery();
            
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
            throw new RuntimeException("Query execution failed: " + e.getMessage());
        }
    }
    
    /**
     * Execute INSERT/UPDATE/DELETE query
     * Usage in feature: * def rowsAffected = Java.type('utilities.DatabaseUtility').executeUpdate('INSERT INTO table VALUES (?, ?)', ['value1', 'value2'])
     */
    public static int executeUpdate(String query, Object[] params) {
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            setParameters(statement, params);
            int result = statement.executeUpdate();
            statement.close();
            return result;
        } catch (SQLException e) {
            throw new RuntimeException("Update execution failed: " + e.getMessage());
        }
    }
    
    /**
     * Execute batch operations
     * Usage in feature: * def batchResults = Java.type('utilities.DatabaseUtility').executeBatch(query, paramsList)
     */
    public static int[] executeBatch(String query, List<Object[]> paramsList) {
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            
            for (Object[] params : paramsList) {
                setParameters(statement, params);
                statement.addBatch();
            }
            
            int[] results = statement.executeBatch();
            statement.close();
            return results;
        } catch (SQLException e) {
            throw new RuntimeException("Batch execution failed: " + e.getMessage());
        }
    }
    
    /**
     * Check if table exists
     * Usage in feature: * def tableExists = Java.type('utilities.DatabaseUtility').tableExists('table_name')
     */
    public static boolean tableExists(String tableName) {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getTables(null, null, tableName, null);
            boolean exists = resultSet.next();
            resultSet.close();
            return exists;
        } catch (SQLException e) {
            return false;
        }
    }
    
    /**
     * Get table row count
     * Usage in feature: * def rowCount = Java.type('utilities.DatabaseUtility').getRowCount('table_name')
     */
    public static int getRowCount(String tableName) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM " + tableName);
            resultSet.next();
            int count = resultSet.getInt(1);
            resultSet.close();
            statement.close();
            return count;
        } catch (SQLException e) {
            return -1;
        }
    }
    
    /**
     * Disconnect from database
     * Usage in feature: * Java.type('utilities.DatabaseUtility').disconnect()
     */
    public static boolean disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                return true;
            }
            return false;
        } catch (SQLException e) {
            return false;
        }
    }
    
    /**
     * Check if connected
     * Usage in feature: * def isConnected = Java.type('utilities.DatabaseUtility').isConnected()
     */
    public static boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
    
    private static void setParameters(PreparedStatement statement, Object[] params) throws SQLException {
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
        }
    }
}