package org.datapool.dto;

public class CreateTokenRq {
    private String name;
    private String projectId;

    public String getName() {
        return name;
    }

    public CreateTokenRq setName(String name) {
        this.name = name;
        return this;
    }

    public String getProjectId() {
        return projectId;
    }

    public CreateTokenRq setProjectId(String projectId) {
        this.projectId = projectId;
        return this;
    }
}
