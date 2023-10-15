package org.datapool.proxy;


import com.google.gson.Gson;
import com.zaxxer.hikari.HikariConfig;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteDataStreamer;
import org.datapool.CacheManager;
import org.datapool.dto.DataPoolHashData;
import org.datapool.dto.DataPoolHashKey;
import org.datapool.dto.DataPoolItem;
import org.datapool.dto.commons.CreateJdbcCacheRequest;
import org.datapool.dto.metadata.CacheMetadata;
import org.datapool.dto.metadata.CacheStatus;
import org.datapool.dto.metadata.Message;
import org.datapool.jdbc.JdbcDataSource;
import org.postgresql.jdbc.PgArray;
import org.postgresql.util.PGInterval;

import java.io.Serializable;
import java.sql.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PostgresJdbcTask extends JdbcDataSource {
    private IgniteDataStreamer<Long, Object> targetCache;
    private IgniteDataStreamer<DataPoolHashKey, DataPoolHashData> targetHashDataCache;
    protected CacheManager cacheManagerService;
    private CacheMetadata cacheMetadata;
    private CreateJdbcCacheRequest request;
    private String query;

    public IgniteDataStreamer<DataPoolHashKey, DataPoolHashData> getTargetHashDataCache() {
        return targetHashDataCache;
    }

    public PostgresJdbcTask setTargetHashDataCache(IgniteDataStreamer<DataPoolHashKey, DataPoolHashData> targetHashDataCache) {
        this.targetHashDataCache = targetHashDataCache;
        return this;
    }

    public IgniteDataStreamer<Long, Object> getTargetCache() {
        return targetCache;
    }

    public PostgresJdbcTask setTargetCache(IgniteDataStreamer<Long, Object> targetCache) {
        this.targetCache = targetCache;
        return this;
    }

    public CacheManager getCacheManagerService() {
        return cacheManagerService;
    }

    public PostgresJdbcTask setCacheManagerService(CacheManager cacheManagerService) {
        this.cacheManagerService = cacheManagerService;
        return this;
    }

    public IgniteDataStreamer<Long, Object> getDataCache() {
        return targetCache;
    }

    public PostgresJdbcTask setDataCache(IgniteDataStreamer<Long, Object> dataCache) {
        this.targetCache = dataCache;
        return this;
    }

    public CacheMetadata getCacheMetadata() {
        return cacheMetadata;
    }

    public PostgresJdbcTask setCacheMetadata(CacheMetadata cacheMetadata) {
        this.cacheMetadata = cacheMetadata;
        return this;
    }

    public CreateJdbcCacheRequest getRequest() {
        return request;
    }

    public PostgresJdbcTask setRequest(CreateJdbcCacheRequest request) {
        this.request = request;
        return this;
    }

    public String getQuery() {
        return query;
    }

    public PostgresJdbcTask setQuery(String query) {
        this.query = query;
        return this;
    }

    public PostgresJdbcTask(HikariConfig config, String query) {
        super(config);
        this.query = query;
    }

    @Override
    public CacheMetadata call() throws Exception {
        long cursor = 0;
        try {
            cacheManagerService.updateCacheMetadata(
                    cacheMetadata.getCache(),
                    cacheMetadata.setStatus(CacheStatus.BUSY).setSize(cursor)
            );
            try (Connection connection = dataSource.getConnection()){
                ResultSet resultSet = connection.createStatement().executeQuery("select count(*) from (" + query + ") t");
                resultSet.next();
                long count = resultSet.getLong(1);
                do {
                    cacheMetadata = selectBatch(query, 3000, connection, cacheMetadata);
                    cacheManagerService.updateCacheMetadata(cacheMetadata.getCache(), cacheMetadata);
                } while (cacheMetadata.getSize() + 3000 < count);
                cacheMetadata = selectBatch(query, 3000, connection, cacheMetadata);
            }
            cacheMetadata.setStatus(CacheStatus.READY);
            cacheMetadata.setMessage(
                    new Message("Success jdbc load task.")
                            .setDate(new Date())
                            .setLastSuccess(true)
            );
            cacheManagerService.updateCacheMetadata(cacheMetadata.getCache(), cacheMetadata);
        } catch (Exception e){
            e.printStackTrace();
            cacheMetadata.setMessage(
                    new Message(e.getMessage())
                            .setDate(new Date())
                            .setLastSuccess(false)
            );
            cacheManagerService.updateCacheMetadata(cacheMetadata.getCache(), cacheMetadata);
        }
        this.targetCache.flush();
        this.targetCache.close();
        this.targetHashDataCache.flush();
        this.targetHashDataCache.close();
        this.dataSource.close();
        return this.cacheMetadata;
    }

    /**
     * SELECT
     *     film_id,
     *     title
     * FROM
     *     film
     * ORDER BY
     *     title
     * OFFSET 5 ROWS
     * FETCH FIRST 5 ROW ONLY;
     * @param query
     * @param batch
     */
    private CacheMetadata selectBatch(
            String query,
            int batch,
            Connection connection,
            CacheMetadata cacheMetadata
    ){
        String finalQuery = "";
        if (cacheMetadata.getSize() == 0){
            finalQuery = "select * from (" + query + ") t limit  " + batch + " offset 0";
        } else {
            finalQuery = "select * from (" + query + ") t limit  " + batch + " offset " + cacheMetadata.getSize();
        }

        try (Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(finalQuery);
            ResultSetMetaData metaData = resultSet.getMetaData();
            Map<String, String> columns = new HashMap<>();
            for (int columnCursor = 1; columnCursor <= metaData.getColumnCount(); columnCursor++){
                columns.put(metaData.getColumnName(columnCursor), metaData.getColumnTypeName(columnCursor));
            }
            cacheMetadata.setColumns(columns);
            while (resultSet.next()){
                Map entity = new HashMap<>();
                for (String column : cacheMetadata.getColumns().keySet()){
                    Object value = resultSet.getObject(column);
                    String serializeble = pgCaster(value);
                    if (serializeble!=null){
                        entity.put(column, serializeble);
                    } else {
                        entity.put(column, value);
                    }
                }
                long key = cacheMetadata.getSize();
                int hash = entity.hashCode();
                DataPoolHashData dataHash = new DataPoolHashData(entity);
                DataPoolItem data = new DataPoolItem(key, hash);

                this.targetHashDataCache.addData(new DataPoolHashKey(hash), dataHash);
                this.targetCache.addData(key, data);
                cacheMetadata.setSize(cacheMetadata.getSize() + 1);
            }
            this.targetHashDataCache.flush();
            this.targetCache.flush();
        } catch (SQLException e) {
            cacheMetadata.setStatus(CacheStatus.READY);
            cacheMetadata.setMessage(new Message("JDBC task exception: " + e.getMessage()));
            return cacheMetadata;
        }
        return cacheMetadata;
    }

    @Override
    public boolean runQuery(String query) {
        return false;
    }

    private String pgCaster(Object data){
        Gson gson = new Gson();
        if (data instanceof String){
            return (String) data;
        } else if (data instanceof Timestamp){
            return ((Timestamp) data).toString();
        } else if (data instanceof Serializable){
            return gson.toJson(data);
        } else if (data instanceof PgArray){
            PgArray pgArray = (PgArray) data;
            try {
                return gson.toJson(pgArray.getArray());
            } catch (SQLException e) {
                e.printStackTrace();
                return pgArray.toString();
            }
        }
        return null;
    }

}
