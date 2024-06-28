package com.backend.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    private Button SignUpButton;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passField;
    @FXML
    private Label msgId;

    public void handleSignIn(ActionEvent event) throws IOException {
       String email = emailField.getText();
       String pass = passField.getText();
       if(email.length() == 0||pass.length() == 0)
       {
           msgId.setText("All fields required!");
           return;
       }
       sendRequest(email,pass);


    }

    private void sendRequest(String email, String pass) throws IOException {
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

    }
}
