package org.datapool.model;

import org.datapool.dto.metadata.CacheMetadataKey;

public class UpdateCursorRequest {
    private CacheMetadataKey cacheMetadataKey;
    private long newCursor;

    public CacheMetadataKey getCacheMetadataKey() {
        return cacheMetadataKey;
    }

    public UpdateCursorRequest setCacheMetadataKey(CacheMetadataKey cacheMetadataKey) {
        this.cacheMetadataKey = cacheMetadataKey;
        return this;
    }

    public long getNewCursor() {
        return newCursor;
    }

    public UpdateCursorRequest setNewCursor(long newCursor) {
        this.newCursor = newCursor;
        return this;
    }
}
