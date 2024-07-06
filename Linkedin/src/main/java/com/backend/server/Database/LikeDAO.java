package com.backend.server.Database;

import com.backend.server.MySql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LikeDAO {

    //I found out that we can create the classes directly in the workbench =)))))))))
    private final Connection connection;

    public LikeDAO() {
        this.connection = MySql.getConnection(); // Assumes you have a DatabaseConnection class for obtaining connections

    }

    public void addLike(int postId, String userId) throws SQLException {
        String sql = "INSERT INTO post_like (post_id, user_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, postId);
            stmt.setString(2, userId);
            stmt.executeUpdate();
        }
    }

    public void removeLike(int postId, String userId) throws SQLException {
        String sql = "DELETE FROM post_like WHERE post_id = ? AND user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, postId);
            stmt.setString(2, userId);
            stmt.executeUpdate();
        }
    }
    // Retrieve usernames who liked a specific post
    public List<String> getUsernamesByPostId(int postId) throws SQLException {
        List<String> usernames = new ArrayList<>();
        String query = "SELECT u.username FROM likes l JOIN users u ON l.user_id = u.user_id WHERE l.post_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, postId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    usernames.add(rs.getString("username"));
                }
            }
        }
        return usernames;
    }
}