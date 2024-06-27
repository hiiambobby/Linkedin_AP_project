package com.backend.client.controllers;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
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
    private Button signUpButton;

    String firstName,lastName,password,confirmPass,email;
    public void submit(ActionEvent actionEvent) {
        try {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String password = passwordField.getText();
            String email = emailField.getText();
            String confPassword = confPassField.getText();
            String additionalName =  null ;
            String country = null;
            String city = null;

            System.out.println("First Name: " + firstName);
            System.out.println("Last Name: " + lastName);
            System.out.println("Password: " + password);
            System.out.println("Email: " + email);
            System.out.println("Confirm Password: " + confPassword);
            System.out.println("Additional Name: " + null);
            System.out.println("Country: " + null);
            System.out.println("City: " + null);

            // Make the HTTP request
            sendPostRequest(firstName, lastName, null, email, password, confPassword, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendPostRequest(String firstName, String lastName, String additionalName, String email, String password, String confPassword, String country, String city) throws Exception {
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
        jsonInput.put("additionalName", "bobby");
        jsonInput.put("email", email);
        jsonInput.put("password", password);
        jsonInput.put("confirmPassword", confPassword);
        jsonInput.put("country", "iran");
        jsonInput.put("city", "tehran");

        String jsonInputString = jsonInput.toString();

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_CREATED) { // success
            System.out.println("User created successfully.");
        } else {
            System.out.println("POST request failed.");
        }
    }
}