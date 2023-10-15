package org.datapool.dto;

public class JdbcSourceInfo {
    private String url;
    private String schema;

    public String getUrl() {
        return url;
    }

    public JdbcSourceInfo setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getSchema() {
        return schema;
    }

    public JdbcSourceInfo setSchema(String schema) {
        this.schema = schema;
        return this;
    }
}
