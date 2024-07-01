package com.backend.server.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Date;
import java.util.ArrayList;

public class Education {
    //school degree field of study //start day and end date Grade Activities and societies  Description Skills
    @JsonProperty("userId")
    private String userId;
    @JsonProperty("id")
    private String id;

    @JsonProperty("school")
    private String school;

    @JsonProperty("degree")
    private String degree;

    @JsonProperty("fieldOfStudy")
    private String fieldOfStudy;

    @JsonProperty("startDateMonth")
    private String startDateMonth;
    @JsonProperty("startDateYear")
    private int startDateYear;

    @JsonProperty("endDateMonth")
    private String endDateMonth;

    @JsonProperty("endDateYear")
    private int endDateYear;

    @JsonProperty("activities")
    private String activities;

    @JsonProperty("description")
    private String description;

    @JsonProperty("skills")
    private ArrayList<String> skills = new ArrayList<>();
    ;

    @JsonProperty("notifyNetwork")
    private Boolean notifyNetwork;

    //default constructor
    public Education() {

    }


    public Education( String school,String userId,String degree, String fieldOfStudy, String startDateMonth, int startDateYear
            , String endDateMonth, int endDateYear, String activities, String description, ArrayList skills, boolean notifyNetwork) {
        this.id = school;
        this.userId= userId;
        this.activities = activities;
        this.degree = degree;
        this.description = description;
        this.school = school;
        this.startDateMonth = startDateMonth;
        this.startDateYear = startDateYear;
        this.endDateMonth = endDateMonth;
        this.endDateYear = endDateYear;
        this.fieldOfStudy = fieldOfStudy;
        this.skills = skills;
        this.notifyNetwork = notifyNetwork;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setActivities(String activities) {
        this.activities = activities;
    }

    public String getActivities() {
        return activities;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public ArrayList<String> getSkills() {
        return skills;
    }

    public void setSkills(ArrayList<String> skills) {
        this.skills = skills;
    }


    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setEndDateMonth(String endDateMonth) {
        this.endDateMonth = endDateMonth;
    }

    public void setEndDateYear(int endDateYear) {
        this.endDateYear = endDateYear;
    }

    public void setStartDateMonth(String startDateMonth) {
        this.startDateMonth = startDateMonth;
    }

    public void setStartDateYear(int startDateYear) {
        this.startDateYear = startDateYear;
    }

    public void setNotifyNetwork(Boolean notifyNetwork) {
        this.notifyNetwork = notifyNetwork;
    }

    public String getUserId() {
        return userId;
    }

    public Boolean getNotifyNetwork() {
        return notifyNetwork;
    }

    public int getEndDateYear() {
        return endDateYear;
    }

    public int getStartDateYear() {
        return startDateYear;
    }

    public String getEndDateMonth() {
        return endDateMonth;
    }

    public String getStartDateMonth() {
        return startDateMonth;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }



    @Override
    public String toString() {
        return "Education{" +
                ", school='" + school + '\'' +
                ", userId='" + userId + '\'' +
                ",  degree='" + degree + '\'' + //common with user
                ", fieldOfStudy=" + fieldOfStudy +
                ", startDateMonth=" + startDateMonth +
                ", startDateYear=" + startDateYear +
                ", endDateMonth=" + endDateMonth +
                ", endDateYear=" + endDateYear +
                ", activities=" + activities +
                ", description=" + description +
                ", skills=" + skills +
                ", notifyNetwork=" + notifyNetwork +
                '}';
    }
}
