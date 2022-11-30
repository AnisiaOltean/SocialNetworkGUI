package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.service.ServiceGUI;
import com.example.socialnetworkgui.utils.events.UserEntityChangeEvent;
import com.example.socialnetworkgui.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserController implements Observer<UserEntityChangeEvent>{

    ServiceGUI service;
    ObservableList<User> model= FXCollections.observableArrayList();

    @FXML
    public TableView<User> tableView;
    @FXML
    public TableColumn<User, Long> idColumn;

    @FXML
    public TableColumn<User, String> fnameColumn;


    @FXML
    public TableColumn<User,String> lnameColumn;

    public void setService(ServiceGUI service){
        this.service= service;
        service.addObserver(this);
        initModel();
    }

    @FXML
    public void initialize(){
        fnameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        lnameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        tableView.setItems(model);
    }

    private void initModel() {
        Iterable<User> allUsers = service.getAll();
        List<User> users = StreamSupport.stream(allUsers.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(users);
    }

    @Override
    public void update(UserEntityChangeEvent userEntityChangeEvent) {

        initModel();
    }
}
