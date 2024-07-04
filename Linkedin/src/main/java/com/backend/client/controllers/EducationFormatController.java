package com.backend.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
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
    private Label skillsLabel;

    @FXML
    private Label startMonthLabel;

    @FXML
    private Label startYearLabel;

    @FXML
    private Label studyFieldLabel;

    public void setEducationDetails(String school, String degree, String field, String startMonth, Integer startYear,
                                    String endMonth, Integer endYear, String activities,String description) {
        schoolLabel.setText(school);
        degreeLabel.setText(degree);
        studyFieldLabel.setText(field);
        startMonthLabel.setText(startMonth);
        startYearLabel.setText(startYear.toString());
        endMonthLabel.setText(endMonth);
        endYearLabel.setText(endYear.toString());
        activityLabel.setText(activities);
        descriptionLabel.setText(description);
    }
}
