package com.backend.LinkedinServer.Database;

import com.backend.LinkedinServer.Model.ContactInfo;
import com.backend.LinkedinServer.MySql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ControllerDAO {
    public void create(ContactInfo contactInfo) throws SQLException {
        String sql = "INSERT INTO contactInfo (profileUrl, phoneNumber, phoneType, address, birthday, instantMessaging)";
        try(Connection conn = MySql.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, contactInfo.getProfileUrl());
            stmt.setString(2, contactInfo.getPhoneNumber());
            stmt.setString(3,contactInfo.getPhoneType());
            stmt.setString(4, contactInfo.getAddress());
            stmt.setDate(9, java.sql.Date.valueOf(contactInfo.getBirthday()));
            stmt.setString(6, contactInfo.getInstantMessaging());
            stmt.executeUpdate();

        }}






    }


