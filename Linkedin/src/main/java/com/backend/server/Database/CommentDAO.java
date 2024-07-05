package com.backend.server.Database;

import com.backend.server.Model.Comment;
import com.backend.server.MySql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentDAO {

    private final Connection connection;

    public CommentDAO() {
        this.connection = MySql.getConnection(); // Assumes you have a DatabaseConnection class for obtaining connections
    }

    // Create table
    public void createTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS comment (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "postId INT NOT NULL, " +
                "userId VARCHAR(255) NOT NULL, " +
                "text TEXT, " +
                "image VARCHAR(255), " +
                "video VARCHAR(255), " +
                "FOREIGN KEY (postId) REFERENCES post(id)" +
                ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            throw new RuntimeException("Error creating comment table", e);
        }
    }

    // Insert a new comment
    public void insertComment(Comment comment) throws SQLException {
        String insertSQL = "INSERT INTO comment (postId, userId, text, image, video) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setInt(1, comment.getPostId());
            pstmt.setString(2, comment.getUserId());
            pstmt.setString(3, comment.getText());
            pstmt.setString(4, comment.getImage());
            pstmt.setString(5, comment.getVideo());
            pstmt.executeUpdate();
        }
    }

    // Get all comments for a post
    public List<Comment> getCommentsByPostId(int postId) throws SQLException {
        List<Comment> comments = new ArrayList<>();
        String selectSQL = "SELECT * FROM comment WHERE postId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(selectSQL)) {
            pstmt.setInt(1, postId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Comment comment = new Comment();
                    comment.setId(rs.getInt("id"));
                    comment.setPostId(rs.getInt("postId"));
                    comment.setUserId(rs.getString("userId"));
                    comment.setText(rs.getString("text"));
                    comment.setImage(rs.getString("image"));
                    comment.setVideo(rs.getString("video"));
                    comments.add(comment);
                }
            }
        }
        return comments;
    }
}
