package org.datapool.dto.db;

import org.datapool.repository.DataSourceDataConverter;

import javax.persistence.*;

@Entity
@Table(name = "datasource")
public class DataSource {
    @Id
    private String id;
    @Column
    private String projectId;
    @Column
    private String name;
    @Convert(converter = DataSourceDataConverter.class)
    @Column(columnDefinition = "jsonb")
    private DataSourceData properties;

    public String getProjectId() {
        return projectId;
    }

    public DataSource setProjectId(String projectId) {
        this.projectId = projectId;
        return this;
    }

    public String getName() {
        return name;
    }

    public DataSource setName(String name) {
        this.name = name;
        return this;
    }

    public String getId() {
        return id;
    }

    public DataSource setId(String id) {
        this.id = id;
        return this;
    }

    public DataSourceData getProperties() {
        return properties;
    }

    public DataSource setProperties(DataSourceData properties) {
        this.properties = properties;
        return this;
    }
}
