package org.datapool.dto.db;

import com.google.gson.internal.LinkedTreeMap;

public class CacheLogEntity {
    private String type;
    private boolean success;
    private String userEmail;
    private String message;
    private LinkedTreeMap props;

    public LinkedTreeMap getProps() {
        return props;
    }

    public CacheLogEntity setProps(LinkedTreeMap props) {
        this.props = props;
        return this;
    }

    public String getType() {
        return type;
    }

    public CacheLogEntity setType(String type) {
        this.type = type;
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public CacheLogEntity setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public CacheLogEntity setUserEmail(String userEmail) {
        this.userEmail = userEmail;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public CacheLogEntity setMessage(String message) {
        this.message = message;
        return this;
    }
}
