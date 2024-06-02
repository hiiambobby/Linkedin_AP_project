package com.backend.LinkedinServer.HTTPHandler;

import com.backend.LinkedinServer.Controller.UserController;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.backend.LinkedinServer.Util.JWT;
import org.json.JSONObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class LoginHandler implements HttpHandler {

    private final UserController userController;

    public LoginHandler() throws SQLException {
        this.userController = new UserController();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            try {
                JSONObject jsonObject = new JSONObject(new String(exchange.getRequestBody().readAllBytes()));
                String action = jsonObject.getString("action"); // "login" or "signup"

                if ("signup".equals(action)) {
                    handleSignUp(exchange, jsonObject);
                } else if ("login".equals(action)) {
                    handleLogin(exchange, jsonObject);
                } else {
                    exchange.sendResponseHeaders(400, 0); // Bad Request
                }
            } catch (SQLException e) {
                e.printStackTrace();
                exchange.sendResponseHeaders(500, 0);
            }
        } else {
            exchange.sendResponseHeaders(405, 0); // Method not allowed
        }
    }

    private void handleSignUp(HttpExchange exchange, JSONObject jsonObject) throws IOException, SQLException {
        String email = jsonObject.getString("email");
        String password = jsonObject.getString("password");
        String confirmPassword = jsonObject.getString("confirmPassword");

        // Validate email format
        if (!isValidEmail(email)) {
            sendResponse(exchange, 400, "Invalid email format");
            return;
        }

        // Check if password matches confirmPassword
        if (!password.equals(confirmPassword)) {
            sendResponse(exchange, 400, "Passwords do not match");
            return;
        }

        // Check if password is at least 8 characters long
        if (password.length() < 8) {
            sendResponse(exchange, 400, "Password must be at least 8 characters long");
            return;
        }

        // Create user
        String id = jsonObject.getString("id");
        String firstName = jsonObject.getString("firstName");
        String lastName = jsonObject.getString("lastName");
        String additionalName = jsonObject.getString("additionalName");
        String phoneNumber = jsonObject.getString("phoneNumber");
        String country = jsonObject.getString("country");
        LocalDate birthday = LocalDate.parse(jsonObject.getString("birthday"));

        userController.createUser(id, firstName, lastName, additionalName, email, phoneNumber, password, country, birthday);

        sendResponse(exchange, 201, "User created successfully");
    }

    public void handleLogin(HttpExchange exchange, JSONObject jsonObject) throws IOException, SQLException {
        System.out.println("Handling login request");
        String email = jsonObject.getString("email");
        String password = jsonObject.getString("password");

        if (!userController.checkUserExists(email, password)) {
            System.out.println("Invalid credentials for email: " + email);
            sendResponse(exchange, 401, "Invalid credentials");
            return;
        }

        // Create a JWT token
        String token;
        try {
            token =JWT.generateToken(email);
            System.out.println("Token generated successfully for email: " + email);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error generating token for email: " + email);
            sendResponse(exchange, 500, "Error generating token");
            return;
        }

        // Include the token in the response
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        JSONObject responseJson = new JSONObject();
        responseJson.put("token", token);
        sendResponse(exchange, 200, responseJson.toString());
        System.out.println("Response sent successfully for email: " + email);
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
}

