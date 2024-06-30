package com.backend.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;

public class ProfileController {

    @FXML
    private Button contactInfoBtn;
    @FXML
    private Button primaryInfoBtn;
    @FXML
    private Button logOut;


    public void ContactInfo(ActionEvent event) throws IOException {
        // Load the FXML for the Contact Info scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ContactInfo.fxml"));
        Parent root = loader.load();

        // Create a new Stage for the popup window
        Stage popupStage = new Stage();

        // Set the scene for the popup stage
        popupStage.setScene(new Scene(root));

        // Set an icon for the popup window (optional)
        Image icon = new Image("/img/photo_2024-05-15_16-05-20.jpg");
        popupStage.getIcons().add(icon);

        // Set the title for the popup window (optional)
        popupStage.setTitle("Contact Info");

        // Set the modality to make sure the popup blocks interaction with the main window
        popupStage.initModality(Modality.APPLICATION_MODAL);

        // Show the popup window
        popupStage.show();

    }
    public void PrimaryInfo(ActionEvent event) throws IOException {
        // Load the FXML for the Contact Info scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PrimaryInfo.fxml"));
        Parent root = loader.load();
        Stage popupStage = new Stage();
        popupStage.setScene(new Scene(root));
        popupStage.setTitle("Primary Info");
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.show();

    }
    public void logOut(ActionEvent event) throws IOException {
        TokenManager.clearToken();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Main.fxml")); // Adjust path as needed
        Stage currentStage = (Stage) logOut.getScene().getWindow();
        currentStage.close(); // Close the current stage if needed

        // Create a new stage with the decorated style
        Stage newStage = new Stage();
        Image icon = new Image("/img/photo_2024-05-15_16-05-20.jpg");
        newStage.getIcons().add(icon);
        Scene newScene = new Scene(root);
        newScene.setFill(Color.TRANSPARENT);
        newStage.setScene(newScene);
        newStage.setTitle("User Profile");
        newStage.initStyle(StageStyle.TRANSPARENT); // Standard window decorations for the new stage
        newStage.show();
    }


}
