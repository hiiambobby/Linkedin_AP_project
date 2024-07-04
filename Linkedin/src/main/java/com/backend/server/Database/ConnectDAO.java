package com.backend.server.Database;

import com.backend.server.Model.Connect;
import com.backend.server.Model.ContactInfo;
import com.backend.server.MySql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConnectDAO {
    private final Connection connection;

    public ConnectDAO() {
        this.connection = MySql.getConnection(); // Assumes you have a DatabaseConnection class for obtaining connections
        createTable();
    }


    private void createTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS connections (" +
//                "id VARCHAR(255) PRIMARY KEY," + // we do not need primary key because it is two sided somehow
                "sender VARCHAR(255) PRIMARY KEY," +
                "receiver VARCHAR(255) NOT NULL," +
                "notes TEXT," +
                "accepted BOOLEAN DEFAULT FALSE" +
                ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addConnection(Connect connect) throws SQLException {
        String sql = "INSERT INTO connections (sender, receiver, notes, accepted) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, connect.getSenderName());
            ps.setString(2, connect.getReceiverName());
            ps.setString(3, connect.getNotes());
            ps.setBoolean(4, connect.isAccepted());
            ps.executeUpdate();
        }
    }
    public void updateConnectionAccepted(String sender, String receiver, boolean accepted) {
        String updateSQL = "UPDATE connections SET accepted = ? WHERE sender = ? AND receiver = ?";
        try (PreparedStatement stmt = connection.prepareStatement(updateSQL)) {
            stmt.setBoolean(1, accepted);
            stmt.setString(2, sender);
            stmt.setString(3, receiver);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Connect> getPending(String sender,String receiver) {
        String querySQL = "SELECT * FROM connections WHERE (accepted = false OR accepted = true) AND sender = ? AND receiver = ?";
        List<Connect> connections = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(querySQL)) {
            stmt.setString(1, sender);
            stmt.setString(2, receiver);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                connections.add(new Connect(
                        rs.getString("sender"),
                        rs.getString("receiver"),
                        rs.getString("notes"),
                        rs.getBoolean("accepted")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connections;
    }


    ///i changed here we will add the users in another way later
    public List<Connect> getConnections(String user) {
        String querySQL = "SELECT * FROM connections WHERE receiver = ? AND accepted = false";
        List<Connect> connections = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(querySQL)) {
            stmt.setString(1, user);
      //      stmt.setString(2, user);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                connections.add(new Connect(
                        rs.getString("sender"),
                        rs.getString("receiver"),
                        rs.getString("notes"),
                        rs.getBoolean("accepted")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connections;
    }
    //if one of them has accepted the request
    public List<Connect> getConnected(String user) {
        // SQL query to fetch connections
        String querySQL = "SELECT * FROM connections WHERE (receiver = ? OR sender = ?) AND accepted = true";
        List<Connect> connections = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(querySQL)) {
            // Set parameters for the prepared statement
            stmt.setString(1, user);
            stmt.setString(2, user);

            // Print the SQL query with parameters for debugging
            System.out.println("Executing query: " + querySQL.replace("?", "'" + user + "'"));

            // Execute the query and get the result set
            ResultSet rs = stmt.executeQuery();

            // Print debug statement if no results are found
            if (!rs.isBeforeFirst()) {
                System.out.println("No results found for user: " + user);
            }

            // Process the result set
            while (rs.next()) {
                connections.add(new Connect(
                        rs.getString("sender"),
                        rs.getString("receiver"),
                        rs.getString("notes"),
                        rs.getBoolean("accepted")
                ));
            }
        } catch (SQLException e) {
            // Print SQL exceptions for debugging
            System.err.println("SQL Exception: " + e.getMessage());
            e.printStackTrace(); // Print the stack trace for more details
            throw new RuntimeException(e);
        }

        // Print the found connections for debugging
        System.out.println("Found connections: " + connections);

        return connections;
    }

    public void deleteConnection(String sender, String receiver) {
        String deleteSQL = "DELETE FROM connections WHERE sender = ? AND receiver = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteSQL)) {
            stmt.setString(1, sender);
            stmt.setString(2, receiver);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}