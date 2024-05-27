package com.backend.LinkedinServer.Model;

import java.time.DayOfWeek;
import java.time.MonthDay;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    private MonthDay birthday; //not sure

    @JsonProperty("instantMessaging")


    private String instantMessaging;
    // Default constructor
    public ContactInfo() {
    }

    public ContactInfo(String profileUrl,String phoneNumber,String phoneType,MonthDay birthday,String address,String instantMessaging)
    {
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.profileUrl = profileUrl;
        this.phoneType = phoneType;
        //this.birthday = birthday;
        this.instantMessaging = instantMessaging;
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

    public void setBirthday(MonthDay birthday) {
        this.birthday = birthday;
    }

    public MonthDay getBirthday() {
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
                ", profileUrl='" + profileUrl + '\'' +
                ",  phoneNumber='" + phoneNumber + '\'' + //common with user
                ", phoneType=" + phoneType +
                ", address=" + address +
                ", birthday=" + birthday +
                ", address=" + instantMessaging+
                '}';
    }
}
