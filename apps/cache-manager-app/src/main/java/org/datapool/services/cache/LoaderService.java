package org.datapool.services.cache;
import com.zaxxer.hikari.HikariConfig;
import org.datapool.dto.commons.*;
import org.datapool.proxy.IgnitePostgresService;
import org.datapool.proxy.PostgresJdbcService;
import org.datapool.services.CacheManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class LoaderService {
    private PostgresJdbcService postgresService;
    private CacheManagerService cacheManagerService;
    private ApplicationContext context;

    public LoaderService(ApplicationContext context){
        this.context = context;
        this.postgresService = new IgnitePostgresService().setIgnite(getCacheManagerService().getIgnite());
    }

    protected CacheManagerService getCacheManagerService(){
        if (cacheManagerService==null){
            cacheManagerService = context.getBean(CacheManagerService.class);
        }
        return cacheManagerService;
    }

    public InternalApiRequest createCache(CreateJdbcCacheRequest request) {
        return postgresService.createCache(request);
    }

    public InternalApiRequest getJdbcTableInfo(String tableName, HikariConfig config){
        return postgresService.getTableInfo(config, tableName);
    }

    public InternalApiRequest getJdbcTablesInfo(HikariConfig config){
        return postgresService.getTables(config);
    }

}
