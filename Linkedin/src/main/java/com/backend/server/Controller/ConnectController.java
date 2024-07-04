package com.backend.server.Controller;

import com.backend.server.Database.ConnectDAO;
import com.backend.server.Database.ContactInfoDAO;
import com.backend.server.Model.Connect;

import java.sql.SQLException;

import java.util.List;

public class ConnectController {
    private final ConnectDAO connectDAO;

    public ConnectController() {
        this.connectDAO = new ConnectDAO();
    }

    public void addConnection(Connect connect) throws SQLException {
        // Check if a connection request already exists
        if (!connectionExists(connect.getSenderName(), connect.getReceiverName())) {
            connectDAO.addConnection(connect);
        } else {
            throw new RuntimeException("Connection request already exists.");
        }
    }

    public void updateConnectionStatus(String sender, String receiver, boolean accepted) {
        if (connectionExists(sender, receiver)) {
            System.out.println("not here");
            connectDAO.updateConnectionAccepted(sender, receiver, accepted);
        } else {
            throw new RuntimeException("Connection request does not exist.");
        }
    }


    public List<Connect> getConnections(String user) {
        return connectDAO.getConnections(user);
    }


    public void deleteConnection(String sender, String receiver) {
        if (connectionExists(sender, receiver)) {
            connectDAO.deleteConnection(sender, receiver);
        } else {
            throw new RuntimeException("Connection request does not exist.");
        }
    }
    private boolean connectionExists(String sender, String receiver) {
        List<Connect> connections = connectDAO.getPending(sender,receiver);
        System.out.println("Connections for sender " + receiver + ": " + connections);

        boolean exists = connections.stream()
                .anyMatch(c -> (c.getSenderName().equals(sender) && c.getReceiverName().equals(receiver)));

        System.out.println("Connection exists between " + sender + " and " + receiver + ": " + exists);
        return exists;
    }


    public List<Connect> getConnected(String user) {
        return connectDAO.getConnected(user);
    }
}
