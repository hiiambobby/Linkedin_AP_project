package com.backend.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SplashScreen extends Application {
    private Stage splashStage;

    @Override
    public void start(Stage primaryStage) {
        splashStage = primaryStage;

        Image splashImage = new Image(getClass().getResourceAsStream("/img/photo_2024-05-15_16-05-20.jpg")); // Replace with your image path
        ImageView imageView = new ImageView(splashImage);
        StackPane splashRoot = new StackPane(imageView);

        Scene splashScene = new Scene(splashRoot, 640, 640); // Adjust the size as needed
        splashStage.initStyle(StageStyle.UNDECORATED);
        splashStage.setScene(splashScene);
        splashStage.show();

        new Thread(() -> {
            try {
                Thread.sleep(1500); // Show splash screen for 1.5 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Platform.runLater(() -> showMainApplication());
        }).start();
    }
    private void showMainApplication() {
        try {
            // Hide the splash screen
            splashStage.hide();

            // Initialize and show the main application
            Stage mainStage = new Stage();
            Launch mainApp = new Launch();
            mainApp.start(mainStage);

            // Close the splash screen window when the main application is closed
            mainStage.setOnCloseRequest(event -> splashStage.close());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}