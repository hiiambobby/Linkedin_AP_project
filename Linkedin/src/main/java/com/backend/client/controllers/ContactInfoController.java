package com.backend.client.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Month;
import java.util.ResourceBundle;
import java.util.UUID;

public class ContactInfoController implements Initializable {

    @FXML
    private Button saveBtn;
    @FXML
    private TextField urlId;
    @FXML
    private TextField emailId;
    @FXML
    private TextField numberId;
    @FXML
    private TextField addrId;
    @FXML
    private Label error1;
    @FXML
    private TextField instantMessage;
    @FXML
    private ComboBox<Integer> dayId;
    @FXML
    private ComboBox<String> monthId;
    @FXML
    private ComboBox<String> visibilityId;
    @FXML
    private ComboBox<String> phoneType;
    private final ObservableList<String> months = FXCollections.observableArrayList(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
    );


    public void Save(ActionEvent event) {
        JSONObject jsonObject = getJsonObject();

        HttpURLConnection conn = null;
        try {
            URL url = new URL("http://localhost:8000/contactInfo");
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
               setAlert.showAlert(Alert.AlertType.INFORMATION, "Success", "Contact info saved successfully.");
                saved(event); // Assuming this method exists to handle UI updates
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
                setAlert.showAlert(Alert.AlertType.ERROR, "Error", "Failed to save contact info. Response code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
            setAlert.showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while saving contact info.");
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }



    private void saved(ActionEvent event) throws IOException {
        Stage popupStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        popupStage.close();
    }

    private JSONObject getJsonObject() {
        // Create JSON object to send to the server
        if(checkLength(addrId.getText(),numberId.getText(),instantMessage.getText())){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("profile_url", urlId.getText() != null ? urlId.getText() : "");
            jsonObject.put("phone_number", numberId.getText() != null ? numberId.getText() : "");
            jsonObject.put("phone_type", phoneType.getValue() != null ? phoneType.getValue() : "");
            jsonObject.put("month", monthId.getValue() != null ? monthId.getValue() : ""); // Corrected to match expected retrieval type
            jsonObject.put("day", dayId.getValue() != null ? dayId.getValue() : 0); // Default to 0 for numeric
            jsonObject.put("visibility", visibilityId.getValue() != null ? visibilityId.getValue() : "");
            jsonObject.put("address", addrId.getText() != null ? addrId.getText() : "");
            jsonObject.put("instant_messaging", instantMessage.getText() != null ? instantMessage.getText() : "");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;}
        return null;

    }


    private boolean checkLength(String addr,String phoneNum,String instant) {
        if (addr.length() > 220){
            error1.setText("max length = 220");
            error1.setTextFill(Color.RED);
            return false;
    }
        if(phoneNum.length() > 40)
        {
            numberId.setText("Enter a valid phone number");
            numberId.setStyle("-fx-text-fill: red;");
            return false;

        }
        if(instant.length() > 40)
        {
            instantMessage.setText("max length = 40");
            instantMessage.setStyle("-fx-text-fill: red;");
            return false;
        }
        return true;

    }




    /***
     *
     * @param url
     * @param resourceBundle
     * options of combo boxes are set here
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ////phoneType boxes
        String[] phoneTypes = {"Mobile", "Home", "Work"};
        phoneType.getItems().addAll(phoneTypes);
        /////birthday boxes
        monthId.setItems(months);
        monthId.getSelectionModel().selectFirst();
        monthId.valueProperty().addListener((obs, oldMonth, newMonth) -> updateDays());
        updateDays();
        ////visibility of birthday
        String[] options = {"Only you","Your connections","Your network","All LinkedIn members"};
        visibilityId.getItems().addAll(options);
        visibilityId.getSelectionModel().selectFirst();
      /////load the prev data
        loadContactInfo();
    }

    private void updateDays() {
        String selectedMonth = monthId.getValue();
        if (selectedMonth == null) {
            return; // No month selected yet
        }

        // Get the number of days in the selected month
        int daysInMonth = getDaysInMonth(selectedMonth);

        // Create list of days
        ObservableList<Integer> days = FXCollections.observableArrayList();
        for (int day = 1; day <= daysInMonth; day++) {
            days.add(day);
        }

        // Update the day ComboBox
        dayId.setItems(days);
    }

    private int getDaysInMonth(String month) {
        try {
            // Convert month name to Month enum
            Month monthEnum = Month.valueOf(month.toUpperCase());
            return monthEnum.length(java.time.Year.isLeap(java.time.LocalDate.now().getYear()));
        } catch (IllegalArgumentException e) {
            return 31; // Default to 31 days if month is invalid (shouldn't happen)
        }
    }

    public void discard(ActionEvent event) throws IOException {
        saved(event);
    }

    // In ContactInfoController.java
    private void loadContactInfo() {
        JSONObject jsonResponse = getcontactInfoJSONObject();
        if(jsonResponse != null){
            populateFields(jsonResponse);
        }
    }

    public JSONObject getcontactInfoJSONObject(){
        HttpURLConnection conn = null;
        try {
            URL url = new URL("http://localhost:8000/contactInfo");
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
                setAlert.showAlert(Alert.AlertType.ERROR, "Error", "Failed to load contact info. Response code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
            setAlert.showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while loading contact info.");
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }

    private void populateFields(JSONObject jsonObject) {
        // Retrieve the email from the stored token
        String email = TokenManager.getEmailFromStoredToken();
        System.out.println("Retrieved email: " + email);

        // Generate the URL
        String url = "http://www.example.com/" + generateRandomId();

        // Set the values in the text fields and combo boxes
        urlId.setText(url);
        urlId.setEditable(false);  // Make the URL field uneditable

        // Check if email is null or empty before setting it
        emailId.setText(jsonObject.optString("user_id", ""));
        emailId.setEditable(false);  // Make the email field uneditable

        // Set other fields based on JSON object
        numberId.setText(jsonObject.optString("phone_number", ""));
        phoneType.setValue(jsonObject.optString("phone_type", ""));
        monthId.setValue(jsonObject.optString("month", ""));
        dayId.setValue(jsonObject.optInt("day", 1)); // Default to 1 if not available
        visibilityId.setValue(jsonObject.optString("visibility", ""));
        addrId.setText(jsonObject.optString("address", ""));
        instantMessage.setText(jsonObject.optString("instant_messaging", ""));
    }

    private String generateRandomId() {
        return UUID.randomUUID().toString().substring(0, 16);
    }




}
