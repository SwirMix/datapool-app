package org.datapool.proxy;

import com.zaxxer.hikari.HikariConfig;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteDataStreamer;
import org.datapool.CacheManager;
import org.datapool.dto.DataPoolHashData;
import org.datapool.dto.commons.*;
import org.datapool.dto.metadata.CacheMetadata;
import org.datapool.dto.metadata.CacheMetadataKey;
import org.datapool.jdbc.CacheService;
import org.datapool.jdbc.JdbcGatewayImpl;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class LoaderService implements CacheService {
    private ExecutorService executorService;
    private int loaderThreads;
    private CacheManager cacheManagerService;
    private JdbcGatewayImpl jdbcGateway;
    public LoaderService(int jdbcLoaderThreads, CacheManager cacheManager, JdbcGatewayImpl jdbcGateway){
        this.loaderThreads = jdbcLoaderThreads;
        this.executorService = Executors.newFixedThreadPool(this.loaderThreads);
        this.cacheManagerService = cacheManager;
        this.jdbcGateway = jdbcGateway;
    }

    protected JdbcGatewayImpl getJdbcGateway(){
        return jdbcGateway;
    }


    @Override
    public InternalApiRequest<String> createCache(CreateJdbcCacheRequest request) {
        InternalApiRequest result = new InternalApiRequest();
        if(checkJdbcCreateRequest(request)){
            HikariConfig config = new HikariConfig();
            config.setAutoCommit(false);
            config.setConnectionTimeout(30000);
            config.setMaximumPoolSize(1);
            config.setMaxLifetime(10*60*10000);
            config.setDriverClassName(request.getProperties().getDriverClassName());
            config.setJdbcUrl(request.getProperties().getUrl());
            config.setUsername(request.getProperties().getUsername());
            config.setPassword(request.getProperties().getPassword());
            config.addDataSourceProperty( "cachePrepStmts" , "true" );
            config.addDataSourceProperty( "prepStmtCacheSize" , "250" );
            config.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );

            CreateCacheRequest createCacheRequest = cacheRequestBuilder(request);
            InternalApiRequest<CacheMetadata> cacheResult = cacheManagerService.createCache(createCacheRequest);
            if (cacheResult.isSuccess()){
                PostgresJdbcTask loadTask = new PostgresJdbcTask(config, request.getQuery());
                InternalApiRequest<IgniteDataStreamer> targetCache = cacheManagerService.getIgniteDatastreamer(cacheResult.getResult());
                InternalApiRequest<IgniteDataStreamer> targetHashDataCache = cacheManagerService.getDataHashCache(cacheResult.getResult());
                if (targetCache.isSuccess() && targetHashDataCache.isSuccess()){
                    loadTask.setDataCache(targetCache.getResult());
                    loadTask.setCacheMetadata(cacheResult.getResult());
                    loadTask.setCacheManagerService(cacheManagerService);
                    loadTask.setTargetHashDataCache(targetHashDataCache.getResult());
                    String uuid = UUID.randomUUID().toString();
                    Future<CacheMetadata> future = executorService.submit(loadTask);
                    result.setSuccess(true);
                    result.setMessage("OK");
                    result.setResult(uuid);
                } else {
                    result.setSuccess(false);
                    result.setMessage("Internal error. Could not get cache with name: " + cacheResult.getResult().getCache().getPublicCacheName());
                }
            } else {
                result.setResult(request);
                result.setSuccess(false);
                result.setMessage("Internal error. Cache is not created. Bad data or internal error.");
                return result;
            }
        } else {
            result.setSuccess(false);
            result.setMessage("Bad params");
        }
        return result;
    }


    private CreateCacheRequest cacheRequestBuilder(CreateCacheRq request){
        CreateCacheRequest createCacheRequest = new CreateCacheRequest();
        CacheMetadataKey key = new CacheMetadataKey(request.getCacheName(), request.getProjectId());
        createCacheRequest.setCache(key);
        createCacheRequest.setCacheType(CacheMetadata.CacheType.PERSISTENCE);
        createCacheRequest.setColumns(new HashMap<>());
        return createCacheRequest;
    }

    private boolean checkJdbcCreateRequest(CreateJdbcCacheRequest request){
        if (request==null){
            return false;
        } else if (request.getProperties()==null){
            return false;
        } else if (request.getName()==null){
            return false;
        } else if (request.getProjectId()==null){
            return false;
        } else if (request.getQuery()==null){
            return false;
        } else if (request.getId()==null){
            return false;
        } else if (request.getId().equals("")){
            return false;
        } else if (request.getQuery().equals("")){
            return false;
        } else if (request.getName().equals("")){
            return false;
        } else if (request.getProjectId().equals("")){
            return false;
        }
        PostgresJdbcProps props = request.getProperties();
        if (props.getDriverClassName()==null){
            return false;
        } else if (props.getDriverClassName().equals("")){
            return false;
        } else if (props.getPassword()==null){
            return false;
        } else if (props.getPassword().equals("")){
            return false;
        } else if (props.getUrl()==null){
            return false;
        } else if (props.getUrl().equals("")){
            return false;
        } else if (props.getUsername()==null){
            return false;
        } else if (props.getUsername().equals("")){
            return false;
        } else
        return true;
    }
}
