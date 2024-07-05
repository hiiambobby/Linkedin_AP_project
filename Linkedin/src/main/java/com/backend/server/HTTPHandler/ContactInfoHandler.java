package com.backend.server.HTTPHandler;

import com.backend.server.Controller.ContactInfoController;
import com.backend.server.Model.Connect;
import com.backend.server.Model.ContactInfo;
import com.backend.server.Util.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;


public class ContactInfoHandler implements HttpHandler {

    private final ContactInfoController contactInfoController;
    private final ObjectMapper objectMapper;

    public ContactInfoHandler() throws SQLException {
        this.contactInfoController = new ContactInfoController();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        String response = "";

        try {
            // Extract user ID (email) from token
            String userId = extractUserIdFromToken(exchange);

            if (path.equals("/contactInfo")) {
                switch (method.toUpperCase()) {
                    case "POST":
                        if (userId == null) {
                            exchange.sendResponseHeaders(401, -1); // Unauthorized
                        } else {
                            JSONObject jsonObject = new JSONObject(new String(exchange.getRequestBody().readAllBytes()));
                            handlePost(exchange, userId, jsonObject);
                        }
                        break;
                    case "GET":
                        if (userId == null) {
                            response = handeGetWithId(exchange, userId);
                        } else {
                            response = handleGet(exchange, userId);
                        }
                        exchange.sendResponseHeaders(200, response.getBytes().length);
                        OutputStream os = exchange.getResponseBody();
                        os.write(response.getBytes());
                        os.close();
                        break;
                    case "PUT":
                        if (userId == null) {
                            exchange.sendResponseHeaders(401, -1); // Unauthorized
                        } else {
                            JSONObject jsonObject = new JSONObject(new String(exchange.getRequestBody().readAllBytes()));
                            response = handlePut(exchange, userId, jsonObject);
                            exchange.sendResponseHeaders(200, response.getBytes().length);
                            OutputStream osPut = exchange.getResponseBody();
                            osPut.write(response.getBytes());
                            osPut.close();
                        }
                        break;
                    case "DELETE":
                        if (userId == null) {
                            exchange.sendResponseHeaders(401, -1); // Unauthorized
                        } else {
                            response = handleDelete(exchange, userId);
                            exchange.sendResponseHeaders(200, response.getBytes().length);
                            OutputStream osDelete = exchange.getResponseBody();
                            osDelete.write(response.getBytes());
                            osDelete.close();
                        }
                        break;
                    default:
                        exchange.sendResponseHeaders(404, -1); // Not Found
                        break;
                }
            } else {
                exchange.sendResponseHeaders(404, -1); // Not Found
            }
        } catch (SQLException e) {
            exchange.sendResponseHeaders(500, -1); // Internal Server Error
            e.printStackTrace();
        }
    }

    private void handlePost(HttpExchange exchange, String userId, JSONObject jsonObject) throws IOException, SQLException {
        try {  // Implement logic to save contact info
            String profileUrl = jsonObject.optString("profile_url", "");
            String phoneNumber = jsonObject.optString("phone_number", "");
            String phoneType = jsonObject.optString("phone_type", "");
            String month = jsonObject.optString("month", ""); // Expecting a String
            int day = jsonObject.optInt("day", 0); // Expecting an int
            String visibility = jsonObject.optString("visibility", "");
            String address = jsonObject.optString("address", "");
            String instantMessaging = jsonObject.optString("instant_messaging", "");


            contactInfoController.saveContactInfo(userId, profileUrl, phoneNumber, phoneType, month, day, visibility, address, instantMessaging);

            JSONObject responseJson = new JSONObject();
            responseJson.put("message", "Contact info saved successfully");

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            sendResponse(exchange, 201, responseJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("error", "Internal Server Error");
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            sendResponse(exchange, 500, errorResponse.toString());
        }
    }

    private String handleGet(HttpExchange exchange, String userId) throws IOException, SQLException {
        ObjectMapper objectMapper = new ObjectMapper();
        ContactInfo contactInfo = contactInfoController.getContactInfo(userId);
        String response;
        if (contactInfo != null) {
            response = objectMapper.writeValueAsString(contactInfo);
        } else {
            response = "{\"message\":\"Contact info not found\"}";
        }
        return response;
    }

    private String handeGetWithId(HttpExchange exchange,String userId) throws IOException, SQLException {
        String query = exchange.getRequestURI().getQuery();
        userId = getQueryParam(query, "id");

        if (userId == null) {
            throw new IllegalArgumentException("User parameter is missing");
        }
        String contactInfo = "";
        ContactInfo contactInfos = contactInfoController.getContactInfo(userId);
        if(contactInfos != null)
        {
            contactInfo = objectMapper.writeValueAsString(contactInfos);
        }
        return objectMapper.writeValueAsString(contactInfo);

    }

    private String handlePut(HttpExchange exchange, String userId, JSONObject jsonObject) throws IOException, SQLException {
        String profileUrl = jsonObject.optString("profile_url", "");
        String phoneNumber = jsonObject.optString("phone_number", "");
        String phoneType = jsonObject.optString("phone_type", "");
        String month = jsonObject.optString("month", "");
        int day = jsonObject.optInt("day", 0);
        String visibility = jsonObject.optString("visibility", "");
        String address = jsonObject.optString("address", "");
        String instantMessaging = jsonObject.optString("instant_messaging", "");

        contactInfoController.updateContactInfo(userId, profileUrl, phoneNumber, phoneType, month, day, visibility, address, instantMessaging);

        if (contactInfoController.getContactInfo(userId) != null) {
            contactInfoController.updateContactInfo(userId, profileUrl, phoneNumber, phoneType, month, day, visibility, address, instantMessaging);
            return "{\"message\":\"Contact info updated successfully\"}";
        } else {
            contactInfoController.saveContactInfo(userId, profileUrl, phoneNumber, phoneType, month, day, visibility, address, instantMessaging);
            return "{\"message\":\"Contact info created successfully\"}";
        }
    }

    private String handleDelete(HttpExchange exchange, String userId) throws IOException, SQLException {
        // Implement logic to delete contact info
        contactInfoController.deleteContactInfo(userId);
        return "{\"message\":\"Contact info deleted successfully\"}";
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


}
