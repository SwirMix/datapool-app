package org.datapool.dto.commons;

/**
 *     {
 *       "id": "66ee7243-ee91-4d3f-a43f-81359e0ed766",
 *       "projectId": "711bff9d-a873-4497-873f-fbfceb220071",
 *       "name": "fdf",
 *       "properties": {
 *         "query": "select * from data profile.test limit 300",
 *         "type": "POSTGRESQL",
 *         "properties": {
 *           "password": "perfcona",
 *           "url": "jdbc:postgresql://localhost:5432/perfcona?currentSchema=datapool",
 *           "username": "perfcona"
 *         }
 *       }
 *     }
 */
public class CreateJdbcCacheRequest extends CreateCacheRq{
    private PostgresJdbcProps properties;
    private String query;

    public String getQuery() {
        return query;
    }

    public CreateJdbcCacheRequest setQuery(String query) {
        this.query = query;
        return this;
    }

    public PostgresJdbcProps getProperties() {
        return properties;
    }

    public CreateJdbcCacheRequest setProperties(PostgresJdbcProps properties) {
        this.properties = properties;
        return this;
    }
}
