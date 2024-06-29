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

import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class SignInController {
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
            // Load the new FXML file
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Profile.fxml")); // Adjust path as needed
            Stage currentStage = (Stage) signInButton.getScene().getWindow();
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
        }

    }

    private boolean sendRequest(String email, String pass) throws IOException {
        //logics to send request to http
        URL url = new URL("http://localhost:8000/login"); // Replace with your server URL
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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

        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            return true;
        } else {
            System.out.println(msg);
            msgId.setText(msg);
            return false;
        }
    }

    private String setResponseMsg(int responseCode) {
        switch (responseCode)
        {
            case 401:
                return "user not found.";
            case 500:
                return "something went wrong. pls try again later...";
            default:
                return "Unexpected response code: " + responseCode;
        }
    }
}
