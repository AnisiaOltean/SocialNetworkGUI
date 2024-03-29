package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.Friendship;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.exceptions.EntityNotFound;
import com.example.socialnetworkgui.service.ServiceGUI;
import com.example.socialnetworkgui.service.ServiceMessage;
import com.example.socialnetworkgui.service.ServiceRequest;
import com.example.socialnetworkgui.utils.events.FriendshipEntityChangeEvent;
import com.example.socialnetworkgui.utils.events.UserEntityChangeEvent;
import com.example.socialnetworkgui.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserController implements Observer<FriendshipEntityChangeEvent>{

    ServiceGUI service;
    ServiceRequest serviceRequest;

    ServiceMessage serviceMessage;
    ObservableList<User> model= FXCollections.observableArrayList();

    @FXML
    private AnchorPane userView;

    @FXML
    private Button chatBtn;

    @FXML
    public TableView<User> tableView;

    @FXML
    private TableColumn<User, String> emailColumn;

    @FXML
    public TableColumn<User, String> fnameColumn;


    @FXML
    public TableColumn<User,String> lnameColumn;

    @FXML
    private Label loggedLabel;

    @FXML
    private Button addFriendBtn;

    @FXML
    private Button removeFriendBtn;

    @FXML
    private Button requestsBtn;
    @FXML
    private Button logOutBtn;

    public void setService(ServiceGUI service, ServiceRequest serviceRequest, ServiceMessage serviceMessage){
        this.service= service;
        this.serviceRequest=serviceRequest;
        this.serviceMessage=serviceMessage;
        //this.stage=stage;
        service.addObserver(this);
        initModel();
    }

    @FXML
    public void initialize(){
        fnameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        lnameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        tableView.setItems(model);
    }

    private void initModel() {
        Iterable<User> allUsers = service.getLoggedUser().getFriends();
        List<User> users = StreamSupport.stream(allUsers.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(users);
        loggedLabel.setText("Logged in as: "+ service.getLoggedUser().getFirstName()+" "+service.getLoggedUser().getLastName());
    }

    @Override
    public void update(FriendshipEntityChangeEvent friendshipEntityChangeEvent) {
        initModel();
    }

    public void handleAddFriend(ActionEvent actionEvent) throws IOException {
        Stage addUserStage= new Stage();
        addUserStage.setTitle("Add friend");
        FXMLLoader loader= new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/addView.fxml"));
        AnchorPane layout= loader.load();

        Scene scene= new Scene(layout);
        addUserStage.setScene(scene);
        AddView ctrl= loader.getController();
        ctrl.setServiceGUI(service, serviceRequest);

        addUserStage.show();
    }

    public void handleRemove(ActionEvent actionEvent) {
        try{
            Long id1= service.getLoggedUser().getId();
            User selected= tableView.getSelectionModel().getSelectedItem();
            Long id2= selected.getId();
            service.removeFriendship(id1, id2);
            serviceRequest.deleteRequest(id1, id2);
            serviceMessage.deleteConversation(id1, id2);
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", "Deleted friendship!");
        }catch (EntityNotFound | NullPointerException e){
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    public void handleRequests(ActionEvent actionEvent) throws IOException {
        Stage stage= new Stage();
        stage.setTitle("Requests page");
        FXMLLoader loader= new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/requestsView.fxml"));

        AnchorPane layout= loader.load();
        Scene scene= new Scene(layout);
        stage.setScene(scene);
        RequestsController ctrl= loader.getController();
        ctrl.setService(serviceRequest, service);
        stage.show();
    }

    public void handleLogOut(ActionEvent actionEvent) throws IOException {
        service.logOut();
        //stage.close();

        //go back to login page
//        Stage stage= new Stage();
//        stage.setTitle("Login page");
//        FXMLLoader loader= new FXMLLoader();
//        loader.setLocation(getClass().getResource("/views/loginView.fxml"));
//        AnchorPane layout= loader.load();
//        Scene scene= new Scene(layout);
//        stage.setScene(scene);
//        //LoginController ctrl= loader.getController();
//        //ctrl.setServiceGUI(service, serviceRequest);
//        stage.show();


        //go back to login page
        FXMLLoader loader= new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/loginView.fxml"));
        Parent root= loader.load();
        Stage stage= (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene= new Scene(root);
        LoginController ctrl= loader.getController();
        ctrl.setServiceGUI(service, serviceRequest, serviceMessage);

        stage.setWidth(600);
        stage.setHeight(427);
        stage.setScene(scene);
        stage.show();
    }

    public void handleChat(ActionEvent actionEvent) throws IOException {
        User selected= tableView.getSelectionModel().getSelectedItem();
        if(selected==null){
            MessageAlert.showErrorMessage(null, "Nu ati selectat prietenul!");
            return;
        }

        FXMLLoader loader= new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/chatView.fxml"));
        Parent root= loader.load();
        Stage stage= new Stage();
        stage.setTitle("Chat page");
        Scene scene= new Scene(root);
        stage.setScene(scene);
        ChatController ctrl= loader.getController();
        ctrl.setService(service, serviceMessage, selected);
        stage.show();
    }
}
