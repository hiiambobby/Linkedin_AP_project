package com.backend.server.Database;

import com.backend.server.Model.Connect;
import com.backend.server.Model.ContactInfo;
import com.backend.server.Model.Follow;
import com.backend.server.MySql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FollowDAO {
    private final Connection connection;

    public FollowDAO() {
        this.connection = MySql.getConnection(); // Assumes you have a DatabaseConnection class for obtaining connections
        createTable();
    }


    private void createTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS follows (" +
                "follower VARCHAR(255) NOT NULL," +
                "following VARCHAR(255) NOT NULL," +
                "PRIMARY KEY (follower, following)" +
                ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void create(Follow follow) throws SQLException {
        String sql = "INSERT INTO follows (follower, following) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, follow.getFollower());
            ps.setString(2, follow.getFollowing());
            ps.executeUpdate();
        }
    }

    // User wants to see who has followed them
    public List<Follow> getFollowersByUserId(String userId) throws SQLException {
        String sql = "SELECT * FROM follows WHERE following = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();
            List<Follow> followers = new ArrayList<>();
            while (rs.next()) {
                followers.add(new Follow(
                        rs.getString("follower"),
                        rs.getString("following")
                ));
            }
            return followers;
        }
    }

    public List<Follow> getFollowingsByUserId(String userId) throws SQLException {
        String sql = "SELECT * FROM follows WHERE follower = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();
            List<Follow> followings = new ArrayList<>();
            while (rs.next()) {
                followings.add(new Follow(
                        rs.getString("follower"),
                        rs.getString("following")
                ));
            }
            return followings;
        }
    }
}