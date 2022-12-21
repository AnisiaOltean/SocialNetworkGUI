package com.example.socialnetworkgui.domain;

public class UserMessageDTO {
    private Long idM;
    private String firstName;
    private String lastName;
    private String text;
    private String sentAt;

    public UserMessageDTO(Long idM, String firstName, String lastName, String text, String sentAt) {
        this.idM = idM;
        this.firstName = firstName;
        this.lastName = lastName;
        this.text = text;
        this.sentAt = sentAt;
    }

    public Long getIdM() {
        return idM;
    }

    public void setIdM(Long idM) {
        this.idM = idM;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSentAt() {
        return sentAt;
    }

    public void setSentAt(String sentAt) {
        this.sentAt = sentAt;
    }

    @Override
    public String toString() {
        return "UserMessageDTO{" +
                "idM=" + idM +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", text='" + text + '\'' +
                ", sentAt='" + sentAt + '\'' +
                '}';
    }
}
