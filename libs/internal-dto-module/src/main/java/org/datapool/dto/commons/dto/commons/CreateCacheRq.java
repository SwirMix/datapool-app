package org.datapool.dto.commons.dto.commons;

public class CreateCacheRq {
    protected String id;
    protected String projectId;
    protected String name;
    private String cacheName;

    public String getCacheName() {
        return cacheName;
    }

    public CreateCacheRq setCacheName(String cacheName) {
        this.cacheName = cacheName;
        return this;
    }

    private String fileName;
    private long createDate;
    private String[] columns;

    public String getId() {
        return id;
    }

    public CreateCacheRq setId(String id) {
        this.id = id;
        return this;
    }

    public String getProjectId() {
        return projectId;
    }

    public CreateCacheRq setProjectId(String projectId) {
        this.projectId = projectId;
        return this;
    }

    public String getName() {
        return name;
    }

    public CreateCacheRq setName(String name) {
        this.name = name;
        return this;
    }
}
