package com.backend.server.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class ContactInfo {
    @JsonProperty("profileUrl")
    private String profileUrl;

//    @JsonProperty("email")
//    private String email;

    @JsonProperty("phoneNumber")
    private String phoneNumber;

    @JsonProperty("phoneType")
    private String phoneType;

    @JsonProperty("address")//we should choose from different phone type
    private String address;


    @JsonProperty("birthdayMonth")
    private String month;
    @JsonProperty("birthdayDay")
    private int day;

    @JsonProperty("instantMessaging")
    private String instantMessaging;

    @JsonProperty("userId")
    private String userId;
    @JsonProperty("visibility")
    private String visibility;

    // Default constructor
    public ContactInfo() {

    }


    public ContactInfo(String userId,String profileUrl,String phoneNumber, String phoneType,String month,int day,String visibility, String address,String instantMessaging) {
        this.userId = userId;
        this.profileUrl = profileUrl;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.phoneType = phoneType;
        this.instantMessaging = instantMessaging;
        this.day = day;
        this.month = month;
        this.visibility = visibility;
    }

    public ContactInfo(String userId, String profileUrl,String email) {
        this.userId = userId;
        this.profileUrl = profileUrl;
//        this.email = email;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }

    public String getPhoneType() {
        return phoneType;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getDay() {
        return day;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getMonth() {
        return month;
    }

    public void setInstantMessaging(String instantMessaging) {
        this.instantMessaging = instantMessaging;
    }

    public String getInstantMessaging() {
        return instantMessaging;
    }

//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getEmail() {
//        return email;
//    }

    @Override
    public String toString() {
        return "ContactInfo{" +
                ", userId='" + userId + '\'' +
//                ", email='" + email + '\'' +
                ", profileUrl='" + profileUrl + '\'' +
                ",  phoneNumber='" + phoneNumber + '\'' + //common with user
                ", phoneType=" + phoneType +
                ", birthMonth" + month +
                ", birthDay" + day +
                ", visibility=" + visibility+
                ", address=" + address +
                ", instantMessaging=" + instantMessaging +
                '}';
    }
}
