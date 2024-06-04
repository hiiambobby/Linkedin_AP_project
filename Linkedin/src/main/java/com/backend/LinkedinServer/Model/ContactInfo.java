package com.backend.LinkedinServer.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class ContactInfo {
    @JsonProperty("profileUrl")
    private String profileUrl;

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

    public ContactInfo(String userId, String profileUrl, String phoneNumber, String phoneType, String address, LocalDate birthday, String instantMessaging) {
        this.userId = userId;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.profileUrl = profileUrl;
        this.phoneType = phoneType;
        this.birthday = birthday;
        this.instantMessaging = instantMessaging;
    }

    public ContactInfo(String userId, String profileUrl, String phoneNumber, LocalDate birthday) {
        this.userId = userId;
        this.profileUrl = profileUrl;
        this.birthday = birthday;
        this.phoneNumber= phoneNumber;
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

    @Override
    public String toString() {
        return "ContactInfo{" +
                ", userId='" + userId + '\'' +
                ", profileUrl='" + profileUrl + '\'' +
                ",  phoneNumber='" + phoneNumber + '\'' + //common with user
                ", phoneType=" + phoneType +
                ", address=" + address +
                ", birthday=" + birthday +
                ", instantMessaging=" + instantMessaging +
                '}';
    }
}
