package com.backend.client.components;

import com.backend.client.controllers.TokenManager;
import com.backend.client.controllers.setAlert;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
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
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;


public class UserProfileComponent extends VBox {

    private ImageView profilePicture;
    private ImageView backgroundPicture;
    private Label nameLabel;
    private Label additionalNameLabel;
    private Label lastNameLabel;
    private Button connectButton;
    private Button followButton;
  //  private Label noteLabel;

    public UserProfileComponent(JSONObject info,String profilePictureUrl, String backgroundPictureUrl, String name, String additionalName, String lastName) {
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

        connectButton = new Button("Connect");
        followButton = new Button("Follow");

        // Create a layout for the user info
        VBox userInfoBox = new VBox(5, nameLabel, additionalName != null && !additionalName.isEmpty() ? additionalNameLabel : new Label(), lastNameLabel);

        HBox buttonBox = new HBox(10, connectButton, followButton);

        // Create a layout for the profile view
        VBox profileBox = new VBox(10, profilePicture, userInfoBox, buttonBox);

        this.getChildren().addAll(backgroundPicture, profileBox);
        this.setStyle("-fx-padding: 10; -fx-background-color: white; -fx-border-color: lightgray; -fx-border-width: 1;");
        this.setPrefWidth(400);

        connectButton.setOnAction(event -> handleConnect(name,info));
        followButton.setOnAction(event -> handleFollow());
    }

    private void handleFollow() {
        boolean connected = true;
        if (connected) {
            updateFollowButton();
        }
    }

    public String findEmail(JSONObject profileData)
    {
        return profileData.optString("userId", "");
    }

    private void updateFollowButton() {
        followButton.setText("Following");
        followButton.setDisable(true);
        followButton.setStyle("-fx-background-color: lightgreen;");
    }

    //send connect request
    private void handleConnect(String name,JSONObject profileJSON) {
        boolean connected = true;
        sendConnectReq(profileJSON);
        if (connected) {
            updateConnectButton();
        }
    }

    //sending
    private boolean sendConnectReq(JSONObject profileJSON) {

        JSONObject jsonObject = getJsonObject(profileJSON);
        HttpURLConnection conn = null;
        try {
            URL url = new URL("http://localhost:8000/connect");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");

            // Retrieve the token from TokenManager
            String tokenLong = TokenManager.getToken();
            String token = TokenManager.extractTokenFromResponse(tokenLong);

            if (token != null && !token.isEmpty()) {
                conn.setRequestProperty("Authorization", "Bearer " + token);
            } else {
                System.out.println("No token available.");
            }
            conn.setDoOutput(true);

            assert jsonObject != null;
            String json = jsonObject.toString();
            System.out.println(jsonObject);
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = json.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("request sent successfully");
                return true;
            } else {
                InputStream errorStream = conn.getErrorStream();
                if (errorStream != null) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(errorStream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                }
                setAlert.showAlert(Alert.AlertType.ERROR, "Error", "Failed to connect. Response code: " + responseCode);
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            setAlert.showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while saving contact info.");
        }
        return false;
    }

    private JSONObject getJsonObject(JSONObject profileJSON) {
    String note = openNotePopup(nameLabel.getText());
        JSONObject jsonObject = new JSONObject();
        String email = findEmail(profileJSON);
        try {
            jsonObject.put("receiver",email);
            jsonObject.put("notes", note);
            jsonObject.put("accepted",false); // Corrected to match expected retrieval type

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    // Add event handlers for the buttons if needed
    public void setOnConnectAction(EventHandler<ActionEvent> event) {
        connectButton.setOnAction(event);
    }

    public void setOnFollowAction(EventHandler<ActionEvent> event) {
        followButton.setOnAction(event);
    }

    public void updateConnectButton() {
        connectButton.setText("Pending");
        connectButton.setDisable(true);
        connectButton.setStyle("-fx-background-color: gray;");
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
}
