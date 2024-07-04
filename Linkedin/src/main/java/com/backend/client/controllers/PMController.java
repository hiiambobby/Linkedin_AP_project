package com.backend.client.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class PMController {
    @FXML
    private ImageView imageImageView;
    @FXML
    private ImageView attachmentImageView;
    //    @FXML
//    private ImageView imageImageView;
//
//    @FXML
//    private ImageView attachmentImageView;
    @FXML
    private TextArea messageTextArea;

    @FXML
    private Button cancelBtn;

    @FXML
    private Button sendBtn;

    private Stage dialogStage;

    @FXML
    private void cancel() {
        dialogStage.close();
    }

    @FXML
    private void send() {
        // Handle sending the message
        String message = messageTextArea.getText();
        System.out.println("Message Sent: " + message);
        dialogStage.close();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setMessage(String message) {
        messageTextArea.setText(message);
    }

    @FXML
    private void handleImage() {
        // Open file chooser for images
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");

        // Add file filters for images
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );

        // Show open file dialog
        File file = fileChooser.showOpenDialog(dialogStage);
        if (file != null) {
            // Handle the selected image file
            System.out.println("Selected image file: " + file.getAbsolutePath());
            // Add code here to display or process the image
        } else {
            System.out.println("No image file selected");
        }
    }

    @FXML
    private void handleFile() {
        // Open file chooser for files (videos, documents, etc.)
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");

        // Add file filters for videos and other files
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.mkv", "*.avi", "*.mov"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        // Show open file dialog
        File file = fileChooser.showOpenDialog(dialogStage);
        if (file != null) {
            // Handle the selected file
            System.out.println("Selected file: " + file.getAbsolutePath());
            // Add code here to handle or process the file
        } else {
            System.out.println("No file selected");
        }
    }
}