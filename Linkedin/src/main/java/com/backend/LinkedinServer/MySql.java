package com.backend.LinkedinServer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySql {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/your_database_name"; // Change to your database name
    private static final String USERNAME = "your_username"; // Change to your MySQL username
    private static final String PASSWORD = "your_password"; // Change to your MySQL password

    private static Connection connection;

    private MySql() {
        // Private constructor to prevent instantiation
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}