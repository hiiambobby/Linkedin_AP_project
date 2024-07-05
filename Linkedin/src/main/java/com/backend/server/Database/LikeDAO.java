package com.backend.server.Database;

import com.backend.server.MySql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}