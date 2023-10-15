package org.datapool;

import org.apache.ignite.cache.*;
import org.apache.ignite.configuration.CacheConfiguration;
import org.datapool.dto.DataPoolHashData;
import org.datapool.dto.DataPoolHashKey;
import org.datapool.dto.DataPoolItem;
import org.datapool.dto.DataPoolToken;
import org.datapool.dto.metadata.CacheMetadata;
import org.datapool.dto.metadata.CacheMetadataKey;

import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import java.util.LinkedHashMap;

public class CacheFactory {

    public static CacheConfiguration defaultCache(
            String cacheName,
            CacheMode cacheMode,
            CacheAtomicityMode atomicityMode,
            String dataRegionName
    ){
        CacheConfiguration cacheCfg = new CacheConfiguration(cacheName);
        cacheCfg.setCacheMode(cacheMode);
        cacheCfg.setBackups(2);
        cacheCfg.setRebalanceMode(CacheRebalanceMode.ASYNC);
        cacheCfg.setAtomicityMode(atomicityMode);
        cacheCfg.setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_SYNC);
        cacheCfg.setPartitionLossPolicy(PartitionLossPolicy.READ_ONLY_SAFE);
        cacheCfg.setDataRegionName(dataRegionName);
        cacheCfg.setStatisticsEnabled(true);
        return cacheCfg;
    }

    /**
     * Кэш хранения метаинформации (проекта, внутреннего имени)
     * @param cacheMode
     * @param atomicityMode
     * @param dataRegionName
     * @return
     */
    public static CacheConfiguration metadataCache(
            CacheMode cacheMode,
            CacheAtomicityMode atomicityMode,
            String dataRegionName
    ){
        CacheConfiguration<CacheMetadataKey, CacheMetadata> cacheCfg = new CacheConfiguration(ExcludeCache.METADATA_CACHE.name());
        cacheCfg.setCacheMode(cacheMode);
        cacheCfg.setBackups(2);
        cacheCfg.setRebalanceMode(CacheRebalanceMode.ASYNC);
        cacheCfg.setAtomicityMode(atomicityMode);
        cacheCfg.setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_SYNC);
        cacheCfg.setPartitionLossPolicy(PartitionLossPolicy.READ_ONLY_SAFE);
        cacheCfg.setDataRegionName(dataRegionName);
        cacheCfg.setStatisticsEnabled(true);
        return cacheCfg;
    }

    /***
     * @param cacheMode
     * @param atomicityMode
     * @param dataRegionName
     * @return
     */
    public static CacheConfiguration cursorsCache(
            CacheMode cacheMode,
            CacheAtomicityMode atomicityMode,
            String dataRegionName
    ){
        CacheConfiguration<CacheMetadataKey, CacheMetadata> cacheCfg = new CacheConfiguration(ExcludeCache.CACHE_CURSORS.name());
        cacheCfg.setCacheMode(cacheMode);
        cacheCfg.setBackups(2);
        cacheCfg.setRebalanceMode(CacheRebalanceMode.ASYNC);
        cacheCfg.setAtomicityMode(atomicityMode);
        cacheCfg.setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_SYNC);
        cacheCfg.setPartitionLossPolicy(PartitionLossPolicy.READ_ONLY_SAFE);
        cacheCfg.setDataRegionName(dataRegionName);
        cacheCfg.setStatisticsEnabled(true);
        return cacheCfg;
    }

    /**
     * Шаблон кэша с данными. Формат ключ-значение.
     * Значением является hash.
     * @param cacheName
     * @param cacheMode
     * @param atomicityMode
     * @param dataRegionName
     * @return
     */
    public static CacheConfiguration<Long, DataPoolItem> defaultDataCache(
            String cacheName,
            CacheMode cacheMode,
            CacheAtomicityMode atomicityMode,
            String dataRegionName
    ){
        CacheConfiguration<Long, DataPoolItem> cacheCfg = new CacheConfiguration(cacheName);
        cacheCfg.setCacheMode(cacheMode);
        cacheCfg.setBackups(2);
        cacheCfg.setRebalanceMode(CacheRebalanceMode.ASYNC);
        cacheCfg.setAtomicityMode(atomicityMode);
        cacheCfg.setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_SYNC);
        cacheCfg.setPartitionLossPolicy(PartitionLossPolicy.READ_ONLY_SAFE);
        cacheCfg.setDataRegionName(dataRegionName);
        cacheCfg.setStatisticsEnabled(true);
        return cacheCfg;
    }


    /***
     * Шаблон библиотеки данных кэша.
     * Ключ - hash данных.
     * Значение - непосредственно HashMap со значением.
     * @param cacheName
     * @param dataRegionName
     * @return
     */
    public static CacheConfiguration<DataPoolHashKey, DataPoolHashData> defaultDataPoolHashDataCacheCfg(
            String cacheName,
            String dataRegionName
    ){
        CacheConfiguration<DataPoolHashKey, DataPoolHashData> cacheCfg = new CacheConfiguration(cacheName);
        cacheCfg.setCacheMode(CacheMode.REPLICATED);
        cacheCfg.setBackups(2);
        cacheCfg.setRebalanceMode(CacheRebalanceMode.ASYNC);
        cacheCfg.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
        cacheCfg.setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_SYNC);
        cacheCfg.setPartitionLossPolicy(PartitionLossPolicy.READ_ONLY_SAFE);
        cacheCfg.setDataRegionName(dataRegionName);
        cacheCfg.setStatisticsEnabled(true);
        return cacheCfg;
    }

    /**
     * Хранилище токенов доступа
     * @param cacheMode
     * @param atomicityMode
     * @param dataRegionName
     * @return
     */
    public static CacheConfiguration<String, DataPoolToken> tokenRepository(
            CacheMode cacheMode,
            CacheAtomicityMode atomicityMode,
            String dataRegionName
    ){
        CacheConfiguration<String, DataPoolToken> cacheCfg = new CacheConfiguration(ExcludeCache.TOKENS_CACHE.name());
        cacheCfg.setCacheMode(cacheMode);
        cacheCfg.setBackups(2);
        cacheCfg.setRebalanceMode(CacheRebalanceMode.ASYNC);
        cacheCfg.setAtomicityMode(atomicityMode);
        cacheCfg.setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_SYNC);
        cacheCfg.setPartitionLossPolicy(PartitionLossPolicy.READ_ONLY_SAFE);
        cacheCfg.setDataRegionName(dataRegionName);
        cacheCfg.setStatisticsEnabled(true);
        return cacheCfg;
    }


}
