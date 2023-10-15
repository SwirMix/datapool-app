package org.datapool.dto;

public class SaveToken {
    private String token;
    private String project;

    public String getToken() {
        return token;
    }

    public SaveToken setToken(String token) {
        this.token = token;
        return this;
    }

    public String getProject() {
        return project;
    }

    public SaveToken setProject(String project) {
        this.project = project;
        return this;
    }
}
