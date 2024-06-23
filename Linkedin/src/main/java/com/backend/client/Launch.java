package com.backend.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import com.backend.client.controllers.SignInController;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

//
//public class Launch extends Application {
//
//    @Override
//    public void start(Stage stage) throws Exception {
//        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Main.fxml")));
//        Scene scene = new Scene(root);
//        scene.setFill(Color.TRANSPARENT);
//        stage.setScene(scene);
//        stage.initStyle(StageStyle.TRANSPARENT);
//        stage.show();
//    }

    public class Launch extends Application {

        @Override
        public void start(Stage primaryStage) throws Exception {
            FXMLLoader loader = new FXMLLoader();
            final String address = "/com/backend/client/Main.fxml";
            final InputStream fxmlStream = getClass().getResourceAsStream(address);

            if (fxmlStream == null) {
                throw new FileNotFoundException("FXML file not found: " + address);
            }

            final Parent root = loader.load(fxmlStream);
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        }

    public static void main(String[] args) {
        launch(args);
    }

}