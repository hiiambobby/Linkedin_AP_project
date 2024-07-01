package com.backend.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ResourceBundle;

import com.backend.client.controllers.PrimaryInfoController;

public class ProfileController implements Initializable {

    @FXML
    private Button contactInfoBtn;
    @FXML
    private Label nameLabel;
    @FXML
    private Button primaryInfoBtn;
    @FXML
    private Button logOut;
    @FXML
    private ImageView headerPic;
    @FXML
    private ImageView profilePic;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadInformation();

        //do the get method on primary info
        HttpURLConnection conn = null;
        try {
             url = new URL("http://localhost:8000/primaryInfo");
            try {
                conn = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                conn.setRequestMethod("GET");
            } catch (ProtocolException e) {
                throw new RuntimeException(e);
            }

            // Retrieve the token from TokenManager
            String tokenLong = TokenManager.getToken();
            String token = TokenManager.extractTokenFromResponse(tokenLong);
            if (token != null && !token.isEmpty()) {
                conn.setRequestProperty("Authorization", "Bearer " + token);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response
                InputStream inputStream = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parse the JSON response
                JSONObject jsonResponse = new JSONObject(response.toString());
                setPictures(jsonResponse);
            } else {
                System.out.println("error");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
    private void loadInformation(){
        JSONObject jsonResponse = PrimaryInfoController.getPrimaryInfoJSONObject();
        if(jsonResponse != null){
            populateFields(jsonResponse);
        }
    }
    private void populateFields(JSONObject jsonObject) {
        // Set other fields based on JSON object
        nameLabel.setText(jsonObject.optString("firstName", ""));
    }
            public void ContactInfo(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ContactInfo.fxml"));
        Parent root = loader.load();
        Stage popupStage = new Stage();
        popupStage.setScene(new Scene(root));
        Image icon = new Image("/img/photo_2024-05-15_16-05-20.jpg");
        popupStage.getIcons().add(icon);
        popupStage.setTitle("Contact Info");
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.show();

    }

    public void PrimaryInfo(ActionEvent event) throws IOException {
        // Load the FXML for the Contact Info scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PrimaryInfo.fxml"));
        Parent root = loader.load();
        Stage popupStage = new Stage();
        popupStage.setScene(new Scene(root));
        popupStage.setTitle("Primary Info");
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.show();

    }

    @FXML
    private void addEducation(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Education.fxml"));
        Parent root = loader.load();
        Stage popupStage = new Stage();
        popupStage.setScene(new Scene(root));
        Image icon = new Image("/img/photo_2024-05-15_16-05-20.jpg");
        popupStage.getIcons().add(icon);
        popupStage.setTitle("Add Education");
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.show();

    }

    public void logOut(ActionEvent event) throws IOException {
        TokenManager.clearToken();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Main.fxml")); // Adjust path as needed
        Stage currentStage = (Stage) logOut.getScene().getWindow();
        currentStage.close(); // Close the current stage if needed

        // Create a new stage with the decorated style
        Stage newStage = new Stage();
        Image icon = new Image("/img/photo_2024-05-15_16-05-20.jpg");
        newStage.getIcons().add(icon);
        Scene newScene = new Scene(root);
        newScene.setFill(Color.TRANSPARENT);
        newStage.setScene(newScene);
        newStage.setTitle("User Profile");
        newStage.initStyle(StageStyle.TRANSPARENT); // Standard window decorations for the new stage
        newStage.show();
    }

    public void setNameLabel(String text){

    }



    private void setPictures(JSONObject jsonObject) {
        // Set other fields based on JSON object

        // Retrieve URLs from the JSON object
        String profilePicUrl = jsonObject.optString("profilePic");
        String headerPicUrl = jsonObject.optString("backgroundPic");

        // Set profile picture
        Image profileImage = loadProfile(profilePicUrl);
        profilePic.setImage(profileImage);

        // Set header picture
        Image headerImage = loadBack(headerPicUrl);
        headerPic.setImage(headerImage);
    }
    private Image loadBack(String url) {
        if (url == null || url.isEmpty()) {
            return new Image("/img/defaultBackgroundPicture.png");
                     // Use default image if URL is null or empty
        }
        try {
            // Attempt to load image from URL
           // System.out.println("im here1");
            return new Image(url);

        } catch (Exception e) {
            // If an exception occurs (e.g., invalid URL), return the default image
            new Image("/img/defaultBackgroundPicture.png");
        }
        return null;
    }
    private Image loadProfile(String url) {
        if (url == null || url.isEmpty()) {
            return new Image("/img/emptyProfilePic.jpg");
            // Use default image if URL is null or empty
        }
        try {
            // Attempt to load image from URL
         //   System.out.println("im here2");
            return new Image(url);
        } catch (Exception e) {
            // If an exception occurs (e.g., invalid URL), return the default image
            System.out.println("erorrrrr");
            new Image("/img/emptyProfilePic.jpg");
        }
        return null;
    }


}
