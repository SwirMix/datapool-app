package org.datapool;

import org.datapool.dto.DataPoolItem;
import org.datapool.dto.metadata.CacheMetadataKey;

public interface DataPoolService {
    public DataPoolItem getRandomItem(CacheMetadataKey cacheMetadataKey);
    public DataPoolItem getSequential(CacheMetadataKey cacheMetadataKey);

    public DataPoolItem getByKey(CacheMetadataKey cacheMetadataKey, long key);

    public DataPoolItem getAsStack(CacheMetadataKey cacheMetadataKey);
}
