package com.backend.server.HTTPHandler;
import com.backend.server.Controller.UserController;
import com.backend.server.Database.ContactInfoDAO;
import com.backend.server.Model.ContactInfo;
import com.backend.server.Util.JWT;
import com.backend.server.Controller.ContactInfoController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;


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

            if (userId == null) {
                exchange.sendResponseHeaders(401, -1); // Unauthorized
                return;
            }

            if (path.equals("/contactInfo") && method.equalsIgnoreCase("POST")) {
                JSONObject jsonObject = new JSONObject(new String(exchange.getRequestBody().readAllBytes()));
                handlePost(exchange, userId, jsonObject);
            } else if (path.equals("/contactInfo") && method.equalsIgnoreCase("GET")) {
                response = handleGet(exchange, userId);
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else if (path.equals("/contactInfo") && method.equalsIgnoreCase("PUT")) {
                JSONObject jsonObject = new JSONObject(new String(exchange.getRequestBody().readAllBytes()));
                response = handlePut(exchange, userId, jsonObject);
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else if (path.equals("/contactInfo") && method.equalsIgnoreCase("DELETE")) {
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
        // Implement logic to save contact info
        String profileUrl= jsonObject.getString("profileUrl");
        String phoneNumber = jsonObject.getString("phoneNumber");
        String phoneType = jsonObject.getString("phoneType");
        String address = jsonObject.getString("address");
        String month = jsonObject.getString("month");
        int day = jsonObject.getInt("day");
        String instantMessaging = jsonObject.getString("instantMessaging");
        String visibility = jsonObject.getString("visibility");

        contactInfoController.saveContactInfo(userId,profileUrl,phoneNumber, phoneType,month, day,visibility,address,instantMessaging);

        JSONObject responseJson = new JSONObject();
        responseJson.put("message", "Contact info saved successfully");

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        sendResponse(exchange, 201, responseJson.toString());
    }

    private String handleGet(HttpExchange exchange, String userId) throws IOException, SQLException {
        // Implement logic to get contact info
        ContactInfo contactInfo = contactInfoController.getContactInfo(userId);
        if (contactInfo != null) {
            return objectMapper.writeValueAsString(contactInfo);
        } else {
            return "{\"message\":\"Contact info not found\"}";
        }
    }

    private String handlePut(HttpExchange exchange, String userId, JSONObject jsonObject) throws IOException, SQLException {
        // Implement logic to update contact info
        String profileUrl= jsonObject.getString("profileUrl");
        String phoneNumber = jsonObject.getString("phoneNumber");
        String phoneType = jsonObject.getString("phoneType");
        String address = jsonObject.getString("address");
        String month = jsonObject.getString("month");
        int day = jsonObject.getInt("day");
        String instantMessaging = jsonObject.getString("instantMessaging");
        String visibility = jsonObject.getString("visibility");

        contactInfoController.updateContactInfo(userId,profileUrl,phoneNumber, phoneType,month, day,visibility,address,instantMessaging);

        return "{\"message\":\"Contact info updated successfully\"}";
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

    // Extract user ID (email) from the JWT token
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
