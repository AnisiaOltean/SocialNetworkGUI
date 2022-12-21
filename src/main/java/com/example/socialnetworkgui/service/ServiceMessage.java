package com.example.socialnetworkgui.service;

import com.example.socialnetworkgui.domain.Message;
import com.example.socialnetworkgui.domain.exceptions.EntityAlreadyFound;
import com.example.socialnetworkgui.domain.exceptions.EntityNotFound;
import com.example.socialnetworkgui.repo.Repository;
import com.example.socialnetworkgui.repo.db.MessageDbRepo;
import com.example.socialnetworkgui.utils.events.MessageEntityChangeEvent;
import com.example.socialnetworkgui.utils.observer.Observable;
import com.example.socialnetworkgui.utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceMessage implements Observable<MessageEntityChangeEvent> {
    private Repository<Long, Message> repoM;

    private List<Observer<MessageEntityChangeEvent>> observerList= new ArrayList<>();

    public ServiceMessage(Repository<Long, Message> repoM) {
        this.repoM = repoM;
    }

    private Long generateID(){
        Long maxID=0L;
        for(Message m : repoM.findAll()){
            if(m.getId()>maxID) maxID=m.getId();
        }
        return maxID+1;
    }

    public Iterable<Message> getAllMessages(){
        return repoM.findAll();
    }

    public void addMessage(Long id1, Long id2, String text){
        Message m= new Message(id1, id2, LocalDateTime.now(), text);
        m.setId(this.generateID());
        Message gasit= repoM.save(m);
        if(gasit!=null&&gasit.getSentAt().equals(m.getSentAt())) throw new EntityAlreadyFound("Message Already exists!");
        notifyObservers(new MessageEntityChangeEvent(m, gasit));
    }

    public void deleteMessage(Long idMessage){
        Message gasit= repoM.delete(idMessage);
        if(gasit==null) throw new EntityNotFound("Message does not exist!");
        else{
            notifyObservers(new MessageEntityChangeEvent(gasit, null));
        }
    }

    public Message getWithId(Long idMessage){
        return repoM.findOne(idMessage);
    }

    @Override
    public void addObserver(Observer<MessageEntityChangeEvent> e) {
        this.observerList.add(e);
    }

    @Override
    public void removeObserver(Observer<MessageEntityChangeEvent> e) {
        //this.observerList.remove(e);
    }

    @Override
    public void notifyObservers(MessageEntityChangeEvent t) {
        this.observerList.forEach(e-> e.update(t));
    }
}
