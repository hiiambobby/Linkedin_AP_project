package com.backend.LinkedinServer.HTTPHandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.backend.LinkedinServer.Model.User;

public class UserHandler implements HttpHandler {
    private List<User> users = new ArrayList<>();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String response = "";

        if ("GET".equalsIgnoreCase(method)) {
            response = getUsers();
        } else if ("POST".equalsIgnoreCase(method)) {
            response = createUser(exchange);
        } else if ("PUT".equalsIgnoreCase(method)) {
            response = updateUser(exchange);
        } else if ("DELETE".equalsIgnoreCase(method)) {
            response = deleteUser(exchange);
        }

        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private String getUsers() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(users);
    }

    private String createUser(HttpExchange exchange) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.readValue(exchange.getRequestBody(), User.class);
        users.add(user);
        return "User created successfully";
    }

    private String updateUser(HttpExchange exchange) throws IOException {
        // Implementation for updating a user
        return "User updated successfully";
    }

    private String deleteUser(HttpExchange exchange) throws IOException {
        // Implementation for deleting a user
        return "User deleted successfully";
    }
}
