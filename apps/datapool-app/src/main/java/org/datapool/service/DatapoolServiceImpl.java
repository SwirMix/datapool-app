package org.datapool.service;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.transactions.Transaction;
import org.datapool.CacheFactory;
import org.datapool.DataPoolService;
import org.datapool.dto.DataPoolHashData;
import org.datapool.dto.DataPoolHashKey;
import org.datapool.dto.DataPoolItem;
import org.datapool.dto.DataPoolToken;
import org.datapool.dto.metadata.CacheMetadata;
import org.datapool.dto.metadata.CacheMetadataKey;
import org.datapool.dto.metadata.CacheStatus;
import org.datapool.services.AbstractCacheProvider;

import java.util.*;

import static org.apache.ignite.transactions.TransactionConcurrency.PESSIMISTIC;
import static org.apache.ignite.transactions.TransactionIsolation.REPEATABLE_READ;
import static org.datapool.Utils.generateRandomRange;

public class DatapoolServiceImpl extends AbstractCacheProvider implements DataPoolService {
    private Ignite ignite;
    private IgniteCache<CacheMetadataKey, CacheMetadata> metadataCache;
    private IgniteCache<CacheMetadataKey, Long> cursorsCache;
    private Map<CacheMetadataKey, IgniteCache<Long, DataPoolItem>> cashes = new HashMap<>();
    private static final String mainDataRegion = "datapool-persistence";

    public DatapoolServiceImpl setIgnite(Ignite ignite){
        super.setIgnite(ignite);
        return this;
    }

    public CacheMetadata getCacheMetadata(CacheMetadataKey key){
        CacheMetadata cacheMetadata = getMetadataCache().get(key);
        return cacheMetadata;
    }

    public void saveCacheMetadata(CacheMetadata cacheMetadata){
        getMetadataCache().put(cacheMetadata.getCache(), cacheMetadata);
    }

