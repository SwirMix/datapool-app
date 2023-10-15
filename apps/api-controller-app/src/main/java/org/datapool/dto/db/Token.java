package org.datapool.dto.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "tokens")
public class Token {
    @Column(name = "token")
    private String token;
    @Column(name = "project_id")
    private String projectId;
    @Id
    private String name;
    @Column
    private String creator;
    @Column(name = "create_date")
    private Date createDate = new Date();

    public String getToken() {
        return token;
    }

    public Token setToken(String token) {
        this.token = token;
        return this;
    }

    public String getProjectId() {
        return projectId;
    }

    public Token setProjectId(String projectId) {
        this.projectId = projectId;
        return this;
    }

    public String getName() {
        return name;
    }

    public Token setName(String name) {
        this.name = name;
        return this;
    }

    public String getCreator() {
        return creator;
    }

    public Token setCreator(String creator) {
        this.creator = creator;
        return this;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Token setCreateDate(Date createDate) {
        this.createDate = createDate;
        return this;
    }
}
