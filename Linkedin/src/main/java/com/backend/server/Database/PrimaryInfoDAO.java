package com.backend.server.Database;

import com.backend.server.Model.PrimaryInfo;
import com.backend.server.MySql;

import java.sql.*;

public class PrimaryInfoDAO {
    private final Connection connection;

    public PrimaryInfoDAO() throws SQLException {
        this.connection = MySql.getConnection();
        createTable();
    }

    private void createTable() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS primary_info ("
                + "user_id VARCHAR(255) PRIMARY KEY,"
                + "first_name VARCHAR(20),"
                + "last_name VARCHAR(40),"
                + "additional_name VARCHAR(20),"
                + "head_title VARCHAR(220),"
                + "city VARCHAR(60),"
                + "country VARCHAR(60),"
                + "profession VARCHAR(60),"
                + "status VARCHAR(30)"
                + ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        }
    }


    // Create a new primary information entry
    public void createPrimaryInfo(PrimaryInfo primaryInfo) throws SQLException {
        String sql = "INSERT INTO primary_info (user_id, first_name, last_name, additional_name, head_title, city, country, profession, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, primaryInfo.getUserId());
            ps.setString(2, primaryInfo.getFirstName());
            ps.setString(3, primaryInfo.getLastName());
            ps.setString(4, primaryInfo.getAdditionalName());
            ps.setString(5, primaryInfo.getHeadTitle());
            ps.setString(6, primaryInfo.getCity());
            ps.setString(7, primaryInfo.getCountry());
            ps.setString(8, primaryInfo.getProfession());
            ps.setString(9, primaryInfo.getStatus());
            ps.executeUpdate();
        }
    }

    // Retrieve primary information by user ID
    public PrimaryInfo getPrimaryInfoByUserId(String userId) throws SQLException {
        String sql = "SELECT * FROM primary_info WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new PrimaryInfo(
                        rs.getString("user_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("additional_name"),
                        rs.getString("head_title"),
                        rs.getString("city"),
                        rs.getString("country"),
                        rs.getString("profession"),
                        rs.getString("status")
                );
            }
            return null; // No primary info found for the given user ID
        }
    }

    // Update existing primary information
    public void updatePrimaryInfo(PrimaryInfo primaryInfo) throws SQLException {
        String sql = "UPDATE primary_info SET first_name = ?, last_name = ?, additional_name = ?, head_title = ?, city = ?, country = ?, profession = ?, status = ? WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, primaryInfo.getFirstName());
            ps.setString(2, primaryInfo.getLastName());
            ps.setString(3, primaryInfo.getAdditionalName());
            ps.setString(4, primaryInfo.getHeadTitle());
            ps.setString(5, primaryInfo.getCity());
            ps.setString(6, primaryInfo.getCountry());
            ps.setString(7, primaryInfo.getProfession());
            ps.setString(8, primaryInfo.getStatus());
            ps.setString(9, primaryInfo.getUserId());
            ps.executeUpdate();
        }
    }

    // Delete primary information by user ID
    public void deletePrimaryInfoByUserId(String userId) throws SQLException {
        String sql = "DELETE FROM primary_info WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.executeUpdate();
        }
    }
}
