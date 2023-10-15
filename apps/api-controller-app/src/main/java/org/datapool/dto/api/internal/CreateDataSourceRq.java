package org.datapool.dto.api.internal;

import org.datapool.dto.db.DataSourceData;

public class CreateDataSourceRq {
    private String projectId;
    private String dataSourceName;
    private DataSourceData properties;

    public String getProjectId() {
        return projectId;
    }

    public CreateDataSourceRq setProjectId(String projectId) {
        this.projectId = projectId;
        return this;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public CreateDataSourceRq setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
        return this;
    }

    public DataSourceData getProperties() {
        return properties;
    }

    public CreateDataSourceRq setProperties(DataSourceData properties) {
        this.properties = properties;
        return this;
    }
}
