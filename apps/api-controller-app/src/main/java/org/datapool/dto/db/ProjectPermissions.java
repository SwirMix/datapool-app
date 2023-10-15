package org.datapool.dto.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "project_permissions")
public class ProjectPermissions {
    @Id
    private PrPermissionKey id;
    @Column
    private int role;

    public PrPermissionKey getId() {
        return id;
    }

    public ProjectPermissions setId(PrPermissionKey id) {
        this.id = id;
        return this;
    }

    public int getRole() {
        return role;
    }

    public ProjectPermissions setRole(int role) {
        this.role = role;
        return this;
    }
}
