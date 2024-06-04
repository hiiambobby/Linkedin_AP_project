package com.backend.LinkedinServer.Model;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.Date;

public class Education {
    //school degree field of study //start day and end date Grade Activities and societies  Description Skills
    @JsonProperty("id")
    private String id;
    @JsonProperty("school")
    private String school;

    @JsonProperty("degree")
    private  String degree;

    @JsonProperty("fieldOfStudy")
    private String  fieldOfStudy;

    @JsonProperty("startDate")
    private Date startDate;

    @JsonProperty("endDate")
    private Date endDate;

    @JsonProperty("activities")
    private String activities;

    @JsonProperty("description")
    private String description;

    @JsonProperty("skills")
    private ArrayList<String> skills =  new ArrayList<>(); ;

    @JsonProperty("notifyNetwork")
    private Boolean notifyNetwork;

    //default constructor
    public Education() {

    }


    public Education(String id,String school,String degree,String fieldOfStudy,Date startDate,Date endDate,String activities,String description,ArrayList skills,boolean notifyNetwork) {
        this.id = id;
        this.activities = activities;
        this.degree = degree;
        this.description = description;
        this.school = school;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public ArrayList<String> getSkills() {
        return skills;
    }

    public void setSkills(ArrayList<String> skills) {
        this.skills = skills;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }


    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    @Override
    public String toString() {
        return "Education{" +
                ", id='" + id + '\'' +
                ", school='" + school + '\'' +
                ",  degree='" + degree + '\'' + //common with user
                ", fieldOfStudy=" + fieldOfStudy+
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", activities=" +activities+
                ", description=" +description+
                ", skills=" +skills+
                ", notifyNetwork=" +notifyNetwork+

                '}';
    }
}
