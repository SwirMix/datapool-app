package org.datapool.dto.api.internal;

import java.util.List;

public class AuthRs {
    private String jwt;
    private String email;
    private String userId;
    private boolean globalAdmin = false;
    private List<UserProject> projects;

    public String getUserId() {
        return userId;
    }

    public AuthRs setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public AuthRs setEmail(String email) {
        this.email = email;
        return this;
    }

    public boolean isGlobalAdmin() {
        return globalAdmin;
    }

    public AuthRs setGlobalAdmin(boolean globalAdmin) {
        this.globalAdmin = globalAdmin;
        return this;
    }

    public String getJwt() {
        return jwt;
    }

    public AuthRs setJwt(String jwt) {
        this.jwt = jwt;
        return this;
    }

    public List getProjects() {
        return projects;
    }

    public AuthRs setProjects(List projects) {
        this.projects = projects;
        return this;
    }
}
