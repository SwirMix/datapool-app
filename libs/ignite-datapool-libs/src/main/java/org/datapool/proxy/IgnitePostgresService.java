package org.datapool.proxy;

import com.zaxxer.hikari.HikariConfig;
import org.apache.ignite.Ignite;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.services.ServiceContext;
import org.datapool.dto.commons.*;
import org.datapool.dto.metadata.CacheMetadata;
import org.datapool.dto.metadata.CacheMetadataKey;
import org.datapool.jdbc.JdbcGatewayImpl;
import org.datapool.jdbc.dto.TableObject;
import java.util.*;

public class IgnitePostgresService implements PostgresJdbcService{
    @IgniteInstanceResource
    private Ignite ignite;
    private static Map<String, LoaderService> gateways = new HashMap<>();

    public Ignite getIgnite() {
        return ignite;
    }

    public IgnitePostgresService setIgnite(Ignite ignite) {
        this.ignite = ignite;
        return this;
    }

    private CacheManagerService getCacheManagerService(){
        CacheManagerService cacheManagerService = new CacheManagerService();
        cacheManagerService.setIgnite(ignite);
        return cacheManagerService;
    }

    /** {@inheritDoc} */
    public void execute(ServiceContext ctx) throws Exception {

    }

    /** {@inheritDoc} */
    public void cancel(ServiceContext ctx) {

    }

    @Override
    public InternalApiRequest<List<TableObject>> getTables(HikariConfig hikariConfig) {
        InternalApiRequest result = new InternalApiRequest();
        try {
            JdbcGatewayImpl gateway = getGateway(hikariConfig);
            List<TableObject> tables = gateway.getTables();
            result.setResult(tables);
            result.setSuccess(true);
            result.setMessage("OK");
        } catch (Exception e) {
            e.printStackTrace();
            result.setMessage(e.getMessage());
            result.setSuccess(false);
            result.setResult(new ArrayList<>());
        }
        return result;
    }

    private JdbcGatewayImpl getGateway(HikariConfig hikariConfig){
        if (gateways.containsKey(hikariConfig.getPoolName())){
            return gateways.get(hikariConfig.getPoolName()).getJdbcGateway();
        } else {
            LoaderService gateway = new LoaderService(
                    5,
                    getCacheManagerService(),
                    new JdbcGatewayImpl(hikariConfig, hikariConfig.getSchema())
            );
            gateways.put(hikariConfig.getPoolName(), gateway);
            return gateway.getJdbcGateway();
        }
    }

    private LoaderService getLoaderService(HikariConfig hikariConfig){
        if (gateways.containsKey(hikariConfig.getPoolName())){
            return gateways.get(hikariConfig.getPoolName());
        } else {
            LoaderService gateway = new LoaderService(
                    5,
                    getCacheManagerService(),
                    new JdbcGatewayImpl(hikariConfig, hikariConfig.getSchema())
            );
            gateways.put(hikariConfig.getPoolName(), gateway);
            return gateway;
        }
    }

    @Override
    public InternalApiRequest<TableObject> getTableInfo(HikariConfig hikariConfig, String tableName) {
        InternalApiRequest result = new InternalApiRequest();
        try {
            JdbcGatewayImpl gateway = getGateway(hikariConfig);
            TableObject table = gateway.getTableInfo(tableName);
            result.setResult(table);
            result.setSuccess(true);
            result.setMessage("OK");
        } catch (Exception e) {
            e.printStackTrace();
            result.setMessage(e.getMessage());
            result.setSuccess(false);
            result.setResult(new ArrayList<>());
        }
        return result;
    }

    @Override
    public InternalApiRequest<Boolean> checkTable(HikariConfig hikariConfig, String tableName) {
        InternalApiRequest result = new InternalApiRequest();
        try {
            JdbcGatewayImpl gateway = getGateway(hikariConfig);
            Boolean status = gateway.checkTableName(tableName);
            result.setResult(status);
            result.setSuccess(true);
            result.setMessage("OK");
        } catch (Exception e) {
            e.printStackTrace();
            result.setMessage(e.getMessage());
            result.setSuccess(false);
            result.setResult(new ArrayList<>());
        }
        return result;
    }

    @Override
    public InternalApiRequest<String> createCache(CreateJdbcCacheRequest request) {
        InternalApiRequest result = new InternalApiRequest();
        StringBuilder builder = new StringBuilder();
        builder.append(request.getProperties().getUrl())
                .append(request.getProperties().getDriverClassName())
                .append(request.getProperties().getUsername())
                .append(request.getProperties().getPassword())
                .append(request.getProperties().getSchema());
        HikariConfig config = new HikariConfig();
        config.setMaximumPoolSize(20);
        config.setPoolName(builder.toString());
        config.setAutoCommit(false);
        config.setConnectionTimeout(30000);
        config.setDriverClassName(request.getProperties().getDriverClassName());
        config.setJdbcUrl(request.getProperties().getUrl());
        config.setUsername(request.getProperties().getUsername());
        config.setPassword(request.getProperties().getPassword());
        config.addDataSourceProperty( "cachePrepStmts" , "true" );
        config.addDataSourceProperty( "prepStmtCacheSize" , "250" );
        config.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );

        return getLoaderService(config).createCache(request);
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
