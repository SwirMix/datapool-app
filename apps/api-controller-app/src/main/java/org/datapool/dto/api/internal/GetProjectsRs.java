package org.datapool.dto.api.internal;

import java.util.List;

public class GetProjectsRs {
    private List<UserProject> projects;

    public List<UserProject> getProjects() {
        return projects;
    }

    public GetProjectsRs setProjects(List<UserProject> projects) {
        this.projects = projects;
        return this;
    }
}
