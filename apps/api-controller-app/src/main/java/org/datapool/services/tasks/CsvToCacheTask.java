package org.datapool.services.tasks;

import org.apache.ignite.client.ClientCache;
import org.apache.ignite.client.IgniteClient;
import org.datapool.ExcludeCache;
import org.datapool.csv.CsvParcer;
import org.datapool.dto.DataPoolHashData;
import org.datapool.dto.DataPoolHashKey;
import org.datapool.dto.DataPoolItem;
import org.datapool.dto.commons.InternalApiRequest;
import org.datapool.dto.metadata.CacheMetadata;
import org.datapool.dto.metadata.CacheStatus;
import org.datapool.dto.metadata.Message;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CsvToCacheTask implements Callable<InternalApiRequest> {
    private IgniteClient client;
    private CsvParcer parcer;
    private ExecutorService executor = Executors.newFixedThreadPool(10);
    private CacheMetadata cacheMetadata;
    private Long cursor = 0l;
    private ClientCache<DataPoolHashKey, DataPoolHashData> hashCache;
    private ClientCache<Long, DataPoolItem> datapoolCache;

    public CsvToCacheTask(IgniteClient client, CsvParcer parcer, CacheMetadata cacheMetadata){
        this.cacheMetadata = cacheMetadata;
        this.parcer = parcer;
        this.client = client;
    }
    @Override
    public InternalApiRequest call() throws Exception {
        InternalApiRequest result = new InternalApiRequest();
        cacheMetadata.setStatus(CacheStatus.BUSY);
        client.cache(ExcludeCache.METADATA_CACHE.name()).put(cacheMetadata.getCache(), cacheMetadata);
        hashCache = client.cache(cacheMetadata.getHashKey());
        datapoolCache = client.cache(cacheMetadata.getCache().getPublicCacheName());
        try {
            Map item = new HashMap<>();
            parcer.reiniteReader();
            long rowNum = parcer.getRownum();
            while (cursor < rowNum) {
                item = parcer.next();
                int hash = item.hashCode();
                if (!item.isEmpty()){
                    DataPoolHashKey hashKey = new DataPoolHashKey(hash);
                    DataPoolHashData hashData = new DataPoolHashData(item);
                    DataPoolItem dataPoolItem = new DataPoolItem();
                    dataPoolItem.setKey(cursor);
                    dataPoolItem.setData(item);
                    dataPoolItem.setHash(hash);
                    hashCache.putAsync(hashKey, hashData);
                    datapoolCache.putAsync(cursor, dataPoolItem);
                    cursor +=1;
                }

                updateCacheSize();
            }
        } catch (Exception e){
            e.printStackTrace();
            result.setMessage("CSV error: " + e.getMessage());
            result.setSuccess(false);
            result.setCode(500);
            cacheMetadata.setMessage(new Message("CSV error: " + parcer.getFilePath() + " [ " + e.getMessage() + " ]"));
        }
        cacheMetadata.setStatus(CacheStatus.READY);
        cacheMetadata.setSize(datapoolCache.size());
        Message message = new Message("Success csv processing. " + parcer.getFilePath());
        message.setLastSuccess(true);
        cacheMetadata.setMessage(message);
        parcer.close();
        client.cache(ExcludeCache.METADATA_CACHE.name()).put(cacheMetadata.getCache(), cacheMetadata);
        client.close();
        return result;
    }

    private void updateCacheSize(){
        if (cursor%10000==0){
            client.cache(ExcludeCache.METADATA_CACHE.name()).put(cacheMetadata.getCache(), cacheMetadata.setSize(cursor));
        }
    }
}
