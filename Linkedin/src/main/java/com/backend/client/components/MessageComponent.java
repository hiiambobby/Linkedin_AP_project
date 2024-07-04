package com.backend.client.components;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
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
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;


public class MessageComponent extends VBox{


    public MessageComponent(String profilePictureUrl, String userName, String videoUrl, String textMessage, String fileUrl, String imageUrl) {
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
        setEffect(new DropShadow(10, Color.GRAY));

        // User profile section
        HBox profileSection = new HBox(10);
        profileSection.setAlignment(Pos.CENTER_LEFT);

        // Add profile picture if URL is provided
        if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
            Image profileImage = new Image(convertToFileUrl(profilePictureUrl));
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
            Media media = createMediaFromUrl(videoUrl);
            if (media != null) {
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                MediaView mediaView = new MediaView(mediaPlayer);
                mediaView.setFitWidth(400);
                mediaView.setFitHeight(300);
                mediaView.setStyle("-fx-background-color: #000000; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 10, 0, 0, 0);");
                getChildren().add(mediaView);
                mediaPlayer.play(); // Play video automatically
            }
        }

        // Add text message if provided
        if (textMessage != null && !textMessage.isEmpty()) {
            Label messageLabel = new Label(textMessage);
            messageLabel.setWrapText(true);
            messageLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333; -fx-padding: 10px; -fx-background-color: #f9f9f9; -fx-border-color: #e0e0e0; -fx-border-width: 1px; -fx-background-radius: 5px;");
            getChildren().add(messageLabel);
        }

        // Add image if URL is provided
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Image image = new Image(convertToFileUrl(imageUrl));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(400); // Adjust size as needed
            imageView.setFitHeight(300); // Adjust size as needed
            imageView.setPreserveRatio(true);
            imageView.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #cccccc; -fx-border-width: 1px;");
            getChildren().add(imageView);
        }

        // Add hyperlink if file URL is provided
        if (fileUrl != null && !fileUrl.isEmpty()) {
            Hyperlink fileLink = new Hyperlink("Open File");
            fileLink.setStyle("-fx-text-fill: #0066cc; -fx-font-size: 12px;");
            fileLink.setOnAction(event -> openFile(fileUrl));
            getChildren().add(fileLink);
        }
   
    }

    private Media createMediaFromUrl(String url) {
        try {
            String decodedUrl = URLDecoder.decode(url, StandardCharsets.UTF_8.toString());
            String standardizedUrl = standardizePath(decodedUrl);
            File file = new File(standardizedUrl);
            if (file.exists()) {
                return new Media(file.toURI().toString());
            } else {
                System.err.println("Media file does not exist: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void openFile(String fileUrl) {
        try {
            String decodedUrl = URLDecoder.decode(fileUrl, StandardCharsets.UTF_8.toString());
            String standardizedUrl = convertToFileUrl(decodedUrl);
            File file = new File(standardizedUrl);
            if (file.exists()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(file);
                } else {
                    System.out.println("Desktop is not supported");
                }
            } else {
                System.out.println("File does not exist: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String convertToFileUrl(String path) {
        try {
            // Normalize and convert the path to a file URL
            File file = new File(Paths.get(path).toAbsolutePath().normalize().toString());
            return file.toURI().toURL().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }
    private String standardizePath(String path) {
        // Normalize the path to handle different file separators and ensure it's a valid URI
        try {
            return Paths.get(path).toAbsolutePath().normalize().toString().replace("\\", "/");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }
}