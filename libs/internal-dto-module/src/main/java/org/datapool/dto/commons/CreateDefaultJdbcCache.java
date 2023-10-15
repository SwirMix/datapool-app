package org.datapool.dto.commons;


public class CreateDefaultJdbcCache {
    private String id;
    private String projectId;
    private String name;
    private String tableName;
    private PostgresJdbcProps properties;

    public PostgresJdbcProps getProperties() {
        return properties;
    }

    public CreateDefaultJdbcCache setProperties(PostgresJdbcProps properties) {
        this.properties = properties;
        return this;
    }

    public String getId() {
        return id;
    }

    public CreateDefaultJdbcCache setId(String id) {
        this.id = id;
        return this;
    }

    public String getProjectId() {
        return projectId;
    }

    public CreateDefaultJdbcCache setProjectId(String projectId) {
        this.projectId = projectId;
        return this;
    }

    public String getName() {
        return name;
    }

    public CreateDefaultJdbcCache setName(String name) {
        this.name = name;
        return this;
    }

    public String getTableName() {
        return tableName;
    }

    public CreateDefaultJdbcCache setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }
}
