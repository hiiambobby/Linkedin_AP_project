package com.backend.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class searchBar implements Initializable {
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox profileList;
    @FXML
    private ImageView profileView;
    @FXML
    private TextField searchField; // For user input

    @FXML
    private Button searchButton; // To trigger search


    public void profileView(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Profile.fxml"));
        Stage currentStage = (Stage) profileView.getScene().getWindow();
        currentStage.close();
        Stage newStage = new Stage();
        Image icon = new Image("/img/photo_2024-05-15_16-05-20.jpg");
        newStage.getIcons().add(icon);
        Scene newScene = new Scene(root);
        newStage.setScene(newScene);
        newStage.setTitle("search");
        newStage.initStyle(StageStyle.DECORATED);
        newStage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize the ScrollPane and VBox
        setupScrollableProfileList();
        addSampleProfiles(); // Add sample profiles for demonstration
    }

    private void setupScrollableProfileList() {
        // Initialize the VBox if needed (should already be defined in FXML)
        if (profileList == null) {
            profileList = new VBox();
            profileList.setSpacing(10); // Space between items
        }

        // Set the VBox to the ScrollPane
        scrollPane.setContent(profileList);
    }

    private void addSampleProfiles() {
        // Example data to add to the VBox
        for (int i = 1; i <= 5; i++) {
            ProfileViewComponent profileComponent = new ProfileViewComponent(
                    "/icons/icons8-male-user-48.png", // Profile picture path
                    "User " + i,                    // User name
                    "Header Title " + i            // Header title
            );
            profileList.getChildren().add(profileComponent);
        }
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        // Handle search logic here
    }
}
