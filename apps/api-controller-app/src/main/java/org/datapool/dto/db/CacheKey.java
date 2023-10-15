package org.datapool.dto.db;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class CacheKey implements Serializable {
    @Column(name = "project_id")
    private String projectId;
    @Column(name = "cache_name")
    private String cacheName;

    public CacheKey(String projectId, String cacheName){
        this.cacheName = cacheName;
        this.projectId = projectId;
    }
    public CacheKey(){

    }

    public String getProjectId() {
        return projectId;
    }

    public CacheKey setProjectId(String projectId) {
        this.projectId = projectId;
        return this;
    }

    public String getCacheName() {
        return cacheName;
    }

    public CacheKey setCacheName(String cacheName) {
        this.cacheName = cacheName;
        return this;
    }
}
