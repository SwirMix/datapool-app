package org.datapool.service;

import io.micrometer.core.annotation.Counted;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.transactions.Transaction;
import org.datapool.dto.*;
import org.datapool.dto.commons.InternalApiRequest;
import org.datapool.dto.metadata.CacheMetadata;
import org.datapool.dto.metadata.CacheMetadataKey;
import org.datapool.dto.metadata.Message;
import org.datapool.metrics.ConsumersMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.ignite.transactions.TransactionConcurrency.PESSIMISTIC;
import static org.apache.ignite.transactions.TransactionIsolation.REPEATABLE_READ;

@Service
public class MainDataPoolService implements MainDataPoolInterface{
    @Autowired
    private DatapoolServiceImpl datapool;
    private Logger logger = LoggerFactory.getLogger(MainDataPoolService.class);
    @Override
    @Counted(value = "counted.datapool.random",description = "get random row from datapool")
    public InternalApiRequest getRandomRow(CacheMetadataKey metadataKey) {
        InternalApiRequest result = new InternalApiRequest();
        try {
            DataPoolItem data = datapool.getRandomItem(metadataKey);
            if (data.getData().size()==0){
                result.setMessage("no data from cache");
                result.setSuccess(false);
                result.setResult(data);
            } else {

                result.setMessage("OK");
                result.setSuccess(true);
                result.setResult(data);
            }
        } catch (Exception e){
            e.printStackTrace();
            result.setResult(new Message("Internal error. Go to log"));
            result.setMessage("Internal error. Go to log");
            result.setSuccess(false);
            logger.error("Error get random data for: " + metadataKey.getPublicCacheName());
        }
        return result;
    }

    public InternalApiRequest getDataPage(CacheMetadataKey key, Long start, Long end){
        InternalApiRequest result = new InternalApiRequest();
        List<Map> page = new ArrayList<>();
        try {
            Map<Long, DataPoolItem> data = datapool.getDataPage(key, start, end);
            for (DataPoolItem item : data.values()){
                Map value = item.getData();
                value.put("key", item.getKey());
                value.put("hash", item.getHash());
                page.add(value);
            }
            result.setMessage("OK");
            result.setSuccess(true);
            result.setResult(page);
        } catch (Exception e){
            e.printStackTrace();
            result.setResult(new Message("Internal error. Go to log"));
            result.setMessage("Internal error. Go to log");
            result.setSuccess(false);
            logger.error("Error get random data for: " + key.getPublicCacheName());
        }
        return result;
    }

    @Override
    @Counted(value = "counted.datapool.sequential",description = "get row from datapool by sequential")
    public InternalApiRequest getSequentialRow(CacheMetadataKey metadataKey) {
        InternalApiRequest result = new InternalApiRequest();
        try {
            DataPoolItem data = datapool.getSequential(metadataKey);
            if (data.getData().size()==0){
                result.setMessage("no data from cache");
                result.setSuccess(false);
                result.setResult(data);
            } else {
                result.setMessage("OK");
                result.setSuccess(true);
                result.setResult(data);
            }
        } catch (Exception e){
            e.printStackTrace();
            result.setResult(new Message("Internal error. Go to log"));
            result.setMessage("Internal error. Go to log");
            result.setSuccess(false);
            logger.error("Error get random data for: " + metadataKey.getPublicCacheName());
        }
        return result;
    }

    @Override
    @Counted(value = "counted.datapool.byKey",description = "get random row from datapool")
    public InternalApiRequest getValueByKey(CacheMetadataKey metadataKey, long key) {
        InternalApiRequest result = new InternalApiRequest();
        try {
            DataPoolItem data = datapool.getByKey(metadataKey, key);
            if (data.getData().size()==0){
                result.setMessage("no data from cache");
                result.setSuccess(false);
                result.setResult(data);
            } else {
                result.setMessage("OK");
                result.setSuccess(true);
                result.setResult(data);
            }
        } catch (Exception e){
            e.printStackTrace();
            result.setResult(new Message("Internal error. Go to log"));
            result.setMessage("Internal error. Go to log");
            result.setSuccess(false);
            logger.error("Error get random data for: " + metadataKey.getPublicCacheName());
        }
        return result;
    }


    @Override
    @Counted(value = "counted.datapool.stack",description = "get random row from datapool")
    public InternalApiRequest getAsStack(CacheMetadataKey metadataKey) {
        InternalApiRequest result = new InternalApiRequest();
        try {
            DataPoolItem data = datapool.getAsStack(metadataKey);
            if (data.getData().size()==0){
                result.setMessage("no data from cache");
                result.setSuccess(false);
                result.setResult(data);
            } else {
                result.setMessage("OK");
                result.setSuccess(true);
                result.setResult(data);
            }
        } catch (Exception e){
            e.printStackTrace();
            result.setResult(new Message("Internal error. Go to log"));
            result.setMessage("Internal error. Go to log");
            result.setSuccess(false);
            logger.error("Error get random data for: " + metadataKey.getPublicCacheName());
        }
        return result;
    }

