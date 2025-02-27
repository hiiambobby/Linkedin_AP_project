package com.backend.server.HTTPHandler;

import com.backend.server.Controller.UserController;
import com.backend.server.Model.User;
import com.backend.server.Util.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.util.prefs.Preferences;
import org.json.JSONObject;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.UUID;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;


public class userActionsHandler implements HttpHandler,HttpStatusCode {

    private Preferences prefs = Preferences.userNodeForPackage(userActionsHandler.class);

    private final UserController userController;
    private final ObjectMapper objectMapper;

    public userActionsHandler() throws SQLException {
        this.userController = new UserController();
        this.objectMapper = new ObjectMapper();
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        String response = "";

        try {
            if (path.equals("/login") && method.equalsIgnoreCase("POST")) {
                JSONObject jsonObject = new JSONObject(new String(exchange.getRequestBody().readAllBytes()));
                handleLogin(exchange, jsonObject);
            } else if (path.equals("/signup") && method.equalsIgnoreCase("POST")) {
                JSONObject jsonObject = new JSONObject(new String(exchange.getRequestBody().readAllBytes()));
                handleSignUp(exchange, jsonObject);
            } else if (path.equals("/user")) {
                switch (method.toUpperCase()) {
                    case "GET":
                        response = handleGet(exchange);
                        break;
//                    case "POST":
//                        response = handlePost(exchange);
//                        break;
                    case "PUT":
                        response = handlePut(exchange);
                        break;
                    case "DELETE":
                        response = handleDelete(exchange);
                        break;
                    default:
                        exchange.sendResponseHeaders(405, -1); // Method Not Allowed
                        return;
                }
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

    private void handleSignUp(HttpExchange exchange, JSONObject jsonObject) throws IOException, SQLException {
        String email = jsonObject.getString("email");
        String password = jsonObject.getString("password");
        String confirmPassword = jsonObject.getString("confirmPassword");

        String id = generateRandomId(); // Implement this method to generate a random ID
        String firstName = jsonObject.getString("firstName");
        String lastName = jsonObject.getString("lastName");
        String additionalName = jsonObject.getString("additionalName");
        String country = jsonObject.getString("country");
        String city = jsonObject.getString("city");

        if (!isValidEmail(email)) {
            sendResponse(exchange, UNAVAILABLE_FOR_LEGAL_REASONS, "Invalid email format");
            return;
        }

        if (!password.equals(confirmPassword)) {
            sendResponse(exchange, CONFLICT, "Passwords do not match");
            return;
        }

        if (password.length() < 8) {
            sendResponse(exchange, LENGTH_REQUIRED, "Password must be at least 8 characters long");
            return;
        }


        if (userController.checkUserExists(email, password)) {
            sendResponse(exchange, 226, "User already exists"); //IM_USED
            return;
        }

        userController.createUser(id, firstName, lastName, additionalName, email, password, country, city);

        String token;
        try {
            token = JWT.generateToken(email);
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 500, "Error generating token"); //INTERNAL_SERVER_ERROR
            return;
        }

        // Prepare JSON response with token
        JSONObject responseJson = new JSONObject();
        responseJson.put("message", "User created successfully");
        responseJson.put("token", token);

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        sendResponse(exchange, 201, responseJson.toString());
    }

    public void handleLogin(HttpExchange exchange, JSONObject jsonObject) throws IOException, SQLException {
        String email = jsonObject.getString("email");
        String password = jsonObject.getString("password");

        if (!userController.checkUserExists(email, password)) {
            sendResponse(exchange, 401, "Invalid credentials"); //UNAUTHORIZED
            return;
        }

        String token;
        try {
            token = JWT.generateToken(email);
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 500, "Error generating token"); //INTERNAL_SERVER_ERROR
            return;
        }

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        JSONObject responseJson = new JSONObject();
        responseJson.put("token", token);
        sendResponse(exchange, 200, responseJson.toString());
    }

    private String handleGet(HttpExchange exchange) throws IOException, SQLException {
        String query = exchange.getRequestURI().getQuery();
        if (query != null && query.startsWith("id=")) {
            String id = query.split("=")[1];
            User user = userController.getUserById(id);
            if (user != null) {
                return objectMapper.writeValueAsString(user);
            } else {
                exchange.sendResponseHeaders(404, -1); // Not Found
                return "User not found";
            }
        } else {
            return userController.getUsers();
        }
    }


    private String handlePut(HttpExchange exchange) throws IOException, SQLException {
        JSONObject jsonObject = new JSONObject(new String(exchange.getRequestBody().readAllBytes()));
        String id = jsonObject.getString("id");
        String firstName = jsonObject.getString("firstName");
        String lastName = jsonObject.getString("lastName");
        String additionalName = jsonObject.getString("additionalName");
        String email = jsonObject.getString("email");
        String password = jsonObject.getString("password");
        String country = jsonObject.getString("country");
        String city = jsonObject.getString("city");

        userController.updateUser(id, firstName, lastName, additionalName, email, password, country, city);
        return "User updated successfully";
    }

    //delete account was not required
    private String handleDelete(HttpExchange exchange) throws SQLException, IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query != null && query.startsWith("id=")) {
            String id = query.split("=")[1];
            userController.deleteUser(id);
            return "User deleted successfully";
        } else {
            exchange.sendResponseHeaders(400, -1); // Bad Request
            return "Invalid query parameter";
        }
    }

    private boolean isValidEmail(String email)
    {
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(email);
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String responseText) throws IOException {
        exchange.sendResponseHeaders(statusCode, responseText.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(responseText.getBytes());
        os.close();
    }

    //this are very unlikely to colide but i will add extra logic to check for the uniqueness later on
    private String generateRandomId() {
        return UUID.randomUUID().toString().substring(0, 16);
    }

}

