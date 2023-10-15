package org.datapool.dto.db;

import java.io.Serializable;
import java.util.Date;

public class UserProjectPublicData implements Serializable {
    private String id;
    private String name;
    private String description;
    private boolean active;
    private String userId;
    private Role role;
    private Date created;

    public Date getCreated() {
        return created;
    }

    public UserProjectPublicData setCreated(Date created) {
        this.created = created;
        return this;
    }

    public UserProjectPublicData() {
    }

    public String getId() {
        return id;
    }

    public UserProjectPublicData setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserProjectPublicData setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public UserProjectPublicData setDescription(String description) {
        this.description = description;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public UserProjectPublicData setActive(boolean active) {
        this.active = active;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public UserProjectPublicData setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public Role getRole() {
        return role;
    }

    public UserProjectPublicData setRole(Role role) {
        this.role = role;
        return this;
    }
}
