package com.backend.server.Database;

import com.backend.server.MySql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.backend.server.Model.Message;
public class MessageDAO {
    private final Connection connection;

    public MessageDAO() {
        this.connection = MySql.getConnection(); // Assumes you have a DatabaseConnection class for obtaining connections
        createTable();
    }

    private void createTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS messages (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " + // Added an ID column for unique identification
                "sender VARCHAR(255) NOT NULL, " +
                "receiver VARCHAR(255) NOT NULL, " +
                "text TEXT, " +
                "video VARCHAR(255), " +
                "textFile VARCHAR(255)" + // Removed the extra comma here
                ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            throw new RuntimeException("Error creating messages table", e);
        }
    }

    // Method to insert a message into the messages table
    public void insertMessage(Message message) {
        String insertSQL = "INSERT INTO messages (sender, receiver, text, video, textFile) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setString(1, message.getSender());
            pstmt.setString(2, message.getReceiver());
            pstmt.setString(3, message.getText());
            pstmt.setString(4, message.getVideo());
            pstmt.setString(5, message.getTextFile());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting message", e);
        }
    }

    public List<Message> getMessagesForReceiver(String receiver) throws SQLException {
        String sql = "SELECT * FROM messages WHERE receiver = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, receiver);
            ResultSet rs = ps.executeQuery();
            List<Message> messages = new ArrayList<>();
            while (rs.next()) {
                messages.add(new Message(
                        rs.getString("sender"),
                        rs.getString("receiver"),
                        rs.getString("text"),
                        rs.getString("video"),
                        rs.getString("textFile")
                ));
            }
            return messages;
        }
    }
}

