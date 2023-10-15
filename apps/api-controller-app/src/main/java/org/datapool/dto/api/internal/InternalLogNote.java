package org.datapool.dto.api.internal;

import com.google.gson.internal.LinkedTreeMap;
import org.datapool.model.Message;

public class InternalLogNote {
    private String projectId;
    private String cacheName;
    private Message message;
    private LinkedTreeMap props;

    public String getProjectId() {
        return projectId;
    }

    public InternalLogNote setProjectId(String projectId) {
        this.projectId = projectId;
        return this;
    }

    public String getCacheName() {
        return cacheName;
    }

    public InternalLogNote setCacheName(String cacheName) {
        this.cacheName = cacheName;
        return this;
    }

    public Message getMessage() {
        return message;
    }

    public InternalLogNote setMessage(Message message) {
        this.message = message;
        return this;
    }

    public LinkedTreeMap getProps() {
        return props;
    }

    public InternalLogNote setProps(LinkedTreeMap props) {
        this.props = props;
        return this;
    }
}
