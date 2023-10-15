package org.datapool.jdbc;

import org.datapool.dto.commons.CreateJdbcCacheRequest;
import org.datapool.dto.commons.InternalApiRequest;

public interface CacheService {
    public InternalApiRequest<String> createCache(CreateJdbcCacheRequest request);
}
