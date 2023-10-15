package org.datapool;

import org.datapool.jwt.GlobalTokenService;
import org.datapool.jwt.PasswordJwtService;
import org.datapool.repository.SettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
public class ApplicationConfiguration {
    private String passwordSecret;
    private String jwtSecret;
    private String cacheManagerApp;
    private String datapoolApp;
    private String csvStorage;
    private String masterToken;
    private String metricsPusher;
    private String ignite;

    public String getIgnite() {
        return ignite;
    }

    public ApplicationConfiguration setIgnite(String ignite) {
        this.ignite = ignite;
        return this;
    }

    public String getMetricsPusher() {
        return metricsPusher;
    }

    public ApplicationConfiguration setMetricsPusher(String metricsPusher) {
        this.metricsPusher = metricsPusher;
        return this;
    }

    public String getMasterToken() {
        return masterToken;
    }

    public ApplicationConfiguration setMasterToken(String masterToken) {
        this.masterToken = masterToken;
        return this;
    }

    public String getDatapoolApp() {
        return datapoolApp;
    }

    public ApplicationConfiguration setDatapoolApp(String datapoolApp) {
        this.datapoolApp = datapoolApp;
        return this;
    }

    public String getCsvStorage() {
        return csvStorage;
    }

    public ApplicationConfiguration setCsvStorage(String csvStorage) {
        this.csvStorage = csvStorage;
        return this;
    }

    public String getCacheManagerApp() {
        return cacheManagerApp;
    }

    public ApplicationConfiguration setCacheManagerApp(String cacheManagerApp) {
        this.cacheManagerApp = cacheManagerApp;
        return this;
    }

    public String getPasswordSecret() {
        return passwordSecret;
    }

    public ApplicationConfiguration setPasswordSecret(String passwordSecret) {
        this.passwordSecret = passwordSecret;
        return this;
    }

    public String getJwtSecret() {
        return jwtSecret;
    }

    public ApplicationConfiguration setJwtSecret(String jwtSecret) {
        this.jwtSecret = jwtSecret;
        return this;
    }


}
