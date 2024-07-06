package com.backend.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;


import java.io.IOException;

public class TimeLineController {

    @FXML
    private ImageView pmId;
    @FXML
    private ImageView homeId;

    @FXML
    private ImageView networkId;

    @FXML
    private ImageView profileId;

    @FXML
    private VBox postsVbox;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchField;

    @FXML
    private ImageView searchId;


    public void handleSearch(ActionEvent event) {
    }

    /*public void addPostFeed() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Post.fxml"));
            VBox postComponent = loader.load();
            // Access the controller of PostComponent to set data
            PostController controller = loader.getController();
            controller.setPostDetails();
            // Add the component to the VBox
            postsVbox.getChildren().add(postComponent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public void profileView(MouseEvent mouseEvent) throws IOException {
        openNewStage("/fxml/Profile.fxml", "Profile");
    }

    public void openMessage(MouseEvent mouseEvent) throws IOException {
        openNewStage("/fxml/Network.fxml", "My Network");
    }

    public void openSearch(MouseEvent mouseEvent) throws IOException {
        openNewStage("/fxml/Search.fxml", "Search");
    }

    private void openNewStage(String fxmlPath, String title) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Stage currentStage = (Stage) pmId.getScene().getWindow();
        currentStage.close();
        Stage newStage = new Stage();
        Image icon = new Image("/img/photo_2024-05-15_16-05-20.jpg");
        newStage.getIcons().add(icon);
        Scene newScene = new Scene(root);
        newStage.setScene(newScene);
        newStage.setTitle(title);
        newStage.initStyle(StageStyle.DECORATED);
        newStage.show();
    }

    public void openNetwork(MouseEvent mouseEvent) throws IOException {
        openNewStage("/fxml/Network.fxml", "My Network");

    }
}
