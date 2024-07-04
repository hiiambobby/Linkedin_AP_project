package com.backend.client.components;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONObject;

public class ProfileViewPv extends ProfileViewComponent{

    public ProfileViewPv(JSONObject profileData, String profilePictureUrl, String name, String headerTitle) {
        super(profileData, profilePictureUrl, name, headerTitle);

        // Add the message button
        ImageView messageButton = new ImageView(new Image("/icons/message-icon.png")); // Replace with your message icon path
        messageButton.setFitHeight(20);
        messageButton.setFitWidth(20);
        messageButton.setPreserveRatio(true);
        messageButton.setStyle("-fx-cursor: hand;");

        // Add click event handler to show the message pop-up page
        messageButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            showMessagePopup(profileData);
        });

        // Add the message button to the profile view layout
        HBox profileBox = (HBox) this.getChildren().get(0); // Assuming profileBox is the first child of contentBox
        profileBox.getChildren().add(messageButton);

        //cancel the event handler that shows the profile:
        String finalProfilePictureUrl = profilePictureUrl;
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            System.out.println("Oops!");
        }); // do nothing
    }

    private void showMessagePopup(JSONObject profileData) {

    }


}
