package com.backend.LinkedinServer.Model;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonProperty;

//in linkedin our id is made automatically but we can change it later
//FIX THE BIRTHDAY OPTION
public class User {
    @JsonProperty("id")
    private String id; //which is the jwt token at first( the user do not choose id in linkedin at first)

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("additionalName")
    private String additionalName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phoneNumber")
    private String phoneNumber;

    @JsonProperty("password")
    private String password;

    @JsonProperty("location")
    private String location;

    @JsonProperty("birthday")
    private LocalDate birthday;



    // Constructor, getters, setters, and toString() method

    public User(String id, String firstName, String lastName,String additionalName, String email, String phoneNumber, String password, String location,LocalDate birthday) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.additionalName = additionalName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.location = location;
        this.birthday = birthday;
    }

    public User() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAdditionalName(String additionalName) {
        this.additionalName = additionalName;
    }

    public String getAdditionalName() {
        return additionalName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLocation() {
        return location;
    }

    public void setCountry(String location) {
        this.location = location;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }


    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", additionalName='" + additionalName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", password='" + password + '\'' +
                ", location='" + location + '\'' +
                ", birthday=" + birthday +
                '}';
    }
}