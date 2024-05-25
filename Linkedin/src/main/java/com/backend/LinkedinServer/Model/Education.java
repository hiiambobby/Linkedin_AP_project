package com.backend.LinkedinServer.Model;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.ArrayList;

public class Education {
    //school degree field of study //start day and end date Grade Activities and societies  Description Skills
    @JsonProperty("school")
    private String school;

    @JsonProperty("degree")
    private  String degree;

    @JsonProperty("fieldOfStudy")
    private String  fieldOfStudy;

    @JsonProperty("startDate")
    private LocalDate startDate;

    @JsonProperty("endDate")
    private LocalDate endDate;

    @JsonProperty("activities")
    private String activities;

    @JsonProperty("description")
    private String description;

    @JsonProperty("skills")
    private ArrayList<String> skills =  new ArrayList<>(); ;

    public Education(String school,String degree,String fieldOfStudy,LocalDate startDate,LocalDate endDate,String activities,String description,ArrayList skills) {
        this.activities = activities;
        this.degree = degree;
        this.description = description;
        this.school = school;
        this.startDate = startDate;
        this.endDate = endDate;
        this.fieldOfStudy = fieldOfStudy;
        this.skills = skills;
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

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public ArrayList<String> getSkills() {
        return skills;
    }

    public void setSkills(ArrayList<String> skills) {
        this.skills = skills;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    @Override
    public String toString() {
        return "ContactInfo{" +
                ", school='" + school + '\'' +
                ",  degree='" + degree + '\'' + //common with user
                ", fieldOfStudy=" + fieldOfStudy+
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", activities=" +activities+
                ", description=" +description+
                ", skills=" +skills+
                '}';
    }
}
