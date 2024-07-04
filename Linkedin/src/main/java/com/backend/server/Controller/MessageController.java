package com.backend.server.Controller;

import com.backend.server.Database.MessageDAO;
import com.backend.server.Model.Message;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MessageController {
    private final MessageDAO messageDAO;

    public MessageController() {
        this.messageDAO = new MessageDAO(); // Initialize your DAO
    }

    // Create a new message
    public void createMessage(Message message) {
        messageDAO.insertMessage(message); // Method to insert message into the database
    }

    // Get messages for a specific receiver
    public List<Message> getMessagesForReceiver(String receiver) throws SQLException {
        return messageDAO.getMessagesForReceiver(receiver); // Method to retrieve messages from the database
    }
}
