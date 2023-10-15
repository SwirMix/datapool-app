package org.datapool.csv;

import org.datapool.csv.dto.ImportCacheDataRequest;
import org.datapool.dto.commons.InternalApiRequest;
import org.datapool.dto.metadata.CacheMetadataKey;

import java.util.List;

public interface ImportCacheService {
    public InternalApiRequest<FileInfo> importCacheData(ImportCacheDataRequest request);
    public InternalApiRequest dropStorage(CacheMetadataKey cache);
    public InternalApiRequest<List<FileInfo>> getStorage(CacheMetadataKey key);

}
