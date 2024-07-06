package com.backend.client.controllers;

import com.backend.server.Model.Post;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PostController{

    @FXML
    private Label captionLabel;

    @FXML
    private Label commentsCounterLabel;

    @FXML
    private Button likeBtn;

    @FXML
    private Label likesCounterLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private ImageView postImage;

    @FXML
    private ImageView profilePhoto;

    private boolean isLiked = false; // Default value
    private int currentPostId; // This should be set based on the current post
    private String userId; // The ID of the current user
    private Post post;

    @FXML
    public void initialize() {
        updateLikeButton();
    }


    public void setPostDetails(Post post, String userId) {
        this.currentPostId = post.getId();
        this.userId = userId;
        captionLabel.setText(post.getText());
        checkIfLiked();
    }

    private void checkIfLiked() {

    }

    @FXML
    void commentSelected(ActionEvent event) {

    }

    @FXML
    void likeSelected(ActionEvent event) {
        try {
            // Check current like status and send appropriate request
            if (isLiked()) {
                unlikePost();
            } else {
                likePost();
            }
        } catch (IOException e) {
            setAlert.showAlert(Alert.AlertType.ERROR, "Error", "Failed to update like status.");
        }

    }
    private void likePost() throws IOException {
        sendLikeRequest("POST");
        updateLikeButton();  // Update UI
    }
    private void unlikePost() throws IOException {
        sendLikeRequest("DELETE");
        updateLikeButton();  // Update UI
    }
    private void sendLikeRequest(String method) throws IOException {
        String urlString = "http://localhost:8000/post/" + post.getId() + "/like";
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setDoOutput(true);

        String userId = UserEmail.readEmail();
        String jsonInputString = "{\"user_id\": \"" + userId + "\"}";

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK && responseCode != HttpURLConnection.HTTP_CREATED) {
            throw new IOException("Failed to update like status. Response Code: " + responseCode);
        }
    }

    @FXML
    void viewCommentsSelected(ActionEvent event) {

    }
    @FXML
    void viewLikesSelected(ActionEvent event) {

        // Fetch the list of usernames who liked the post
        List<String> usernames = fetchUsernamesWhoLikedPost(currentPostId);

        // Update the ListView with the list of usernames
        if (usernames != null) {
//            likesListView.getItems().clear();
//            likesListView.getItems().addAll(usernames);
            System.out.println(usernames);
        } else {
            showAlert("Error", "Failed to retrieve likes.");
        }
    }
    private List<String> parseUsernamesFromResponse(String jsonResponse) {
        List<String> usernames = new ArrayList<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonResponse);

            if (root.isArray()) {
                for (JsonNode node : root) {
                    usernames.add(node.asText());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return usernames;
    }

    // Method to fetch usernames who liked a post
    private List<String> fetchUsernamesWhoLikedPost(int postId) {
        List<String> usernames = new ArrayList<>();
        HttpURLConnection conn = null;
        try {
            // Construct the URL for the request
            URL url = new URL("http://localhost:8000/post/" + postId + "/likes");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            // Get the response
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                usernames = parseUsernamesFromResponse(response.toString());
            } else {
                System.err.println("Failed to retrieve likes. Response Code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return usernames;
    }

    public void setPostDetails(String caption) {
        captionLabel.setText(caption);
    }

    // Method to show alert dialogs
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void updateLikeButton() {
        if (isLiked()) {
            likeBtn.setText("Unlike");
        } else {
            likeBtn.setText("Like");
        }
    }

    private boolean isLiked() {
        try {

            // Make a GET request to fetch usernames who liked the post
            String urlString = "http://localhost:8000/posts/" + currentPostId + "/likes";
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            // Read the response
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();


                List<String> usernames = parseUsernamesFromResponse(response.toString());

                return usernames.contains(userId);
            } else {
                System.out.println("Failed to fetch likes. Response code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
    /*public void setPostDetails() {
        captionLabel.setText("");
    }*/

}
