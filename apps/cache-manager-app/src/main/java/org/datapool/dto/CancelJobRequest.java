package org.datapool.dto;

public class CancelJobRequest {
    private String jobId;
    private boolean dropCache;

    public String getJobId() {
        return jobId;
    }

    public CancelJobRequest setJobId(String jobId) {
        this.jobId = jobId;
        return this;
    }

    public boolean isDropCache() {
        return dropCache;
    }

    public CancelJobRequest setDropCache(boolean dropCache) {
        this.dropCache = dropCache;
        return this;
    }
}
