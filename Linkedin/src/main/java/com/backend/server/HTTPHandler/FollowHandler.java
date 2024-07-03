package com.backend.server.HTTPHandler;

import com.backend.server.Controller.FollowController;
import com.backend.server.Model.Follow;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.sql.SQLException;
import java.util.List;

public class FollowHandler implements HttpHandler {
    private final FollowController followController = new FollowController();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response;
        int statusCode;

        try {
            String method = exchange.getRequestMethod();
            if ("POST".equalsIgnoreCase(method)) {
                handlePostRequest(exchange);
                response = "Follow created successfully";
                statusCode = 201; // Created
            } else if ("GET".equalsIgnoreCase(method)) {
                response = handleGetRequest(exchange);
                statusCode = 200; // OK
            } else {
                response = "Unsupported HTTP method";
                statusCode = 405; // Method Not Allowed
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = "Internal Server Error: " + e.getMessage();
            statusCode = 500; // Internal Server Error
        }

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private void handlePostRequest(HttpExchange exchange) throws IOException, SQLException {
        String jsonInput = new String(exchange.getRequestBody().readAllBytes());
        followController.createFollow(jsonInput);
    }

    private String handleGetRequest(HttpExchange exchange) throws SQLException, IOException {
        String query = exchange.getRequestURI().getQuery();
        String userId = getQueryParam(query, "userId");
        String type = getQueryParam(query, "type");

        if (userId == null || type == null) {
            throw new IllegalArgumentException("userId or type parameter is missing");
        }

        return followController.getFollows(userId, type);
    }

    private String getQueryParam(String query, String param) {
        for (String pair : query.split("&")) {
            String[] keyValue = pair.split("=");
            if (keyValue.length > 1 && keyValue[0].equalsIgnoreCase(param)) {
                return keyValue[1];
            }
        }
        return null;
    }
}