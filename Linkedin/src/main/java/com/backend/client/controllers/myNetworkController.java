package com.backend.client.controllers;

import com.backend.client.components.ConnectRequestComponent;
import com.backend.client.components.ProfileViewComponent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class myNetworkController {

    @FXML
    private ImageView profileId;
    @FXML
    private VBox networkList;

    @FXML
    public void showRequests(ActionEvent event) {
        try {
            List<JSONObject> requests = getRequests();
            if (requests != null && !requests.isEmpty()) {
                List<JSONObject> requestDetails = fetchPrimaryInfoForRequests(requests);
                displaySearchResults(requestDetails);
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while fetching connection requests.");
            e.printStackTrace();
        }
    }

    @FXML
    public void showFollowers(ActionEvent event) {
        try {
            List<JSONObject> followers = getFollows(readEmail(), "followers");
            if (followers != null && !followers.isEmpty()) {
                List<JSONObject> followerDetails = fetchPrimaryInfoForFollows(followers, "follower");
                displaySearchResultsForFollow(followerDetails);
            } else {
                showAlert(Alert.AlertType.INFORMATION, "No Followers", "You have no followers.");
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while fetching followers.");
            e.printStackTrace();
        }
    }

    @FXML
    public void showFollowings(ActionEvent event) {
        try {
            List<JSONObject> followings = getFollows(readEmail(), "followings");
            if (followings != null && !followings.isEmpty()) {
                List<JSONObject> followingDetails = fetchPrimaryInfoForFollows(followings, "following");
                displaySearchResultsForFollow(followingDetails);
            } else {
                showAlert(Alert.AlertType.INFORMATION, "No Followings", "You are not following anyone.");
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while fetching followings.");
            e.printStackTrace();
        }
    }

    @FXML
    public void searchId(MouseEvent mouseEvent) throws IOException {
        openNewStage("/fxml/Search.fxml", "Search");
    }

    @FXML
    public void profileView(MouseEvent mouseEvent) throws IOException {
        openNewStage("/fxml/Profile.fxml", "Profile");
    }
    @FXML
    public void openNetwork(MouseEvent mouseEvent) throws IOException {
        openNewStage("/fxml/MessagePage.fxml", "Messages");
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
    private List<JSONObject> fetchPrimaryInfoForFollows(List<JSONObject> follows, String type) throws IOException {
        List<JSONObject> detailedFollows = new ArrayList<>();
        for (JSONObject follow : follows) {
            try {
                String followEmail = follow.getString(type);
                System.out.println("Fetching primary info for: " + followEmail);
                JSONObject primaryInfo = getPrimaryInfoByEmail(followEmail);
                if (primaryInfo != null) {
                    detailedFollows.add(primaryInfo);
                }
            } catch (JSONException e) {
                System.err.println("Error: " + e.getMessage());
                System.err.println(type + " JSON: " + follow.toString());
            }
        }
        return detailedFollows;
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
            Node profileView = connectListView(result);
            networkList.getChildren().add(profileView);
        }
    }

    private Node connectListView(JSONObject result) {
        String profilePicUrl = result.optString("profilePic", "/icons/icons8-male-user-48.png");
        String name = result.optString("firstName", "") + " " + result.optString("lastName", "");
        String headerTitle = result.optString("headTitle", "");
        String note = result.optString("note", "No note provided");
        String email =  result.optString("userId", "");
        return new ConnectRequestComponent(profilePicUrl, name, headerTitle, note,email);
    }

    private Node followListView(JSONObject result) {
        String profilePicUrl = result.optString("profilePic", "/icons/icons8-male-user-48.png");
        String name = result.optString("firstName", "") + " " + result.optString("lastName", "");
        String headerTitle = result.optString("headTitle", "");
        return new ProfileViewComponent(result,profilePicUrl, name, headerTitle);
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
    //do a get on followers and followings send the result to the profile view
    private List<JSONObject> getFollows(String userId, String type) {
        try {
            // Validate the type parameter
            if (!"followers".equalsIgnoreCase(type) && !"followings".equalsIgnoreCase(type)) {
                throw new IllegalArgumentException("Invalid type. Must be 'followers' or 'followings'.");
            }

            // Construct the URL with query parameters for userId and type
            String urlString = String.format("http://localhost:8000/follow?userId=%s&type=%s",
                    URLEncoder.encode(userId, "UTF-8"), URLEncoder.encode(type, "UTF-8"));
            URL url = new URL(urlString);

            // Open connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read response
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }

                    // Parse the response JSON
                    JSONArray jsonResponse = new JSONArray(response.toString());
                    System.out.println(type + " JSON response: " + jsonResponse.toString());

                    // Extract the relevant field from each object in the array
                    List<JSONObject> followsList = new ArrayList<>();
                    for (int i = 0; i < jsonResponse.length(); i++) {
                        JSONObject followObj = jsonResponse.getJSONObject(i);
                        String followEmail = followObj.getString(type.equals("followers") ? "follower" : "following");
                        JSONObject followJson = new JSONObject();
                        followJson.put(type.equals("followers") ? "follower" : "following", followEmail);
                        followsList.add(followJson);
                    }
                    return followsList;
                }
            } else {
                System.err.println("GET request failed. Response Code: " + responseCode);
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private void displaySearchResultsForFollow(List<JSONObject> results) {
        networkList.getChildren().clear();
        for (JSONObject result : results) {
            Node profileView = followListView(result);
            networkList.getChildren().add(profileView);
        }
    }


}
