package com.philimonnag.chatsapp.Model;

public class User {
    String personName,personEmail,personId,personPhoto;

    public User(String personName, String personEmail, String personId, String personPhoto) {
        this.personName = personName;
        this.personEmail = personEmail;
        this.personId = personId;
        this.personPhoto = personPhoto;
    }

    public User() {
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonEmail() {
        return personEmail;
    }

    public void setPersonEmail(String personEmail) {
        this.personEmail = personEmail;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getPersonPhoto() {
        return personPhoto;
    }

    public void setPersonPhoto(String personPhoto) {
        this.personPhoto = personPhoto;
    }
}
