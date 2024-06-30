package com.backend.server.Controller;

import com.backend.server.Database.ContactInfoDAO;
import com.backend.server.Model.ContactInfo;

import java.sql.SQLException;

public class ContactInfoController {

    private final ContactInfoDAO contactInfoDAO;

    public ContactInfoController() throws SQLException {
        this.contactInfoDAO = new ContactInfoDAO();
    }

    // Save a new contact information entry
    public void saveContactInfo(String userId,String profileUrl, String phoneNumber, String phoneType, String month, int day, String visibility, String address, String instantMessaging) throws SQLException {
        ContactInfo contactInfo = new ContactInfo(userId,profileUrl,phoneNumber, phoneType,month, day,visibility,address,instantMessaging);
        contactInfoDAO.createContactInfo(contactInfo);
    }

    // Retrieve contact information for a user
    public ContactInfo getContactInfo(String userId) throws SQLException {
        return contactInfoDAO.getContactInfoByUserId(userId);
    }

    // Update existing contact information
    public void updateContactInfo(String userId,String profileUrl, String phoneNumber, String phoneType, String month, int day, String visibility, String address, String instantMessaging) throws SQLException {
        ContactInfo contactInfo = new ContactInfo(userId,profileUrl,phoneNumber, phoneType,month, day,visibility,address,instantMessaging);
        contactInfoDAO.updateContactInfo(contactInfo);
    }

    // Delete contact information for a user
    public void deleteContactInfo(String userId) throws SQLException {
        contactInfoDAO.deleteContactInfoByUserId(userId);
    }
}
