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
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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



            if (path.equals("/contactInfo") && method.equalsIgnoreCase("POST")) {
                if (userId == null) {
                    exchange.sendResponseHeaders(401, -1); // Unauthorized
                    return;
                }
                JSONObject jsonObject = new JSONObject(new String(exchange.getRequestBody().readAllBytes()));
                handlePost(exchange, userId, jsonObject);
            } else if (path.equals("/contactInfo") && method.equalsIgnoreCase("GET")) {
                String email = "";
                String query = exchange.getRequestURI().getQuery();
                if (query != null && query.startsWith("id=")) {
                    // Extract email from query parameter
                    email = query.substring(3); // Extract email part after 'id='
                    email = URLDecoder.decode(email, StandardCharsets.UTF_8.toString());
                }  else {
                    // If no query parameters are provided, return an error
                    response = "{\"error\":\"Missing email parameter.\"}";
                    exchange.sendResponseHeaders(400, response.getBytes().length); // Bad Request
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                    return; // Exit the handler
                }

                // Handle the request and generate a response
                response = handleGet(email);

                // Set response code based on the presence of primary info
                if (response == null || response.isEmpty() || response.equals("{\"message\":\"Primary info not found\"}")) {
                    exchange.sendResponseHeaders(404, response.getBytes().length);
                } else {
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                }

                // Send response
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else if (path.equals("/contactInfo") && method.equalsIgnoreCase("PUT")) {
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
      try{  // Implement logic to save contact info
        String profileUrl = jsonObject.optString("profileUrl", "");
        String phoneNumber = jsonObject.optString("phoneNumber", "");
        String phoneType = jsonObject.optString("phoneType", "");
        String month = jsonObject.optString("birthMonth", ""); // Expecting a String
        int day = jsonObject.optInt("birthDay", 0); // Expecting an int
        String visibility = jsonObject.optString("visibility", "");
        String address = jsonObject.optString("address", "");
        String instantMessaging = jsonObject.optString("instantMessaging", "");


        contactInfoController.saveContactInfo(userId,profileUrl,phoneNumber, phoneType,month, day,visibility,address,instantMessaging);

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
      }}

    private String handleGet(HttpExchange exchange, String userId) throws IOException, SQLException {
        // Create an ObjectMapper instance for JSON serialization
        ObjectMapper objectMapper = new ObjectMapper();

        // Retrieve the contact info from the database or other data source
        ContactInfo contactInfo = contactInfoController.getContactInfo(userId);

        // Prepare the response
        String response;
        if (contactInfo != null) {
            // Serialize ContactInfo object to JSON
            response = objectMapper.writeValueAsString(contactInfo);
        } else {
            // Return an error message if contact info is not found
            response = "{\"message\":\"Contact info not found\"}";
        }

        return response;
    }

    private String handlePut(HttpExchange exchange, String userId, JSONObject jsonObject) throws IOException, SQLException {
        // Implement logic to update contact info
        String profileUrl = jsonObject.optString("profileUrl", "");
        String phoneNumber = jsonObject.optString("phoneNumber", "");
        String phoneType = jsonObject.optString("phoneType", "");
        String month = jsonObject.optString("month", "");
        int day = jsonObject.optInt("day", 0);
        String visibility = jsonObject.optString("visibility", "");
        String address = jsonObject.optString("address", "");
        String instantMessaging = jsonObject.optString("instantMessaging", "");

        contactInfoController.updateContactInfo(userId,profileUrl,phoneNumber, phoneType,month, day,visibility,address,instantMessaging);

        if (contactInfoController.getContactInfo(userId) != null) {
            contactInfoController.updateContactInfo(userId,profileUrl,phoneNumber, phoneType,month, day,visibility,address,instantMessaging);
            return "{\"message\":\"Contact info updated successfully\"}";
        } else {
            contactInfoController.saveContactInfo(userId,profileUrl,phoneNumber, phoneType,month, day,visibility,address,instantMessaging);
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

}
