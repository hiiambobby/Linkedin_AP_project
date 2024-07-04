package com.backend.client.components;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import java.net.URI;
import java.net.URISyntaxException;


public class MessageComponent extends VBox{

    public MessageComponent(String profilePictureUrl, String userName, String videoUrl, String textMessage, String fileUrl) {
        // Set padding, spacing, and background
        setPadding(new Insets(10));
        setSpacing(10);
        setBackground(new Background(new BackgroundFill(Color.web("#ffffff"), new CornerRadii(10), Insets.EMPTY)));
        setBorder(new Border(new BorderStroke(
                Color.web("#dddddd"),
                BorderStrokeStyle.SOLID,
                new CornerRadii(10),
                new BorderWidths(1)
        )));

        // Optionally, add shadow effect
        setEffect(new javafx.scene.effect.DropShadow(10, Color.GRAY));

        // User profile section
        HBox profileSection = new HBox(10);
        profileSection.setAlignment(Pos.CENTER_LEFT);

        // Add profile picture if URL is provided
        if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
            Image profileImage = new Image(profilePictureUrl, 50, 50, true, true); // Adjust size as needed
            ImageView profileImageView = new ImageView(profileImage);
            profileImageView.setFitWidth(50);
            profileImageView.setFitHeight(50);
            profileImageView.setStyle("-fx-border-radius: 50%; -fx-border-color: #cccccc; -fx-border-width: 1px;");
            profileSection.getChildren().add(profileImageView);
        }

        // Add user name
        if (userName != null && !userName.isEmpty()) {
            Label userNameLabel = new Label(userName);
            userNameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #333333;");
            profileSection.getChildren().add(userNameLabel);
        }

        // Add profile section to the main VBox
        getChildren().add(profileSection);

        // Add video if URL is provided
        if (videoUrl != null && !videoUrl.isEmpty()) {
            Media media = new Media(videoUrl);
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            MediaView mediaView = new MediaView(mediaPlayer);
            mediaView.setFitWidth(400);
            mediaView.setFitHeight(300);
            mediaView.setStyle("-fx-background-color: #000000; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 10, 0, 0, 0);");
            getChildren().add(mediaView);
        }

        // Add text message if provided
        if (textMessage != null && !textMessage.isEmpty()) {
            Label messageLabel = new Label(textMessage);
            messageLabel.setWrapText(true);
            messageLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333; -fx-padding: 10px; -fx-background-color: #f9f9f9; -fx-border-color: #e0e0e0; -fx-border-width: 1px; -fx-background-radius: 5px;");
            getChildren().add(messageLabel);
        }

        // Add hyperlink if file URL is provided
        if (fileUrl != null && !fileUrl.isEmpty()) {
            Hyperlink fileLink = new Hyperlink("Open File");
            fileLink.setStyle("-fx-text-fill: #0066cc; -fx-font-size: 12px;");
            fileLink.setOnAction(event -> openFile(fileUrl));
            getChildren().add(fileLink);
        }
    }

    private void openFile(String fileUrl) {
        try {
            URI fileUri = new URI(fileUrl);
            File file = new File(fileUri);
            if (file.exists()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(file);
                } else {
                    System.out.println("Desktop is not supported");
                }
            } else {
                System.out.println("File does not exist: " + file.getAbsolutePath());
            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }
}