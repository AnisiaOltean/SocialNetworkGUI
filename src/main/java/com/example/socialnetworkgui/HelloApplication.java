package com.example.socialnetworkgui;

import com.example.socialnetworkgui.controller.LoginController;
import com.example.socialnetworkgui.controller.UserController;
import com.example.socialnetworkgui.domain.*;
import com.example.socialnetworkgui.domain.validators.*;
import com.example.socialnetworkgui.repo.Repository;
import com.example.socialnetworkgui.repo.db.FriendshipDBRepository;
import com.example.socialnetworkgui.repo.db.MessageDbRepo;
import com.example.socialnetworkgui.repo.db.RequestDbRepo;
import com.example.socialnetworkgui.repo.db.UserDbRepo;
import com.example.socialnetworkgui.service.ServiceGUI;
import com.example.socialnetworkgui.service.ServiceMessage;
import com.example.socialnetworkgui.service.ServiceRequest;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    ServiceGUI service;
    ServiceRequest serviceRequest;

    ServiceMessage serviceMessage;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        Validator<User> validator = new UserValidator();
        UserDbRepo uRepo = new UserDbRepo(validator, "jdbc:postgresql://localhost:5432/laborator", "postgres", "postgres");
        Validator<Friendship> valF = new FriendshipValidator();
        FriendshipDBRepository fRepo = new FriendshipDBRepository("jdbc:postgresql://localhost:5432/laborator", "postgres", "postgres", valF);
        Validator<Request> valR = new RequestValidator();
        RequestDbRepo rRepo = new RequestDbRepo("jdbc:postgresql://localhost:5432/laborator", "postgres", "postgres", valR);
        Validator<Message> valM= new MessageValidator();
        MessageDbRepo mRepo= new MessageDbRepo("jdbc:postgresql://localhost:5432/laborator", "postgres", "postgres", valM);

        service = new ServiceGUI(uRepo, fRepo);
        serviceRequest = new ServiceRequest(uRepo, fRepo, rRepo);
        serviceMessage= new ServiceMessage(mRepo);
        initView(primaryStage);
        primaryStage.setTitle("LogIn page");
        primaryStage.setWidth(600);
        primaryStage.setHeight(427);
        primaryStage.show();
    }

    private void initView(Stage primaryStage) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/userView.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/loginView.fxml"));
        AnchorPane userLayout = fxmlLoader.load();
        primaryStage.setScene(new Scene(userLayout));

        //UserController userController = fxmlLoader.getController();
        LoginController loginController= fxmlLoader.getController();
        //userController.setService(service);
        loginController.setServiceGUI(service, serviceRequest, serviceMessage);
    }
}