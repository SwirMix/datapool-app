package org.datapool.dto.commons;

import org.datapool.dto.metadata.CacheMetadata;
import org.datapool.dto.metadata.CacheMetadataKey;

import java.util.Map;

public class CreateCacheRequest {
    private CacheMetadataKey cache;
    /**
     * Ключ - имя поля. Значение - тип поля.
     */
    private Map<String, String> columns;
    private CacheMetadata.CacheType cacheType;

    public CacheMetadata.CacheType getCacheType() {
        return cacheType;
    }

    public CreateCacheRequest setCacheType(CacheMetadata.CacheType cacheType) {
        this.cacheType = cacheType;
        return this;
    }

    public Map<String, String> getColumns() {
        return columns;
    }

    public CreateCacheRequest setColumns(Map<String, String> columns) {
        this.columns = columns;
        return this;
    }

    public CacheMetadataKey getCache() {
        return cache;
    }

    public CreateCacheRequest setCache(CacheMetadataKey cache) {
        this.cache = cache;
        return this;
    }
}
