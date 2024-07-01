package com.backend.server.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PrimaryInfo {
    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("additionalName")
    private String additionalName;

    @JsonProperty("headTitle")
    private String headTitle;


    @JsonProperty("city")
    private String city;
    @JsonProperty("country")
    private String country;

    @JsonProperty("profession")
    private String profession;

    @JsonProperty("userId")
    private String userId;
    @JsonProperty("status")
    private String status;
    @JsonProperty("backgroundPic")
    private String backPic;
    @JsonProperty("profilePic")
    private String profilePic;

    // Default constructor
    public PrimaryInfo() {
    }


    public PrimaryInfo(String userId, String firstName, String lastName, String additionalName, String profilePic
            , String backPic, String headTitle,
                       String city, String country, String profession, String status) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.additionalName = additionalName;
        this.profilePic = profilePic;
        this.backPic = backPic;
        this.headTitle = headTitle;
        this.city = city;
        this.country = country;
        this.profession = profession;
        this.status = status;
    }

    public PrimaryInfo(String userId) {
        this.userId = userId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAdditionalName(String additionalName) {
        this.additionalName = additionalName;
    }

    public String getBackPic() {
        return backPic;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setBackPic(String backPic) {
        this.backPic = backPic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setHeadTitle(String headTitle) {
        this.headTitle = headTitle;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAdditionalName() {
        return additionalName;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getHeadTitle() {
        return headTitle;
    }

    public String getProfession() {
        return profession;
    }

    public String getStatus() {
        return status;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "PrimaryInfo{" +
                ", userId='" + userId + '\'' +
                ", first_name='" + firstName + '\'' +
                ", last_name='" + lastName + '\'' + //common with user
                ", additional_name=" + additionalName +
                ", city=" + city +
                ", profilePic=" + profilePic +
                ", backgroundPic=" + backPic +
                ", country=" + country +
                ", head_title=" + headTitle +
                ", profession=" + profession +
                ", status=" + status +
                '}';
    }
}
