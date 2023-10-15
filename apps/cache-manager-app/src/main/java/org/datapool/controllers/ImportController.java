package org.datapool.controllers;

import com.zaxxer.hikari.HikariConfig;
import org.datapool.dto.CacheStatus;
import org.datapool.dto.commons.*;
import org.datapool.dto.metadata.CacheMetadataKey;
import org.datapool.services.CacheManagerService;
import org.datapool.services.cache.LoaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/import")
public class ImportController {

    @Autowired
    private LoaderService jdbcLoaderService;
    @Autowired
    private CacheManagerService managerService;

    @PostMapping("/default/cache")
    public ResponseEntity<InternalApiRequest> createJdbcCache(
            @RequestBody CreateDefaultJdbcCache request
    ){
        CreateJdbcCacheRequest body = new CreateJdbcCacheRequest();
        StringBuilder builder = new StringBuilder();
        builder.append(request.getProperties().getUrl())
                .append(request.getProperties().getDriverClassName())
                .append(request.getProperties().getUsername())
                .append(request.getProperties().getPassword())
                .append(request.getProperties().getSchema());
        HikariConfig config = new HikariConfig();
        config.setPoolName(builder.toString());
        config.setAutoCommit(false);
        config.setConnectionTimeout(30000);
        config.setDriverClassName(request.getProperties().getDriverClassName());
        config.setJdbcUrl(request.getProperties().getUrl());
        config.setUsername(request.getProperties().getUsername());
        config.setPassword(request.getProperties().getPassword());
        config.setSchema(request.getProperties().getSchema());

        String query = "select * from " + config.getSchema() + "." + request.getTableName();
        body.setId(request.getId());
        body.setName(request.getName());
        body.setCacheName(request.getName());
        body.setId(request.getId());
        body.setProjectId(request.getProjectId());
        body.setQuery(query);
        body.setProperties(new PostgresJdbcProps()
                .setDriverClassName(config.getDriverClassName())
                .setPassword(config.getPassword())
                .setUsername(config.getUsername())
                .setUrl(config.getJdbcUrl())
        );
        InternalApiRequest result = new InternalApiRequest();
        try {
            result = jdbcLoaderService.createCache(body);
            if (!result.isSuccess()){
                return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("Internal error. Check logs");
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PostMapping("/jdbc/cache")
    public ResponseEntity<InternalApiRequest> createJdbcCache(
            @RequestBody CreateJdbcCacheRequest request
    ){
        InternalApiRequest result = new InternalApiRequest();
        try {
            jdbcLoaderService.createCache(request);
            result.setSuccess(true);
            result.setMessage("OK");
            if (result.getMessage().equals("Bad params")){
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("Internal error. Check logs");
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PostMapping("/jdbc/reload")
    public ResponseEntity<InternalApiRequest> reloadJdbcCache(
            @RequestBody CreateJdbcCacheRequest request
    ){
        InternalApiRequest result = new InternalApiRequest();
        CacheMetadataKey metadataKey = new CacheMetadataKey(request.getCacheName(), request.getCacheName());
        result = managerService.clearCache(metadataKey);
        if (result.isSuccess()){
            managerService.updateCacheStatus(new CacheStatus()
                    .setStatus(org.datapool.dto.metadata.CacheStatus.BUSY)
                    .setKey(metadataKey)
            );
            try {
                jdbcLoaderService.createCache(request);
                result.setSuccess(true);
                result.setMessage("OK");
                if (result.getMessage().equals("Bad params")){
                    return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
                }
            } catch (Exception e){
                e.printStackTrace();
                result.setSuccess(false);
                result.setMessage("Internal error. Check logs");
                return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

}
