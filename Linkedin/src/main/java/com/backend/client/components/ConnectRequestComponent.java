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

public class ConnectRequestComponent extends VBox {

    private ImageView profilePicture;
    private ImageView backgroundPicture;
    private Label nameLabel;
    private Label headerLabel;
    private Button acceptButton;
    private Button ignoreButton;
    private Label noteLabel;

    public ConnectRequestComponent(String profilePictureUrl, String name, String header, String note) {
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
    }

    // Methods to set actions for accept and ignore buttons
    public void setOnAcceptAction(EventHandler<ActionEvent> event) {
        acceptButton.setOnAction(event);
    }

    public void setOnIgnoreAction(EventHandler<ActionEvent> event) {
        ignoreButton.setOnAction(event);
    }
}