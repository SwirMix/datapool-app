package org.datapool.dto;

import java.util.Date;

public class Passwd {
    private String password;
    private Date timestamp = new Date();

    public Date getTimestamp() {
        return timestamp;
    }

    public Passwd setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Passwd setPassword(String password) {
        this.password = password;
        return this;
    }
}
