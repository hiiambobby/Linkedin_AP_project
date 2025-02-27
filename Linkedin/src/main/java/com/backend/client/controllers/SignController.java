package com.backend.client.controllers;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 *
 * @author Admin
 */
public class SignController implements Initializable {

    @FXML
    private VBox vbox;
    private Parent fxml;


    @FXML
    private Button signInButton;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TranslateTransition t = new TranslateTransition(Duration.seconds(1), vbox);
        t.setToX(vbox.getLayoutX() * 20);
        t.play();
        t.setOnFinished((e) ->{
            try{
                fxml = FXMLLoader.load(getClass().getResource("/fxml/SignIn.fxml"));
                vbox.getChildren().removeAll();
                vbox.getChildren().setAll(fxml);
            }catch(IOException ex){

            }
        });
    }
    @FXML
    private void open_signin(ActionEvent event){
        TranslateTransition t = new TranslateTransition(Duration.seconds(1), vbox);
        t.setToX(vbox.getLayoutX() * 20);
        t.play();
        t.setOnFinished((e) ->{
            try{
                fxml = FXMLLoader.load(getClass().getResource("/fxml/SignIn.fxml"));
                vbox.getChildren().removeAll();
                vbox.getChildren().setAll(fxml);
            }catch(IOException ex){

            }
        });
    }
    @FXML
    private void open_signup(ActionEvent event){
        TranslateTransition t = new TranslateTransition(Duration.seconds(1), vbox);
        t.setToX(0);
        t.play();
        t.setOnFinished((e) ->{
            try{
                fxml = FXMLLoader.load(getClass().getResource("/fxml/SignUp.fxml"));
                vbox.getChildren().removeAll();
                vbox.getChildren().setAll(fxml);
            }catch(IOException ex){

            }
        });
    }

    @FXML
    void exitSelected(ActionEvent event) {
        System.exit(0);
    }


}