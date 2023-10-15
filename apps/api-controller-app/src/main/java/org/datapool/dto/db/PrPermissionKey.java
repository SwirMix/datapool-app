package org.datapool.dto.db;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class PrPermissionKey implements Serializable {
    @Column(name = "project_id")
    private String projectId;
    @Column(name = "user_id")
    private String userId;


    public PrPermissionKey(){

    }
    public PrPermissionKey(String projectId, String userId) {
        this.projectId = projectId;
        this.userId = userId;
    }

    public String getProjectId() {
        return projectId;
    }

    public PrPermissionKey setProjectId(String projectId) {
        this.projectId = projectId;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public PrPermissionKey setUserId(String userId) {
        this.userId = userId;
        return this;
    }
}