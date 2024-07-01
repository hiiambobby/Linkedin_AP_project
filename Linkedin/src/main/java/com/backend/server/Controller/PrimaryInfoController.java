package com.backend.server.Controller;

import com.backend.server.Database.PrimaryInfoDAO;
import com.backend.server.Model.PrimaryInfo;

import java.sql.SQLException;

public class PrimaryInfoController {
    private final PrimaryInfoDAO primaryInfoDAO;

    public PrimaryInfoController() throws SQLException {
        this.primaryInfoDAO = new PrimaryInfoDAO();
    }

    // Save a new primary information entry
    public void savePrimaryInfo(String userId, String firstName, String lastName, String additionalName, String headTitle, String city, String country, String profession, String status) throws SQLException {
        PrimaryInfo primaryInfo = new PrimaryInfo(userId,firstName,lastName, additionalName,headTitle, city,country,profession,status);
        primaryInfoDAO.createPrimaryInfo(primaryInfo);
    }

    // Retrieve primary information for a user
    public PrimaryInfo getPrimaryInfo(String userId) throws SQLException {
        return primaryInfoDAO.getPrimaryInfoByUserId(userId);
    }

    // Update existing primary information
    public void updatePrimaryInfo(String userId, String firstName, String lastName, String additionalName, String headTitle, String city, String country, String profession, String status) throws SQLException {
        PrimaryInfo primaryInfo = new PrimaryInfo(userId,firstName,lastName, additionalName,headTitle, city,country,profession,status);
        primaryInfoDAO.updatePrimaryInfo(primaryInfo);
    }

    // Delete primary information for a user
    public void deletePrimaryInfo(String userId) throws SQLException {
        primaryInfoDAO.deletePrimaryInfoByUserId(userId);
    }
}
