package org.datapool.dto;

import org.datapool.dto.metadata.CacheMetadata;

import java.util.ArrayList;
import java.util.List;

public class ProjectCaches {
    private List<CacheInfo> caches = new ArrayList<>();

    public Integer getSize(){
        return caches.size();
    }

    public List<CacheInfo> getCaches() {
        return caches;
    }

    public ProjectCaches setCaches(List<CacheInfo> caches) {
        this.caches = caches;
        return this;
    }
}
