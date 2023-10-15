package org.datapool.dto.db;

import org.datapool.repository.CacheLogDataConverter;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "cache_log")
public class CacheLog {
    @Id
    private String id;
    @Column(name = "project_id")
    private String projectId;
    @Column(name = "cache_name")
    private String cacheName;

    @Column
    private Date date;
    @Convert(converter = CacheLogDataConverter.class)
    @Column(columnDefinition = "jsonb")
    private CacheLogEntity data;
    @Column
    private String type;

    public String getType() {
        return type;
    }

    public CacheLog setType(String type) {
        this.type = type;
        return this;
    }

    public String getId() {
        return id;
    }

    public CacheLog setId(String id) {
        this.id = id;
        return this;
    }

    public String getProjectId() {
        return projectId;
    }

    public CacheLog setProjectId(String projectId) {
        this.projectId = projectId;
        return this;
    }

    public String getCacheName() {
        return cacheName;
    }

    public CacheLog setCacheName(String cacheName) {
        this.cacheName = cacheName;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public CacheLog setDate(Date date) {
        this.date = date;
        return this;
    }

    public CacheLogEntity getData() {
        return data;
    }

    public CacheLog setData(CacheLogEntity data) {
        this.data = data;
        return this;
    }
}
