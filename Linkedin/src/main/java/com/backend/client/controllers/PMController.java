package com.backend.client.controllers;


import com.backend.client.components.ProfileViewPv;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PMController {
    @FXML
    private ImageView imageImageView;
    @FXML
    private ImageView attachmentImageView;

    @FXML
    private TextArea messageTextArea;

    @FXML
    private Button cancelBtn;

    @FXML
    private Button sendBtn;

    private Stage dialogStage;

    private String imageUrl;
    private String fileUrl;
    private String videoUrl;

    @FXML
    private void cancel() {
        dialogStage.close();
    }

    @FXML
    private void send() throws IOException {
        // Handle sending the message
        String message = messageTextArea.getText();
        System.out.println("Message Sent: " + message);
        System.out.println(imageUrl);
        System.out.println(fileUrl);
        sendMessage();
        dialogStage.close();
    }





    //do a POST request(get the users email)
    private void sendMessage() throws IOException {
        String userEmail = ProfileViewPv.getProfileEmail();
        String urlString = "http://localhost:8000/message";
        URL url = new URL(urlString);

        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setDoOutput(true);

            // Ensure videoUrl, imageUrl, and fileUrl are properly initialized
            if (videoUrl == null) videoUrl = "";
            if (imageUrl == null) imageUrl = "";
            if (fileUrl == null) fileUrl = "";

            // Create JSON body using org.json.JSONObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("sender", UserEmail.readEmail());
            jsonObject.put("receiver", userEmail);
            jsonObject.put("text", messageTextArea.getText());
            jsonObject.put("image", imageUrl);
            jsonObject.put("file", fileUrl);
            jsonObject.put("video", videoUrl);

            String jsonInputString = jsonObject.toString();

            // Write JSON body to the output stream
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("UTF-8");
                os.write(input, 0, input.length);
            }

            // Handle the response
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED) {
                System.out.println("Message Sent.");
                // Optionally, read the response
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    System.out.println("Response: " + response.toString());
                }
            } else {
                System.out.println("Failed to send message. Response Code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Failed to send message due to I/O error.", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
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
         setImageUrl(file.getAbsolutePath());
            // Add code here to display or process the image
        } else {
            System.out.println("No image file selected");
        }
    }

    @FXML
    private void handleFile() {
        // Open file chooser for text files only
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Text File");

        // Add file filter for text files only
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );

        // Show open file dialog
        File file = fileChooser.showOpenDialog(dialogStage);
        if (file != null) {
            // Handle the selected file
           setFileUrl(convertToFileUrl(file.getAbsolutePath()));
            setFileUrl(fileUrl);
            System.out.println("File selected: " + fileUrl);
        } else {
            System.out.println("No file selected");
        }
    }

    private String convertToFileUrl(String path) {
        try {
            // Normalize and convert the path to a file URL
            File file = new File(Paths.get(path).toAbsolutePath().normalize().toString());
            return file.toURI().toURL().toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @FXML
    private void handleVideo(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Video File");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.mkv", "*.avi", "*.mov")
        );

        File file = fileChooser.showOpenDialog(dialogStage);
        if (file != null) {
            setVideoUrl(file.getAbsolutePath());
        } else {
            System.out.println("No video file selected");
        }
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}