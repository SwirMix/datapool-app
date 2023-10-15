package org.datapool.dto.metadata;

import java.util.Date;

public class Message {
    private String message;
    private Date date = new Date();
    private boolean lastSuccess = false;

    public boolean isLastSuccess() {
        return lastSuccess;
    }

    public Message setLastSuccess(boolean lastSuccess) {
        this.lastSuccess = lastSuccess;
        return this;
    }

    public Message(String message){
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public Message setDate(Date date) {
        this.date = date;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Message setMessage(String message) {
        this.message = message;
        return this;
    }
}
