package com.backend.LinkedinServer.Controller;

import com.backend.LinkedinServer.Model.User;
import com.backend.LinkedinServer.Database.UserDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.backend.LinkedinServer.HTTPHandler.ContactInfoHandler.objectMapper;

public class UserController {
    private UserDAO userDAO; // Inject UserDAO dependency

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
        if (user!= null) {
            try {
                return user;
            } catch (Exception e) {
                throw new RuntimeException("Failed to convert user to JSON", e);
            }
        } else {
            return null;
        }
    }
    public void createUser(String id, String firstName, String lastName, String additionalName, String email, String phoneNumber,
                           String password, String country, LocalDate birthday) {
        User newUser = new User(id, firstName, lastName, additionalName, email, phoneNumber, password, country, birthday);
        try {
            userDAO.saveUser(newUser);
            //System.out.println("user created");
        } catch (SQLException e) {

            System.err.println("Failed to create user: " + e.getMessage());
        }
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