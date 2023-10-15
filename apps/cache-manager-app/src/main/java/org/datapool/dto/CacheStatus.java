package org.datapool.dto;

import org.datapool.dto.metadata.CacheMetadataKey;

public class CacheStatus {

    private CacheMetadataKey key;
    private org.datapool.dto.metadata.CacheStatus status;

    public CacheMetadataKey getKey() {
        return key;
    }

    public CacheStatus setKey(CacheMetadataKey key) {
        this.key = key;
        return this;
    }

    public org.datapool.dto.metadata.CacheStatus getStatus() {
        return status;
    }

    public CacheStatus setStatus(org.datapool.dto.metadata.CacheStatus status) {
        this.status = status;
        return this;
    }
}
