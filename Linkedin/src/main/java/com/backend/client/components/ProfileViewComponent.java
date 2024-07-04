package com.backend.client.components;


import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONObject;

public class ProfileViewComponent extends VBox {

    public ProfileViewComponent(JSONObject profileData, String profilePictureUrl, String name, String headerTitle) {
        // Set default profile picture URL if the provided URL is null or empty
        if (profilePictureUrl == null || profilePictureUrl.isEmpty()) {
            profilePictureUrl = "/icons/icons8-male-user-48.png"; // Default image path
        }

        // Initialize and configure the UI components
        ImageView profilePicture = new ImageView(new Image(profilePictureUrl));
        profilePicture.setFitHeight(50);
        profilePicture.setFitWidth(50);
        profilePicture.setPreserveRatio(true);

        Label nameLabel = new Label(name);
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label headerTitleLabel = new Label(headerTitle);
        headerTitleLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: gray;");

        // Create a layout for the profile view
        HBox profileBox = new HBox(10, profilePicture, nameLabel);
        VBox contentBox = new VBox(5, profileBox, headerTitleLabel);

        this.getChildren().add(contentBox);
        this.setStyle("-fx-padding: 10; -fx-background-color: white; -fx-border-color: lightgray; -fx-border-width: 1;");
        this.setPrefWidth(300);

        // Add click event handler to show the profile page
        String finalProfilePictureUrl = profilePictureUrl;
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            showUserProfile(profileData, finalProfilePictureUrl, "img/defaultBackgroundPicture.png", name, "", "test");
        });
    }

    private void showUserProfile(JSONObject info,String profilePictureUrl, String backgroundPictureUrl
            , String name, String additionalName, String lastName) {
        UserProfileComponent userProfileComponent = new UserProfileComponent(info,profilePictureUrl, backgroundPictureUrl, name, additionalName, lastName);

        Stage profileStage = new Stage();
        profileStage.initModality(Modality.APPLICATION_MODAL);
        profileStage.setTitle("Profile");
        profileStage.setScene(new Scene(userProfileComponent));
        profileStage.show();
    }
}
