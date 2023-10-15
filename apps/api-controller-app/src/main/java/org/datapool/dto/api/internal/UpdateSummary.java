package org.datapool.dto.api.internal;

public class UpdateSummary {
    private String project;
    private String cacheName;
    private String summary;

    public String getCacheName() {
        return cacheName;
    }

    public UpdateSummary setCacheName(String cacheName) {
        this.cacheName = cacheName;
        return this;
    }

    public String getSummary() {
        return summary;
    }

    public UpdateSummary setSummary(String summary) {
        this.summary = summary;
        return this;
    }

    public String getProject() {
        return project;
    }

    public UpdateSummary setProject(String project) {
        this.project = project;
        return this;
    }
}
