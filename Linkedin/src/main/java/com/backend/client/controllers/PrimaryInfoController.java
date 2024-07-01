package com.backend.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

public class PrimaryInfoController implements Initializable {
    @FXML
    private ComboBox<String> statusId;
    @FXML
    private TextField nameId;
    @FXML
    private TextField lastNameId;
    @FXML
    private TextField additionalNameId;
    @FXML
    private TextField backgroundPicId;
    @FXML
    private TextField profilePicId;
    @FXML
    private TextField headTitleId;
    @FXML
    private TextField countryId;
    @FXML
    private TextField cityId;
    @FXML
    private TextField professionId;

    public void save(ActionEvent event) {
        JSONObject jsonObject = getJsonObject();

        HttpURLConnection conn = null;
        try {
            URL url = new URL("http://localhost:8000/primaryInfo");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            // Retrieve the token from TokenManager
            String tokenLong = TokenManager.getToken();
            String token = TokenManager.extractTokenFromResponse(tokenLong);

            if (token != null && !token.isEmpty()) {
                conn.setRequestProperty("Authorization", "Bearer " + token);
            } else {
                System.out.println("No token available.");
            }

            conn.setDoOutput(true);

            assert jsonObject != null;
            String json = jsonObject.toString();
            System.out.println(jsonObject);
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = json.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();


            // Handle response
            if (responseCode == HttpURLConnection.HTTP_OK) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Primary info saved successfully.");
                closePage(event); // Assuming this method exists to handle UI updates
            } else {
                // Read and log any error stream
                InputStream errorStream = conn.getErrorStream();
                if (errorStream != null) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(errorStream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                }
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to save primary info. Response code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while saving primary info.");
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }



    private void closePage(ActionEvent event) throws IOException {
        Stage popupStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        popupStage.close();
    }
    public void cancel(ActionEvent event) throws IOException {
        closePage(event);
    }

    private JSONObject getJsonObject() {
        // Create JSON object to send to the server
        if(checkLength(nameId.getText(),lastNameId.getText(),additionalNameId.getText(),headTitleId.getText(),
                        cityId.getText(),countryId.getText(),professionId.getText())){
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("firstName", nameId.getText() != null ? nameId.getText() : "");
                jsonObject.put("lastName", lastNameId.getText() != null ? lastNameId.getText() : "");
                jsonObject.put("additionalName", additionalNameId.getText() != null ? additionalNameId.getText() : "");
                jsonObject.put("headTitle", headTitleId.getText() != null ? headTitleId.getText() : "");
                jsonObject.put("city", cityId.getText() != null ? cityId.getText() : "");
                jsonObject.put("country", countryId.getText() != null ? countryId.getText() : "");
                jsonObject.put("profession", professionId.getText() != null ? professionId.getText() : "");
                jsonObject.put("status", statusId.getValue() != null ? statusId.getValue() : "");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return jsonObject;}
        return null;

    }
    private boolean checkLength(String name,String lastName,String additionalName,String headTitle,
                                String city,String country,String profession) {
        if (name.length() > 20){
            nameId.setText("Name length must be less than 20 characters");
            nameId.setStyle("-fx-text-fill: red;");
            return false;
        }
        if(lastName.length() > 40)
        {
            lastNameId.setText("Last name length must be less than 40 characters");
            lastNameId.setStyle("-fx-text-fill: red;");
            return false;

        }
        if (additionalName.length() > 20){
            additionalNameId.setText("Additional name length must be less than 20 characters");
            additionalNameId.setStyle("-fx-text-fill: red;");
            return false;
        }
        if (headTitle.length() > 220){
            headTitleId.setText("Head title length must be less than 220 characters");
            headTitleId.setStyle("-fx-text-fill: red;");
            return false;
        }
        if (city.length() > 60){
            cityId.setText("city length must be less than 60 characters");
            cityId.setStyle("-fx-text-fill: red;");
            return false;
        }
        if (country.length() > 60){
            countryId.setText("country length must be less than 60 characters");
            countryId.setStyle("-fx-text-fill: red;");
            return false;
        }
        if (profession.length() > 60){
            professionId.setText("profession length must be less than 60 characters");
            professionId.setStyle("-fx-text-fill: red;");
            return false;
        }
        return true;

    }

    private static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadPrimaryInfo() {
        JSONObject jsonResponse = getPrimaryInfoJSONObject();
        if(jsonResponse != null){
            populateFields(jsonResponse);
        }
    }
    public static JSONObject getPrimaryInfoJSONObject(){
        HttpURLConnection conn = null;
        try {
            URL url = new URL("http://localhost:8000/primaryInfo");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Retrieve the token from TokenManager
            String tokenLong = TokenManager.getToken();
            String token = TokenManager.extractTokenFromResponse(tokenLong);
            if (token != null && !token.isEmpty()) {
                conn.setRequestProperty("Authorization", "Bearer " + token);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response
                InputStream inputStream = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parse the JSON response
                JSONObject jsonResponse = new JSONObject(response.toString());
                return jsonResponse;
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to load primary info. Response code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while loading primary info.");
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }

    private void populateFields(JSONObject jsonObject) {
        // Set other fields based on JSON object
        nameId.setText(jsonObject.optString("firstName", ""));
        lastNameId.setText(jsonObject.optString("lastName", ""));
        additionalNameId.setText(jsonObject.optString("additionalName", ""));
        headTitleId.setText(jsonObject.optString("headTitle", ""));
        cityId.setText(jsonObject.optString("city", ""));
        countryId.setText(jsonObject.optString("country", ""));
        professionId.setText(jsonObject.optString("profession", ""));
        statusId.setValue(jsonObject.optString("status","None"));
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String[] statusList = {"None", "Open to work", "Hiring"};
        statusId.getItems().addAll(statusList);
        statusId.getSelectionModel().selectFirst();
        // load previous data
        loadPrimaryInfo();
    }
}
