package org.datapool;

import io.micrometer.core.aop.CountedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.UUID;

@Configuration
@ConfigurationProperties(prefix = "app")
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class ApplicationConfiguration {
    private String igniteClient;
    private String mainDataRegion;
    private String csvStorage;
    private String secret;
    private String metricsPusher;
    private boolean auth = false;

    public String getMetricsPusher() {
        return metricsPusher;
    }

    public ApplicationConfiguration setMetricsPusher(String metricsPusher) {
        this.metricsPusher = metricsPusher;
        return this;
    }

    public String getSecret() {
        return secret;
    }

    public ApplicationConfiguration setSecret(String secret) {
        this.secret = secret;
        return this;
    }

    public boolean isAuth() {
        return auth;
    }

    public ApplicationConfiguration setAuth(boolean auth) {
        this.auth = auth;
        return this;
    }

    public String getCsvStorage() {
        return csvStorage;
    }

    public ApplicationConfiguration setCsvStorage(String csvStorage) {
        this.csvStorage = csvStorage;
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


    @Bean
    CountedAspect countedAspect(MeterRegistry registry) {
        return new CountedAspect(registry);
    }
}
