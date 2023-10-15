package org.datapool.dto.commons;

/**
 *          "properties": {
 *            "password": "perfcona",
 *            "url": "jdbc:postgresql://localhost:5432/perfcona?currentSchema=datapool",
 *            "username": "perfcona"
 *          }
 */
public class PostgresJdbcProps {
    private String url;
    private String username;
    private String password;
    private String driverClassName;
    private String schema;

    public String getSchema() {
        return schema;
    }

    public PostgresJdbcProps setSchema(String schema) {
        this.schema = schema;
        return this;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public PostgresJdbcProps setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public PostgresJdbcProps setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public PostgresJdbcProps setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public PostgresJdbcProps setPassword(String password) {
        this.password = password;
        return this;
    }
}
