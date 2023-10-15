package org.datapool.dto;

import org.datapool.dto.metadata.CacheMetadata;

public class CacheInfo {
    private CacheMetadata cacheMetadata;
    private long cacheSize;
    private long cursor;

    public long getCursor() {
        return cursor;
    }

    public CacheInfo setCursor(long cursor) {
        this.cursor = cursor;
        return this;
    }

    public CacheInfo(){

    }

    public CacheInfo(CacheMetadata cacheMetadata, long cacheSize){
        this.cacheMetadata = cacheMetadata;
        this.cacheSize = cacheSize;
    }

    public CacheMetadata getCacheMetadata() {
        return cacheMetadata;
    }

    public CacheInfo setCacheMetadata(CacheMetadata cacheMetadata) {
        this.cacheMetadata = cacheMetadata;
        return this;
    }

    public long getCacheSize() {
        return cacheSize;
    }

    public CacheInfo setCacheSize(long cacheSize) {
        this.cacheSize = cacheSize;
        return this;
    }
}
