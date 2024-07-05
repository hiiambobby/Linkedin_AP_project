package com.backend.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.util.List;

public class EducationFormatController {
    @FXML
    private Label activityLabel;

    @FXML
    private Label degreeLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label endMonthLabel;

    @FXML
    private Label endYearLabel;

    @FXML
    private Label schoolLabel;

    @FXML
    private TextArea skillsLabel;

    @FXML
    private Label startMonthLabel;

    @FXML
    private Label startYearLabel;

    @FXML
    private Label studyFieldLabel;

    public void setEducationDetails(String school, String degree, String field, String startMonth, Integer startYear,
                                    String endMonth, Integer endYear, String activities, String description, List<String> skills) {
        schoolLabel.setText(school);
        degreeLabel.setText(degree);
        studyFieldLabel.setText(field);
        startMonthLabel.setText(startMonth);
        startYearLabel.setText(startYear.toString());
        endMonthLabel.setText(endMonth);
        endYearLabel.setText(endYear.toString());
        activityLabel.setText(activities);
        degreeLabel.setText(description);
        // Concatenate skills into a single string
        StringBuilder skillsText = new StringBuilder();
        for (String skill : skills) {
            skillsText.append(skill).append("\n");
        }

        // Set the concatenated string to the skillsTextArea
        skillsLabel.setText(skillsText.toString());
    }
}
