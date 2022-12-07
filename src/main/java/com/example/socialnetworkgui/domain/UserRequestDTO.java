package com.example.socialnetworkgui.domain;

public class UserRequestDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String sentAt;
    private String status;

    public UserRequestDTO(Long id, String firstName, String lastName, String sentAt, String status) {
        this.id=id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.sentAt = sentAt;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getSentAt() {
        return sentAt;
    }

    public void setSentAt(String sentAt) {
        this.sentAt = sentAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UserRequestDTO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", sentAt='" + sentAt + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
