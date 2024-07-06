package com.backend.client.controllers;

import com.backend.server.Model.Post;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
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


public class TimeLineController {

    @FXML
    private ImageView pmId;
    @FXML
    private ImageView homeId;

    @FXML
    private ImageView networkId;

    @FXML
    private ImageView profileId;

    @FXML
    private VBox postsVbox;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchField;

    @FXML
    private ImageView searchId;


    private List<Post> searchResults = new ArrayList<>();

    @FXML
    public void handleSearch(ActionEvent event) {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            System.out.println("Search keyword is empty.");
            return;
        }

        try {
            // Define the search URL with the keyword as a query parameter
            //keyword?query={keyword}
            String urlString = "http://localhost:8000/post/keyword?query=" + URLEncoder.encode(keyword, "UTF-8");
            URL url = new URL(urlString);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            // Handle the response
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read and parse the response
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }

                    // Parse JSON response
                    JSONArray jsonArray = new JSONArray(response.toString());
                    searchResults.clear(); // Clear previous results

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Post post = new Post();
                        post.setId(jsonObject.getInt("id"));
                        post.setSender(jsonObject.getString("sender"));
                        post.setText(jsonObject.getString("text"));

                        // Convert JSON arrays to lists
                        JSONArray videoArray = jsonObject.getJSONArray("video");
                        List<String> videoList = new ArrayList<>();
                        for (int j = 0; j < videoArray.length(); j++) {
                            videoList.add(videoArray.getString(j));
                        }
                        post.setVideo(videoList);

                        JSONArray imageArray = jsonObject.getJSONArray("image");
                        List<String> imageList = new ArrayList<>();
                        for (int j = 0; j < imageArray.length(); j++) {
                            imageList.add(imageArray.getString(j));
                        }
                        post.setImage(imageList);

                        searchResults.add(post);
                    }

                    // Use or display searchResults
                    System.out.println("Search Results: " + searchResults);
                    // Update UI or perform other actions with searchResults

                }
            } else {
                System.out.println("Failed to search posts. Response Code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Getters for searchResults if needed
    public List<Post> getSearchResults() {
        return searchResults;
    }

    public void addPostFeed(String caption) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Post.fxml"));
            VBox postComponent = loader.load();
            // Access the controller of PostComponent to set data
            PostController controller = loader.getController();
            controller.setPostDetails(caption);
            // Add the component to the VBox
            postsVbox.getChildren().add(postComponent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void profileView(MouseEvent mouseEvent) throws IOException {
        openNewStage("/fxml/Profile.fxml", "Profile");
    }



    public void openMessage(MouseEvent mouseEvent) throws IOException {
        openNewStage("/fxml/Network.fxml", "My Network");
    }

    public void openSearch(MouseEvent mouseEvent) throws IOException {
        openNewStage("/fxml/Search.fxml", "Search");
    }

    private void openNewStage(String fxmlPath, String title) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Stage currentStage = (Stage) pmId.getScene().getWindow();
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

    public void openNetwork(MouseEvent mouseEvent) throws IOException {
        openNewStage("/fxml/Network.fxml", "My Network");

    }

    public void searchwithHashtag(ActionEvent event) {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            System.out.println("Search keyword is empty.");
            return;
        }

        try {
            // Define the search URL with the keyword as a query parameter
            String urlString = "http://localhost:8000/post/hashtag?query=" + URLEncoder.encode(keyword, "UTF-8");
            URL url = new URL(urlString);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            // Handle the response
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read and parse the response
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }

                    // Parse JSON response
                    JSONArray jsonArray = new JSONArray(response.toString());
                    searchResults.clear(); // Clear previous results

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Post post = new Post();
                        post.setId(jsonObject.getInt("id"));
                        post.setSender(jsonObject.getString("sender"));
                        post.setText(jsonObject.getString("text"));

                        // Convert JSON arrays to lists
                        JSONArray videoArray = jsonObject.getJSONArray("video");
                        List<String> videoList = new ArrayList<>();
                        for (int j = 0; j < videoArray.length(); j++) {
                            videoList.add(videoArray.getString(j));
                        }
                        post.setVideo(videoList);

                        JSONArray imageArray = jsonObject.getJSONArray("image");
                        List<String> imageList = new ArrayList<>();
                        for (int j = 0; j < imageArray.length(); j++) {
                            imageList.add(imageArray.getString(j));
                        }
                        post.setImage(imageList);

                        searchResults.add(post);
                    }

                    // Use or display searchResults
                    System.out.println("Search Results: " + searchResults);
                    // Update UI or perform other actions with searchResults

                }
            } else {
                System.out.println("Failed to search posts. Response Code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
