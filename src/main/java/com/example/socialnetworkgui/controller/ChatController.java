package com.example.socialnetworkgui.controller;


import com.example.socialnetworkgui.domain.Message;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.UserMessageDTO;
import com.example.socialnetworkgui.domain.exceptions.EntityAlreadyFound;
import com.example.socialnetworkgui.domain.exceptions.ValidationException;
import com.example.socialnetworkgui.service.ServiceGUI;
import com.example.socialnetworkgui.service.ServiceMessage;
import com.example.socialnetworkgui.utils.events.MessageEntityChangeEvent;
import com.example.socialnetworkgui.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.example.socialnetworkgui.utils.Constants.DATE_TIME_FORMATTER;

public class ChatController implements Observer<MessageEntityChangeEvent> {
    private ServiceGUI serviceGUI;
    private ServiceMessage serviceMessage;

    private User conversationPartner;

    @FXML
    private TableColumn<UserMessageDTO, String> fnameColumn;

    @FXML
    private TableColumn<UserMessageDTO, String> lNameColumn;

    @FXML
    private TableView<UserMessageDTO> messageTable;

    @FXML
    private TableColumn<UserMessageDTO, String> sentColumn;

    @FXML
    private TableColumn<UserMessageDTO, String> textColumn;

    @FXML
    private TextField messageTextField;

    @FXML
    private Label messageToLbl;


    @FXML
    private Button sendMessageBtn;


    ObservableList<UserMessageDTO> model= FXCollections.observableArrayList();

    public void setService(ServiceGUI serviceGUI, ServiceMessage serviceMessage, User conversationPartner){
        this.serviceGUI=serviceGUI;
        this.serviceMessage=serviceMessage;
        this.conversationPartner= conversationPartner;
        this.serviceMessage.addObserver(this);
        initModel();
    }

    public void initialize(){
        messageTable.setPlaceholder(new Text("Start conversation"));
        fnameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        textColumn.setCellValueFactory(new PropertyValueFactory<>("text"));
        sentColumn.setCellValueFactory(new PropertyValueFactory<>("sentAt"));

        messageTable.setItems(model);
        messageTable.setFixedCellSize(30);
    }
    private void initModel(){
        messageToLbl.setText("Message To: "+ conversationPartner.getFirstName()+" "+conversationPartner.getLastName());

        Iterable<Message> all = serviceMessage.getAllMessages();
        Predicate<Message> p1= m-> m.getId1().equals(serviceGUI.getLoggedUser().getId())&&m.getId2().equals(conversationPartner.getId());
        Predicate<Message> p2= m-> m.getId2().equals(serviceGUI.getLoggedUser().getId())&&m.getId1().equals(conversationPartner.getId());

        List<Message> allM= StreamSupport.stream(all.spliterator(), false).filter(p1.or(p2)).collect(Collectors.toList());
        allM.sort(Comparator.comparing(Message::getSentAt));
        List<UserMessageDTO> msgs= allM.stream().map(x-> new UserMessageDTO(x.getId(),
                serviceGUI.getWithId(x.getId1()).getFirstName(), serviceGUI.getWithId(x.getId1()).getLastName(), x.getText(),
                x.getSentAt().format(DATE_TIME_FORMATTER))).collect(Collectors.toList());
        model.setAll(msgs);
    }

    public void sendMessage(ActionEvent actionEvent) {
        String messageText= messageTextField.getText();
        Long id1= serviceGUI.getLoggedUser().getId();
        Long id2= conversationPartner.getId();

        Predicate<User> getWithId= u-> u.getId().equals(id2);

        if(serviceGUI.getLoggedUser().getFriends().stream().filter(getWithId).findFirst().isEmpty()){
            MessageAlert.showErrorMessage(null, "Partenerul nu exista!");
            return;
        }
        try{
            serviceMessage.addMessage(id1, id2, messageText);
            messageTextField.clear();
        }catch (ValidationException| IllegalArgumentException| EntityAlreadyFound e){
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    @Override
    public void update(MessageEntityChangeEvent messageEntityChangeEvent) {
        initModel();
    }
}
