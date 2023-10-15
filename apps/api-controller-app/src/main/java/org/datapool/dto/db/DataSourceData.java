package org.datapool.dto.db;

import org.datapool.dto.commons.DataSourceType;

import java.util.Properties;

public class DataSourceData {
    private DataSourceType type;
    private Properties properties;

    public DataSourceType getType() {
        return type;
    }

    public DataSourceData setType(DataSourceType type) {
        this.type = type;
        return this;
    }

    public Properties getProperties() {
        return properties;
    }

    public DataSourceData setProperties(Properties properties) {
        this.properties = properties;
        return this;
    }
}
