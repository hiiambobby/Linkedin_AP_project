package com.backend.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
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
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

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
    @FXML
    private ScrollPane educationScrollPane;
    @FXML
    private VBox educationVBox;

    // add education to profile
    /*public void addEducationProfile(String school, String degree, String field, String startMonth, Integer startYear,
                                    String endMonth, Integer endYear, String activities,String description) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EducationFormat.fxml"));
            VBox educationComponent = loader.load();

            // Access the controller of EducationComponent to set data
            EducationFormatController controller = loader.getController();
            controller.setEducationDetails(school, degree, field,startMonth, startYear, endMonth,endYear,activities,description);

            // Add the component to the VBox
            educationVBox.getChildren().add(educationComponent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

       loadInformation();

    }
    private void loadInformation(){
        JSONObject jsonResponse = getPrimaryInfoJSONObject();
        if(jsonResponse != null){
            populateFields(jsonResponse);
            setPictures(jsonResponse);
        }
    }
    public  JSONObject getPrimaryInfoJSONObject() {
        HttpURLConnection conn = null;
        try {
            // Retrieve the email from TokenManager
            String email = readEmail();
            System.out.println("Email before encoding: " + email);


            // Check if the email is null and handle it
            if (email == null || email.trim().isEmpty()) {
                System.err.println("Email is null or empty.");
                setAlert.showAlert(Alert.AlertType.ERROR, "Error", "Email is not available.");
                return null;
            }

            // Ensure the email ID is URL-encoded
            String encodedEmailId = URLEncoder.encode(email, StandardCharsets.UTF_8.toString());

            // Construct the URL with the email ID as a query parameter
            URL url = new URL("http://localhost:8000/primaryInfo?id=" + encodedEmailId);

            // Open connection
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // No token required; we skip setting the Authorization header

            // Get the response code
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
                return new JSONObject(response.toString());
            } else {
                // Handle HTTP error response
              //  setAlert.showAlert(Alert.AlertType.ERROR, "Error", "Failed to load primary info. Response code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
            setAlert.showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while loading primary info.");
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }

    public String readEmail()
    {
        String filePath = "userdata.txt"; // Path to your JSON file
        try {
            String jsonString = readJsonFile(filePath);
            JSONObject jsonObject = new JSONObject(jsonString);
            return (jsonObject.optString("email", "Unknown"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String readJsonFile(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    private void populateFields(JSONObject jsonObject) {
        // Set other fields based on JSON object
        nameLabel.setText((jsonObject.optString("firstName", "") + jsonObject.optString("additionalName", "")
                + jsonObject.optString("lastName", "")));
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
            new Image("/img/emptyProfilePic.jpg");
        }
        return null;
    }


    public void updateProfile() {
        loadInformation();
    }



    public void openSearch(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Search.fxml"));
        Stage currentStage = (Stage) logOut.getScene().getWindow();
        currentStage.close(); // Close the current stage if needed

        // Create a new stage with the decorated style
        Stage newStage = new Stage();
        Image icon = new Image("/img/photo_2024-05-15_16-05-20.jpg");
        newStage.getIcons().add(icon);
        Scene newScene = new Scene(root);
        newStage.setScene(newScene);
        newStage.setTitle("search");
        newStage.initStyle(StageStyle.DECORATED);
        newStage.setFullScreen(true);
        newStage.show();
    }
    public void openNetwork(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Network.fxml"));
        Stage currentStage = (Stage) logOut.getScene().getWindow();
        currentStage.close(); // Close the current stage if needed

        // Create a new stage with the decorated style
        Stage newStage = new Stage();
        Image icon = new Image("/img/photo_2024-05-15_16-05-20.jpg");
        newStage.getIcons().add(icon);
        Scene newScene = new Scene(root);
        newStage.setScene(newScene);
        newStage.setTitle("my network");
        newStage.initStyle(StageStyle.DECORATED);
        newStage.setFullScreen(true);
        newStage.show();
    }
}
