package org.datapool.dto;

import org.datapool.dto.metadata.CacheMetadataKey;

public class UpdateCacheInfoRequest {
    private CacheMetadataKey key;
    private UpdateCacheInfo cacheInfo;

    public CacheMetadataKey getKey() {
        return key;
    }

    public UpdateCacheInfoRequest setKey(CacheMetadataKey key) {
        this.key = key;
        return this;
    }

    public UpdateCacheInfo getCacheInfo() {
        return cacheInfo;
    }

    public UpdateCacheInfoRequest setCacheInfo(UpdateCacheInfo cacheInfo) {
        this.cacheInfo = cacheInfo;
        return this;
    }
}
