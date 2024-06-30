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
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Month;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class ContactInfoController implements Initializable {
    private Preferences prefs = Preferences.userNodeForPackage(SignInController.class);


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


    public void Save(ActionEvent event) {
        JSONObject jsonObject = getJsonObject();

        try {
            URL url = new URL("http://localhost:8000/contactInfo");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            // Retrieve the token from TokenManager
            String token = TokenManager.getToken();
            System.out.println(token);
            if (token != null) {
                conn.setRequestProperty("Authorization", "Bearer " + token);
            }

            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonObject.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Contact info saved successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to save contact info."+responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while saving contact info.");
        }}

    private JSONObject getJsonObject() {
        String profileUrl = urlId.getText();
        String phoneNumber = numberId.getText();
        String phoneTypeSelected = phoneType.getValue();
        String month = monthId.getValue();
        int day = dayId.getValue();
        String visibility = visibilityId.getValue();
        String address = addrId.getText();
        String instantMessaging = instantMessage.getText();

        // Create JSON object to send to the server
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("profileUrl", profileUrl);
        jsonObject.put("phoneNumber", phoneNumber);
        jsonObject.put("phoneType", phoneTypeSelected);
        jsonObject.put("month", month);
        jsonObject.put("day", day);
        jsonObject.put("visibility", visibility);
        jsonObject.put("address", address);
        jsonObject.put("instantMessaging", instantMessaging);
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
        /////set the email text field
//        String token = JWT.validateToken(TokenManager.getToken());
//        emailId.setText(token);
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
}
