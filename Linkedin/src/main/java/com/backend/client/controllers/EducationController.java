package com.backend.client.controllers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import com.backend.server.Model.Education;
import org.json.JSONObject;

import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;


public class EducationController implements Initializable {


        // FXML fields
        @FXML
        private ScrollPane scrollPane;

        @FXML
        private Button addSkillButton;

        @FXML
        private Button saveButton;

        @FXML
        private Button discardButton;

        // VBox for holding skill fields
        private VBox skillsVBox;

        // Maximum number of skills allowed
        private int maxSkills = 5;

        // Range of years
        private ObservableList<Integer> years = FXCollections.observableArrayList();
        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
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
            HBox schoolHBox = createField("School:", new TextField(), labelStyle, fieldStyle);

            // Degree
            HBox degreeHBox = createField("Degree:", new TextField(), labelStyle, fieldStyle);

            // Field of Study
            HBox fieldOfStudyHBox = createField("Field of Study:", new TextField(), labelStyle, fieldStyle);

            // Start Date Month
            ComboBox<String> startDateMonthCombo = new ComboBox<>();
            startDateMonthCombo.getItems().addAll("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
            HBox startDateMonthHBox = createField("Start Date Month:", startDateMonthCombo, labelStyle, fieldStyle);

            // Start Date Year
            ComboBox<Integer> startDateYearCombo = new ComboBox<>(years);
            HBox startDateYearHBox = createField("Start Date Year:", startDateYearCombo, labelStyle, fieldStyle);

            // End Date Month
            ComboBox<String> endDateMonthCombo = new ComboBox<>();
            endDateMonthCombo.getItems().addAll("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
            HBox endDateMonthHBox = createField("End Date Month:", endDateMonthCombo, labelStyle, fieldStyle);

            // End Date Year
            ComboBox<Integer> endDateYearCombo = new ComboBox<>(years);
            HBox endDateYearHBox = createField("End Date Year:", endDateYearCombo, labelStyle, fieldStyle);

            // Activities
            HBox activitiesHBox = createField("Activities:", new TextField(), labelStyle, fieldStyle);

            // Description
            HBox descriptionHBox = createField("Description:", new TextField(), labelStyle, fieldStyle);

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
            CheckBox notifyNetworkCheckBox = new CheckBox();
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

        private void saveEducation() {
            // Your code to save education data
        }

        private void discardChanges() {
            // Your code to discard changes
        }
    }
