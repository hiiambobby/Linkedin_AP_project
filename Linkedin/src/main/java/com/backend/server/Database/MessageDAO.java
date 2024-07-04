package com.backend.server.Database;

import com.backend.server.MySql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.backend.server.Model.Message;
public class MessageDAO {
    private final Connection connection;

    public MessageDAO() {
        this.connection = MySql.getConnection(); // Assumes you have a DatabaseConnection class for obtaining connections
        createTable();
    }

    private void createTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS message (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " + // Added an ID column for unique identification
                "sender VARCHAR(255) NOT NULL, " +
                "receiver VARCHAR(255) NOT NULL, " +
                "text TEXT, " +
                "image VARCHAR(255), " +
                "textFile VARCHAR(255)," + // Removed the extra comma
                "video VARCHAR(255)" + // Removed the extra comma here
                ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            throw new RuntimeException("Error creating message table", e);
        }
    }

    // Method to insert a message into the messages table
    public void insertMessage(Message message) {
        String insertSQL = "INSERT INTO message (sender, receiver, text, image,textFile,video) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setString(1, message.getSender());
            pstmt.setString(2, message.getReceiver());
            pstmt.setString(3, message.getText());
            pstmt.setString(4, message.getImage());
            pstmt.setString(5, message.getFile());
            pstmt.setString(6, message.getVideo());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting message", e);
        }
    }

    public List<Message> getMessagesForReceiver(String receiver) throws SQLException {
        String sql = "SELECT * FROM message WHERE receiver = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, receiver);
            ResultSet rs = ps.executeQuery();
            List<Message> messages = new ArrayList<>();
            while (rs.next()) {
                messages.add(new Message(
                        rs.getString("sender"),
                        rs.getString("receiver"),
                        rs.getString("text"),
                        rs.getString("image"),
                        rs.getString("textFile"),
                        rs.getString("video")
                ));
            }
            return messages;
        }
    }
}

