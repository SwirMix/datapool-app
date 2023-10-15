package org.datapool.csv.dto;

import org.datapool.dto.metadata.CacheMetadataKey;

public class ImportCacheDataRequest {
    private long startId;
    private long endId;
    private CacheMetadataKey cache;
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public ImportCacheDataRequest setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public long getStartId() {
        return startId;
    }

    public ImportCacheDataRequest setStartId(long startId) {
        this.startId = startId;
        return this;
    }

    public long getEndId() {
        return endId;
    }

    public ImportCacheDataRequest setEndId(long endId) {
        this.endId = endId;
        return this;
    }

    public CacheMetadataKey getCache() {
        return cache;
    }

    public ImportCacheDataRequest setCache(CacheMetadataKey cache) {
        this.cache = cache;
        return this;
    }
}
