package com.backend.client.controllers;

import com.backend.client.components.UserProfileComponent;
import com.fasterxml.jackson.annotation.JsonAlias;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class infoControl {

    @FXML
    private TextField emailId;

    @FXML
    private TextField monthId;

    @FXML
    private TextField dayId;

    @FXML
    private TextField instantId;

    @FXML
    private TextField phoneId;

    @FXML
    private TextField profileUrlId;

    @FXML
    private TextArea locationId;

    public infoControl() {
        // Constructor should be empty or contain only initialization logic that does not involve @FXML fields
    }

    @FXML
    private void initialize() {
        try {
            JSONObject infos = UserProfileComponent.getContactInfo();
            System.out.println("Retrieved JSON: " + infos.toString());

            emailId.setText(infos.optString("user_id", ""));
            emailId.setEditable(false);

            profileUrlId.setText(infos.optString("profile_url", ""));
            profileUrlId.setEditable(false);

            phoneId.setText(infos.optString("phone_number", "") +
                    " (" + infos.optString("phone_type", "") + ")");
            phoneId.setEditable(false);

            String visibility = infos.optString("visibility", "");
            switch (visibility) {
                case "All LinkedIn members":
                    setEditableDateFields(infos);
                    break;
                case "Only you":
                    disableDateFields();
                    break;
                case "Your connections":
                    if (isUserConnected(emailId.getText())) {
                        setEditableDateFields(infos);
                    } else {
                        disableDateFields();
                    }
                    break;
                default:
                    disableDateFields();
                    break;
            }


            instantId.setText(infos.optString("instant_messaging", ""));
            instantId.setEditable(false);

            locationId.setText(infos.optString("address", ""));
            locationId.setEditable(false);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error retrieving contact info: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error parsing JSON: " + e.getMessage());
        }
    }

    private void setEditableDateFields(JSONObject infos) {
        monthId.setText(infos.optString("month", ""));
        dayId.setText(String.valueOf(infos.optInt("day", 0)));
        monthId.setEditable(false);
        dayId.setEditable(false);
    }

    // Method to disable the date fields
    private void disableDateFields() {
        monthId.setEditable(false);
        dayId.setEditable(false);
    }

    public List<JSONObject> findConnections() throws IOException {
        System.out.println("Starting findConnections method");

        String urlString = String.format("http://localhost:8000/connect?user=%s&connected=%s",
                URLEncoder.encode(UserEmail.readEmail(), "UTF-8"), URLEncoder.encode("type", "UTF-8"));

        System.out.println("Connecting to URL: " + urlString);

        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        int responseCode = conn.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Print debug statement indicating a successful connection
            System.out.println("Connection successful, reading response");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            System.out.println("Raw JSON Response: " + response.toString());

            // Parse the JSON response and convert it to a list of JSON objects
            JSONArray jsonResponse = new JSONArray(response.toString());
            List<JSONObject> requestList = new ArrayList<>();
            for (int i = 0; i < jsonResponse.length(); i++) {
                requestList.add(jsonResponse.getJSONObject(i));
            }

            System.out.println("Parsed request list: " + requestList);

            return requestList;
        } else {
            System.err.println("Failed to get connect requests, Response code: " + responseCode);

            BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            StringBuilder errorResponse = new StringBuilder();
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                errorResponse.append(errorLine);
            }
            errorReader.close();

            System.err.println("Error response: " + errorResponse.toString());
        }
        conn.disconnect();
        return null;
    }

    // Method to check if a specific userId is in the connections
    public boolean isUserConnected(String userId) throws IOException {
        List<JSONObject> connections = findConnections();
        if (connections != null) {
            for (JSONObject connection : connections) {
                if (userId.equals(connection.optString("sender")) || userId.equals(connection.optString("receiver")) ) {
                    System.out.println("check connection =>" + connection.optString("sender"));
                    return true;
                }
            }
        }
        return false;
    }


}
