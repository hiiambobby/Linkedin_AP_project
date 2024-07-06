package com.backend.client.controllers;

import com.backend.client.components.ProfileViewComponent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class NewPostController {

    @FXML
    private TextArea captionField;

    @FXML
    private ImageView imageId;

    @FXML
    private ImageView videoId;
    @FXML
    private Label nameLabel;

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
    void savePressed(ActionEvent event) throws IOException {
        String caption = captionField.getText();
        if (caption.length() > 3000) {
            setAlert.showAlert(Alert.AlertType.ERROR, "Error", "Please enter less than 3000 characters");
            dialogStage.close();
            return;
        }
        List<String> imageUrlsConverted = convertToFileUrls(imageUrls);
        List<String> videoUrlsConverted = convertToFileUrls(videoUrls);

        String urlString = "http://localhost:8000/post";
        URL url = new URL(urlString);

        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setDoOutput(true);


            // Create JSON body using org.json.JSONObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("sender", UserEmail.readEmail());
            jsonObject.put("text", caption);
            jsonObject.put("image", imageUrlsConverted);
            jsonObject.put("video", videoUrlsConverted);


            String jsonInputString = jsonObject.toString();

            // Write JSON body to the output stream
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("UTF-8");
                os.write(input, 0, input.length);
            }

            // Handle the response
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED) {
                System.out.println("post Sent.");
                // Optionally, read the response
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    System.out.println("Response: " + response.toString());
                }
                // adding to feed
                String captionText = captionField.getText();
                TimeLineController timeLineController = ControllerManager.getTimeLineController();
                if (timeLineController != null) {
                    timeLineController.addPostFeed(captionText);
                } else {
                    System.out.println("TimeLineController reference is null");
                }
            } else {
                System.out.println("Failed to send post. Response Code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Failed to send message due to I/O error.", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
                dialogStage.close();
            }
        }
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
