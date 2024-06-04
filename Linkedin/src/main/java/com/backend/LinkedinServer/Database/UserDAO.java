package com.backend.LinkedinServer.Database;

import com.backend.LinkedinServer.Model.User;
import com.backend.LinkedinServer.MySql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;


public class UserDAO {
    private Connection connection;

    public UserDAO() throws SQLException {
        connection = MySql.getConnection();
        createUserTable();
    }

    public void createUserTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS users ("
                + "id VARCHAR(36) PRIMARY KEY, "
                + "first_name VARCHAR(255), "
                + "last_name VARCHAR(255), "
                + "additional_name VARCHAR(255), "
                + "email VARCHAR(255), "
                + "password VARCHAR(255), "
                + "country VARCHAR(255), "
                + "city VARCHAR(255))"; // Add closing parenthesis here
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Or use a logger
            System.out.println("SQL Exception: " + e.getMessage());
        }
    }

    public void saveUser(User user) throws SQLException {
        String sql = "INSERT INTO users (id, first_name, last_name, additional_name, email, password, country, city) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getId());
            stmt.setString(2, user.getFirstName());
            stmt.setString(3, user.getLastName());
            stmt.setString(4, user.getAdditionalName());
            stmt.setString(5, user.getEmail());
            stmt.setString(6, user.getPassword());
            stmt.setString(7, user.getCountry());
            stmt.setString(8, user.getCity());
            stmt.executeUpdate();
        }
    }

    public User getUserById(String id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getString("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("additional_name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("country"),
                        rs.getString("city"));
            }
        }
        return null;
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                users.add(new User(
                        rs.getString("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("additional_name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("country"),
                        rs.getString("city")));
            }
        }
        return users;
    }

    public void updateUser(User user) throws SQLException {
        String sql = "UPDATE users SET first_name =?, last_name =?, additional_name =?, email =?, password =?, country =?, city =? WHERE id =?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getAdditionalName());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getPassword()); // Corrected order
            stmt.setString(6, user.getCountry()); // Corrected order
            stmt.setString(7, user.getCity());
            stmt.setString(8, user.getId());
            stmt.executeUpdate();
        }
    }


    public void deleteUser(String id) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    public boolean checkUserExists(String email, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // returns true if a user is found
        }
    }
}
