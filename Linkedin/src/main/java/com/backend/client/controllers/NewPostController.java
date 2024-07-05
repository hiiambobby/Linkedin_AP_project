package com.backend.client.controllers;

import com.backend.server.Model.Post;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class NewPostController {

    @FXML
    private TextArea captionField;

    @FXML
    private ImageView imageId;

    @FXML
    private  ImageView videoId;

    private Stage dialogStage;
    @FXML
    void discardPressed(ActionEvent event) {
        dialogStage.close();


    }
    private List<String> imageUrls = new ArrayList<>();
    private List<String> videoUrls = new ArrayList<>();
    private final String userId = UserEmail.readEmail();


    //use POST
    @FXML
    void savePressed(ActionEvent event) {
        String caption = captionField.getText();
        List<String> imageUrlsConverted = convertToFileUrls(imageUrls);
        List<String> videoUrlsConverted = convertToFileUrls(videoUrls);

        Post newPost = new Post(userId, caption, videoUrlsConverted, imageUrlsConverted);

        HttpURLConnection conn = null;
        OutputStream os = null;
        try {
            // Convert Post object to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String postJson = objectMapper.writeValueAsString(newPost);

            // Send HTTP POST request
            URL url = new URL("http://localhost:8000/post");
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Content-Length", Integer.toString(postJson.getBytes("utf-8").length));

            // Write JSON data to the request body
            os = conn.getOutputStream();
            os.write(postJson.getBytes("utf-8"));

            // Get response from the server
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED) {
                System.out.println("Post created successfully.");
            } else {
                System.out.println("Failed to create post. Response code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Close output stream and connection
            try {
                if (os != null) {
                    os.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        dialogStage.close();
    }
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
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

    public void selectVideo(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Video File");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.mkv", "*.avi", "*.mov")
        );

        List<File> files = fileChooser.showOpenMultipleDialog(dialogStage);
        if (files != null) {
            for (File file : files) {
                videoUrls.add(file.getAbsolutePath());
            }
        } else {
            System.out.println("No video file selected");
        }
    }

    public void selectImage(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");

        // Add file filters for images
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );

        // Show open file dialog
        List<File> files = fileChooser.showOpenMultipleDialog(dialogStage);
        if (files != null) {
            for (File file : files) {
                imageUrls.add(file.getAbsolutePath());
            }
        } else {
            System.out.println("No image file selected");
        }
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public void setVideoUrls(List<String> videoUrls) {
        this.videoUrls = videoUrls;
    }

    private List<String> convertToFileUrls(List<String> paths) {
        List<String> urls = new ArrayList<>();
        for (String path : paths) {
            try {
                File file = new File(Paths.get(path).toAbsolutePath().normalize().toString());
                urls.add(file.toURI().toURL().toString());
            } catch (Exception e) {
                e.printStackTrace();
                urls.add("");
            }
        }
        return urls;
    }
}
