package com.backend.server.Database;

import com.backend.server.HTTPHandler.HashtagUtils;
import com.backend.server.Model.Message;
import com.backend.server.Model.Post;
import com.backend.server.MySql;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PostDAO {
    private final Connection connection;

    public PostDAO() {
        this.connection = MySql.getConnection(); // Assumes you have a DatabaseConnection class for obtaining connections
        createTable();
    }

    private void createTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS post (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " + // Unique ID for each post
                "sender VARCHAR(255) NOT NULL, " + // The sender of the post
                "text TEXT, " + // The text content of the post
                "video JSON, " + // Comma-separated list of video file paths
                "image JSON " + // Comma-separated list of image file paths
                ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            throw new RuntimeException("Error creating post table", e);
        }
    }

    //add

    public void insertPost(Post post) throws SQLException {
            String insertPostQuery = "INSERT INTO post (sender, text, video, image) VALUES (?, ?, ?, ?)";
            String insertHashtagQuery = "INSERT INTO hashtags (post_id, hashtag) VALUES (?, ?)";

            try (PreparedStatement postStmt = connection.prepareStatement(insertPostQuery, Statement.RETURN_GENERATED_KEYS)) {
                postStmt.setString(1, post.getSender());
                postStmt.setString(2, post.getText());
                postStmt.setString(3, post.getVideoJson());
                postStmt.setString(4, post.getImageJson());

                int affectedRows = postStmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Creating post failed, no rows affected.");
                }

                ResultSet generatedKeys = postStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int postId = generatedKeys.getInt(1);

                    // Extract and insert hashtags
                    Set<String> hashtags = HashtagUtils.extractHashtags(post.getText());
                    try (PreparedStatement hashtagStmt = connection.prepareStatement(insertHashtagQuery)) {
                        for (String hashtag : hashtags) {
                            hashtagStmt.setInt(1, postId);
                            hashtagStmt.setString(2, hashtag);
                            hashtagStmt.addBatch();
                        }
                        hashtagStmt.executeBatch();
                    }
                } else {
                    throw new SQLException("Creating post failed, no ID obtained.");
                }
            }
        }
    public List<Post> getAllPosts() throws SQLException, JsonProcessingException {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT * FROM post";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Post post = new Post();
                post.setId(rs.getInt("id"));
                post.setSender(rs.getString("sender"));
                post.setText(rs.getString("text"));
                post.setVideo(parseJsonToList(rs.getString("video")));
                post.setImage(parseJsonToList(rs.getString("image")));
                posts.add(post);
            }
        }
        return posts;
    }
    private List<String> parseJsonToList(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Post> getPostsByKeyword(String keyword) throws SQLException {
        List<Post> posts = new ArrayList<>();
        String query = "SELECT id, sender, text, video, image " +
                "FROM post " +
                "WHERE text LIKE ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Post post = new Post();
                post.setId(rs.getInt("id"));
                post.setSender(rs.getString("sender"));
                post.setText(rs.getString("text"));
                post.setVideo(parseJsonToList(rs.getString("video")));
                post.setImage(parseJsonToList(rs.getString("image")));
                posts.add(post);
            }
        }
        return posts;
    }
    public List<Post> getPostsByHashtag(String hashtag) throws SQLException {
        List<Post> posts = new ArrayList<>();
        String query = "SELECT p.id, p.sender, p.text, p.video, p.image " +
                "FROM post p " +
                "JOIN hashtags h ON p.id = h.post_id " +
                "WHERE h.hashtag = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, hashtag.toLowerCase()); // Search with lowercase
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Post post = new Post();
                post.setId(rs.getInt("id"));
                post.setSender(rs.getString("sender"));
                post.setText(rs.getString("text"));
                post.setVideo(parseJsonToList(rs.getString("video")));
                post.setImage(parseJsonToList(rs.getString("image")));
                posts.add(post);
            }
        }
        return posts;
    }
}
