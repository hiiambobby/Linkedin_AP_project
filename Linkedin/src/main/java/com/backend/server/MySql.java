package com.backend.server;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySql {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/userdatabase";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "saba5782";

//    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/mysql";
//    private static final String USERNAME = "root";
//    private static final String PASSWORD = "Iya07Ydi04#!";

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