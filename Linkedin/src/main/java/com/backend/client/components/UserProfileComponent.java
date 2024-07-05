package com.backend.client.components;

import com.backend.client.controllers.TokenManager;
import com.backend.client.controllers.UserEmail;
import com.backend.client.controllers.infoControl;
import com.backend.client.controllers.setAlert;
import com.backend.server.Model.Follow;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UserProfileComponent extends VBox {

    private ImageView profilePicture;
    private ImageView backgroundPicture;
    private Label nameLabel;
    private Label additionalNameLabel;
    private Label lastNameLabel;
    private Button connectButton;
    private Button followButton;
    private Button contactInfoButton;
  //  private Label noteLabel;
  private JSONObject info;
    private static String profileEmail;

    public UserProfileComponent(JSONObject info,String profilePictureUrl, String backgroundPictureUrl, String name, String additionalName, String lastName) throws IOException {

        this.info = info;
        this.profileEmail = info.optString("userId", "");
        System.out.println(profileEmail);

        // Set default profile picture URL if the provided URL is null or empty
        if (profilePictureUrl == null || profilePictureUrl.isEmpty()) {
            profilePictureUrl = "/icons/icons8-male-user-48.png"; // Default image path
        }

        // Set default background picture URL if the provided URL is null or empty
        if (backgroundPictureUrl == null || backgroundPictureUrl.isEmpty()) {
            backgroundPictureUrl = "img/blankHeader.png"; // Default image path
        }

        // Initialize and configure the UI components
        profilePicture = new ImageView(new Image(profilePictureUrl));
        profilePicture.setFitHeight(100);
        profilePicture.setFitWidth(100);
        profilePicture.setPreserveRatio(true);

        backgroundPicture = new ImageView(new Image(backgroundPictureUrl));
        backgroundPicture.setFitHeight(200);
        backgroundPicture.setFitWidth(400);
        backgroundPicture.setPreserveRatio(true);

        nameLabel = new Label(name);
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");

        additionalNameLabel = new Label(additionalName);
        additionalNameLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: gray;");

        lastNameLabel = new Label(lastName);
        lastNameLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: gray;");
        if(infoControl.isUserConnected(profileEmail))
        {
            connectButton = new Button("Connected");
            connectButton.setDisable(true);

        }
        else if(isUserInConnectRequests(UserEmail.readEmail()))
        {
            connectButton = new Button("Pending");
            updateConnectButton();
            connectButton.setDisable(true);
        }
        else {
            connectButton = new Button("Connect");
        }
        followButton = new Button("Follow");


        // Create the "Contact Info" button
        contactInfoButton = new Button("Contact Info");
        contactInfoButton.setStyle("-fx-font-size: 12px; -fx-background-color: #007bff; -fx-text-fill: cyan;");
        contactInfoButton.setOnAction(e -> {
            try {
                showInfoPopup();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        // Create a layout for the user info
        VBox userInfoBox = new VBox(5, nameLabel, additionalName != null && !additionalName.isEmpty() ? additionalNameLabel : new Label(), lastNameLabel);

        HBox contactInfo = new HBox(10,contactInfoButton);
        HBox buttonBox = new HBox(10, connectButton, followButton,contactInfo);

        // Create a layout for the profile view
        VBox profileBox = new VBox(10, profilePicture, userInfoBox, buttonBox);

        this.getChildren().addAll(backgroundPicture, profileBox);
        this.setStyle("-fx-padding: 10; -fx-background-color: white; -fx-border-color: lightgray; -fx-border-width: 1;");
        this.setPrefWidth(400);

        connectButton.setOnAction(event -> {
            try {
                handleConnect(name,info);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        followButton.setOnAction(event -> handleFollow(info));


        //change the button if following
        updateFollowButton();
    }

    private void handleFollow(JSONObject info) {
        updateFollowButton();
         follow(info);
    }



    private void follow(JSONObject info) {
        HttpURLConnection connection = null;
        try {
            // Create a Follow object from the provided JSON info
            Follow follow = new Follow(UserEmail.readEmail(), profileEmail);
            System.out.println("Following: " + profileEmail);

            // Convert the Follow object to a JSON string
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonInputString = objectMapper.writeValueAsString(follow);

            // Define the URL for the POST request
            URL url = new URL("http://localhost:8000/follow"); // Replace with your actual URL

            // Open the connection
            connection = (HttpURLConnection) url.openConnection();

            // Set the request method to POST
            connection.setRequestMethod("POST");

            // Set request headers
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true); // Enable input/output streams

            // Write the JSON data to the output stream
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Get the response code
            int responseCode = connection.getResponseCode();

            // Check if the response code is HTTP_OK (200)
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Optional: Read the response if needed
                followButton.setText("Following");
                followButton.setStyle("-fx-background-color: grey;"); // Example styling
                followButton.setDisable(true);
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println("Response: " + response.toString());
                }
            } else {
                // Print error message
                System.err.println("POST request failed. Response Code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Print exception details
        } finally {
            if (connection != null) {
                connection.disconnect(); // Ensure the connection is disconnected
            }
        }
    }

    public void setOnConnectAction(EventHandler<ActionEvent> event) {
        connectButton.setOnAction(event);
    }

    public void setOnFollowAction(EventHandler<ActionEvent> event) {
        followButton.setOnAction(event);
    }


    //send connect request
    private void handleConnect(String name,JSONObject profileJSON) throws IOException {
        boolean connected = sendConnectReq(profileJSON);
        if (connected) {
            updateConnectButton();
        }
    }

    //sending
    private boolean sendConnectReq(JSONObject profileJSON) {
        HttpURLConnection conn = null;
        try {
            // Setup URL and connection
            URL url = new URL("http://localhost:8000/connect");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            // Retrieve and set the token
            String token = TokenManager.extractTokenFromResponse(TokenManager.getToken());
            if (token != null && !token.isEmpty()) {
                conn.setRequestProperty("Authorization", "Bearer " + token);
            } else {
                System.err.println("No token available, authorization header not set.");
            }

            // Write JSON data to the output stream
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = profileJSON.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input);
            }

            // Check response code
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Request sent successfully.");
                return true;
            } else {
                handleErrorResponse(conn);
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            setAlert.showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while sending the connect request.");
            return false;
        } finally {
            // Ensure the connection is always closed
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private void handleErrorResponse(HttpURLConnection conn) throws IOException {
        try (InputStream errorStream = conn.getErrorStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(errorStream))) {
            String line;
            StringBuilder errorResponse = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                errorResponse.append(line);
            }
            System.err.println("Error response: " + errorResponse.toString());
            setAlert.showAlert(Alert.AlertType.ERROR, "Error", "Failed to connect. Response code: " + conn.getResponseCode());
        }
    }

    private JSONObject getJsonObject(JSONObject profileJSON) {
    String note = openNotePopup(nameLabel.getText());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("receiver",profileEmail);
            jsonObject.put("notes", note);
            jsonObject.put("accepted",false); // Corrected to match expected retrieval type

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    private String openNotePopup(String fullName) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Add a Note to " + fullName);

        Label noteLabel = new Label("Connection Note:");
        TextArea noteTextArea = new TextArea();
        noteTextArea.setWrapText(true);
        AtomicReference<String> note = new AtomicReference<>("");
        Button sendButton = new Button("Send");
        sendButton.setOnAction(event -> {
            note.set(noteTextArea.getText());
            // Handle the logic to send the connection request with the note
            System.out.println("Sending connection request to " + fullName + " with note: " + note);
            // Update the note label with the entered note
            popupStage.close();

        });

        VBox layout = new VBox(10, noteLabel, noteTextArea, sendButton);
        layout.setPadding(new Insets(10));
        Scene scene = new Scene(layout, 300, 200);

        popupStage.setScene(scene);
        popupStage.showAndWait();
        return note.get();
    }

    public void updateConnectButton() throws IOException {

        connectButton.setText("Pending");
        connectButton.setDisable(true);
        connectButton.setStyle("-fx-background-color: gray;");
    }


    private void updateFollowButton() {
        boolean following = isFollowing(UserEmail.readEmail(),"followings");
        if (following) {
            followButton.setText("Following");
            followButton.setStyle("-fx-background-color: grey;"); // Example styling
            followButton.setDisable(true); // Optionally disable the button if following
        } else {
            followButton.setText("Follow");
            followButton.setStyle("-fx-background-color: lightgreen;"); // Example styling
            followButton.setDisable(false); // Enable the button if not following
        }
    }

    private boolean isFollowing(String userId,String type) {
        try {
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
                    JSONArray jsonArray = new JSONArray(response.toString());

                    // Check if the profileId is in the list based on the type
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if ("followers".equalsIgnoreCase(type)) {
                            if (jsonObject.getString("follower").equals(profileEmail)) {
                                return true;
                            }
                        } else if ("followings".equalsIgnoreCase(type)) {
                            if (jsonObject.getString("following").equals(profileEmail)) {
                                return true;
                            }
                        }
                    }
                }
            } else {
                System.err.println("GET request failed. Response Code: " + responseCode);
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



    public void showInfoPopup() throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/info.fxml")); // Ensure this path is correct
            Parent root = loader.load();

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Contact Info");
            popupStage.setScene(new Scene(root));
            popupStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static JSONObject getContactInfo() throws IOException {
        String urlString = "http://localhost:8000/contactInfo?id=" + URLEncoder.encode(profileEmail, StandardCharsets.UTF_8.toString());
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        int responseCode = conn.getResponseCode();
        StringBuilder response = new StringBuilder();

        if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
        } else {
            System.err.println("Failed to retrieve Contact Info, Response code: " + responseCode);
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                response.append(errorLine);
            }
            errorReader.close();
            System.err.println("Error response: " + response.toString());
        }

        conn.disconnect();
        String responseStr = response.toString().trim();
        System.out.println("Raw response: " + responseStr); // Debugging line

        // Unescape the JSON string
        String unescapedJson = responseStr.substring(1, responseStr.length() - 1).replace("\\\"", "\"");
        System.out.println("Unescaped JSON: " + unescapedJson); // Debugging line

        try {
            return new JSONObject(unescapedJson);
        } catch (org.json.JSONException e) {
            System.err.println("Error parsing JSON: " + e.getMessage());
            System.err.println("Response content: " + unescapedJson);
            throw e;
        }
    }


    //to check pending reqs
    private static String getConnectRequests() throws IOException {
        System.out.println("Retrieving connect requests...");
        URL url = new URL("http://localhost:8000/connect?user=" + URLEncoder.encode(profileEmail, StandardCharsets.UTF_8.toString()));
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

            // Print the raw response for debugging
            System.out.println("Raw Response: " + response.toString());

            // Return the raw response as a string
            return response.toString();
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
        return "";
    }

    // Method to check if a user is in connect requests
    public static boolean isUserInConnectRequests(String userId) throws IOException {
        // Retrieve the connect requests
        String connectRequests = getConnectRequests();

        // Check if connectRequests is not empty
        if (connectRequests.isEmpty()) {
            return false;
        }

        // Parse the JSON response
        try {
            JSONArray jsonArray = new JSONArray(connectRequests);

            // Iterate through the JSON array and check for the userId in the sender field
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String sender = jsonObject.optString("sender", null);

                if (userId.equals(sender)) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing JSON response: " + e.getMessage());
        }

        return false;
    }
}