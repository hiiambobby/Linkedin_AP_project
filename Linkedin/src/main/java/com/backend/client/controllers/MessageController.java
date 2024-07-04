package com.backend.client.controllers;

import com.backend.client.components.ProfileViewComponent;
import com.backend.client.components.ProfileViewPv;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MessageController implements Initializable {
    @FXML
    private ImageView networkId;

    @FXML
    private VBox viewConnections;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showConnections();
    }

    private void showConnections() {
        try {
            List<JSONObject> requests = findConnections();
            if (requests != null && !requests.isEmpty()) {
                List<JSONObject> requestDetails = fetchPrimaryInfoForRequests(requests);
                displaySearchResults(requestDetails);
            }
        } catch (IOException e) {
            setAlert.showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while fetching connection requests.");
            e.printStackTrace();
        }
    }


    public void profileView(MouseEvent mouseEvent) throws IOException {
        openNewStage("/fxml/Profile.fxml", "Profile");
    }



    public void openNetwork(MouseEvent mouseEvent) throws IOException {
        openNewStage("/fxml/Network.fxml", "My Network");
    }

    public void openSearch(MouseEvent mouseEvent) throws IOException {
        openNewStage("/fxml/Search.fxml", "Search");
    }

    private void openNewStage(String fxmlPath, String title) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Stage currentStage = (Stage) networkId.getScene().getWindow();
        currentStage.close();
        Stage newStage = new Stage();
        Image icon = new Image("/img/photo_2024-05-15_16-05-20.jpg");
        newStage.getIcons().add(icon);
        Scene newScene = new Scene(root);
        newStage.setScene(newScene);
        newStage.setTitle(title);
        newStage.initStyle(StageStyle.DECORATED);
        newStage.show();
    }

    //we should first find all the connections emails
    public List<JSONObject> findConnections() throws IOException {
        String urlString = String.format("http://localhost:8000/connect?user=%s&connected=%s",
                URLEncoder.encode(readEmail(), "UTF-8"), URLEncoder.encode("type", "UTF-8"));
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONArray jsonResponse = new JSONArray(response.toString());
            List<JSONObject> requestList = new ArrayList<>();
            for (int i = 0; i < jsonResponse.length(); i++) {
                requestList.add(jsonResponse.getJSONObject(i));
            }
            return requestList;
        } else {
            System.err.println("Failed to get connect requests, Response code: " + responseCode);
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            StringBuilder errorResponse = new StringBuilder();
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                errorResponse.append(errorLine);
            }
            errorReader.close();
            System.err.println("Error response: " + errorResponse);
        }

        conn.disconnect();
        return null;
    }



    private List<JSONObject> fetchPrimaryInfoForRequests(List<JSONObject> requests) throws IOException {
        List<JSONObject> detailedRequests = new ArrayList<>();
        for (JSONObject request : requests) {
            String receiverEmail = request.optString("sender");
            JSONObject primaryInfo = getPrimaryInfoByEmail(receiverEmail);
            if (primaryInfo != null) {
                // Add the request note to the primary info
                primaryInfo.put("note", request.optString("note", "No note provided"));
                detailedRequests.add(primaryInfo);
            }
        }
        return detailedRequests;
    }

    private JSONObject getPrimaryInfoByEmail(String email) throws IOException {
        URL url = new URL("http://localhost:8000/primaryInfo?id=" + URLEncoder.encode(email, StandardCharsets.UTF_8.toString()));
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            return new JSONObject(response.toString());
        } else {
            System.err.println("Failed to retrieve primary info, Response code: " + responseCode);
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            StringBuilder errorResponse = new StringBuilder();
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                errorResponse.append(errorLine);
            }
            errorReader.close();
            System.err.println("Error response: " + errorResponse);
        }

        conn.disconnect();
        return null;
    }

    private void displaySearchResults(List<JSONObject> results) {
        viewConnections.getChildren().clear();
        for (JSONObject result : results) {
            Node profileView = connectListView(result);
            viewConnections.getChildren().add(profileView);
        }
    }

    private Node connectListView(JSONObject result) {

        String profilePicUrl = result.optString("profilePic", "/icons/icons8-male-user-48.png");
        String name = result.optString("firstName", "") + " " + result.optString("lastName", "");
        String headerTitle = result.optString("headTitle", "");
        return new ProfileViewPv(result,profilePicUrl, name, headerTitle);
    }


    public String readEmail() {
        String filePath = "userdata.txt"; // Path to your JSON file
        try {
            String jsonString = readJsonFile(filePath);
            JSONObject jsonObject = new JSONObject(jsonString);
            return jsonObject.optString("email", "Unknown");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String readJsonFile(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }


}
