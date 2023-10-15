package org.datapool.dto;

import org.datapool.dto.metadata.CacheMetadataKey;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CacheShowCase {
    private Map<String, Long> caches = new HashMap<>();
    private Date lastUpdate = new Date();

    public void push(CacheMetadataKey key, Long size){
        caches.put(key.getPublicCacheName(), size);
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public CacheShowCase setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
        return this;
    }

    public Map<String, Long> getCaches() {
        return caches;
    }
}
