package org.datapool.dto;

import org.datapool.dto.metadata.CacheMetadataKey;

public class CacheSize {
    private CacheMetadataKey key;
    private int size = -1;

    public CacheMetadataKey getKey() {
        return key;
    }

    public CacheSize setKey(CacheMetadataKey key) {
        this.key = key;
        return this;
    }

    public int getSize() {
        return size;
    }

    public CacheSize setSize(int size) {
        this.size = size;
        return this;
    }
}
