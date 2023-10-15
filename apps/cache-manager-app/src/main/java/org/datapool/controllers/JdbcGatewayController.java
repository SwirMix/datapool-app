package org.datapool.controllers;

import com.zaxxer.hikari.HikariConfig;
import org.datapool.dto.commons.InternalApiRequest;
import org.datapool.dto.commons.PostgresJdbcProps;
import org.datapool.dto.metadata.Message;
import org.datapool.services.cache.LoaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/default")
public class JdbcGatewayController {
    @Autowired
    private LoaderService service;

    @PostMapping("/tables/")
    public ResponseEntity<InternalApiRequest> getJdbcTables(
            @RequestBody PostgresJdbcProps jdbcProps,
            @RequestParam(required = false) String tableName
    ){
        InternalApiRequest result = new InternalApiRequest();
        StringBuilder builder = new StringBuilder();
        builder.append(jdbcProps.getUrl())
                .append(jdbcProps.getDriverClassName())
                .append(jdbcProps.getUsername())
                .append(jdbcProps.getPassword())
                .append(jdbcProps.getSchema());
        HikariConfig config = new HikariConfig();
        config.setPoolName(builder.toString());
        config.setAutoCommit(false);
        config.setConnectionTimeout(30000);
        config.setDriverClassName(jdbcProps.getDriverClassName());
        config.setJdbcUrl(jdbcProps.getUrl());
        config.setUsername(jdbcProps.getUsername());
        config.setPassword(jdbcProps.getPassword());
        config.setSchema(jdbcProps.getSchema());
        config.setMaximumPoolSize(2);

        config.addDataSourceProperty( "cachePrepStmts" , "true" );
        config.addDataSourceProperty( "prepStmtCacheSize" , "250" );
        config.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
        if (tableName!=null){
            try {
                result = service.getJdbcTableInfo(tableName, config);
            } catch (Exception e){
                e.printStackTrace();
                result.setMessage("Internal Error!");
                result.setSuccess(false);
                result.setResult(new Message("Internal error"));
                return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            result = service.getJdbcTablesInfo(config);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
