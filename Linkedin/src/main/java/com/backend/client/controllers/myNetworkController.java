package com.backend.client.controllers;

import com.backend.client.components.ConnectRequestComponent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class myNetworkController {

    @FXML
    private ImageView profileId;
    @FXML
    private VBox networkList;

    @FXML
    public void showRequests(ActionEvent event) {
        try {
            // Get the pending connection requests
            List<JSONObject> requests = getRequests();
            if (requests != null && !requests.isEmpty()) {
                // Fetch primary information for each request
                List<JSONObject> requestDetails = fetchPrimaryInfoForRequests(requests);
                displaySearchResults(requestDetails);
            } else {
                // Show an alert if no requests are found
                showAlert(Alert.AlertType.INFORMATION, "No Requests", "You have no pending connection requests.");
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while fetching connection requests.");
            e.printStackTrace();
        }
    }

    @FXML
    public void showFollowers(ActionEvent event) {
        // Implementation for showing followers goes here
    }

    @FXML
    public void showFollowings(ActionEvent event) {
        // Implementation for showing followings goes here
    }

    @FXML
    public void searchId(MouseEvent mouseEvent) throws IOException {
        openNewStage("/fxml/Search.fxml", "Search");
    }

    @FXML
    public void profileView(MouseEvent mouseEvent) throws IOException {
        openNewStage("/fxml/Profile.fxml", "Profile");
    }

    private void openNewStage(String fxmlPath, String title) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Stage currentStage = (Stage) profileId.getScene().getWindow();
        currentStage.close();
        Stage newStage = new Stage();
        Image icon = new Image("/img/photo_2024-05-15_16-05-20.jpg");
        newStage.getIcons().add(icon);
        Scene newScene = new Scene(root);
        newStage.setScene(newScene);
        newStage.setTitle(title);
        newStage.initStyle(StageStyle.DECORATED);
        newStage.show();
    }

    private List<JSONObject> getRequests() throws IOException {
        String email = readEmail();
        URL url = new URL("http://localhost:8000/connect?user=" + URLEncoder.encode(email, StandardCharsets.UTF_8.toString()));
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONArray jsonResponse = new JSONArray(response.toString());
            List<JSONObject> requestList = new ArrayList<>();
            for (int i = 0; i < jsonResponse.length(); i++) {
                requestList.add(jsonResponse.getJSONObject(i));
            }
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
            System.err.println("Error response: " + errorResponse);
        }

        conn.disconnect();
        return null;
    }

    private List<JSONObject> fetchPrimaryInfoForRequests(List<JSONObject> requests) throws IOException {
        List<JSONObject> detailedRequests = new ArrayList<>();
        for (JSONObject request : requests) {
            String receiverEmail = request.optString("sender");
            JSONObject primaryInfo = getPrimaryInfoByEmail(receiverEmail);
            if (primaryInfo != null) {
                // Add the request note to the primary info
                primaryInfo.put("note", request.optString("note", "No note provided"));
                detailedRequests.add(primaryInfo);
            }
        }
        return detailedRequests;
    }

    private JSONObject getPrimaryInfoByEmail(String email) throws IOException {
        URL url = new URL("http://localhost:8000/primaryInfo?id=" + URLEncoder.encode(email, StandardCharsets.UTF_8.toString()));
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            return new JSONObject(response.toString());
        } else {
            System.err.println("Failed to retrieve primary info, Response code: " + responseCode);
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            StringBuilder errorResponse = new StringBuilder();
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                errorResponse.append(errorLine);
            }
            errorReader.close();
            System.err.println("Error response: " + errorResponse);
        }

        conn.disconnect();
        return null;
    }

    private void displaySearchResults(List<JSONObject> results) {
        networkList.getChildren().clear();
        for (JSONObject result : results) {
            Node profileView = networkListView(result);
            networkList.getChildren().add(profileView);
        }
    }

    private Node networkListView(JSONObject result) {
        String profilePicUrl = result.optString("profilePic", "/icons/icons8-male-user-48.png");
        String name = result.optString("firstName", "") + " " + result.optString("lastName", "");
        String headerTitle = result.optString("headTitle", "");
        String note = result.optString("note", "No note provided");
        return new ConnectRequestComponent(profilePicUrl, name, headerTitle, note);
    }

    public String readEmail() {
        String filePath = "userdata.txt"; // Path to your JSON file
        try {
            String jsonString = readJsonFile(filePath);
            JSONObject jsonObject = new JSONObject(jsonString);
            return jsonObject.optString("email", "Unknown");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String readJsonFile(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
