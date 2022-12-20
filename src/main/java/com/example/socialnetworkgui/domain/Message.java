package com.example.socialnetworkgui.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.example.socialnetworkgui.utils.Constants.DATE_TIME_FORMATTER;

public class Message extends Entity<Long>{
    private Long id1;
    private Long id2;
    private LocalDateTime sentAt;

    private String text;

    public Message(Long id1, Long id2, LocalDateTime sentAt, String text) {
        this.id1 = id1;
        this.id2 = id2;
        this.sentAt = sentAt;
        this.text=text;
    }

    public Long getId1() {
        return id1;
    }

    public void setId1(Long id1) {
        this.id1 = id1;
    }

    public Long getId2() {
        return id2;
    }

    public void setId2(Long id2) {
        this.id2 = id2;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id="+super.getId()+", "+
                "id1=" + id1 +
                ", id2=" + id2 +
                ", sentAt=" + sentAt.format(DATE_TIME_FORMATTER) +
                ", text="+ text+
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(super.getId(), message.getId())&&Objects.equals(id1, message.id1) && Objects.equals(id2, message.id2) && Objects.equals(sentAt, message.sentAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getId(), id1, id2, sentAt);
    }
}
