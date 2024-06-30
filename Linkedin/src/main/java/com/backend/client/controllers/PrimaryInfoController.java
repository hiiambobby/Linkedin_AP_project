package com.backend.client.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.ResourceBundle;

public class PrimaryInfoController implements Initializable {
    @FXML
    private ComboBox<String> statusId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String[] statusList = {"None", "Open to work", "Hiring"};
        statusId.getItems().addAll(statusList);
        statusId.getSelectionModel().selectFirst();
    }
}
