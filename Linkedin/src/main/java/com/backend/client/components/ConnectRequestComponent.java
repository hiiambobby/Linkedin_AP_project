package com.backend.client.components;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
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

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConnectRequestComponent extends VBox {

    private ImageView profilePicture;
    private ImageView backgroundPicture;
    private Label nameLabel;
    private Label headerLabel;
    private Button acceptButton;
    private Button ignoreButton;
    private Label noteLabel;

    public ConnectRequestComponent(String profilePictureUrl, String name, String header, String note,String email) {
        // Set default profile picture URL if the provided URL is null or empty
        if (profilePictureUrl == null || profilePictureUrl.isEmpty()) {
            profilePictureUrl = "/icons/icons8-male-user-48.png"; // Default image path
        }

        // Initialize and configure the UI components
        profilePicture = new ImageView(new Image(profilePictureUrl));
        profilePicture.setFitHeight(50);
        profilePicture.setFitWidth(50);
        profilePicture.setPreserveRatio(true);

        nameLabel = new Label(name);
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        headerLabel = new Label(header);
        headerLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: gray;");

        acceptButton = new Button("Accept");
        ignoreButton = new Button("Ignore");

        // Set button styles
        acceptButton.setStyle("-fx-background-color: lightgreen;");
        ignoreButton.setStyle("-fx-background-color: lightcoral;");

        // Note label
        noteLabel = new Label("Note: " + note);
        noteLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: gray;");

        // Create a layout for the user info
        VBox userInfoBox = new VBox(5, nameLabel, headerLabel, noteLabel);
        userInfoBox.setPadding(new Insets(5, 10, 5, 10));

        // Create a layout for the buttons
        HBox buttonBox = new HBox(10, acceptButton, ignoreButton);
        buttonBox.setPadding(new Insets(5, 10, 5, 10));

        // Create a layout for the connect request
        HBox requestBox = new HBox(10, profilePicture, userInfoBox, buttonBox);
        requestBox.setStyle("-fx-padding: 10; -fx-background-color: white; -fx-border-color: lightgray; -fx-border-width: 1;");
        requestBox.setPrefWidth(400);

        this.getChildren().addAll(requestBox);
        acceptButton.setOnAction(event -> {
            try {
                accept(email,readEmail(),true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        ignoreButton.setOnAction(event -> {
            try {
                ignore(email,readEmail());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    //do the delete request
    public void ignore(String sender,String receiver) throws IOException {
        String encodedSender = URLEncoder.encode(sender, StandardCharsets.UTF_8.toString());
        String encodedReceiver = URLEncoder.encode(receiver, StandardCharsets.UTF_8.toString());
        String urlString = "http://localhost:8000/connection?sender=" + encodedSender + "&receiver=" + encodedReceiver;

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            System.out.println("Connection request deleted successfully.");
        } else {
            System.out.println("Failed to delete request. Response Code: " + responseCode);
        }

        connection.disconnect();
    }



    //do the update request
    public void accept(String sender,String receiver,boolean answer) throws IOException {
        sendPutRequest(sender,receiver,true);

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

    // Methods to set actions for accept and ignore buttons
    public void setOnAcceptAction(EventHandler<ActionEvent> event) {
        acceptButton.setOnAction(event);
    }

    public void setOnIgnoreAction(EventHandler<ActionEvent> event) {
        ignoreButton.setOnAction(event);
    }

    public static void sendPutRequest(String sender, String receiver, boolean accepted) throws IOException {
        // Define the URL for the PUT request
        String urlString = "http://localhost:8000/connection";
        URL url = new URL(urlString);

        // Open the connection
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setDoOutput(true);

        // Create JSON body using org.json.JSONObject
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sender", sender);
        jsonObject.put("receiver", receiver);
        jsonObject.put("accepted", accepted);

        String jsonInputString = jsonObject.toString();

        // Write JSON body to the output stream
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("UTF-8");
            os.write(input, 0, input.length);
        }

        // Handle the response
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            System.out.println("Connection status updated successfully.");
        } else {
            System.out.println("Failed to update connection status. Response Code: " + responseCode);
        }

        // Disconnect the connection
        connection.disconnect();
    }
}