package org.datapool.dto.api.internal;

import org.datapool.model.CacheMetadataKey;

public class LoadFromJdbcRq {
    private String datasourceId;
    private String entity;
    private Type type;
    private CacheMetadataKey key;

    public CacheMetadataKey getKey() {
        return key;
    }

    public LoadFromJdbcRq setKey(CacheMetadataKey key) {
        this.key = key;
        return this;
    }

    public String getEntity() {
        return entity;
    }

    public LoadFromJdbcRq setEntity(String entity) {
        this.entity = entity;
        return this;
    }

    public Type getType() {
        return type;
    }

    public LoadFromJdbcRq setType(Type type) {
        this.type = type;
        return this;
    }

    public String getDatasourceId() {
        return datasourceId;
    }

    public LoadFromJdbcRq setDatasourceId(String datasourceId) {
        this.datasourceId = datasourceId;
        return this;
    }

    public enum Type {
        QUERY,
        DEFAULT
    }
}
