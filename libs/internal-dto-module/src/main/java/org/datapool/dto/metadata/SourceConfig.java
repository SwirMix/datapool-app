package org.datapool.dto.metadata;

import java.util.HashMap;
import java.util.Map;

public class SourceConfig {
    private DataSources source;
    private Map<String, String> properties = new HashMap<>();

    public DataSources getSource() {
        return source;
    }

    public SourceConfig setSource(DataSources source) {
        this.source = source;
        return this;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public SourceConfig setProperties(Map<String, String> properties) {
        this.properties = properties;
        return this;
    }
}