    public DataPoolItem getRandomItem(CacheMetadataKey key) {
        CacheMetadata cacheMetadata = getCacheMetadata(key);
        DataPoolItem dataPoolItem = new DataPoolItem(-1, -1);
        if (cacheMetadata!=null){
            if (cacheMetadata.getStatus().name().equals(CacheStatus.READY.name())){
                try {
                    Long cacheKey = generateRandomRange(0l, cacheMetadata.getSize());
                    if (cacheMetadata!=null){
                        IgniteCache<Long, DataPoolItem> cache = getTargetCache(key);
                        if (cache!=null){
                            dataPoolItem = cache.get(cacheKey);
                            DataPoolHashData dataPoolHashData = getTargetDataPoolHashData(cacheMetadata).get(new DataPoolHashKey(dataPoolItem.getHash()));
                            dataPoolItem.setData(dataPoolHashData.getData());
                        }
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return dataPoolItem;
    }

    public Map<Long, DataPoolItem> getDataPage(CacheMetadataKey key, Long start, Long end){
        CacheMetadata cacheMetadata = getCacheMetadata(key);
        Set<Long> keys = new HashSet<>();
        if (end > cacheMetadata.getSize()){
            end = cacheMetadata.getSize();
        }
        for (Long index = start; index <= end; index++){
            keys.add(index);
        }
        Map<Long, DataPoolItem> data = new HashMap<>();
        IgniteCache<DataPoolHashKey, DataPoolHashData> dataPoolHashDataIgniteCache = getTargetDataPoolHashData(cacheMetadata);
        if (cacheMetadata!=null){
            try {
                if (cacheMetadata!=null){
                    IgniteCache<Long, DataPoolItem> cache = getTargetCache(key);
                    if (cache!=null){
                        data = cache.getAll(keys);
                        for (DataPoolItem item : data.values()){
                            if (item != null){
                                DataPoolHashData hashData = dataPoolHashDataIgniteCache.get(new DataPoolHashKey(item.getHash()));
                                if (hashData!=null){
                                    item.setData(hashData.getData());
                                }

                            }

                        }
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return data;
    }

    public boolean checkToken(String token, String project){
        DataPoolToken value = getTokensCache().get(token);
        if (value!=null){
            if (value.getProject().equals(project)){
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public IgniteCache<Long, DataPoolItem> getTargetCache(CacheMetadataKey cacheMetadataKey){
        if (cashes.containsKey(cacheMetadataKey)){
            return cashes.get(cacheMetadataKey);
        } else {
            IgniteCache<Long, DataPoolItem> cache = getIgnite().cache(cacheMetadataKey.getPublicCacheName());
            cashes.put(cacheMetadataKey, cache);
            return cache;
        }
    }

    public IgniteCache<DataPoolHashKey, DataPoolHashData> getTargetDataPoolHashData(CacheMetadata cacheMetadata){
        return getIgnite().cache(cacheMetadata.getHashKey());
    }
    @Override
    public DataPoolItem getSequential(CacheMetadataKey cacheMetadataKey) {
        CacheMetadata cacheMetadata = getCacheMetadata(cacheMetadataKey);
        DataPoolItem dataPoolItem = new DataPoolItem();
        try {
            if (cacheMetadata!=null){
                if (cacheMetadata.getStatus().name().equals(CacheStatus.READY.name())){
                    long cacheKey = getNextNum(cacheMetadata);
                    IgniteCache<Long, DataPoolItem> cache = getTargetCache(cacheMetadataKey);
                    if (cache!=null){
                        dataPoolItem = cache.get(cacheKey);
                        if (dataPoolItem!=null){
                            DataPoolHashData dataPoolHashData = getTargetDataPoolHashData(cacheMetadata).get(new DataPoolHashKey(dataPoolItem.getHash()));
                            dataPoolItem.setData(dataPoolHashData.getData());
                        }

                        if (dataPoolItem != null){
                        } else {
                            dataPoolItem.setKey(-1l);
                            dataPoolItem.setData(new HashMap<>());
                        }

                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return dataPoolItem;
    }

    @Override
    public DataPoolItem getByKey(CacheMetadataKey cacheMetadataKey, long key) {
        CacheMetadata cacheMetadata = getCacheMetadata(cacheMetadataKey);
        DataPoolItem dataPoolItem = new DataPoolItem();
        try {
            if (cacheMetadata!=null){
                if (cacheMetadata.getStatus().name().equals(CacheStatus.READY.name())){
                    IgniteCache<Long, DataPoolItem> cache = getTargetCache(cacheMetadataKey);
                    if (cache!=null){
                        dataPoolItem = cache.get(key);
                        if (dataPoolItem!=null){
                            DataPoolHashData dataPoolHashData = getTargetDataPoolHashData(cacheMetadata).get(new DataPoolHashKey(dataPoolItem.getHash()));
                            dataPoolItem.setData(dataPoolHashData.getData());
                        }

                        if (dataPoolItem == null){
                            dataPoolItem = new DataPoolItem();
                        }
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return dataPoolItem;
    }

    @Override
    public DataPoolItem getAsStack(CacheMetadataKey cacheMetadataKey) {
        CacheMetadata cacheMetadata = getMetadataCache().get(cacheMetadataKey);
        DataPoolItem dataPoolItem = new DataPoolItem();
        IgniteCache<Long, DataPoolItem> targetCache = getTargetCache(cacheMetadataKey);
        long key = 0l;
        if (cacheMetadata!=null){
            if (cacheMetadata.getStatus().name().equals(CacheStatus.READY.name())){
                try (Transaction tx = getIgnite().transactions().txStart(PESSIMISTIC, REPEATABLE_READ)) {
                    long size = targetCache.size();
                    if (size != 0){
                        key = (long) size - 1;
                        dataPoolItem = targetCache.getAndRemove(key);
                        if (dataPoolItem!=null){
                            DataPoolHashData dataPoolHashData = getTargetDataPoolHashData(cacheMetadata).get(new DataPoolHashKey(dataPoolItem.getHash()));
                            dataPoolItem.setData(dataPoolHashData.getData());
                        }

                        cacheMetadata.setSize(key);
                        getMetadataCache().put(cacheMetadataKey, cacheMetadata);
                    }
                    tx.commit();
                }
                if (dataPoolItem == null){
                    dataPoolItem = new DataPoolItem();
                }
            }
        }
        return dataPoolItem;
    }


    private Long getNextNum(CacheMetadata key){
        IgniteCache<CacheMetadataKey, Long> cursorCache = getCursorsCache();
        Long cursor = cursorCache.get(key.getCache());
        try (Transaction tx = getIgnite().transactions().txStart(PESSIMISTIC, REPEATABLE_READ)) {
            if (cursor==null){
                cursorCache.put(key.getCache(), 0l);
                cursor = 0l;
                tx.commit();
            } else {
                cursor +=1;
                if (cursor >= key.getSize()){
                    cursor = 0l;
                    cursorCache.put(key.getCache(), 0l);
                } else {
                    cursorCache.put(key.getCache(), cursor);
                }
                cursorCache.put(key.getCache(), cursor);
                tx.commit();
            }
        }
        return cursor;
    }
}
