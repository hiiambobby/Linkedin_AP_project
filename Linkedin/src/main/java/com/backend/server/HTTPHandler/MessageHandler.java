package com.backend.server.HTTPHandler;

import com.backend.server.Controller.MessageController;
import com.backend.server.Model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class MessageHandler implements HttpHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final MessageController messageController = new MessageController();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        switch (method) {
            case "POST":
                if (path.equals("/message")) {
                    handlePostMessage(exchange);
                } else {
                    sendResponse(exchange, "Not Found", 404);
                }
                break;

            case "GET":
                if (path.equals("/message")) {
                    try {
                        handleGetMessages(exchange);
                    } catch (SQLException e) {
                        e.printStackTrace(); // Print stack trace for debugging
                        sendResponse(exchange, "Internal Server Error", 500);
                    }
                } else {
                    sendResponse(exchange, "Not Found", 404);
                }
                break;

            default:
                sendResponse(exchange, "Method Not Allowed", 405);
                break;
        }
    }

    private void handlePostMessage(HttpExchange exchange) throws IOException {
        String requestBody = null;
        try (InputStream inputStream = exchange.getRequestBody()) {
            requestBody = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            System.out.println("Request Body: " + requestBody);

            Message message = objectMapper.readValue(requestBody, Message.class);
            messageController.createMessage(message);
            sendResponse(exchange, "Message Created", 201);
        } catch (InvalidDefinitionException e) {
            // Handle specific deserialization exceptions
            System.err.println("Invalid message format: " + e.getMessage());
            sendResponse(exchange, "Invalid Message Format", 400);
        } catch (IOException e) {
            e.printStackTrace(); // Print stack trace for debugging
            System.err.println("Failed to read request body: " + e.getMessage());
            sendResponse(exchange, "Invalid Request Body", 400);
        } catch (Exception e) {
            e.printStackTrace(); // Print stack trace for debugging
            System.err.println("Unexpected error: " + e.getMessage());
            sendResponse(exchange, "Internal Server Error", 500);
        }
    }



    // Handles GET requests to retrieve messages for a specific receiver
    private void handleGetMessages(HttpExchange exchange) throws IOException, SQLException {
        Map<String, String> queryParams = parseQuery(exchange.getRequestURI().getQuery());
        String receiver = queryParams.get("receiver");

        if (receiver != null) {
            List<Message> messages = messageController.getMessagesForReceiver(receiver);
            String responseJson = objectMapper.writeValueAsString(messages);
            sendResponse(exchange, responseJson, 200);
        } else {
            sendResponse(exchange, "Receiver parameter missing", 400);
        }
    }


    private void sendResponse(HttpExchange exchange, String response, int statusCode) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }



    private Map<String, String> parseQuery(String query) {
        if (query == null || query.isEmpty()) {
            return Map.of();
        }

        return Stream.of(query.split("&"))
                .map(param -> param.split("=", 2)) // Limit split to 2 parts
                .filter(pair -> pair.length == 2)
                .collect(Collectors.toMap(
                        pair -> pair[0],
                        pair -> pair[1]
                ));
    }
}