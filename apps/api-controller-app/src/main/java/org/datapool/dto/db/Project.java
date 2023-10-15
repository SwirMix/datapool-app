package org.datapool.dto.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    private String id;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private Date created;
    @Column
    private boolean active;

    public boolean isActive() {
        return active;
    }

    public Project setActive(boolean active) {
        this.active = active;
        return this;
    }

    public String getId() {
        return id;
    }

    public Project setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Project setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Project setDescription(String description) {
        this.description = description;
        return this;
    }

    public Date getCreated() {
        return created;
    }

    public Project setCreated(Date created) {
        this.created = created;
        return this;
    }
}
