package com.backend.LinkedinServer.Database;

import com.backend.LinkedinServer.Model.User;
import com.backend.LinkedinServer.MySql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public void create(User user) throws SQLException {
        String sql = "INSERT INTO user (id, firstName, lastName, additionalName, email, phoneNumber, password, country, birthday) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = MySql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getId());
            stmt.setString(2, user.getFirstName());
            stmt.setString(3, user.getLastName());
            stmt.setString(4, user.getAdditionalName());
            stmt.setString(5, user.getEmail());
            stmt.setString(6, user.getPhoneNumber());
            stmt.setString(7, user.getPassword());
         //   stmt.setString(8, user.getConfirmPassword());
            stmt.setString(8, user.getLocation());
            stmt.setDate(9, java.sql.Date.valueOf(user.getBirthday()));
            stmt.executeUpdate();
        }
    }

    public User getUserById(String id) throws SQLException {
        String sql = "SELECT * FROM user WHERE id = ?";
        try (Connection conn = MySql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getString("id"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("additionalName"),
                        rs.getString("email"),
                        rs.getString("phoneNumber"),
                        rs.getString("password"),
                       // rs.getString("confirmPassword"),
                        rs.getString("country"),
                        rs.getDate("birthday").toLocalDate()
                );
            }
        }
        return null;
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM user";
        try (Connection conn = MySql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                users.add(new User(
                        rs.getString("id"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("additionalName"),
                        rs.getString("email"),
                        rs.getString("phoneNumber"),
                        rs.getString("password"),
                   //     rs.getString("confirmPassword"),
                        rs.getString("country"),
                        rs.getDate("birthday").toLocalDate()
                ));
            }
        }
        return users;
    }

    public void updateUser(User user) throws SQLException {
        String sql = "UPDATE user SET firstName = ?, lastName = ?, additionalName = ?, email = ?, phoneNumber = ?, password = ?, country = ?, birthday = ? WHERE id = ?";
        try (Connection conn = MySql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getAdditionalName());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getPhoneNumber());
            stmt.setString(6, user.getPassword());
           // stmt.setString(7, user.getConfirmPassword());
            stmt.setString(7, user.getLocation());
            stmt.setDate(8, java.sql.Date.valueOf(user.getBirthday()));
            stmt.setString(9, user.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteUser(String id) throws SQLException {
        String sql = "DELETE FROM user WHERE id = ?";
        try (Connection conn = MySql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }
    public boolean checkUserExists(String email, String password) throws SQLException {
        String sql = "SELECT * FROM user WHERE email = ? AND password = ?";
        try (Connection conn = MySql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // returns true if a user is found
        }
    }
}
