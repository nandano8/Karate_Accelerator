# ExaAPI
API Automation

Created Files:
PostgresUtil.java - Core database connection and operation class
DatabaseHelper.java - Static helper for easy access from feature files
database-test.feature - Sample usage demonstration

Key Features:
Connection Management: Handles PostgreSQL connections with proper resource cleanup
Query Execution: Supports both SELECT queries returning result sets and UPDATE/INSERT operations
Karate Integration: Available globally through DatabaseHelper in all feature files
Simple API: Connect, query/update, disconnect pattern


PostgreSQL JDBC driver (org.postgresql:postgresql dependency)
DriverManager.getConnection() - standard JDBC connection method
PreparedStatement - JDBC statement interface
ResultSet - JDBC result interface
Java utility class  - ExaAPI-master\src\test\java\utilities\PostgresUtil.java
implemented as JDBC connection for parameter binding and result parsing.
