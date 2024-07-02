package com.backend.client.controllers;


import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ProfileViewComponent extends VBox {

    private ImageView profilePicture;
    private Label nameLabel;
    private Label headerTitleLabel;

    public ProfileViewComponent(String profilePictureUrl, String name, String headerTitle) {
        // Initialize and configure the UI components
        profilePicture = new ImageView(new Image(profilePictureUrl));
        profilePicture.setFitHeight(50);
        profilePicture.setFitWidth(50);
        profilePicture.setPreserveRatio(true);

        nameLabel = new Label(name);
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        headerTitleLabel = new Label(headerTitle);
        headerTitleLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: gray;");

        // Create a layout for the profile view
        HBox profileBox = new HBox(10, profilePicture, nameLabel);
        VBox contentBox = new VBox(5, profileBox, headerTitleLabel);

        this.getChildren().add(contentBox);
        this.setStyle("-fx-padding: 10; -fx-background-color: white; -fx-border-color: lightgray; -fx-border-width: 1;");
        this.setPrefWidth(300);
    }

}
