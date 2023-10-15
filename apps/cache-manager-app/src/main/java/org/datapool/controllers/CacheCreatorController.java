package org.datapool.controllers;

import org.datapool.dto.CacheStatus;
import org.datapool.dto.commons.*;
import org.datapool.dto.metadata.CacheMetadata;
import org.datapool.dto.metadata.CacheMetadataKey;
import org.datapool.services.CacheManagerService;
import org.datapool.services.cache.LoaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/manager/cache")
public class CacheCreatorController {
    @Autowired
    private CacheManagerService cacheManagerService;

    @PostMapping("/runtime")
    public ResponseEntity<InternalApiRequest<CacheMetadata>> createCache(
            @RequestBody CreateCacheRequest request
    ){
        try {
            InternalApiRequest response = cacheManagerService.createCache(request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity(
                    new InternalApiRequest<>().
                            setSuccess(false).
                            setMessage("Go to logs"),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @DeleteMapping("/{project}/{cacheName}")
    public ResponseEntity<InternalApiRequest> deleteCache(
            @PathVariable String project,
            @PathVariable String cacheName
    ){
        CacheMetadataKey key = new CacheMetadataKey(cacheName, project);
        InternalApiRequest result = cacheManagerService.dropCache(key);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/status")
    public ResponseEntity changeCacheState(
            @RequestBody CacheStatus status
    ){
        InternalApiRequest result = cacheManagerService.updateCacheStatus(status);
        return new ResponseEntity(result, HttpStatus.OK);
    }
}
