package com.backend.client.controllers;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import com.backend.server.Model.Education;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EducationController implements Initializable {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button saveButton;

    @FXML
    private Button discardButton;

    // Method to initialize the data
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        populateScrollPane();
    }

    private void populateScrollPane() {
        VBox vbox = new VBox();
        vbox.setSpacing(10); // Space between fields

        // School
        HBox schoolHBox = new HBox(10);
        Label schoolLabel = new Label("School:");
        TextField schoolField = new TextField();
        schoolHBox.getChildren().addAll(schoolLabel, schoolField);

        // Degree
        HBox degreeHBox = new HBox(10);
        Label degreeLabel = new Label("Degree:");
        TextField degreeField = new TextField();
        degreeHBox.getChildren().addAll(degreeLabel, degreeField);

        // Field of Study
        HBox fieldOfStudyHBox = new HBox(10);
        Label fieldOfStudyLabel = new Label("Field of Study:");
        TextField fieldOfStudyField = new TextField();
        fieldOfStudyHBox.getChildren().addAll(fieldOfStudyLabel, fieldOfStudyField);

        // Start Date Month
        HBox startDateMonthHBox = new HBox(10);
        Label startDateMonthLabel = new Label("Start Date Month:");
        ComboBox<String> startDateMonthCombo = new ComboBox<>();
        startDateMonthCombo.getItems().addAll("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
        startDateMonthHBox.getChildren().addAll(startDateMonthLabel, startDateMonthCombo);

        // Start Date Year
        HBox startDateYearHBox = new HBox(10);
        Label startDateYearLabel = new Label("Start Date Year:");
        TextField startDateYearField = new TextField();
        startDateYearHBox.getChildren().addAll(startDateYearLabel, startDateYearField);

        // End Date Month
        HBox endDateMonthHBox = new HBox(10);
        Label endDateMonthLabel = new Label("End Date Month:");
        ComboBox<String> endDateMonthCombo = new ComboBox<>();
        endDateMonthCombo.getItems().addAll("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
        endDateMonthHBox.getChildren().addAll(endDateMonthLabel, endDateMonthCombo);

        // End Date Year
        HBox endDateYearHBox = new HBox(10);
        Label endDateYearLabel = new Label("End Date Year:");
        TextField endDateYearField = new TextField();
        endDateYearHBox.getChildren().addAll(endDateYearLabel, endDateYearField);

        // Activities
        HBox activitiesHBox = new HBox(10);
        Label activitiesLabel = new Label("Activities:");
        TextField activitiesField = new TextField();
        activitiesHBox.getChildren().addAll(activitiesLabel, activitiesField);

        // Description
        HBox descriptionHBox = new HBox(10);
        Label descriptionLabel = new Label("Description:");
        TextField descriptionField = new TextField();
        descriptionHBox.getChildren().addAll(descriptionLabel, descriptionField);

        // Skills
        HBox skillsHBox = new HBox(10);
        Label skillsLabel = new Label("Skills:");
        TextField skillsField = new TextField();
        skillsHBox.getChildren().addAll(skillsLabel, skillsField);

        // Notify Network
        HBox notifyNetworkHBox = new HBox(10);
        Label notifyNetworkLabel = new Label("Notify Network:");
        CheckBox notifyNetworkCheckBox = new CheckBox();
        notifyNetworkHBox.getChildren().addAll(notifyNetworkLabel, notifyNetworkCheckBox);

        // Add all HBoxes to VBox
        vbox.getChildren().addAll(
                schoolHBox, degreeHBox, fieldOfStudyHBox, startDateMonthHBox, startDateYearHBox,
                endDateMonthHBox, endDateYearHBox, activitiesHBox, descriptionHBox, skillsHBox, notifyNetworkHBox
        );

        // Set the VBox as the content of the ScrollPane
        scrollPane.setContent(vbox);
    }
}
