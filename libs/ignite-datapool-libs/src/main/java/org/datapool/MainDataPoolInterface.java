package org.datapool;

import org.datapool.dto.PutData;
import org.datapool.dto.commons.InternalApiRequest;
import org.datapool.dto.metadata.CacheMetadataKey;

public interface MainDataPoolInterface {
    public InternalApiRequest getRandomRow(CacheMetadataKey metadataKey);
    public InternalApiRequest getSequentialRow(CacheMetadataKey metadataKey);
    public InternalApiRequest getValueByKey(CacheMetadataKey metadataKey, long key);
    public InternalApiRequest getAsStack(CacheMetadataKey metadataKey);
    public InternalApiRequest getCacheSize(CacheMetadataKey metadataKey);

    public InternalApiRequest putData(PutData data);
}
