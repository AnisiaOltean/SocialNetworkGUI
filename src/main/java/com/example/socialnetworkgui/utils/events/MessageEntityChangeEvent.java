package com.example.socialnetworkgui.utils.events;

import com.example.socialnetworkgui.domain.Message;

public class MessageEntityChangeEvent implements Event{
    private Message data, oldData;

    public MessageEntityChangeEvent(Message data) {
        this.data = data;
    }

    public MessageEntityChangeEvent(Message data, Message oldData) {
        this.data = data;
        this.oldData = oldData;
    }

    public Message getData() {
        return data;
    }

    public void setData(Message data) {
        this.data = data;
    }

    public Message getOldData() {
        return oldData;
    }

    public void setOldData(Message oldData) {
        this.oldData = oldData;
    }
}
