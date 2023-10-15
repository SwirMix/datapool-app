package org.datapool;

import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteDataStreamer;
import org.apache.ignite.services.Service;
import org.datapool.dto.commons.CreateCacheRequest;
import org.datapool.dto.commons.InternalApiRequest;
import org.datapool.dto.metadata.CacheMetadata;
import org.datapool.dto.metadata.CacheMetadataKey;

public interface CacheManager {
    public InternalApiRequest<CacheMetadata> createCache(CreateCacheRequest request);
    public InternalApiRequest updateCacheMetadata(CacheMetadataKey key, CacheMetadata metadata);
    public InternalApiRequest clearCache(CacheMetadataKey cacheMetadataKey);
    public InternalApiRequest dropCache(CacheMetadataKey cacheMetadataKey);

    public InternalApiRequest getCacheMetadata(CacheMetadataKey cacheMetadataKey);

    public InternalApiRequest getProject(String projectId);

    public InternalApiRequest<IgniteCache> getIgniteCache(CacheMetadata cacheMetadata);

    public InternalApiRequest<IgniteDataStreamer> getIgniteDatastreamer(CacheMetadata cacheMetadata);
    public InternalApiRequest<IgniteDataStreamer> getDataHashCache(CacheMetadata cacheMetadata);

    public InternalApiRequest getAll();

}
