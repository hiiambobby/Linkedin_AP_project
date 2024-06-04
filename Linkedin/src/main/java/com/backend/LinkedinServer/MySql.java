package com.backend.LinkedinServer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySql {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/userdatabase"; // Change to your database name
    private static final String USERNAME = "root"; // Change to your MySQL username
    private static final String PASSWORD = "saba5782"; // Change to your MySQL password

    public static Connection connection;

    private MySql() {

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