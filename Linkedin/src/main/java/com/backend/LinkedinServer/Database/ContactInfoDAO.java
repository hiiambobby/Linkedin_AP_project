//package com.backend.LinkedinServer.Database;
//
//import com.backend.LinkedinServer.Model.ContactInfo;
//import com.backend.LinkedinServer.MySql;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//
//public class ContactInfoDAO {
//    private Connection connection;
//
//    public ContactInfoDAO() throws SQLException {
//        connection = MySql.getConnection();
//        createContactTable();
//    }
//
//    private void createContactTable() throws SQLException {
//        String sql = "CREATE TABLE IF NOT EXISTS contactInfo ("
//                + "userId VARCHAR(16) PRIMARY KEY, "
//                + "profileUrl VARCHAR(255), "
//                + "email VARCHAR(255), "
//                + "phoneNumber VARCHAR(255), "
//                + "phoneType VARCHAR(255), "
//                + "address VARCHAR(255), "
//                + "instantMessaging VARCHAR(255), "
//                + "birthday DATE)";
//        try (PreparedStatement statement = connection.prepareStatement(sql)) {
//            statement.executeUpdate();
//        }
//    }
//
//
//    public void saveContactInfo(ContactInfo contactInfo) {
//    }
//}
//
//
//
