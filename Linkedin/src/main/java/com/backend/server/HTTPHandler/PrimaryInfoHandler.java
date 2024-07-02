package com.backend.server.HTTPHandler;

import com.backend.server.Controller.PrimaryInfoController;
import com.backend.server.Model.PrimaryInfo;
import com.backend.server.Util.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

public class PrimaryInfoHandler implements HttpHandler {
    private final PrimaryInfoController  primaryInfoController;
    private final ObjectMapper objectMapper;

    public PrimaryInfoHandler() throws SQLException {
        this.primaryInfoController = new PrimaryInfoController();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        String response = "";

        try {
            // Extract user ID (email) from token if available
            String userId = extractUserIdFromToken(exchange);

            if (path.equals("/primaryInfo") && method.equalsIgnoreCase("POST")) {
                if (userId == null) {
                    exchange.sendResponseHeaders(401, -1); // Unauthorized
                    return;
                }
                JSONObject jsonObject = new JSONObject(new String(exchange.getRequestBody().readAllBytes()));
                handlePost(exchange, userId, jsonObject);
            } else if (path.equals("/primaryInfo") && method.equalsIgnoreCase("GET")) {
                // No authentication required for GET request
                response = handleGet(exchange, userId);
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else if (path.equals("/primaryInfo") && method.equalsIgnoreCase("PUT")) {
                if (userId == null) {
                    exchange.sendResponseHeaders(401, -1); // Unauthorized
                    return;
                }
                JSONObject jsonObject = new JSONObject(new String(exchange.getRequestBody().readAllBytes()));
                response = handlePut(exchange, userId, jsonObject);
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else if (path.equals("/primaryInfo") && method.equalsIgnoreCase("DELETE")) {
                if (userId == null) {
                    exchange.sendResponseHeaders(401, -1); // Unauthorized
                    return;
                }
                response = handleDelete(exchange, userId);
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                exchange.sendResponseHeaders(404, -1); // Not Found
            }
        } catch (SQLException e) {
            exchange.sendResponseHeaders(500, -1); // Internal Server Error
            e.printStackTrace();
        }
    }
    private void handlePost(HttpExchange exchange, String userId, JSONObject jsonObject) throws IOException, SQLException {
        try{  // Implement logic to save primary info
            String firstName = jsonObject.optString("firstName", "");
            String lastName = jsonObject.optString("lastName", "");
            String additionalName = jsonObject.optString("additionalName", "");
            String profilePic = jsonObject.optString("profilePic", "");
            String backgroundPic = jsonObject.optString("backgroundPic", "");
            String headTitle = jsonObject.optString("headTitle", ""); // Expecting a String
            String city = jsonObject.optString("city", ""); // Expecting an int
            String country = jsonObject.optString("country", "");
            String profession = jsonObject.optString("profession", "");
            String status = jsonObject.optString("status", "");


            primaryInfoController.savePrimaryInfo(userId,firstName,lastName, additionalName,profilePic,backgroundPic,headTitle, city,country,profession,status);

            JSONObject responseJson = new JSONObject();
            responseJson.put("message", "Primary info saved successfully");

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            sendResponse(exchange, 201, responseJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("error", "Internal Server Error");
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            sendResponse(exchange, 500, errorResponse.toString());
        }}

    private String handleGet(HttpExchange exchange, String userId) throws IOException, SQLException {
        // Create an ObjectMapper instance for JSON serialization
        ObjectMapper objectMapper = new ObjectMapper();

        // Retrieve the primary info from the database
        PrimaryInfo primaryInfo = primaryInfoController.getPrimaryInfo(userId);

        // Prepare the response
        String response;
        if (primaryInfo != null) {
            // Serialize PrimaryInfo object to JSON
            response = objectMapper.writeValueAsString(primaryInfo);
        } else {
            // Return an error message if primary info is not found
            response = "{\"message\":\"Primary info not found\"}";
        }

        return response;
    }

    private String handlePut(HttpExchange exchange, String userId, JSONObject jsonObject) throws IOException, SQLException {
        // Implement logic to update primary info
        String firstName = jsonObject.optString("firstName", "");
        String lastName = jsonObject.optString("lastName", "");
        String additionalName = jsonObject.optString("additionalName", "");
        String profilePic = jsonObject.optString("profilePic", "");
        String backgroundPic = jsonObject.optString("backgroundPic", "");
        String headTitle = jsonObject.optString("headTitle", ""); // Expecting a String
        String city = jsonObject.optString("city", ""); // Expecting an int
        String country = jsonObject.optString("country", "");
        String profession = jsonObject.optString("profession", "");
        String status = jsonObject.optString("status", "");

        primaryInfoController.updatePrimaryInfo(userId,firstName,lastName, additionalName,profilePic,backgroundPic,headTitle, city,country,profession,status);

        if (primaryInfoController.getPrimaryInfo(userId) != null) {
            primaryInfoController.updatePrimaryInfo(userId,firstName,lastName, additionalName,profilePic,backgroundPic,headTitle, city,country,profession,status);
            return "{\"message\":\"Primary info updated successfully\"}";
        } else {
            primaryInfoController.savePrimaryInfo(userId,firstName,lastName, additionalName,profilePic,backgroundPic,headTitle, city,country,profession,status);
            return "{\"message\":\"Primary info created successfully\"}";
        }
    }

    private String handleDelete(HttpExchange exchange, String userId) throws IOException, SQLException {
        // Implement logic to delete primary info
        primaryInfoController.deletePrimaryInfo(userId);
        return "{\"message\":\"Primary info deleted successfully\"}";
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String responseText) throws IOException {
        exchange.sendResponseHeaders(statusCode, responseText.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(responseText.getBytes());
        os.close();
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
