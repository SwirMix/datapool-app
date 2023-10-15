package org.datapool.dto.storage;

import org.datapool.dto.metadata.CacheMetadataKey;

public class CacheToCsvRequest {
    private CacheMetadataKey cache;
    private long startId;
    private long endId;
    private String targetFileName;

    public CacheMetadataKey getCache() {
        return cache;
    }

    public CacheToCsvRequest setCache(CacheMetadataKey cache) {
        this.cache = cache;
        return this;
    }

    public long getStartId() {
        return startId;
    }

    public CacheToCsvRequest setStartId(long startId) {
        this.startId = startId;
        return this;
    }

    public long getEndId() {
        return endId;
    }

    public CacheToCsvRequest setEndId(long endId) {
        this.endId = endId;
        return this;
    }

    public String getTargetFileName() {
        return targetFileName;
    }

    public CacheToCsvRequest setTargetFileName(String targetFileName) {
        this.targetFileName = targetFileName;
        return this;
    }
}
