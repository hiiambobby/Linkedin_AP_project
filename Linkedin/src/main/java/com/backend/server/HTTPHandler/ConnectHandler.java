package com.backend.server.HTTPHandler;

import com.backend.server.Controller.ConnectController;
import com.backend.server.Model.Connect;
import com.backend.server.Util.JWT;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

import java.sql.SQLException;

public class ConnectHandler implements HttpHandler {
    private final ConnectController connectController;
    private final ObjectMapper objectMapper;

    public ConnectHandler() {
        this.connectController = new ConnectController(); // Initialize DAO for database operations
        this.objectMapper = new ObjectMapper();
        // Configure ObjectMapper as needed (e.g., for pretty-printing, ignore unknown properties)
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response;
        int statusCode;

        try {
            switch (exchange.getRequestMethod()) {
                case "POST":
                    response = handlePostRequest(exchange);
                    statusCode = HttpURLConnection.HTTP_OK;
                    break;
                case "GET":
                    response = handleGetRequest(exchange);
                    statusCode = HttpURLConnection.HTTP_OK;
                    break;
                case "DELETE":
                    response = handleDeleteRequest(exchange);
                    statusCode = HttpURLConnection.HTTP_NO_CONTENT;
                    break;
                case "PUT":
                    response = handlePutRequest(exchange);
                    statusCode = HttpURLConnection.HTTP_OK;
                    break;
                default:
                    response = "Unsupported HTTP method";
                    statusCode = HttpURLConnection.HTTP_BAD_METHOD;
                    break;
            }
        } catch (SQLException e) {
            response = "Database error: " + e.getMessage();
            statusCode = HttpURLConnection.HTTP_INTERNAL_ERROR;
        } catch (InvalidFormatException e) {
            response = "Invalid JSON format: " + e.getMessage();
            statusCode = HttpURLConnection.HTTP_BAD_REQUEST;
        } catch (JsonProcessingException e) {
            response = "Error processing JSON: " + e.getMessage();
            statusCode = HttpURLConnection.HTTP_BAD_REQUEST;
        } catch (Exception e) {
            response = "Error: " + e.getMessage();
            statusCode = HttpURLConnection.HTTP_INTERNAL_ERROR;
        }

        // Send response
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private String handlePutRequest(HttpExchange exchange) throws IOException, SQLException {
        // Parse request body to get connection details
        JSONObject jsonObject = new JSONObject(new String(exchange.getRequestBody().readAllBytes()));
        String sender = jsonObject.optString("sender", "");
        String receiver = jsonObject.optString("receiver", "");
        boolean accepted = jsonObject.optBoolean("accepted", false);

        if (sender.isEmpty() || receiver.isEmpty()) {
            throw new IllegalArgumentException("Sender or receiver parameter is missing");
        }

        // Update connection status
        connectController.updateConnectionStatus(sender, receiver, accepted);

        return "Connection status updated";
    }

    //this method i a little bit different from others we are not sending sender's name
    private String handlePostRequest(HttpExchange exchange) throws IOException, SQLException {
        // Extract user ID (email) from the token
        String sender = extractUserIdFromToken(exchange);
        if (sender == null) {
            return "Unauthorized: Invalid or missing token";
        }
        // Parse request body to get connection details
        Connect newConnect = objectMapper.readValue(exchange.getRequestBody(), Connect.class);
        // Set the senderName from the extracted user ID
        newConnect.setSenderName(sender);
        // Add connection request to the database
        connectController.addConnection(newConnect);
        return "Connection request added";
    }

    private String handleGetRequest(HttpExchange exchange) throws IOException, SQLException {
        String query = exchange.getRequestURI().getQuery();
        String user = getQueryParam(query, "user");

        if (user == null) {
            throw new IllegalArgumentException("User parameter is missing");
        }

        List<Connect> connections = connectController.getConnections(user);
        return objectMapper.writeValueAsString(connections);
    }

    private String handleDeleteRequest(HttpExchange exchange) throws IOException, SQLException {
        String query = exchange.getRequestURI().getQuery();
        String sender = getQueryParam(query, "sender");
        String receiver = getQueryParam(query, "receiver");

        if (sender == null || receiver == null) {
            throw new IllegalArgumentException("Sender or receiver parameter is missing");
        }

        connectController.deleteConnection(sender, receiver);
        return "Connection request deleted";
    }

    private String getQueryParam(String query, String param) {
        if (query == null) return null;
        for (String pair : query.split("&")) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2 && keyValue[0].equals(param)) {
                return keyValue[1];
            }
        }
        return null;
    }

    private String extractUserIdFromToken(HttpExchange exchange) {
        String authorizationHeader = exchange.getRequestHeaders().getFirst("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            try {
                // Validate the token and extract the user ID (email)
                System.out.println(token);
                return JWT.validateToken(token);
            } catch (Exception e) {
                // Handle invalid token case
                return null;
            }
        }
        return null;
    }

}
