package com.backend.server.Database;

import com.backend.server.Model.Education;
import com.backend.server.MySql;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EducationDAO {
    private final Connection connection;

    public EducationDAO() throws SQLException {
        connection = MySql.getConnection();
        createTable();
    }

    public void createTable() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS educations (" +
                "school VARCHAR(255) PRIMARY KEY, " +
                "userId VARCHAR(255), " +
                "degree VARCHAR(255), " +
                "fieldOfStudy VARCHAR(255), " +
                "startDateMonth VARCHAR(50), " +
                "startDateYear INT, " +
                "endDateMonth VARCHAR(50), " +
                "endDateYear INT, " +
                "activities TEXT, " +
                "description TEXT, " +
                "skills JSON, " +  // Use JSON if supported; otherwise, use TEXT
                "notifyNetwork BOOLEAN NOT NULL" +
                // Uncomment the following line if `userId` is intended to be a foreign key
                //"FOREIGN KEY (userId) REFERENCES users(email)" +
                ")";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace(); // Log or handle the exception
            System.out.println("SQL Exception: " + e.getMessage());
        }
    }
    public Education getEducationById(String id) throws SQLException {
        String sql = "SELECT * FROM educations WHERE school = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractEducationFromResultSet(rs);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<Education> getEducationsByUserId(String userId) throws SQLException {
        String sql = "SELECT * FROM educations WHERE userId = ?";
        List<Education> educations = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                educations.add(extractEducationFromResultSet(rs));
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return educations;
    }

    public void addEducation(Education education) throws SQLException {
        String sql = "INSERT INTO educations (school, userId, degree, fieldOfStudy, startDateMonth, startDateYear, endDateMonth, endDateYear, activities, description, skills, notifyNetwork) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            setEducationStatementParameters(stmt, education);
            stmt.executeUpdate();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateEducation(Education education) throws SQLException, JsonProcessingException {
        String sql = "UPDATE educations SET userId = ?, degree = ?, fieldOfStudy = ?, startDateMonth = ?, startDateYear = ?, endDateMonth = ?, endDateYear = ?, activities = ?, description = ?, skills = ?, notifyNetwork = ? " +
                "WHERE school = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            setEducationStatementParameters(stmt, education);
            stmt.setString(13, education.getSchool());  // school is the primary key
            stmt.executeUpdate();
        }
    }

    public void deleteEducation(String id) throws SQLException {
        String sql = "DELETE FROM educations WHERE school = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    private Education extractEducationFromResultSet(ResultSet rs) throws SQLException, JsonProcessingException {
        Education education = new Education();
        education.setSchool(rs.getString("school"));  // school is used as the primary key
        education.setUserId(rs.getString("userId"));
        education.setDegree(rs.getString("degree"));
        education.setFieldOfStudy(rs.getString("fieldOfStudy"));
        education.setStartDateMonth(rs.getString("startDateMonth"));
        education.setStartDateYear(rs.getInt("startDateYear"));
        education.setEndDateMonth(rs.getString("endDateMonth"));
        education.setEndDateYear(rs.getInt("endDateYear"));
        education.setActivities(rs.getString("activities"));
        education.setDescription(rs.getString("description"));
        // Convert JSON to ArrayList
        String skillsJson = rs.getString("skills");
        // Assuming skills is a JSON array of strings
        education.setSkills(new ArrayList<>(List.of(new ObjectMapper().readValue(skillsJson, String[].class))));
        education.setNotifyNetwork(rs.getBoolean("notifyNetwork"));
        return education;
    }

    private void setEducationStatementParameters(PreparedStatement stmt, Education education) throws SQLException, JsonProcessingException {
        stmt.setString(1, education.getSchool());  // school is used as the primary key
        stmt.setString(2, education.getUserId());
        stmt.setString(3, education.getDegree());
        stmt.setString(4, education.getFieldOfStudy());
        stmt.setString(5, education.getStartDateMonth());
        stmt.setInt(6, education.getStartDateYear());
        stmt.setString(7, education.getEndDateMonth());
        stmt.setInt(8, education.getEndDateYear());
        stmt.setString(9, education.getActivities());
        stmt.setString(10, education.getDescription());
        stmt.setString(11, new ObjectMapper().writeValueAsString(education.getSkills()));  // Convert ArrayList to JSON
        stmt.setBoolean(12, education.getNotifyNetwork());
    }
}
