//package com.backend.LinkedinServer.HTTPHandler;
//
//import com.backend.LinkedinServer.Controller.UserController;
//import org.json.JSONObject;
//import com.sun.net.httpserver.HttpExchange;
//import com.sun.net.httpserver.HttpHandler;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.nio.file.LinkOption;
//import java.sql.SQLException;
//import java.time.Instant;
//import java.time.LocalDate;
//import java.time.ZoneId;
//import java.util.Date;
//import java.util.ArrayList;
//import java.util.List;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.backend.LinkedinServer.Model.User;
//
//import static com.backend.LinkedinServer.HTTPHandler.HttpStatusCode.*;
//
//public class UserHandler implements HttpHandler {
//    private final UserController userController;
//    private final ObjectMapper objectMapper;
//
//    public UserHandler() throws SQLException {
//        userController = new UserController();
//        objectMapper = new ObjectMapper();
//    }
//
//    @Override
//    public void handle(HttpExchange exchange) throws IOException {
//        String method = exchange.getRequestMethod();
//        String response = "";
//
//        try {
//            switch (method.toUpperCase()) {
//                case "GET":
//                    response = handleGet(exchange);
//                    break;
//                case "POST":
//                    response = handlePost(exchange);
//                    break;
//                case "PUT":
//                    response = handlePut(exchange);
//                    break;
//                case "DELETE":
//                    response = handleDelete(exchange);
//                    break;
//                default:
//                    exchange.sendResponseHeaders(METHOD_NOT_ALLOWED, -1); //
//                    return;
//            }
//            exchange.sendResponseHeaders(200, response.getBytes().length);
//        } catch (SQLException e) {
//            exchange.sendResponseHeaders(500, -1); // 500 Internal Server Error
//            e.printStackTrace();
//        }
//
//        OutputStream os = exchange.getResponseBody();
//        os.write(response.getBytes());
//        os.close();
//    }
//
//    private String handleGet(HttpExchange exchange) throws IOException, SQLException {
//        String query = exchange.getRequestURI().getQuery();
//        if (query != null && query.startsWith("id=")) {
//            String id = query.split("=")[1];
//            User user = userController.getUserById(id);
//            if (user != null) {
//                return objectMapper.writeValueAsString(user);
//            } else {
//                exchange.sendResponseHeaders(404, -1); // 404 Not Found
//                return "User not found";
//            }
//        } else {
//            return userController.getUsers();
//        }
//    }
//
//    private String handlePost(HttpExchange exchange) throws IOException, SQLException {
//        JSONObject jsonObject = new JSONObject(new String(exchange.getRequestBody().readAllBytes()));
//        String id = jsonObject.getString("id");
//        String firstName = jsonObject.getString("firstName");
//        String lastName = jsonObject.getString("lastName");
//        String additionalName = jsonObject.getString("additionalName");
//        String email = jsonObject.getString("email");
//        String phoneNumber = jsonObject.getString("phoneNumber");
//        String password = jsonObject.getString("password");
//        String country = jsonObject.getString("country");
//        LocalDate birthday = LocalDate.parse(jsonObject.getString("birthday"));
//
//        userController.createUser(id, firstName, lastName, additionalName, email, phoneNumber, password, country, birthday);
//        return "User created successfully";
//    }
//
//    private String handlePut(HttpExchange exchange) throws IOException, SQLException {
//        JSONObject jsonObject = new JSONObject(new String(exchange.getRequestBody().readAllBytes()));
//        String id = jsonObject.getString("id");
//        String firstName = jsonObject.getString("firstName");
//        String lastName = jsonObject.getString("lastName");
//        String additionalName = jsonObject.getString("additionalName");
//        String email = jsonObject.getString("email");
//        String phoneNumber = jsonObject.getString("phoneNumber");
//        String password = jsonObject.getString("password");
//        String country = jsonObject.getString("country");
//        LocalDate birthday = LocalDate.parse(jsonObject.getString("birthday"));
//
//        userController.updateUser(id, firstName, lastName, additionalName, email, phoneNumber, password, country, birthday);
//        return "User updated successfully";
//    }
//
//    private String handleDelete(HttpExchange exchange) throws SQLException, IOException {
//        String query = exchange.getRequestURI().getQuery();
//        if (query != null && query.startsWith("id=")) {
//            String id = query.split("=")[1];
//            userController.deleteUser(id);
//            return "User deleted successfully";
//        } else {
//            exchange.sendResponseHeaders(400, -1); // 400 Bad Request
//            return "Invalid query parameter";
//        }
//    }
//}
