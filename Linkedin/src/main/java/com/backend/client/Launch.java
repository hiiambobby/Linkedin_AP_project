package com.backend.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


    public class Launch extends Application {

        @Override
        public void start(Stage stage) throws Exception {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Main.fxml"));
            stage.setTitle("Mewedin");
            Image icon = new Image("/img/photo_2024-05-15_16-05-20.jpg");
            stage.getIcons().add(icon);
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.show();
            }

    public static void main(String[] args) {
        launch(args);
    }

}