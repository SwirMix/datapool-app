package org.datapool.jdbc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.util.concurrent.Callable;

public abstract class JdbcDataSource implements JdbcConnector, Callable {
    protected HikariConfig config;
    protected HikariDataSource dataSource;

    public JdbcDataSource(HikariConfig config){
        this.config = config;
        dataSource = new HikariDataSource(config);
    }


}
