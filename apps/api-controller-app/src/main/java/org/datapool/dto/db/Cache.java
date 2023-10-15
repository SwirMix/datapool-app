package org.datapool.dto.db;

import org.datapool.dto.metadata.CacheStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "caches")
public class Cache {
    @Id
    private CacheKey id;
    @Column
    private String summary = "Hello my friend!";
    @Column
    private String title = "";
    @Column
    private String conditions = "";
    @Column
    private String content = "[]";

    public String getTitle() {
        return title;
    }

    public Cache setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getConditions() {
        return conditions;
    }

    public Cache setConditions(String conditions) {
        this.conditions = conditions;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Cache setContent(String content) {
        this.content = content;
        return this;
    }

    public CacheKey getId() {
        return id;
    }

    public Cache setId(CacheKey id) {
        this.id = id;
        return this;
    }

    public String getSummary() {
        return summary;
    }

    public Cache setSummary(String summary) {
        this.summary = summary;
        return this;
    }
}
