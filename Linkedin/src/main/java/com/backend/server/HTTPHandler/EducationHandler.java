package com.backend.server.HTTPHandler;

import com.backend.server.Controller.EducationController;
import com.backend.server.Model.Education;
import com.backend.server.Util.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EducationHandler implements HttpHandler {

    private final EducationController educationController;
    private final ObjectMapper objectMapper;

    public EducationHandler() throws SQLException {
        this.educationController = new EducationController();
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

            if (path.equals("/education") && method.equalsIgnoreCase("POST")) {
                JSONObject jsonObject = new JSONObject(new String(exchange.getRequestBody().readAllBytes()));
                handlePost(exchange, userId, jsonObject);
            } else if (path.equals("/education") && method.equalsIgnoreCase("GET")) {
                response = handleGet(exchange, userId);
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else if (path.equals("/education") && method.equalsIgnoreCase("PUT")) {
                JSONObject jsonObject = new JSONObject(new String(exchange.getRequestBody().readAllBytes()));
                response = handlePut(exchange, userId, jsonObject);
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else if (path.equals("/education") && method.equalsIgnoreCase("DELETE")) {
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
        try {
            // Extract individual fields from JSON
            String school = jsonObject.optString("school", "");
            String degree = jsonObject.optString("degree", "");
            String fieldOfStudy = jsonObject.optString("fieldOfStudy", "");
            String startDateMonth = jsonObject.optString("startDateMonth", "");
            int startDateYear = jsonObject.optInt("startDateYear", 0);
            String endDateMonth = jsonObject.optString("endDateMonth", "");
            int endDateYear = jsonObject.optInt("endDateYear", 0);
            String activities = jsonObject.optString("activities", "");
            String description = jsonObject.optString("description", "");

            // Convert skills from JSONArray to List<String>
            List<String> skills = new ArrayList<>();
            JSONArray skillsArray = jsonObject.optJSONArray("skills");
            if (skillsArray != null) {
                for (int i = 0; i < skillsArray.length(); i++) {
                    skills.add(skillsArray.optString(i, ""));
                }
            }

            boolean notifyNetwork = jsonObject.optBoolean("notifyNetwork", false);

            // Create an Education object and set its fields
            Education education = new Education();
            education.setSchool(school);
            education.setUserId(userId);
            education.setDegree(degree);
            education.setFieldOfStudy(fieldOfStudy);
            education.setStartDateMonth(startDateMonth);
            education.setStartDateYear(startDateYear);
            education.setEndDateMonth(endDateMonth);
            education.setEndDateYear(endDateYear);
            education.setActivities(activities);
            education.setDescription(description);
            education.setSkills((ArrayList<String>) skills);
            education.setNotifyNetwork(notifyNetwork);

            // Save the education record
            educationController.addEducation(education);

            // Prepare and send success response
            JSONObject responseJson = new JSONObject();
            responseJson.put("message", "Education info saved successfully");

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
        // Create an ObjectMapper instance for JSON serialization
        ObjectMapper objectMapper = new ObjectMapper();

        // Check if specific education ID is requested
        String query = exchange.getRequestURI().getQuery();
        String response;

        if (query != null && query.startsWith("id=")) {
            String id = query.split("=")[1];
            Education education = educationController.getEducationById(id);
            if (education != null && education.getUserId().equals(userId)) {
                response = objectMapper.writeValueAsString(education);
            } else {
                response = "{\"message\":\"Education info not found\"}";
            }
        } else {
            // Retrieve the education info from the database or other data source
            List<Education> educationList = educationController.getEducationsByUserId(userId);
            if (!educationList.isEmpty()) {
                // Serialize education list to JSON
                response = objectMapper.writeValueAsString(educationList);
            } else {
                // Return an error message if education info is not found
                response = "{\"message\":\"Education info not found\"}";
            }
        }

        return response;
    }

    private String handlePut(HttpExchange exchange, String userId, JSONObject jsonObject) throws IOException, SQLException {
        // Extract individual fields from JSON
        String school = jsonObject.optString("school", "");
        String degree = jsonObject.optString("degree", "");
        String fieldOfStudy = jsonObject.optString("fieldOfStudy", "");
        String startDateMonth = jsonObject.optString("startDateMonth", "");
        int startDateYear = jsonObject.optInt("startDateYear", 0);
        String endDateMonth = jsonObject.optString("endDateMonth", "");
        int endDateYear = jsonObject.optInt("endDateYear", 0);
        String activities = jsonObject.optString("activities", "");
        String description = jsonObject.optString("description", "");

        // Convert skills from JSONArray to List<String>
        List<String> skills = new ArrayList<>();
        JSONArray skillsArray = jsonObject.optJSONArray("skills");
        if (skillsArray != null) {
            for (int i = 0; i < skillsArray.length(); i++) {
                skills.add(skillsArray.optString(i, ""));
            }
        }

        boolean notifyNetwork = jsonObject.optBoolean("notifyNetwork", false);

        // Create an Education object and set its fields
        Education education = new Education();
        education.setSchool(school);
        education.setUserId(userId);
        education.setDegree(degree);
        education.setFieldOfStudy(fieldOfStudy);
        education.setStartDateMonth(startDateMonth);
        education.setStartDateYear(startDateYear);
        education.setEndDateMonth(endDateMonth);
        education.setEndDateYear(endDateYear);
        education.setActivities(activities);
        education.setDescription(description);
        education.setSkills((ArrayList<String>) skills);
        education.setNotifyNetwork(notifyNetwork);


        if (educationController.getEducationById(education.getSchool()) != null) {
            educationController.updateEducation(education);
            return "{\"message\":\"Education info updated successfully\"}";
        } else {
            educationController.addEducation(education);
            return "{\"message\":\"Education info created successfully\"}";
        }
    }

    private String handleDelete(HttpExchange exchange, String userId) throws IOException, SQLException {
        // Implement logic to delete education info
        String query = exchange.getRequestURI().getQuery();
        if (query != null && query.startsWith("id=")) {
            String id = query.split("=")[1];
            Education education = educationController.getEducationById(id);
            if (education != null && education.getUserId().equals(userId)) {
                educationController.deleteEducation(id);
                return "{\"message\":\"Education info deleted successfully\"}";
            } else {
                return "{\"message\":\"Education info not found\"}";
            }
        } else {
            return "{\"message\":\"Education info id not provided\"}";
        }
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
