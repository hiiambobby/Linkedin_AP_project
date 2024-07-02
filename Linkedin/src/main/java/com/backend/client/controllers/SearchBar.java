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
import java.util.ResourceBundle;
import java.util.stream.Collectors;



public class SearchBar {
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox profileList;
    @FXML
    private ImageView profileId;
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
        Stage currentStage = (Stage) profileId.getScene().getWindow();
        currentStage.close();
        Stage newStage = new Stage();
        Image icon = new Image("/img/photo_2024-05-15_16-05-20.jpg");
        newStage.getIcons().add(icon);
        Scene newScene = new Scene(root);
        newStage.setScene(newScene);
        newStage.setTitle("profile");
        newStage.initStyle(StageStyle.DECORATED);
        newStage.show();
    }


    @FXML
    public void handleSearch(ActionEvent event) {
        String searchText = searchField.getText();
        System.out.println("Search button clicked. Search text: " + searchText);
    try {


        if (searchText != null && !searchText.isEmpty()) {
            try {
                List<JSONObject> results = searchUsers(searchText);
                System.out.println(results);
                displaySearchResults(results);
                System.out.println("Search completed successfully. Results: " + results);
            } catch (IOException e) {
                setAlert.showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while searching for users.");
                System.err.println("IOException occurred during search: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            setAlert.showAlert(Alert.AlertType.WARNING, "Warning", "Please enter a search term.");
        }
    }
    catch (Exception e)
    {
        System.out.println("found where the error is");
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

    public List<String> getAllUserEmails() throws IOException {
        List<String> emails = new ArrayList<>();
        URL url = new URL("http://localhost:8000/user");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        int responseCode = conn.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parse JSON response
            JSONArray usersArray = new JSONArray(response.toString());

            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject userObject = usersArray.getJSONObject(i);
                // Extract email, handle optional fields
                String email = userObject.optString("email", "");
                if (!email.isEmpty()) {
                    emails.add(email);
                }
            }
        } else {
            // Handle error response
            System.err.println("Failed to retrieve user emails. Response code: " + responseCode);
        }

        conn.disconnect();
        return emails;
    }




    private JSONObject getPrimaryInfoByEmail(String email) throws IOException {
        URL url = new URL("http://localhost:8000/primaryInfo?id=" + URLEncoder.encode(email, StandardCharsets.UTF_8.toString()));
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        int responseCode = conn.getResponseCode();
        System.out.println("Response Code for primary info retrieval: " + responseCode);
        System.out.println("Request URL: " + url);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            System.out.println("Primary info response: " + jsonResponse);
            return jsonResponse;
        } else {
            System.err.println("Failed to retrieve primary info. Response code: " + responseCode);
            System.err.println("Error message: " + conn.getResponseMessage());
            // Optionally, read error stream for more details
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