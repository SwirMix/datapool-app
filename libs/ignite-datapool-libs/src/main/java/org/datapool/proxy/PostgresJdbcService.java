package org.datapool.proxy;

import com.zaxxer.hikari.HikariConfig;
import org.apache.ignite.services.Service;
import org.datapool.dto.commons.CreateJdbcCacheRequest;
import org.datapool.dto.commons.InternalApiRequest;
import org.datapool.jdbc.dto.TableObject;

import java.util.List;

public interface PostgresJdbcService extends Service {
    public static final String SERVICE_NAME = "POSTGRESQL_JDBC_SERVICE";

    public InternalApiRequest<List<TableObject>> getTables(HikariConfig hikariConfig);

    public InternalApiRequest<TableObject> getTableInfo(HikariConfig hikariConfig, String tableName);

    public InternalApiRequest<Boolean> checkTable(HikariConfig hikariConfig, String tableName);
    public InternalApiRequest createCache(CreateJdbcCacheRequest request);

}
