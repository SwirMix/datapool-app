package org.datapool.dto.api.internal;

import java.util.Date;

public class CacheMetadata {
    private String id;
    private String project;
    private String name;
    private String status;
    private int rowCount;
    private Date createDate;
    private String creator;
    private Date lastUpdateDate;
    private String lastUser;

    public String getId() {
        return id;
    }

    public CacheMetadata setId(String id) {
        this.id = id;
        return this;
    }

    public String getProject() {
        return project;
    }

    public CacheMetadata setProject(String project) {
        this.project = project;
        return this;
    }

    public String getName() {
        return name;
    }

    public CacheMetadata setName(String name) {
        this.name = name;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public CacheMetadata setStatus(String status) {
        this.status = status;
        return this;
    }

    public int getRowCount() {
        return rowCount;
    }

    public CacheMetadata setRowCount(int rowCount) {
        this.rowCount = rowCount;
        return this;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public CacheMetadata setCreateDate(Date createDate) {
        this.createDate = createDate;
        return this;
    }

    public String getCreator() {
        return creator;
    }

    public CacheMetadata setCreator(String creator) {
        this.creator = creator;
        return this;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public CacheMetadata setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
        return this;
    }

    public String getLastUser() {
        return lastUser;
    }

    public CacheMetadata setLastUser(String lastUser) {
        this.lastUser = lastUser;
        return this;
    }
}
