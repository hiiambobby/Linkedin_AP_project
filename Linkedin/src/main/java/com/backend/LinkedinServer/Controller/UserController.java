package com.backend.LinkedinServer.Controller;

import com.backend.LinkedinServer.Database.ContactInfoDAO;
import com.backend.LinkedinServer.Database.UserDAO;
import com.backend.LinkedinServer.Model.ContactInfo;
import com.backend.LinkedinServer.Model.User;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class UserController {
    private UserDAO userDAO;
    private ContactInfoDAO contactInfoDAO;

    public UserController() throws SQLException {
        this.userDAO = new UserDAO(); // Initialize UserDAO instance
    }

    public String getUsers() {
        try {
            List<User> users = userDAO.getAllUsers();
            return users.toString();
        } catch (SQLException e) {
            return "Error fetching users: " + e.getMessage();
        }
    }

    public User getUserById(String id) throws SQLException {
        User user = userDAO.getUserById(id);
        if (user != null) {
            try {
                return user;
            } catch (Exception e) {
                throw new RuntimeException("Failed to convert user to JSON", e);
            }
        } else {
            return null;
        }
    }

    public void createUser(String firstName, String lastName,String additionalName, String email, String password, String phoneNumber, String country, LocalDate birthday) throws SQLException {
        String userId = UUID.randomUUID().toString(); // Generate random ID for user
        User newUser = new User(userId, firstName, lastName,additionalName, email, password, phoneNumber, country, birthday);
        userDAO.saveUser(newUser); // Save user to User table

        // Create profile URL for user
        String profileUrl = generateProfileUrl(firstName, lastName, userId);


        ContactInfo contactInfo = new ContactInfo(userId,profileUrl, phoneNumber,birthday);
        contactInfoDAO.saveContactInfo(contactInfo); // Save contact info to ContactInfo table
    }

    private String generateProfileUrl(String firstName, String lastName, String userId) {
        // Example format: www.linkedin.com/in/first-name-last-name-uuid
        String uniqueId = userId.substring(0, 8); // Use a portion of ID for uniqueness
        return String.format("www.linkedin.com/in/%s-%s-%s", firstName, lastName, uniqueId);
    }

    public void updateUser(String id, String firstName, String lastName, String email, String additionalName, String phoneNumber,
                           String password, String country, LocalDate birthday) {
        try {
            User userToUpdate = new User(id, firstName, lastName, additionalName, email, phoneNumber, password, country, birthday);
            userDAO.updateUser(userToUpdate);
        } catch (SQLException e) {
            // Handle exception appropriately
            System.err.println("Failed to update user: " + e.getMessage());
        }
    }


    public void deleteUser(String id) {
        try {
            userDAO.deleteUser(id);
        } catch (SQLException e) {
            // Handle exception appropriately
            System.err.println("Failed to delete user: " + e.getMessage());
        }
    }

    public boolean checkUserExists(String email, String password) {
        try {
            return userDAO.checkUserExists(email, password);
        } catch (SQLException e) {
            // Handle exception appropriately
            System.err.println("Failed to check user existence: " + e.getMessage());
            return false;
        }
    }

}