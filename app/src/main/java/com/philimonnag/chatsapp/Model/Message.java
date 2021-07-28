package com.philimonnag.chatsapp.Model;

public class Message {
    String message,senderEmail,timeStamp;

    public Message(String message, String senderEmail, String timeStamp) {
        this.message = message;
        this.senderEmail = senderEmail;
        this.timeStamp = timeStamp;
    }

    public Message() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
