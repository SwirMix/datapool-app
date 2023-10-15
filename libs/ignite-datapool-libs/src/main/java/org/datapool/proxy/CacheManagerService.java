package org.datapool.proxy;

import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteDataStreamer;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.datapool.CacheFactory;
import org.datapool.CacheManager;
import org.datapool.ExcludeCache;
import org.datapool.dto.*;
import org.datapool.dto.commons.CreateCacheRequest;
import org.datapool.dto.commons.InternalApiRequest;
import org.datapool.dto.metadata.CacheMetadata;
import org.datapool.dto.metadata.CacheMetadataKey;
import org.datapool.dto.metadata.CacheStatus;
import org.datapool.dto.metadata.Message;
import org.datapool.services.AbstractCacheProvider;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CacheManagerService extends AbstractCacheProvider implements CacheManager {

    private static String mainDataRegion;
    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    public CacheManagerService(){
        this.mainDataRegion = "datapool-persistence";
    }

    @Override
    public InternalApiRequest getDataHashCache(CacheMetadata cacheMetadata) {
        InternalApiRequest result = new InternalApiRequest();
        try {
            getIgnite().getOrCreateCache(CacheFactory.defaultDataPoolHashDataCacheCfg(
                    cacheMetadata.getHashKey(),
                    this.getMainDataRegion()
            ));
            IgniteDataStreamer<DataPoolHashKey, DataPoolHashData> cache = getIgnite().dataStreamer(cacheMetadata.getHashKey());
            result.setResult(cache);
            result.setSuccess(true);
            result.setMessage("OK");
        } catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("internal error");
            result.setResult(new Message("internal error"));
        }
        return result;
    }

    private CacheMetadata createCacheMetadataNote(CreateCacheRequest request){
        CacheMetadata metadata = new CacheMetadata();
        metadata.setCache(request.getCache());
        metadata.setCacheId(request.getCache().getPublicCacheName());
        metadata.setType(request.getCacheType());
        metadata.setStatus(CacheStatus.CREATED);
        metadata.setColumns(request.getColumns());
        metadata.setMessage(new Message("successfully created").setLastSuccess(true).setDate(new Date()));
        getMetadataCache().put(request.getCache(), metadata);
        getCursorsCache().put(metadata.getCache(), 0l);
        return metadata;
    }

    @Override
    public InternalApiRequest<CacheMetadata> createCache(CreateCacheRequest request) {
        InternalApiRequest result = new InternalApiRequest();
        result = getCacheMetadata(request.getCache());
        if (result.getCode()==200 && result.isSuccess()){
            CacheMetadata cacheMetadata = getMetadataCache().get(request.getCache());
            result.setSuccess(true);
            result.setResult(cacheMetadata);
        } else {
            if (!request.getCache().getCacheName().contains("_")){
                if (getIgnite().cache(request.getCache().getPublicCacheName())==null){
                    CacheMetadata metadata = createCacheMetadataNote(request);
                    getIgnite().createCache(CacheFactory.defaultDataCache(
                            request.getCache().getPublicCacheName(),
                            CacheMode.REPLICATED,
                            CacheAtomicityMode.TRANSACTIONAL,
                            mainDataRegion
                    ));
                    getIgnite().createCache(CacheFactory.defaultDataPoolHashDataCacheCfg(
                            metadata.getHashKey(),
                            mainDataRegion
                    ));
                    result.setSuccess(true);
                    result.setResult(metadata);
                } else {
                    result.setMessage("Cache already exist. Rename or reuse");
                    result.setSuccess(false);
                }
            } else {
                result.setMessage("Bad cacheName. Invalid character '_' ");
                result.setSuccess(false);
            }
        }
        return result;
    }

    @Override
    public InternalApiRequest updateCacheMetadata(CacheMetadataKey key, CacheMetadata metadata) {
        InternalApiRequest result = new InternalApiRequest();
        try {
            getMetadataCache().put(key, metadata);
            result.setResult(metadata);
            result.setSuccess(true);
            result.setMessage("OK");
        } catch (Exception e){
            e.printStackTrace();
            result.setResult(metadata);
            result.setSuccess(false);
            result.setMessage("Internal error");
        }
        return result;
    }

    @Override
    public InternalApiRequest dropCache(CacheMetadataKey cacheMetadataKey) {
        InternalApiRequest result = new InternalApiRequest();
        InternalApiRequest<CacheInfo> call = getCacheMetadata(cacheMetadataKey);
        if (call.isSuccess()){
            CacheInfo metadata = call.getResult();
            try {
                IgniteCache cache = getIgnite().cache(cacheMetadataKey.getPublicCacheName());
                IgniteCache hashDataCache = getIgnite().cache(getMetadataCache().get(cacheMetadataKey).getHashKey());
                if (cache==null){
                    result.setSuccess(false);
                    result.setMessage("no cache with current name.");
                } else {
                    executorService.submit(new Runnable() {
                        @Override
                        public void run() {
                            cache.destroy();
                            hashDataCache.destroy();
                            getMetadataCache().remove(cacheMetadataKey);
                        }
                    });
                    result.setMessage("success");
                    result.setResult(metadata);
                    result.setSuccess(true);
                }
            } catch (Exception e){
                e.printStackTrace();
                result.setSuccess(false);
                result.setMessage("Internal error. Go to log.");
            }
        } else {
            result.setSuccess(false);
            result.setMessage("No cache with current params");
        }
        return result;
    }

    @Override
    public InternalApiRequest clearCache(CacheMetadataKey cacheMetadataKey) {
        InternalApiRequest result = new InternalApiRequest();
        result = getCacheMetadata(cacheMetadataKey);
        if (result.isSuccess()){
            CacheMetadata cacheMetadata = (CacheMetadata) result.getResult();
            try {
                IgniteCache cache = getIgnite().cache(cacheMetadataKey.getPublicCacheName());
                IgniteCache hashDataCache = getIgnite().cache(getMetadataCache().get(cacheMetadataKey).getHashKey());
                if (cache==null){
                    result.setSuccess(false);
                    result.setMessage("no cache with current name.");
                } else {
                    cache.clear();
                    hashDataCache.clear();
                    cacheMetadata.setSize(0);
                    getMetadataCache().put(cacheMetadataKey, cacheMetadata);
                    result.setMessage("success");
                    result.setResult(cacheMetadata);
                    result.setSuccess(true);
                }
            } catch (Exception e){
                e.printStackTrace();
                result.setSuccess(false);
                result.setMessage("Internal error. Go to log.");
            }
        }
        return result;
    }

    @Override
    public InternalApiRequest getCacheMetadata(CacheMetadataKey cacheMetadataKey) {
        InternalApiRequest result = new InternalApiRequest();
        try {
            CacheMetadata metadata = getMetadataCache().get(cacheMetadataKey);
            if (metadata==null){
                result.setSuccess(true);
                result.setCode(404);
                result.setMessage("no cache with current params.");
            } else {
                long cacheSize = getIgnite().cache(metadata.getCache().getPublicCacheName()).metrics().getCacheSize();
                long cursor = getCursorsCache().get(cacheMetadataKey);
                result.setResult(new CacheInfo(metadata, cacheSize).setCursor(cursor));
                result.setMessage("success");
                result.setSuccess(true);
            }
        } catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("Internal error. Go to log.");
        }
        return result;
    }

    public InternalApiRequest updateCacheCursor(CacheMetadataKey key, long cursor){
        InternalApiRequest result = new InternalApiRequest();
        try {
            CacheMetadata metadata = getMetadataCache().get(key);
            if (metadata==null){
                result.setSuccess(false);
                result.setMessage("no cache with current params.");
            } else {
                long cacheSize = getIgnite().cache(metadata.getCache().getPublicCacheName()).metrics().getCacheSize();
                getCursorsCache().put(key, cursor);
                result.setResult(new CacheInfo(metadata, cacheSize).setCursor(cursor));
                result.setMessage("success");
                result.setSuccess(true);
            }
        } catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("Internal error. Go to log.");
        }
        return result;
    }

    @Override
    public InternalApiRequest getProject(String projectId) {
        InternalApiRequest result = new InternalApiRequest();
        ProjectCaches caches = new ProjectCaches();
        for (String cacheName : getIgnite().cacheNames()){
            if (cacheName.contains(projectId)){
                if (!exclude(cacheName)){
                    try {
                        CacheMetadata cacheMetadata = getMetadataCache().get(
                                new CacheMetadataKey(cacheName.replace(projectId+"_", ""), projectId)
                        );
                        //long cacheSize = getIgnite().cache(cacheMetadata.getCache().getPublicCacheName()).metrics().getCacheSize();
                        long cursor = getCursorsCache().get(cacheMetadata.getCache());
                        caches.getCaches().add(
                                new CacheInfo(cacheMetadata, cacheMetadata.getSize()).setCursor(cursor)
                        );
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
        result.setResult(caches);
        result.setSuccess(true);
        return result;
    }

    public InternalApiRequest<CacheMetadata> updateCacheStatus(org.datapool.dto.CacheStatus status){
        InternalApiRequest result = this.getCacheMetadata(status.getKey());
        if (result.isSuccess()){
            CacheInfo cacheInfo = (CacheInfo) result.getResult();
            CacheMetadata metadata = cacheInfo.getCacheMetadata().setStatus(status.getStatus());
            metadata.setMessage(new Message("Cache status updated"));
            this.updateCacheMetadata(status.getKey(), metadata);
            result.setResult(metadata);
            return result;
        } else {
            result.setSuccess(false);
            result.setMessage("No cache with current params.");
            return result;
        }
    }
    public List<String> getProjectNames(){
        List<String> projects = new ArrayList<>();
        for (String cacheName : getIgnite().cacheNames()) {
            String[] cache = cacheName.split("_");
            if (cache.length == 2){
                projects.add(cache[0]);
            }
        }
        return projects;
    }

    public InternalApiRequest getProjects(){
        InternalApiRequest result = new InternalApiRequest();
        Map<String, Object> data = new HashMap<>();
        for (String project : getProjectNames()){
            data.put(project, getProject(project).getResult());
        }
        result.setSuccess(true);
        result.setResult(data);
        result.setMessage("OK");
        return result;
    }

    @Override
    public InternalApiRequest getAll() {
        InternalApiRequest result = new InternalApiRequest();
        ProjectCaches caches = new ProjectCaches();
        for (String cacheName : getIgnite().cacheNames()){
            String[] cache = cacheName.split("_");
            if (!exclude(cacheName)){
                try {
                    CacheMetadata cacheMetadata = getMetadataCache().get(
                            new CacheMetadataKey(cache[1], cache[0])
                    );
                    long cacheSize = getIgnite().cache(cacheMetadata.getCache().getPublicCacheName()).metrics().getCacheSize();
                    long cursor = getCursorsCache().get(cacheMetadata.getCache());
                    caches.getCaches().add(
                            new CacheInfo(cacheMetadata, cacheSize).setCursor(cursor)
                    );
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        result.setResult(caches);
        result.setSuccess(true);
        return result;
    }

    protected boolean exclude(String cacheName){
        for (ExcludeCache excludeCache : ExcludeCache.values()){
            if (cacheName.equals(excludeCache.name())) return true;
        }
        return false;
    }

    @Override
    public InternalApiRequest<IgniteCache> getIgniteCache(CacheMetadata cacheMetadata) {
        InternalApiRequest result = new InternalApiRequest();
        try {
            IgniteCache<Integer, Object> cache = getIgnite().cache(cacheMetadata.getCache().getPublicCacheName());
            result.setResult(cache);
            result.setSuccess(true);
            result.setMessage("OK");
        } catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("internal error");
            result.setResult(new Message("internal error"));
        }
        return result;
    }

    @Override
    public InternalApiRequest<IgniteDataStreamer> getIgniteDatastreamer(CacheMetadata cacheMetadata) {
        InternalApiRequest result = new InternalApiRequest();
        try {
            IgniteDataStreamer<Integer, DataPoolItem> cache = getIgnite().dataStreamer(cacheMetadata.getCache().getPublicCacheName());
            result.setResult(cache);
            result.setSuccess(true);
            result.setMessage("OK");
        } catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("internal error");
            result.setResult(new Message("internal error"));
        }
        return result;
    }

}
