package com.backend.LinkedinServer.HTTPHandler;

import com.backend.LinkedinServer.Controller.UserController;
import org.json.JSONObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.LinkOption;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.backend.LinkedinServer.Model.User;

public class UserHandler implements HttpHandler {

    private final UserController userController;

    public UserHandler() {
        userController = new UserController();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String response = "";

        switch (method.toUpperCase()) {
            case "GET":
                response = handleGetUser(exchange);
                break;
            case "POST":
                response = handlePostUser(exchange);
                break;
            case "PUT":
                response = handlePutUser(exchange);
                break;
            case "DELETE":
                response = handleDeleteUser(exchange);
                break;
            default:
                exchange.sendResponseHeaders(405, -1); // 405 Method Not Allowed
                return;
        }

        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private String handleGetUser(HttpExchange exchange) throws IOException {
        try {
            return userController.getUsers();
        } catch (Exception e) {
            e.printStackTrace();
            return "Internal Server Error: " + e.getMessage();
        }
    }

    private String handlePostUser(HttpExchange exchange) throws IOException {
        JSONObject jsonObject = new JSONObject(new String(exchange.getRequestBody().readAllBytes()));

        String id = jsonObject.getString("id");
        String firstName = jsonObject.getString("firstName");
        String lastName = jsonObject.getString("lastName");
        String additionalName = jsonObject.getString("additionalName");
        String email = jsonObject.getString("email");
        String phoneNumber = jsonObject.getString("phoneNumber");
        String password = jsonObject.getString("password");
        String confirmPassword = jsonObject.getString("confirmPassword");
        String country = jsonObject.getString("country");
        LocalDate birthday = Instant.ofEpochMilli(jsonObject.getLong("birthday")).atZone(ZoneId.systemDefault()).toLocalDate();
        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            return "Passwords do not match";
        }

        // Check email format
        if (!isValidEmail(email)) {
            return "Invalid email format";
        }

        try {
            // Check if email already exists
            if (userController.isEmailExists(email)) {
                return "Email already exists";
            }

            userController.createUser(id, firstName, lastName,additionalName, email, phoneNumber, password, country, birthday);
            return "User created successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error creating user: " + e.getMessage();
        }
    }

    private String handlePutUser(HttpExchange exchange) throws IOException {
        JSONObject jsonObject = new JSONObject(new String(exchange.getRequestBody().readAllBytes()));

        String id = jsonObject.getString("id");
        String firstName = jsonObject.getString("firstName");
        String lastName = jsonObject.getString("lastName");
        String additionalName = jsonObject.getString("additionalName");
        String email = jsonObject.getString("email");
        String phoneNumber = jsonObject.getString("phoneNumber");
        String password = jsonObject.getString("password");
        String confirmPassword = jsonObject.getString("confirmPassword");
        String country = jsonObject.getString("country");
        LocalDate birthday = Instant.ofEpochMilli(jsonObject.getLong("birthday")).atZone(ZoneId.systemDefault()).toLocalDate();

        try {
            userController.updateUser(id, firstName, lastName,additionalName, email, phoneNumber, password, country, birthday);
            return "User updated successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error updating user: " + e.getMessage();
        }
    }

    private String handleDeleteUser(HttpExchange exchange) throws IOException {
        String id = exchange.getRequestURI().getPath().replace("/users/", "");

        try {
            userController.deleteUser(id);
            return "User deleted successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error deleting user: " + e.getMessage();
        }
    }

    private boolean isValidEmail(String email) {
        // Very basic email format validation
        return email != null && email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    }
}