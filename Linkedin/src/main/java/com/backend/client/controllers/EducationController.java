package com.backend.client.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


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
    private TextArea activitiesField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private CheckBox notifyNetworkCheckBox;


    private VBox skillsVBox;

    // Maximum number of skills allowed
    private final int maxSkills = 5;

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
        activitiesField = new TextArea();
        activitiesField.setPrefRowCount(5); // Set the preferred number of rows
        HBox activitiesHBox = createField("Activities:", activitiesField, labelStyle, fieldStyle);

// Description
        descriptionField = new TextArea();
        descriptionField.setPrefRowCount(10); // Set the preferred number of rows
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
        HBox hbox = new HBox(10, label, field);
        hbox.setAlignment(Pos.CENTER_LEFT);
        return hbox;
    }

    public boolean maxLength() {
        if (schoolField.getText().length() > 40) {

            showAlert(Alert.AlertType.ERROR, "Error", "Please enter between(1-500) words");
            return false;
        }
        if (activitiesField.getText().length() > 500) {

            showAlert(Alert.AlertType.ERROR, "Error", "Please enter between(1-500) words");
            return false;
        }
        if (descriptionField.getText().length() > 1000) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter between(1-1000) words");
            return false;
        }
        return true;
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
            int currentYear = java.time.Year.now().getValue();

            // Create a list of years starting from the startYear to the currentYear
            ObservableList<Integer> endYears = FXCollections.observableArrayList();
            for (int year = startYear; year <= currentYear; year++) {
                endYears.add(year);
            }

            // Add the next year to the endYears list if the start month is late in the year
            int startMonthIndex = months.indexOf(startMonth);
            if (startMonthIndex >= months.indexOf("July")) {
                // Allow selection of the next year if start month is July or later
                endYears.add(startYear + 1);
            }

            // Set items in the end year combo box
            endYearCombo.setItems(endYears);

            // Ensure the current end year value is still in the updated list
            if (endYearCombo.getValue() != null && !endYears.contains(endYearCombo.getValue())) {
                endYearCombo.setValue(endYears.get(endYears.size() - 1)); // Set to the latest available year
            }
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void saveEducation() {
        if (maxLength()) {
            addEducation();
        }
        // readEducationData(); //for testing
        Stage stage = (Stage) discardButton.getScene().getWindow();
        stage.close();
    }

    private void discardChanges() {
        Stage stage = (Stage) discardButton.getScene().getWindow();
        stage.close();
    }

    private JSONObject getJsonObject() {
        // Create JSON object to send to the server
        JSONObject jsonObject = new JSONObject();

        try {
            // Retrieve values from fields, handle nulls and provide default values
            jsonObject.put("school", schoolField.getText() != null ? schoolField.getText() : "");
            jsonObject.put("userId", TokenManager.getEmailFromStoredToken());
            jsonObject.put("degree", degreeField.getText() != null ? degreeField.getText() : "");
            jsonObject.put("fieldOfStudy", fieldOfStudyField.getText() != null ? fieldOfStudyField.getText() : "");

            // For ComboBoxes, provide default empty string or null
            jsonObject.put("startDateMonth", startDateMonthCombo.getValue() != null ? startDateMonthCombo.getValue() : "");
            jsonObject.put("startDateYear", startDateYearCombo.getValue() != null ? startDateYearCombo.getValue() : 0); // Default to 0 for numeric

            jsonObject.put("endDateMonth", endDateMonthCombo.getValue() != null ? endDateMonthCombo.getValue() : "");
            jsonObject.put("endDateYear", endDateYearCombo.getValue() != null ? endDateYearCombo.getValue() : 0); // Default to 0 for numeric

            jsonObject.put("activities", activitiesField.getText() != null ? activitiesField.getText() : "");
            jsonObject.put("description", descriptionField.getText() != null ? descriptionField.getText() : "");

            // For CheckBox, convert to boolean (true/false)
            jsonObject.put("notifyNetwork", notifyNetworkCheckBox.isSelected());

            // Add skills information (assuming skillsVBox contains the skill fields)
            JSONArray skillsArray = new JSONArray();
            for (Node node : skillsVBox.getChildren()) {
                if (node instanceof HBox) {
                    HBox hbox = (HBox) node;
                    for (Node child : hbox.getChildren()) {
                        if (child instanceof TextField) {
                            TextField skillField = (TextField) child;
                            skillsArray.put(skillField.getText() != null ? skillField.getText() : "");
                        }
                    }
                }
            }
            jsonObject.put("skills", skillsArray);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public void addEducation() {
        JSONObject jsonObject = getJsonObject();

        HttpURLConnection conn = null;
        try {
            URL url = new URL("http://localhost:8000/education");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            // Retrieve the token from TokenManager
            String tokenLong = TokenManager.getToken();
            String token = TokenManager.extractTokenFromResponse(tokenLong);

            if (token != null && !token.isEmpty()) {
                conn.setRequestProperty("Authorization", "Bearer " + token);
            } else {
                System.out.println("No token available.");
            }

            conn.setDoOutput(true);

            String json = jsonObject.toString();
            System.out.println(jsonObject);
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = json.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();


            // Handle response
            if (responseCode == HttpURLConnection.HTTP_OK) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Education info saved successfully.");
            } else {
                // Read and log any error stream
                InputStream errorStream = conn.getErrorStream();
                if (errorStream != null) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(errorStream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                }
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to save Education info. Response code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while saving contact info.");
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }


}
