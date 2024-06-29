package com.backend.client.controllers;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONObject;


public class SignUpController {

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

            if (firstName.length() == 0 || lastName.length() == 0 || email.length() == 0 || password.length() == 0) {
                msgId.setText("All fields are required!");
                return;
            } else if (!password.equals(confPassword)) {
                msgId.setText("Passwords do not match");
                return;
            }

            // Make the HTTP request
            boolean success = sendPostRequest(firstName, lastName, email, password, confPassword);
            if (success) {
                // Load the new FXML file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Profile.fxml"));
                Parent root = loader.load();

                // Get the current stage

                Stage stage = (Stage) signUpField.getScene().getWindow();

                // Set the new scene
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean sendPostRequest(String firstName, String lastName,String email, String password, String confPassword) throws Exception {
        URL url = new URL("http://localhost:8000/signup"); // Replace with your server URL
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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
            String response = new String(conn.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            JSONObject jsonResponse = new JSONObject(response);
            String token = jsonResponse.getString("token");

            TokenManager.saveToken(token);
            msgId.setText("User created successfully.");
            msgId.setStyle("-fx-text-fill: green;");
            return true;
        } else {
            System.out.println(msg);
            msgId.setText(msg);
            return false;
        }
    }

    public String setResponseMsg(int code) {
        switch (code) {
            case 201:
                return "User created successfully";
            case 226:
                return "User already exists! login to continue";
            case 451:
                return "Please enter a valid email";
            case 411:
                return "Password should be 8 words or longer!";
            case 409:
                return "Password do not match";
            case 500:
                return "Server error";
            default:
                return "Unexpected response code: " + code;

        }
    }

}

