package org.datapool.dto.commons.dto.metadata;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CacheMetadata {
    private CacheStatus status;
    private String cacheId;
    private CacheMetadataKey cache;
    private CacheType type = CacheType.PERSISTENCE;
    private Map<String, String> columns;
    private Message message;
    private long size = 0;

    public long getSize() {
        return size;
    }

    public CacheMetadata setSize(long size) {
        this.size = size;
        return this;
    }

    public Message getMessage() {
        return message;
    }

    public CacheMetadata setMessage(Message message) {
        this.message = message;
        return this;
    }

    public String getCacheId() {
        return cacheId;
    }

    public CacheMetadata setCacheId(String cacheId) {
        this.cacheId = cacheId;
        return this;
    }

    public CacheMetadataKey getCache() {
        return cache;
    }

    public CacheMetadata setCache(CacheMetadataKey cache) {
        this.cache = cache;
        return this;
    }

    public Map<String, String> getColumns() {
        return columns;
    }

    public CacheMetadata setColumns(Map<String, String> columns) {
        this.columns = columns;
        return this;
    }

    public CacheType getType() {
        return type;
    }

    public CacheMetadata setType(CacheType type) {
        this.type = type;
        return this;
    }
    public CacheStatus getStatus() {
        return status;
    }

    public CacheMetadata setStatus(CacheStatus status) {
        this.status = status;
        return this;
    }

    public enum CacheType {
        PERSISTENCE,
        DATA_TIME_LIMIT
    }
}