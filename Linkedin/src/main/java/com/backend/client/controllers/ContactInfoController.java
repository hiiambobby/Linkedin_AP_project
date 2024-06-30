package com.backend.client.controllers;

import com.backend.server.Util.JWT;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Month;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

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


    public void Save(ActionEvent event) throws MalformedURLException {
        JSONObject jsonObject = getJsonObject();
        URL url = new URL("http://localhost:8000/contactInfo");
        HttpURLConnection conn = null;

        try {
             conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");


            String tokenLong = TokenManager.getToken();
            String token = TokenManager.extractTokenFromResponse(tokenLong);

            System.out.println("Token: " + token);
            if (token != null && !token.isEmpty()) {
                conn.setRequestProperty("Authorization", "Bearer " + token);
            } else {
                System.out.println("No token available.");
            }

           conn.setDoOutput(true);

            // Write the JSON data to the output stream
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonObject.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();

            // Log request details
            System.out.println("Request URL: " + url);
            System.out.println("Request Method: " + conn.getRequestMethod());
            System.out.println("Request Headers: " + conn.getRequestProperties());
            System.out.println("Response Code: " + responseCode);

            // Log any error stream
            InputStream errorStream = conn.getErrorStream();
            if (errorStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(errorStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

            if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Contact info saved successfully.");
                saved(event);
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to save contact info. Response code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while saving contact info.");
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }


    private void saved(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Image icon = new Image("/img/photo_2024-05-15_16-05-20.jpg");
        stage.getIcons().add(icon);
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/Profile.fxml")));
        stage.setScene(new Scene(root));
        stage.show();
    }

    private JSONObject getJsonObject() {
        // Create JSON object to send to the server
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("profileUrl", urlId.getText() != null ? urlId.getText() : "");
            jsonObject.put("phoneNumber", numberId.getText() != null ? numberId.getText() : "");
            jsonObject.put("phoneType", phoneType.getValue() != null ? phoneType.getValue() : "");
            jsonObject.put("birthMonth", monthId.getValue() != null ? monthId.getValue() : ""); // Corrected to match expected retrieval type
            jsonObject.put("birthDay", dayId.getValue() != null ? dayId.getValue() : 0); // Default to 0 for numeric
            jsonObject.put("visibility", visibilityId.getValue() != null ? visibilityId.getValue() : "");
            jsonObject.put("address", addrId.getText() != null ? addrId.getText() : "");
            jsonObject.put("instantMessaging", instantMessage.getText() != null ? instantMessage.getText() : "");
            System.out.println(visibilityId.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;

    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
      /////load the prev datas
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
    public void loadContactInfo() {
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
                populateFields(jsonResponse);
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to load contact info. Response code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while loading contact info.");
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private void populateFields(JSONObject jsonObject) {
        // Set the values in the text fields and combo boxes
        urlId.setText(jsonObject.optString("profileUrl", ""));
        numberId.setText(jsonObject.optString("phoneNumber", ""));
        phoneType.setValue(jsonObject.optString("phoneType", ""));
        monthId.setValue(jsonObject.optString("birthMonth", ""));
        dayId.setValue(jsonObject.optInt("birthDay", 1)); // Default to 1 if not available
        visibilityId.setValue(jsonObject.optString("visibility", ""));
        addrId.setText(jsonObject.optString("address", ""));
        instantMessage.setText(jsonObject.optString("instantMessaging", ""));
    }




}
