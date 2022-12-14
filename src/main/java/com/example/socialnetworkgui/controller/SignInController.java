package com.example.socialnetworkgui.controller;
import com.example.socialnetworkgui.domain.exceptions.EntityAlreadyFound;
import com.example.socialnetworkgui.domain.exceptions.EntityNotFound;
import com.example.socialnetworkgui.domain.exceptions.ValidationException;
import com.example.socialnetworkgui.service.ServiceGUI;
import com.example.socialnetworkgui.service.ServiceRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SignInController {

    @FXML
    private Button btnBack;

    @FXML
    private TextField emailText;

    @FXML
    private TextField firstNameText;

    @FXML
    private TextField lastNameText;

    @FXML
    private PasswordField passwordText;

    @FXML
    private Button signinBtn;

    private ServiceGUI serviceGUI;

    private ServiceRequest serviceRequest;

    @FXML
    public void initialize(){
        firstNameText.setPromptText("First Name");
        lastNameText.setPromptText("Last Name");
        emailText.setPromptText("adress@email.com");
        passwordText.setPromptText("******");
    }

    public void setServiceGUI(ServiceGUI serviceGUI, ServiceRequest serviceRequest){
        this.serviceGUI=serviceGUI;
        this.serviceRequest= serviceRequest;
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

    public void handleSignIn(ActionEvent event) {
        String firstName= firstNameText.getText();
        String lastName= lastNameText.getText();
        String email= emailText.getText();
        String password= passwordText.getText();

        try{
            serviceGUI.addUser(firstName, lastName, email, password);
            serviceGUI.logIn(email);
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", "Account created!");
            showUserDialog();

            //close current window and log into account
            Stage thisStage= (Stage) signinBtn.getScene().getWindow();
            thisStage.close();

            //stage.close();  //inchid fereastra de create acount si ma loghez
        }catch (IOException | EntityAlreadyFound | EntityNotFound | ValidationException e){
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    public void handleBack(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader= new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/loginView.fxml"));
        Parent root= loader.load();

        Stage stage= (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene= new Scene(root);
        stage.setWidth(600);
        stage.setHeight(427);
        stage.setScene(scene);
        LoginController ctrl= loader.getController();
        ctrl.setServiceGUI(serviceGUI, serviceRequest);

        stage.show();
    }
}
