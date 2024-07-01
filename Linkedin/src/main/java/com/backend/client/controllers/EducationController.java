package com.backend.client.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;


public class EducationController {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Button addSkillButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button discardButton;

    @FXML
    private TextField schoolField;

    @FXML
    private TextField degreeField;

    @FXML
    private TextField fieldOfStudyField;

    @FXML
    private ComboBox<String> startDateMonthCombo;

    @FXML
    private ComboBox<Integer> startDateYearCombo;

    @FXML
    private ComboBox<String> endDateMonthCombo;

    @FXML
    private ComboBox<Integer> endDateYearCombo;

    @FXML
    private TextField activitiesField;

    @FXML
    private TextField descriptionField;

    @FXML
    private CheckBox notifyNetworkCheckBox;


    private VBox skillsVBox;

    // Maximum number of skills allowed
    private int maxSkills = 5;

    // Range of years
    private ObservableList<Integer> years = FXCollections.observableArrayList();
    private ObservableList<String> months = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        populateYears();
        populateScrollPane(); // Initialize the scroll pane content
        addSkillButton.setOnAction(event -> addSkillField());
        saveButton.setOnAction(event -> saveEducation());
        discardButton.setOnAction(event -> discardChanges());
    }

    private void populateYears() {
        int currentYear = java.time.Year.now().getValue();
        for (int year = currentYear - 100; year <= currentYear; year++) {
            years.add(year);
        }
    }

    private void populateScrollPane() {
        VBox vbox = new VBox();
        vbox.setSpacing(10); // Space between fields
        vbox.setPadding(new Insets(20)); // Padding around the VBox

        // Define common styles
        String labelStyle = "-fx-font-weight: bold; -fx-font-size: 14px;";
        String fieldStyle = "-fx-pref-width: 200px;";

        // School
        schoolField = new TextField();
        HBox schoolHBox = createField("School:", schoolField, labelStyle, fieldStyle);

        // Degree
        degreeField = new TextField();
        HBox degreeHBox = createField("Degree:", degreeField, labelStyle, fieldStyle);

        // Field of Study
        fieldOfStudyField = new TextField();
        HBox fieldOfStudyHBox = createField("Field of Study:", fieldOfStudyField, labelStyle, fieldStyle);

        // Start Date Month
        startDateMonthCombo = new ComboBox<>();
        startDateMonthCombo.getItems().addAll("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
        HBox startDateMonthHBox = createField("Start Date Month:", startDateMonthCombo, labelStyle, fieldStyle);

        // Start Date Year
        startDateYearCombo = new ComboBox<>(years);
        HBox startDateYearHBox = createField("Start Date Year:", startDateYearCombo, labelStyle, fieldStyle);

        // End Date Month
        endDateMonthCombo = new ComboBox<>();
        endDateMonthCombo.getItems().addAll("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
        HBox endDateMonthHBox = createField("End Date Month:", endDateMonthCombo, labelStyle, fieldStyle);

        // End Date Year
        endDateYearCombo = new ComboBox<>(years);
        HBox endDateYearHBox = createField("End Date Year:", endDateYearCombo, labelStyle, fieldStyle);

        startDateYearCombo.setOnAction(event -> updateEndYearCombo(startDateYearCombo, endDateMonthCombo, endDateYearCombo));
        endDateMonthCombo.setOnAction(event -> updateEndYearCombo(startDateYearCombo, endDateMonthCombo, endDateYearCombo));

        // Activities
        activitiesField = new TextField();
        HBox activitiesHBox = createField("Activities:", activitiesField, labelStyle, fieldStyle);

        // Description
        descriptionField = new TextField();
        HBox descriptionHBox = createField("Description:", descriptionField, labelStyle, fieldStyle);

        // Skills VBox
        skillsVBox = new VBox(10); // 10px spacing between skills
        skillsVBox.setPadding(new Insets(5, 0, 5, 20)); // Padding around the skills VBox
        skillsVBox.setStyle("-fx-background-color: #f0f0f0;"); // Optional background color for better visibility

        // Add initial skill fields
        for (int i = 0; i < maxSkills; i++) {
            addSkillField();
        }

        // Add Skills VBox to a HBox with label
        HBox skillsHBox = new HBox(10);
        skillsHBox.setPadding(new Insets(5, 0, 5, 20)); // Padding for the HBox
        Label skillsLabel = new Label("Skills:");
        skillsLabel.setStyle(labelStyle);
        skillsHBox.getChildren().addAll(skillsLabel, skillsVBox);

        // Notify Network
        notifyNetworkCheckBox = new CheckBox();
        HBox notifyNetworkHBox = createField("Notify Network:", notifyNetworkCheckBox, labelStyle, "-fx-pref-width: 200px;");

        // Add all HBoxes to VBox
        vbox.getChildren().addAll(
                schoolHBox, degreeHBox, fieldOfStudyHBox, startDateMonthHBox, startDateYearHBox,
                endDateMonthHBox, endDateYearHBox, activitiesHBox, descriptionHBox, skillsHBox, notifyNetworkHBox
        );

        // Set the VBox as the content of the ScrollPane
        scrollPane.setContent(vbox);
    }

    private HBox createField(String labelText, Control field, String labelStyle, String fieldStyle) {
        Label label = new Label(labelText);
        label.setStyle(labelStyle);
        field.setStyle(fieldStyle);

        HBox hbox = new HBox(10);
        hbox.setPadding(new Insets(5, 0, 5, 20)); // Padding for the HBox
        hbox.getChildren().addAll(label, field);

        return hbox;
    }

    private void addSkillField() {
        if (skillsVBox.getChildren().size() < maxSkills) {
            // Create a new skill field
            TextField skillField = new TextField();
            skillField.setPromptText("Skill " + (skillsVBox.getChildren().size() + 1));
            skillField.setPrefWidth(200);

            // Create a label for the skill field
            Label skillLabel = new Label("Skill:");

            // Create a HBox to hold the label and the skill field
            HBox skillHBox = new HBox(10);
            skillHBox.setPadding(new Insets(5, 0, 5, 20)); // Padding for each skill row
            skillHBox.getChildren().addAll(skillLabel, skillField);

            // Add the HBox to the VBox
            skillsVBox.getChildren().add(skillHBox);
        } else {
            // Optionally show an alert if trying to add more than the limit
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Skill Limit Reached");
            alert.setHeaderText(null);
            alert.setContentText("You can only add up to " + maxSkills + " skills.");
            alert.showAndWait();
        }
    }

    private void updateEndYearCombo(ComboBox<Integer> startYearCombo, ComboBox<String> startMonthCombo, ComboBox<Integer> endYearCombo) {
        Integer startYear = startYearCombo.getValue();
        String startMonth = startMonthCombo.getValue();

        if (startYear != null && startMonth != null) {
            int startMonthIndex = months.indexOf(startMonth);
            int endYear = startYear + 1;

            // Adjust the end year based on the start month
            if (startMonthIndex < months.indexOf("July")) { // If start month is before or in July, end year should be the same
                endYear = startYear;
            }

            ObservableList<Integer> endYears = FXCollections.observableArrayList();
            for (int year = startYear; year <= endYear; year++) {
                endYears.add(year);
            }
            endYearCombo.setItems(endYears);

            // Ensure the current end year value is still in the updated list
            if (endYearCombo.getValue() != null && !endYears.contains(endYearCombo.getValue())) {
                endYearCombo.setValue(endYears.get(endYears.size() - 1)); // Set to the latest available year
            }
        }
    }

    private void saveEducation() {
        readEducationData(); //for testing
    }

    private void discardChanges() {
        // Your code to discard changes
    }
    private void readEducationData() {
        String school = schoolField.getText();

        // Check if the school field is empty and show an alert if necessary
        if (school == null || school.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR,"Error", "School field cannot be empty.");
            return;
        }

        String degree = degreeField.getText();
        String fieldOfStudy = fieldOfStudyField.getText();
        String startDateMonth = startDateMonthCombo.getValue();
        Integer startDateYear = startDateYearCombo.getValue();
        String endDateMonth = endDateMonthCombo.getValue();
        Integer endDateYear = endDateYearCombo.getValue();
        String activities = activitiesField.getText();
        String description = descriptionField.getText();
        boolean notifyNetwork = notifyNetworkCheckBox.isSelected();

        // Retrieve skills
        List<String> skills = new ArrayList<>();
        for (Node node : skillsVBox.getChildren()) {
            if (node instanceof HBox) {
                HBox skillHBox = (HBox) node;
                for (Node child : skillHBox.getChildren()) {
                    if (child instanceof TextField) {
                        TextField skillField = (TextField) child;
                        skills.add(skillField.getText());
                    }
                }
            }
        }

        // Now you can use these values as needed
        System.out.println("School: " + school);
        System.out.println("Degree: " + (degree != null ? degree : "Not specified"));
        System.out.println("Field of Study: " + (fieldOfStudy != null ? fieldOfStudy : "Not specified"));
        System.out.println("Start Date: " + (startDateMonth != null ? startDateMonth : "Not specified") + " " + (startDateYear != null ? startDateYear : "Not specified"));
        System.out.println("End Date: " + (endDateMonth != null ? endDateMonth : "Not specified") + " " + (endDateYear != null ? endDateYear : "Not specified"));
        System.out.println("Activities: " + (activities != null ? activities : "Not specified"));
        System.out.println("Description: " + (description != null ? description : "Not specified"));
        System.out.println("Notify Network: " + notifyNetwork);
        System.out.println("Skills: " + skills);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



}
