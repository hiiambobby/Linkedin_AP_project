package com.backend.LinkedinServer.Database;

import com.backend.LinkedinServer.Model.ContactInfo;
import com.backend.LinkedinServer.MySql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ContactInfoDAO {
    private Connection connection;

    public ContactInfoDAO() throws SQLException {
        connection = MySql.getConnection();
        createContactTable();
    }

    private void createContactTable() {

    }


    public void saveContactInfo(ContactInfo contactInfo) {
    }
}



