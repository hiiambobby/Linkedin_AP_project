package com.backend.client.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.Month;
import java.util.Objects;
import java.util.ResourceBundle;

public class ContactInfoController implements Initializable {
    @FXML
    private Button saveBtn;
    @FXML
    private TextField urlId;
    @FXML
    private TextField emailId;
    @FXML
    private TextField numberId;
    @FXML
    private TextField addrId;
    @FXML
    private TextField instantMessage;
    @FXML
    private ComboBox<Integer> dayId;
    @FXML
    private ComboBox<String> monthId;
    @FXML
    private ComboBox<String> visibilityId;
    @FXML
    private ComboBox<String> phoneType;
    private final ObservableList<String> months = FXCollections.observableArrayList(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
    );


    public void Save(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/Profile.fxml")));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String[] phoneTypes = {"Mobile", "Home", "Work"};
        phoneType.getItems().addAll(phoneTypes);
        phoneType.setOnAction(event -> {
            String data = phoneType.getSelectionModel().toString();
            numberId.setText(data);
        });
        monthId.setItems(months);

        // Set default month
        monthId.getSelectionModel().selectFirst();

        // Update days ComboBox based on selected month
        monthId.valueProperty().addListener((obs, oldMonth, newMonth) -> updateDays());

        // Initial population of days based on default month
        updateDays();
    }

    private void updateDays() {
        String selectedMonth = monthId.getValue();
        if (selectedMonth == null) {
            return; // No month selected yet
        }

        // Get the number of days in the selected month
        int daysInMonth = getDaysInMonth(selectedMonth);

        // Create list of days
        ObservableList<Integer> days = FXCollections.observableArrayList();
        for (int day = 1; day <= daysInMonth; day++) {
            days.add(day);
        }

        // Update the day ComboBox
        dayId.setItems(days);
    }

    private int getDaysInMonth(String month) {
        try {
            // Convert month name to Month enum
            Month monthEnum = Month.valueOf(month.toUpperCase());
            return monthEnum.length(java.time.Year.isLeap(java.time.LocalDate.now().getYear()));
        } catch (IllegalArgumentException e) {
            return 31; // Default to 31 days if month is invalid (shouldn't happen)
        }
    }
}
