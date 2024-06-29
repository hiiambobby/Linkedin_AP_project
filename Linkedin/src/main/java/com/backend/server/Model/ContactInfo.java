package com.backend.server.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class ContactInfo {
    @JsonProperty("profileUrl")
    private String profileUrl;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phoneNumber")
    private String phoneNumber;

    @JsonProperty("phoneType")
    private String phoneType;

    @JsonProperty("address")//we should choose from different phone type
    private String address;


    @JsonProperty("birthday")
    private LocalDate birthday; //not sure

    @JsonProperty("instantMessaging")
    private String instantMessaging;

    @JsonProperty("userId")
    private String userId;

    // Default constructor
    public ContactInfo() {

    }


    public ContactInfo(String userId,String profileUrl,String email,String phoneNumber, String phoneType, String address,String instantMessaging,LocalDate birthday) {
        this.userId = userId;
        this.profileUrl = profileUrl;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.phoneType = phoneType;
        this.instantMessaging = instantMessaging;
        this.birthday = birthday;
    }

    public ContactInfo(String userId, String profileUrl,String email) {
        this.userId = userId;
        this.profileUrl = profileUrl;
        this.email = email;
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

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setInstantMessaging(String instantMessaging) {
        this.instantMessaging = instantMessaging;
    }

    public String getInstantMessaging() {
        return instantMessaging;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "ContactInfo{" +
                ", userId='" + userId + '\'' +
                ", profileUrl='" + profileUrl + '\'' +
                ", email='" + email + '\'' +
                ",  phoneNumber='" + phoneNumber + '\'' + //common with user
                ", phoneType=" + phoneType +
                ", address=" + address +
                ", instantMessaging=" + instantMessaging +
                ", birthday=" + birthday +
                '}';
    }
}
