package com.backend.client.components;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class UserProfileComponent extends VBox {

    private ImageView profilePicture;
    private ImageView backgroundPicture;
    private Label nameLabel;
    private Label additionalNameLabel;
    private Label lastNameLabel;
    private Button connectButton;
    private Button followButton;

    public UserProfileComponent(String profilePictureUrl, String backgroundPictureUrl, String name, String additionalName, String lastName) {
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

        connectButton.setOnAction(event -> handleConnect());
        followButton.setOnAction(event -> handleFollow());
    }

    private void handleFollow() {
        boolean connected = true;
        if (connected) {
            updateFollowButton();
        }
    }

    private void updateFollowButton() {
        followButton.setText("Following");
        followButton.setDisable(true);
        followButton.setStyle("-fx-background-color: lightgreen;");
    }

    private void handleConnect() {
        boolean connected = true;
        if (connected) {
            updateConnectButton();
        }
    }

    // Add event handlers for the buttons if needed
    public void setOnConnectAction(EventHandler<ActionEvent> event) {
        connectButton.setOnAction(event);
    }

    public void setOnFollowAction(EventHandler<ActionEvent> event) {
        followButton.setOnAction(event);
    }
    public void updateConnectButton() {
        connectButton.setText("Connected");
        connectButton.setDisable(true);
        connectButton.setStyle("-fx-background-color: lightgreen;");
    }
}
