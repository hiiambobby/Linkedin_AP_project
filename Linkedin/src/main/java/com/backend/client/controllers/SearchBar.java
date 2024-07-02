package com.backend.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;



public class SearchBar {
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox profileList;
    @FXML
    private ImageView profileView;
    @FXML
    private TextField searchField; // For user input

    @FXML
    private Button searchButton; // To trigger search


    private void setupScrollableProfileList() {
        // Initialize the VBox if needed (should already be defined in FXML)
        if (profileList == null) {
            profileList = new VBox();
            profileList.setSpacing(10); // Space between items
        }
    }

    public void profileView(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Profile.fxml"));
        Stage currentStage = (Stage) profileView.getScene().getWindow();
        currentStage.close();
        Stage newStage = new Stage();
        Image icon = new Image("/img/photo_2024-05-15_16-05-20.jpg");
        newStage.getIcons().add(icon);
        Scene newScene = new Scene(root);
        newStage.setScene(newScene);
        newStage.setTitle("Search");
        newStage.initStyle(StageStyle.DECORATED);
        newStage.show();
    }

    @FXML
    public void handleSearch(ActionEvent event) {
        String searchText = searchField.getText();
        if (searchText != null && !searchText.isEmpty()) {
            try {
                List<JSONObject> results = searchUsers(searchText);
                displaySearchResults(results);
            } catch (IOException e) {
                setAlert.showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while searching for users.");
                e.printStackTrace();
            }
        } else {
            setAlert.showAlert(Alert.AlertType.WARNING, "Warning", "Please enter a search term.");
        }
    }

    private List<JSONObject> searchUsers(String searchText) throws IOException {
        List<JSONObject> filteredResults = new ArrayList<>();

        // Get all user emails
        List<String> emails = getAllUserEmails();

        // Get primary information for each user and filter based on search text
        for (String email : emails) {
            JSONObject primaryInfo = getPrimaryInfoByEmail(email);
            if (primaryInfo != null &&
                    (primaryInfo.optString("firstName", "").toLowerCase().contains(searchText.toLowerCase()) ||
                            primaryInfo.optString("lastName", "").toLowerCase().contains(searchText.toLowerCase()) ||
                            primaryInfo.optString("headTitle", "").toLowerCase().contains(searchText.toLowerCase()))) {
                filteredResults.add(primaryInfo);
            }
        }

        return filteredResults;
    }

    private List<String> getAllUserEmails() throws IOException {
        List<String> emails = new ArrayList<>();
        URL url = new URL("http://localhost:8000/user");
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

            // Parse JSON response to extract emails
            JSONObject jsonResponse = new JSONObject(response.toString());
            for (String email : jsonResponse.keySet()) {
                emails.add(email);
            }
        } else {
            setAlert.showAlert(Alert.AlertType.ERROR, "Error", "Failed to retrieve user emails. Response code: " + responseCode);
        }

        conn.disconnect();
        return emails;
    }

    private JSONObject getPrimaryInfoByEmail(String email) throws IOException {
        URL url = new URL("http://localhost:8000/primaryInfo?id=" + email);
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
            setAlert.showAlert(Alert.AlertType.ERROR, "Error", "Failed to retrieve primary info for email: " + email);
        }

        conn.disconnect();
        return null;
    }

    private void displaySearchResults(List<JSONObject> results) {
        profileList.getChildren().clear();

        for (JSONObject result : results) {
            Node profileView = createProfileView(result);
            profileList.getChildren().add(profileView);
        }
    }

    private Node createProfileView(JSONObject profileData) {
        String profilePicUrl = profileData.optString("profilePic", "/icons/icons8-male-user-48.png");
        String name = profileData.optString("firstName", "") + " " + profileData.optString("lastName", "");
        String headerTitle = profileData.optString("headTitle", "");

        return new ProfileViewComponent(profilePicUrl, name, headerTitle);
    }


}