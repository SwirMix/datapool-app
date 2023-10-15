package org.datapool.dto.api.internal;

import org.datapool.dto.db.Role;

public class TeamMemberRequest {
    private String projectId;
    private String userId;
    private String email;
    private Role role;

    public String getEmail() {
        return email;
    }

    public TeamMemberRequest setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getProjectId() {
        return projectId;
    }

    public TeamMemberRequest setProjectId(String projectId) {
        this.projectId = projectId;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public TeamMemberRequest setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public Role getRole() {
        return role;
    }

    public TeamMemberRequest setRole(Role role) {
        this.role = role;
        return this;
    }
}
