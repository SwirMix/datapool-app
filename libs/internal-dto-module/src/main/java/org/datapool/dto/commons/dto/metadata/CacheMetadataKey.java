package org.datapool.dto.commons.dto.metadata;

public class CacheMetadataKey {
    private String cacheName = "";
    private String project = "";

    public CacheMetadataKey(){

    }
    public CacheMetadataKey(String cacheName, String project){
        this.cacheName = cacheName;
        this.project = project;
    }

    public String getPublicCacheName(){
        return project + "_" + cacheName;
    }

    public String getCacheName() {
        return cacheName;
    }

    public CacheMetadataKey setCacheName(String cacheName) {
        this.cacheName = cacheName;
        return this;
    }

    public String getProject() {
        return project;
    }

    public CacheMetadataKey setProject(String project) {
        this.project = project;
        return this;
    }
}
