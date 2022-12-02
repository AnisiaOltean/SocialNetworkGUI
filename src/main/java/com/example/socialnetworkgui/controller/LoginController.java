package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.exceptions.EntityNotFound;
import com.example.socialnetworkgui.service.ServiceGUI;
import com.example.socialnetworkgui.service.ServiceRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    ServiceGUI serviceGUI;
    ServiceRequest serviceRequest;
    @FXML
    private TextField emailField;

    @FXML
    private TextField firstNameField;

    @FXML
    private GridPane gridLayout;

    @FXML
    private TextField lastNameField;

    @FXML
    private Button logInBtn;

    public void setServiceGUI(ServiceGUI serviceGUI, ServiceRequest serviceRequest){
        this.serviceGUI= serviceGUI;
        this.serviceRequest=serviceRequest;
    }

    @FXML
    public void initialize(){
        firstNameField.textProperty().addListener(o-> handleChange());
        lastNameField.textProperty().addListener(o-> handleChange());
        emailField.textProperty().addListener(o->handleChange());
    }

    private void handleChange(){

    }

    public void handleLogIn(ActionEvent actionEvent) {
        String firstName= firstNameField.getText();
        String lastName= lastNameField.getText();
        String email= emailField.getText();

        try{
            serviceGUI.logIn(firstName, lastName,email);
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Succes", "V-ati logat cu succes!");
            showUserDialog();
        }catch (EntityNotFound e){
            MessageAlert.showErrorMessage(null, e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showUserDialog() throws IOException {
        Stage userStage= new Stage();
        userStage.setTitle("User page");
        FXMLLoader loader= new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/userView.fxml"));
        AnchorPane layout= loader.load();

        Scene scene= new Scene(layout);
        userStage.setScene(scene);
        UserController userController= loader.getController();
        userController.setService(serviceGUI, serviceRequest);

        userStage.show();
    }
}
