package com.backend.LinkedinServer.Database;

import com.backend.LinkedinServer.Model.ContactInfo;
import com.backend.LinkedinServer.MySql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class ContactInfoDAO implements sqlOperations{
    private Connection connection;

    public ContactInfoDAO() throws SQLException {
        connection = MySql.getConnection();
        createTable();
    }

    private void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS contactInfo ("
                + "userId VARCHAR(16) PRIMARY KEY, "
                + "profileUrl VARCHAR(255), "
                + "email VARCHAR(255), "
                + "phoneNumber VARCHAR(255), "
                + "phoneType VARCHAR(255), "
                + "address VARCHAR(255), "
                + "instantMessaging VARCHAR(255), "
                + "birthday DATE)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        }
    }


    public void save(ContactInfo contactInfo) {
        String sql = "INSERT INTO users (userId, profileUrl, email, phoneNumber, phoneType, address, instantMessaging, birthday) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, contactInfo.getUserId());
            stmt.setString(2, contactInfo.getProfileUrl());
            stmt.setString(3, contactInfo.getEmail());
            stmt.setString(4, contactInfo.getPhoneNumber());
            stmt.setString(5, contactInfo.getPhoneType());
            stmt.setString(6, contactInfo.getAddress());
            stmt.setString(7, contactInfo.getInstantMessaging());
            stmt.setDate(8, java.sql.Date.valueOf(contactInfo.getBirthday()));

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void update(ContactInfo contactInfo)
    {
        String sql = "UPDATE users SET userId =?, profileUrl =?, email =?,phoneNumber =?, phoneType =?, address =?,instantMessaging=? ," +
                "birthday =? WHERE id =?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, contactInfo.getUserId());
            stmt.setString(2, contactInfo.getProfileUrl());
            stmt.setString(3, contactInfo.getEmail());
            stmt.setString(4, contactInfo.getPhoneNumber());
            stmt.setString(5, contactInfo.getPhoneType());
            stmt.setString(6, contactInfo.getAddress());
            stmt.setString(7, contactInfo.getInstantMessaging());
            stmt.setDate(8, java.sql.Date.valueOf(contactInfo.getBirthday()));

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void delete(String userId) throws SQLException {
        String sql = "DELETE FROM contactInfo WHERE userId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userId);
            stmt.executeUpdate();
        }
    }

}



