package org.datapool.dto;

public class DataPoolToken {
    private String token;
    private String project;


    public String getProject() {
        return project;
    }

    public DataPoolToken setProject(String project) {
        this.project = project;
        return this;
    }

    public String getToken() {
        return token;
    }

    public DataPoolToken setToken(String token) {
        this.token = token;
        return this;
    }
}
