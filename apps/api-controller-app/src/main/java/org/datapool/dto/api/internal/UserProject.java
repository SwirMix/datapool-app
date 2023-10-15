package org.datapool.dto.api.internal;

public class UserProject {
    private String id;
    private String name;
    private String description;
    private String permission;


    public String getId() {
        return id;
    }

    public UserProject setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserProject setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public UserProject setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getPermission() {
        return permission;
    }

    public UserProject setPermission(String permission) {
        this.permission = permission;
        return this;
    }
}
