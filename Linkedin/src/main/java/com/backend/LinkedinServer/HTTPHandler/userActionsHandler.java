package com.backend.LinkedinServer.HTTPHandler;

import com.backend.LinkedinServer.Controller.UserController;
import com.backend.LinkedinServer.Model.User;
import com.backend.LinkedinServer.Util.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.util.regex.Pattern;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.regex.Matcher;


public class userActionsHandler implements HttpHandler {

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
                    case "POST":
                        response = handlePost(exchange);
                        break;
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

        if (!isValidEmail(email)) {
            sendResponse(exchange, 400, "Invalid email format");
            return;
        }

        if (!password.equals(confirmPassword)) {
            sendResponse(exchange, 400, "Passwords do not match");
            return;
        }

        if (password.length() < 8) {
            sendResponse(exchange, 400, "Password must be at least 8 characters long");
            return;
        }

        String id = generateRandomId(); // Implement this method to generate a random ID
        String firstName = jsonObject.getString("firstName");
        String lastName = jsonObject.getString("lastName");
        String additionalName = jsonObject.getString("additionalName");
        String country = jsonObject.getString("country");
        String city = jsonObject.getString("city");


        userController.createUser(id,firstName, lastName, additionalName, email,password, country, city);

        sendResponse(exchange, 201, "User created successfully");
    }

    public void handleLogin(HttpExchange exchange, JSONObject jsonObject) throws IOException, SQLException {
        String email = jsonObject.getString("email");
        String password = jsonObject.getString("password");

        if (!userController.checkUserExists(email, password)) {
            sendResponse(exchange, 401, "Invalid credentials");
            return;
        }

        String token;
        try {
            token = JWT.generateToken(email);
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 500, "Error generating token");
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

    private String handlePost(HttpExchange exchange) throws IOException, SQLException {
        JSONObject jsonObject = new JSONObject(new String(exchange.getRequestBody().readAllBytes()));
        String id = generateRandomId(); // Implement this method to generate a random ID
        String firstName = jsonObject.getString("firstName");
        String lastName = jsonObject.getString("lastName");
        String additionalName = jsonObject.getString("additionalName");
        String email = jsonObject.getString("email");
        String password = jsonObject.getString("password");
        String country = jsonObject.getString("country");
        String city = jsonObject.getString("city");

        userController.createUser(id,firstName, lastName, additionalName, email, password, country, city);
        return "User created successfully";
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

        userController.updateUser(id, firstName, lastName, additionalName, email,password, country, city);
        return "User updated successfully";
    }

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

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String responseText) throws IOException {
        exchange.sendResponseHeaders(statusCode, responseText.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(responseText.getBytes());
        os.close();
    }

    private String generateRandomId() {
        // Implement logic to generate a random unique ID
        return java.util.UUID.randomUUID().toString().substring(0, 16);
    }
}

