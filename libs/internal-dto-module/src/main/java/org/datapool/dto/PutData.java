package org.datapool.dto;

import org.datapool.dto.metadata.CacheMetadataKey;

import java.util.Map;

public class PutData {
    private CacheMetadataKey key;
    private Map<String, Object> data;

    public CacheMetadataKey getKey() {
        return key;
    }

    public PutData setKey(CacheMetadataKey key) {
        this.key = key;
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public PutData setData(Map<String, Object> data) {
        this.data = data;
        return this;
    }
}
