package com.backend.client.controllers;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
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
        // Sample data
        List<Education> educationList = List.of(
                new Education("University of Example", "user1", "BSc Computer Science", "Computer Science", "September", 2010, "June", 2014, "Coding Club", "Studied hard", new ArrayList<>(List.of("Java", "SQL")), true),
                new Education("Tech University", "user2", "MSc Software Engineering", "Software Engineering", "September", 2014, "June", 2016, "Research Society", "Advanced topics", new ArrayList<>(List.of("Machine Learning", "Data Science")), false)
        );

        populateScrollPane(educationList);
    }

    private void populateScrollPane(List<Education> educationList) {
        VBox vbox = new VBox(); // VBox to hold all HBoxes
        vbox.setSpacing(10); // Space between HBoxes

        for (Education education : educationList) {
            HBox hbox = new HBox();
            hbox.setSpacing(10); // Space between items in HBox

            Text schoolText = new Text("School: " + education.getSchool());
            Text degreeText = new Text("Degree: " + education.getDegree());
            Text fieldOfStudyText = new Text("Field of Study: " + education.getFieldOfStudy());
            Text datesText = new Text("From: " + education.getStartDateMonth() + " " + education.getStartDateYear() + " To: " + education.getEndDateMonth() + " " + education.getEndDateYear());
            Text activitiesText = new Text("Activities: " + education.getActivities());
            Text descriptionText = new Text("Description: " + education.getDescription());

            hbox.getChildren().addAll(schoolText, degreeText, fieldOfStudyText, datesText, activitiesText, descriptionText);
            vbox.getChildren().add(hbox);
        }

        // Set the VBox as the content of the ScrollPane
        scrollPane.setContent(vbox);
    }


}
