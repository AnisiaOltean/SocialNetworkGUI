package com.example.socialnetworkgui.service;

import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.repo.Repository;
import com.example.socialnetworkgui.utils.events.UserEntityChangeEvent;
import com.example.socialnetworkgui.utils.observer.Observable;
import com.example.socialnetworkgui.utils.observer.Observer;

import java.util.ArrayList;
import java.util.List;

public class ServiceGUI implements Observable<UserEntityChangeEvent> {
    private Repository<Long, User> repo;
    private List<Observer<UserEntityChangeEvent>> observers= new ArrayList<>();

    public ServiceGUI(Repository<Long, User> repo) {
        this.repo = repo;
    }

    @Override
    public void addObserver(Observer<UserEntityChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<UserEntityChangeEvent> e) {
        //observers.remove(e);
    }

    @Override
    public void notifyObservers(UserEntityChangeEvent t) {
        observers.stream().forEach(x->x.update(t));
    }

    public Iterable<User> getAll(){
        return repo.findAll();
    }
}
