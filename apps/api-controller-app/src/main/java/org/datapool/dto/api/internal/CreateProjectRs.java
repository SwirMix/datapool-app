package org.datapool.dto.api.internal;

public class CreateProjectRs {
    private UserProject created;
    private UserProject[] projects;

    public UserProject getCreated() {
        return created;
    }

    public CreateProjectRs setCreated(UserProject created) {
        this.created = created;
        return this;
    }

    public UserProject[] getProjects() {
        return projects;
    }

    public CreateProjectRs setProjects(UserProject[] projects) {
        this.projects = projects;
        return this;
    }
}
