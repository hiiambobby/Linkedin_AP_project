package com.backend.client.components;

import com.backend.client.controllers.PMController;
import com.backend.client.controllers.setAlert;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONObject;

public class ProfileViewPv extends VBox{
    private static String profileEmail;

    public ProfileViewPv(String profileEmail, String profilePictureUrl, String name, String headerTitle) {
        this.profileEmail = profileEmail;
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
            showMessagePopup();
        });
    }

    private void showMessagePopup() {
        //show the message box
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Messages.fxml"));
            Parent root = loader.load();

            // Get the controller and set the message
            PMController controller = loader.getController();


            // Create and configure the stage
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setTitle("Message");
            dialogStage.setScene(new Scene(root));

            // Pass the stage to the controller
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();

            setAlert.showAlert(Alert.AlertType.ERROR,"Error","An error occured....");
        }
    }

    public static String getProfileEmail() {
        return profileEmail;
    }
}
