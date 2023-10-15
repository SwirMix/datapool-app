package org.datapool.dto;

public class ClusterStatus {
    private String name;
    private String path;
    private String status;

    public String getName() {
        return name;
    }

    public ClusterStatus setName(String name) {
        this.name = name;
        return this;
    }

    public String getPath() {
        return path;
    }

    public ClusterStatus setPath(String path) {
        this.path = path;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public ClusterStatus setStatus(String status) {
        this.status = status;
        return this;
    }
}
