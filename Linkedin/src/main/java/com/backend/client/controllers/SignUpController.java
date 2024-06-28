package com.backend.client.controllers;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

    @FXML
    private Label msgId;

    String firstName,lastName,password,confirmPass,email;
    public void submit(ActionEvent actionEvent) {
        try {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String password = passwordField.getText();
            String email = emailField.getText();
            String confPassword = confPassField.getText();


            // Make the HTTP request
            sendPostRequest(firstName, lastName,email, password, confPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendPostRequest(String firstName, String lastName,String email, String password, String confPassword) throws Exception {
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
            System.out.println("User created successfully.");
        } else {
            System.out.println(msg);
        }
        msgId.setText(msg);
    }


    public String setResponseMsg(int code) {
        switch (code) {
            case 226:
                return "User already exists! login to continue";
            case 304:
                return "Fill all the fields!";
            case 451:
                return "Please enter a valid email";
            case 411:
                return "Password should be 8 words or longer!";
            case 409:
                return "Password do not match";
            default:
                return "Error...";
                
        }
    }

}

