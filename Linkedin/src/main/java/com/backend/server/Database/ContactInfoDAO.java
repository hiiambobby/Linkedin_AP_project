package com.backend.server.Database;

import com.backend.server.Model.ContactInfo;
import com.backend.server.MySql;

import java.sql.*;

public class ContactInfoDAO {

    private final Connection connection;

    public ContactInfoDAO() throws SQLException {
        this.connection = MySql.getConnection(); // Assumes you have a DatabaseConnection class for obtaining connections
        createTable();
    }

    private void createTable() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS contact_info ("
                + "user_id VARCHAR(255) PRIMARY KEY,"
                + "profile_url VARCHAR(255),"
                + "phone_number VARCHAR(20),"
                + "phone_type VARCHAR(20),"
                + "month VARCHAR(20),"
                + "day INT,"
                + "visibility VARCHAR(50),"
                + "address VARCHAR(255),"
                + "instant_messaging VARCHAR(255)"
                + ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        }
    }


    // Create a new contact information entry
    public void createContactInfo(ContactInfo contactInfo) throws SQLException {
        String sql = "INSERT INTO contact_info (user_id, profile_url, phone_number, phone_type, month, day, visibility, address, instant_messaging) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, contactInfo.getUserId());
            ps.setString(2, contactInfo.getProfileUrl());
            ps.setString(3, contactInfo.getPhoneNumber());
            ps.setString(4, contactInfo.getPhoneType());
            ps.setString(5, contactInfo.getMonth());
            ps.setInt(6, contactInfo.getDay());
            ps.setString(7, contactInfo.getVisibility());
            ps.setString(8, contactInfo.getAddress());
            ps.setString(9, contactInfo.getInstantMessaging());
            ps.executeUpdate();
        }
    }

    // Retrieve contact information by user ID
    public ContactInfo getContactInfoByUserId(String userId) throws SQLException {
        String sql = "SELECT * FROM contact_info WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new ContactInfo(
                        rs.getString("user_id"),
                        rs.getString("profile_url"),
                        rs.getString("phone_number"),
                        rs.getString("phone_type"),
                        rs.getString("month"),
                        rs.getInt("day"),
                        rs.getString("visibility"),
                        rs.getString("address"),
                        rs.getString("instant_messaging")
                );
            }
            return null; // No contact info found for the given user ID
        }
    }

    // Update existing contact information
    public void updateContactInfo(ContactInfo contactInfo) throws SQLException {
        String sql = "UPDATE contact_info SET profile_url = ?, phone_number = ?, phone_type = ?, month = ?, day = ?, visibility = ?, address = ?, instant_messaging = ? WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, contactInfo.getProfileUrl());
            ps.setString(2, contactInfo.getPhoneNumber());
            ps.setString(3, contactInfo.getPhoneType());
            ps.setString(4, contactInfo.getMonth());
            ps.setInt(5, contactInfo.getDay());
            ps.setString(6, contactInfo.getVisibility());
            ps.setString(7, contactInfo.getAddress());
            ps.setString(8, contactInfo.getInstantMessaging());
            ps.setString(9, contactInfo.getUserId());
            ps.executeUpdate();
        }
    }

    // Delete contact information by user ID
    public void deleteContactInfoByUserId(String userId) throws SQLException {
        String sql = "DELETE FROM contact_info WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.executeUpdate();
        }
    }
}
