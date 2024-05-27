package com.backend.LinkedinServer.Controller;

import com.backend.LinkedinServer.Model.User;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserController {

    private List<User> users = new ArrayList<>();

    public UserController() {
        // Initialize with some dummy data
        users.add(new User("1", "John", "Doe", "john@example", "john@example.com", "091232312312", "2222", "iran"));
        users.add(new User("2", "Jane", "Smith", "Bobby", "saba@gmail.com", "09126854323", "1234","Canada"));
    }

    public String getUsers() {
        // Convert users list to JSON or String format
        return "List of users: " + users.toString();
    }

    public String getUserById(String id) {
        // Find user by id and return as JSON or String
        for (User user : users) {
            if (user.getId().equals(id)) {
                return user.toString();
            }
        }
        return "User not found";
    }

    public void createUser(String id, String firstName, String lastName,String additionalName, String email, String phoneNumber,
                           String password, String country) {
        // Create a new user and add to the users list
        User newUser = new User(id, firstName, lastName,additionalName,email, phoneNumber, password, country);
        users.add(newUser);
    }

    public void updateUser(String id, String firstName, String lastName, String email, String phoneNumber,
                           String password, String country, Date birthday) {
        // Update user details based on id
        for (User user : users) {
            if (user.getId().equals(id)) {
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setEmail(email);
                user.setPhoneNumber(phoneNumber);
                user.setPassword(password);
                user.setCountry(country);
                user.setBirthday(birthday);
                return;
            }
        }
        // Handle user not found scenario
        throw new RuntimeException("User not found");
    }

    public void deleteUser(String id) {
        // Delete user based on id
        users.removeIf(user -> user.getId().equals(id));
    }

    public boolean isEmailExists(String email) {
        // Check if email already exists
        return users.stream().anyMatch(user -> user.getEmail().equals(email));
    }
}