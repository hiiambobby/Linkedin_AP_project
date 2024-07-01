package com.backend.server.Controller;

import com.backend.server.Database.EducationDAO;
import com.backend.server.Model.Education;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.sql.SQLException;
import java.util.List;

public class EducationController {
    private EducationDAO educationDAO;


    public EducationController() throws SQLException {
        this.educationDAO = new EducationDAO();
    }



    public Education getEducationById(String id) throws SQLException {
        return educationDAO.getEducationById(id);
    }

    public List<Education> getEducationsByUserId(String userId) throws SQLException {
        return educationDAO.getEducationsByUserId(userId);
    }

    public void addEducation(Education education) throws SQLException {
        educationDAO.addEducation(education);
    }

    public void updateEducation(Education education) throws SQLException, JsonProcessingException {
        educationDAO.updateEducation(education);
    }

    public void deleteEducation(String id) throws SQLException {
        educationDAO.deleteEducation(id);
    }
}
