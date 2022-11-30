package com.example.socialnetworkgui;

import com.example.socialnetworkgui.controller.UserController;
import com.example.socialnetworkgui.domain.Friendship;
import com.example.socialnetworkgui.domain.Pair;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.validators.UserValidator;
import com.example.socialnetworkgui.domain.validators.Validator;
import com.example.socialnetworkgui.repo.Repository;
import com.example.socialnetworkgui.repo.db.UserDbRepo;
import com.example.socialnetworkgui.service.ServiceGUI;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    ServiceGUI service;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        Validator<User> validator= new UserValidator();
        UserDbRepo uRepo= new UserDbRepo(validator,"jdbc:postgresql://localhost:5432/laborator", "postgres", "postgres");
        service= new ServiceGUI(uRepo);
        initView(primaryStage);
        primaryStage.setWidth(800);
        primaryStage.show();
    }

    private void initView(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/user-view.fxml"));

        AnchorPane userLayout = fxmlLoader.load();
        primaryStage.setScene(new Scene(userLayout));

        UserController userController = fxmlLoader.getController();
        userController.setService(service);
    }
}