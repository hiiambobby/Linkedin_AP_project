package com.backend.client.controllers;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONObject;


public class SignUpController {
    private static final String USER_DATA_FILE = "userData.txt";

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField confPassField;
    @FXML
    private Button signUpField;

    @FXML
    private Label msgId;

    public void submit(ActionEvent actionEvent) {
        try {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String password = passwordField.getText();
            String email = emailField.getText();
            String confPassword = confPassField.getText();

            if (firstName.length() > 20) {
                msgId.setText("First name cannot be longer than 20 characters");
                msgId.setStyle("-fx-text-fill: red;");
                return;
            }

            if (lastName.length() > 40) {
                msgId.setText("Last name cannot be longer than 40 characters");
                msgId.setStyle("-fx-text-fill: red;");
                return;
            }

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                msgId.setText("All fields are required!");
                return;
            } else if (!password.equals(confPassword)) {
                msgId.setText("Passwords do not match");
                return;
            }

            // Make the HTTP request
            boolean success = sendPostRequest(firstName, lastName, email, password, confPassword);
            if (success) {
                fetchAndSaveUserData();
                // Load the new FXML file
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/Profile.fxml"))); // Adjust path as needed
                Stage currentStage = (Stage) signUpField.getScene().getWindow();
                currentStage.close(); // Close the current stage if needed

                // Create a new stage with the decorated style
                Stage newStage = new Stage();
                Image icon = new Image("/img/photo_2024-05-15_16-05-20.jpg");
                newStage.getIcons().add(icon);
                Scene newScene = new Scene(root);
                newStage.setScene(newScene);
                newStage.setTitle("User Profile");
                newStage.initStyle(StageStyle.DECORATED); // Standard window decorations for the new stage
                newStage.show();
            }} catch (Exception e) {
                e.printStackTrace();
            }
    }

    private boolean sendPostRequest(String firstName, String lastName,String email, String password, String confPassword) throws Exception {
        URL url = new URL("http://localhost:8000/signup"); // Replace with your server URL
        HttpURLConnection conn = null;
        try {
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        JSONObject jsonInput = new JSONObject();
        jsonInput.put("action", "signup");
        jsonInput.put("firstName", firstName);
        jsonInput.put("lastName", lastName);
        jsonInput.put("additionalName", Optional.ofNullable(null));
        jsonInput.put("email", email);
        jsonInput.put("password", password);
        jsonInput.put("confirmPassword", confPassword);
        jsonInput.put("country", Optional.ofNullable(null));
        jsonInput.put("city", Optional.ofNullable(null));

        String jsonInputString = jsonInput.toString();

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);
        String msg = setResponseMsg(responseCode);

        if (responseCode == HttpURLConnection.HTTP_CREATED) { // success
            String token = new String(conn.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            TokenManager.storeToken(token);
            msgId.setText("User created successfully.");
            msgId.setStyle("-fx-text-fill: green;");
            return true;
        } else {
            System.out.println(msg);
            msgId.setText(msg);
            return false;
        }
    } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
    private static void fetchAndSaveUserData() throws IOException {
        String token = TokenManager.getToken();
        if (token == null) {
            System.out.println("No token available");
            return;
        }

        URL url = new URL("http://localhost:8000/user"); // Replace with your server URL
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");

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

    public String setResponseMsg(int code) {
        return switch (code) {
            case 201 -> "User created successfully";
            case 226 -> "User already exists! login to continue";
            case 451 -> "Please enter a valid email";
            case 411 -> "Password should be 8 words or longer!";
            case 409 -> "Password do not match";
            case 500 -> "Server error";
            default -> "Unexpected response code: " + code;
        };
    }

}

