package com.example.socialnetworkgui.controller;

import com.example.socialnetworkgui.domain.Request;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.UserRequestDTO;
import com.example.socialnetworkgui.domain.exceptions.EntityNotFound;
import com.example.socialnetworkgui.domain.exceptions.ValidationException;
import com.example.socialnetworkgui.service.ServiceGUI;
import com.example.socialnetworkgui.service.ServiceRequest;
import com.example.socialnetworkgui.utils.events.RequestEntityChangeEvent;
import com.example.socialnetworkgui.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.example.socialnetworkgui.utils.Constants.DATE_TIME_FORMATTER;

public class SentRequestsController implements Observer<RequestEntityChangeEvent> {
    @FXML
    private Button backBtn;

    @FXML
    private Button cancelReqBtn;

    @FXML
    private TableColumn<UserRequestDTO, String> firstNameTo;

    @FXML
    private TableColumn<UserRequestDTO, User> lastNameTo;

    @FXML
    private TableColumn<UserRequestDTO, User> sentAt;

    @FXML
    private TableColumn<UserRequestDTO, User> status;

    @FXML
    private TableView<UserRequestDTO> tableTo;

    ServiceRequest serviceRequest;
    ServiceGUI serviceGUI;

    ObservableList<UserRequestDTO> model= FXCollections.observableArrayList();

    public void setServiceGUI(ServiceGUI serviceGUI, ServiceRequest serviceRequest){
        this.serviceGUI= serviceGUI;
        this.serviceRequest= serviceRequest;
        serviceRequest.addObserver(this);
        initModel();
    }

    @FXML
    public void initialize(){
        firstNameTo.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameTo.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        sentAt.setCellValueFactory(new PropertyValueFactory<>("sentAt"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));

        tableTo.setItems(model);
    }

    private void initModel(){
        Iterable<Request> allReq= serviceRequest.getAllRequests();
        Predicate<Request> r= req-> req.getId().getFirst().equals(serviceGUI.getLoggedUser().getId());

        List<Request> allR= StreamSupport.stream(allReq.spliterator(), false).filter(r).collect(Collectors.toList());
        List<UserRequestDTO> all= allR.stream().map(x-> new UserRequestDTO(x.getId().getSecond(), serviceRequest.getWithId(x.getId().getSecond()).getFirstName(), serviceRequest.getWithId(x.getId().getSecond()).getLastName(),
                x.getSentAt().format(DATE_TIME_FORMATTER), x.getStatus().toString())).collect(Collectors.toList());
        model.setAll(all);
    }

    @Override
    public void update(RequestEntityChangeEvent requestEntityChangeEvent) {
        initModel();
    }

    public void backToReceivedRequests(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader= new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/requestsView.fxml"));

        Parent root= loader.load();
        Stage stage= (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene= new Scene(root);
        stage.setTitle("Received requests");
        //stage.setWidth(615);
        //stage.setHeight(400);
        stage.setScene(scene);
        RequestsController ctrl= loader.getController();
        ctrl.setService(serviceRequest, serviceGUI);

        stage.show();
    }

    public void cancelRequest(ActionEvent actionEvent) {
        UserRequestDTO req= tableTo.getSelectionModel().getSelectedItem();
        if(!Objects.equals(req.getStatus(), "SENT")){
            MessageAlert.showErrorMessage(null, "Cererea trebuie sa fie SENT");
            return;
        }
        Long id1= serviceGUI.getLoggedUser().getId();
        Long id2= req.getId();

        try{
            serviceRequest.deleteRequest(id1, id2);
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", "Cancelled request!");

            Stage stage= (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.close();
        }catch (EntityNotFound| ValidationException|IllegalArgumentException e){
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }
}
