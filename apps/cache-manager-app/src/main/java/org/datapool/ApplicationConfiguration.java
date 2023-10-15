package org.datapool;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
@ConfigurationProperties(prefix = "app")
public class ApplicationConfiguration {
    private String igniteClient;
    private String mainDataRegion;
    private int jdbcLoaderThreads;

    public int getJdbcLoaderThreads() {
        return jdbcLoaderThreads;
    }

    public ApplicationConfiguration setJdbcLoaderThreads(int jdbcLoaderThreads) {
        this.jdbcLoaderThreads = jdbcLoaderThreads;
        return this;
    }

    public String getMainDataRegion() {
        return mainDataRegion;
    }

    public ApplicationConfiguration setMainDataRegion(String mainDataRegion) {
        this.mainDataRegion = mainDataRegion;
        return this;
    }

    public String getIgniteClient() {
        return igniteClient;
    }

    public ApplicationConfiguration setIgniteClient(String igniteClient) {
        this.igniteClient = igniteClient;
        return this;
    }

}
