package org.datapool.dto.storage;

public class CsvToCache {
    private String cacheName;
    private String project;
    private long startId;
    private long endId;
    private String fileId;

    public String getCacheName() {
        return cacheName;
    }

    public CsvToCache setCacheName(String cacheName) {
        this.cacheName = cacheName;
        return this;
    }

    public String getProject() {
        return project;
    }

    public CsvToCache setProject(String project) {
        this.project = project;
        return this;
    }

    public long getStartId() {
        return startId;
    }

    public CsvToCache setStartId(long startId) {
        this.startId = startId;
        return this;
    }

    public long getEndId() {
        return endId;
    }

    public CsvToCache setEndId(long endId) {
        this.endId = endId;
        return this;
    }

    public String getFileId() {
        return fileId;
    }

    public CsvToCache setFileId(String fileId) {
        this.fileId = fileId;
        return this;
    }
}