    @Override
    @Counted(value = "counted.datapool.size",description = "cache size")
    public InternalApiRequest getCacheSize(CacheMetadataKey metadataKey) {
        InternalApiRequest result = new InternalApiRequest();
        try {
            IgniteCache<Long, DataPoolItem> targetCache = datapool.getTargetCache(metadataKey);
            int size = targetCache.size();
            CacheSize cacheSize = new CacheSize().setSize(size).setKey(metadataKey);
            result.setResult(cacheSize);
            result.setSuccess(true);
            result.setMessage("Internal cacheName: " + metadataKey.getPublicCacheName());
        } catch (Exception e){
            e.printStackTrace();
            result.setResult(new CacheSize().setSize(-1).setKey(metadataKey));
            result.setSuccess(false);
            result.setMessage("Internal cacheName: " + metadataKey.getPublicCacheName() + ". Internal error.");
        }
        return result;
    }

    @Override
    @Counted(value = "counted.datapool.size",description = "cache size")
    public InternalApiRequest getDataByHash(CacheMetadataKey metadataKey, Integer hash) {
        InternalApiRequest result = new InternalApiRequest();
        try {
            IgniteCache<DataPoolHashKey, DataPoolHashData> targetCache = datapool.getTargetDataPoolHashData(datapool.getCacheMetadata(metadataKey));
            result.setResult(targetCache.get(new DataPoolHashKey(hash)));
            result.setSuccess(true);
            result.setMessage("Internal cacheName: " + metadataKey.getPublicCacheName());
        } catch (Exception e){
            e.printStackTrace();
            result.setResult(new CacheSize().setSize(-1).setKey(metadataKey));
            result.setSuccess(false);
            result.setMessage("Internal cacheName: " + metadataKey.getPublicCacheName() + ". Internal error.");
        }
        return result;
    }

    @Override
    @Counted(value = "counted.datapool.put",description = "put data to cache")
    public InternalApiRequest putData(PutData data) {
        InternalApiRequest result = new InternalApiRequest();
        CacheMetadata cacheMetadata = datapool.getCacheMetadata(data.getKey());
        if (cacheMetadata!=null){
            for (String column : cacheMetadata.getColumns().keySet().toArray(new String[0])){
                if (!data.getData().containsKey(column)){
                    result.setSuccess(false);
                    result.setResult(new Message("column not found: " + column));
                    result.setMessage("bad data");
                    return result;
                }
            }
            IgniteCache<Long, DataPoolItem> cache = datapool.getTargetCache(data.getKey());
            IgniteCache<DataPoolHashKey, DataPoolHashData> dataPoolHashCache = datapool.getTargetDataPoolHashData(cacheMetadata);
            long cacheSize = cache.size();
            try (Transaction tx = datapool.getIgnite().transactions().txStart(PESSIMISTIC, REPEATABLE_READ)) {
                if (cacheSize==0){
                    cache.put(0l, new DataPoolItem(0l, data.getData().hashCode()));
                    dataPoolHashCache.put(new DataPoolHashKey(data.getData().hashCode()), new DataPoolHashData(data.getData()));
                } else {
                    cache.put(cacheSize, new DataPoolItem(cacheSize, data.getData().hashCode()));
                    dataPoolHashCache.put(new DataPoolHashKey(data.getData().hashCode()), new DataPoolHashData(data.getData()));
                }
                tx.commit();
                cacheMetadata.setSize(cache.size());
                datapool.saveCacheMetadata(cacheMetadata);
                result.setMessage("OK");
                result.setSuccess(true);
                result.setResult(cacheSize);
            }
        } else {
            result.setSuccess(false);
            result.setResult(new Message("CacheMetada not found"));
            result.setMessage("bad data");
        }
        return result;
    }

    public InternalApiRequest getCacheMetadata(CacheMetadataKey key){
        InternalApiRequest result = new InternalApiRequest();
        CacheMetadata cacheMetadata = null;
        try {
            cacheMetadata = datapool.getCacheMetadata(key);
            if (cacheMetadata!=null){
                result.setResult(cacheMetadata);
                result.setMessage("OK");
                result.setSuccess(true);
            } else {
                result.setSuccess(false);
                result.setMessage("Not found");
            }
        } catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("Internal error.");
        }
        return result;
    }

    public boolean checkToken(String token, String project){
        return datapool.checkToken(token, project);
    }
}

