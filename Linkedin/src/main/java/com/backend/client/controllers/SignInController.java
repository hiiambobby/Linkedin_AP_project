package com.backend.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.prefs.Preferences;

import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;

public class SignInController {
    private static final String USER_DATA_FILE = "userData.txt";

    @FXML
    private Button signInButton;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passField;
    @FXML
    private Label msgId;


    public void handleSignIn(ActionEvent event) throws IOException {
       String email = emailField.getText();
       String pass = passField.getText();
       if(email.isEmpty() || pass.isEmpty())
       {
           msgId.setText("All fields required!");
           return;
       }
       boolean success = sendRequest(email,pass);
        if (success) {
            fetchAndSaveUserData(email);

            // Load the new FXML file and get the controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Profile.fxml")); // Adjust path as needed
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
                return; // Exit the method if the page cannot be loaded
            }

            // Get the ProfileController instance from the loader
            ProfileController controller = loader.getController();
            ControllerManager.setProfileController(controller);

            // Close the current stage
            Stage currentStage = (Stage) signInButton.getScene().getWindow();
            currentStage.close(); // Close the current stage

            // Create and configure a new stage for the Profile page
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("User Profile");

            // Set the stage icon
            Image icon = new Image(getClass().getResourceAsStream("/img/photo_2024-05-15_16-05-20.jpg"));
            newStage.getIcons().add(icon);

            // Set window style
            newStage.initStyle(StageStyle.DECORATED); // Standard window decorations

            // Show the new stage
            newStage.show();
        }

    }

    private boolean sendRequest(String email, String pass) throws IOException {
        //logics to send request to http
        URL url = new URL("http://localhost:8000/login"); // Replace with your server URL
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            JSONObject jsonInput = new JSONObject();

            jsonInput.put("email", email);
            jsonInput.put("password", pass);


            String jsonInputString = jsonInput.toString();

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            int responseCode = conn.getResponseCode();
            System.out.println("POST Response Code :: " + responseCode);
            String msg = setResponseMsg(responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                String token = new String(conn.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                // Save the token in preferences
                TokenManager.storeToken(token);
                System.out.println(token);
                return true;
            } else {
                System.out.println(msg);
                msgId.setText(msg);
                return false;
            }
        }  finally {
            if (conn != null) {
                conn.disconnect();
                System.out.println("closed");
            }
        }
        }

    public static void fetchAndSaveUserData(String userId) throws IOException {
        String token = TokenManager.getToken();
        if (token == null) {
            System.out.println("No token available");
            return;
        }

        URL url = new URL("http://localhost:8000/user?id=" + userId); // Append user ID to URL
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + token);

            int responseCode = conn.getResponseCode();
            System.out.println("GET Response Code :: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                String response = new String(conn.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                saveUserDataToFile(response);
            } else {
                System.out.println("Error: " + conn.getResponseMessage());
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
                System.out.println("Connection closed");
            }
        }
    }

    private static void saveUserDataToFile(String data) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_DATA_FILE))) {
            writer.write(data);
            System.out.println("User data saved to " + USER_DATA_FILE);
        }
    }

    private String setResponseMsg(int responseCode) {
        return switch (responseCode) {
            case 401 -> "user not found.";
            case 500 -> "something went wrong. pls try again later...";
            default -> "Unexpected response code: " + responseCode;
        };
    }
}
