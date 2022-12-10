package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.exceptions.EntityAlreadyFound;
import com.example.socialnetworkgui.domain.exceptions.EntityNotFound;
import com.example.socialnetworkgui.domain.exceptions.ValidationException;
import com.example.socialnetworkgui.service.ServiceGUI;
import com.example.socialnetworkgui.service.ServiceRequest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AddView {

    ServiceGUI serviceGUI;
    ServiceRequest serviceRequest;

    private ObservableList<User> model= FXCollections.observableArrayList();

    @FXML
    private TextField searchLbl;

    @FXML
    private TableView<User> usersTable;

    @FXML
    private TableColumn<User, String> emailColumn;

    @FXML
    private TableColumn<User, String> firstNameColumn;

    @FXML
    private TableColumn<User, String> lastNameColumn;

    @FXML
    private Button addBtn;

    public void setServiceGUI(ServiceGUI serviceGUI, ServiceRequest serviceRequest){
        this.serviceGUI=serviceGUI;
        this.serviceRequest= serviceRequest;
        initModel();
    }

    private void initModel(){
        //Iterable<User> allU= serviceGUI.getAllUsers();
        //model.setAll(StreamSupport.stream(allU.spliterator(), false).collect(Collectors.toList()));
        searchLbl.textProperty().addListener(o->handleSearch());
    }

    private void handleSearch(){
        String[] text = searchLbl.getText().split(" ");
        String nume1= text[0];
        String nume2;
        if(text.length>1)
            nume2= text[1];
        else nume2="";


        Predicate<User> p1= u-> u.getFirstName().toLowerCase().startsWith(nume1.toLowerCase());
        Predicate<User> p2= u-> u.getLastName().toLowerCase().startsWith(nume2.toLowerCase());
        Predicate<User> p3= u-> u.getFirstName().toLowerCase().startsWith(nume2.toLowerCase());
        Predicate<User> p4= u-> u.getLastName().toLowerCase().startsWith(nume1.toLowerCase());
        Iterable<User> allU= serviceGUI.getAllUsers();
        List<User> filtered= StreamSupport.stream(allU.spliterator(), false).filter(p1.and(p2).or(p3.and(p4))).collect(Collectors.toList());
        model.setAll(filtered);
    }

    @FXML
    private void initialize(){
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<User,String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        usersTable.setItems(model);
    }

    public void addFriend(ActionEvent actionEvent) {
        try{
            User selected= usersTable.getSelectionModel().getSelectedItem();
            Long id1= serviceGUI.getLoggedUser().getId();
            Long id2= selected.getId();
            serviceGUI.addFriendship(id1, id2);
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", "Added friendship!");
        } catch (EntityNotFound| EntityAlreadyFound| NullPointerException e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    public void addRequest(ActionEvent actionEvent) {
        try{
            User selected= usersTable.getSelectionModel().getSelectedItem();
            Long id1= serviceGUI.getLoggedUser().getId();
            Long id2= selected.getId();
            serviceRequest.sendRequest(id1, id2);
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", "Sent friend request!");

            //close window
            Node source= (Node) actionEvent.getSource();
            Stage stage= (Stage) source.getScene().getWindow();
            stage.close();
        }catch (EntityNotFound | EntityAlreadyFound | NullPointerException | ValidationException e){
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }
}
