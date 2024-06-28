package com.backend.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SignInController {
    @FXML
    private Button SignUpButton;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passField;
    @FXML
    private Label msgId;

    public void handleSignIn(ActionEvent event)
    {
       String email = emailField.getText();
       String pass = passField.getText();
       if(email.length() == 0||pass.length() == 0)
       {
           msgId.setText("All fields required!");
           return;
       }
       sendRequest(email,pass);


    }

    private void sendRequest(String email, String pass) {
        System.out.println("hala vaght hast.....");
    }
}
