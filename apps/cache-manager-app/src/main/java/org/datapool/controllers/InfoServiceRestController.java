package org.datapool.controllers;

import org.datapool.dto.UpdateCursorRequest;
import org.datapool.dto.commons.InternalApiRequest;
import org.datapool.dto.metadata.CacheMetadataKey;
import org.datapool.services.CacheManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class InfoServiceRestController {
    @Autowired
    private CacheManagerService cacheManagerService;

    @GetMapping("/projects")
    public ResponseEntity<Map> getProject(
    ){
        Map<String, Object> result = new HashMap<>();
        InternalApiRequest cashes = cacheManagerService.getProjects();
        result.put("caches", cashes.getResult());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping("/{project}")
    public ResponseEntity<InternalApiRequest> getProjectCache(
            @PathVariable(required = false) String project
    ){
        InternalApiRequest result = null;
        if (project!=null){
            result = cacheManagerService.getProject(project);
        } else {
            result = cacheManagerService.getAll();
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping("/{project}/{cacheName}")
    public ResponseEntity<InternalApiRequest> getMetadata(
        @PathVariable String project,
        @PathVariable String cacheName
    ){
        CacheMetadataKey key = new CacheMetadataKey(cacheName, project);
        InternalApiRequest result = cacheManagerService.getCacheMetadata(key);
        return resultFilter(result);
    }

    @PostMapping("/update/cursor")
    public ResponseEntity<InternalApiRequest> updateCursor(
            @RequestBody UpdateCursorRequest request
    ){
        InternalApiRequest result = cacheManagerService.updateCacheCursor(request.getCacheMetadataKey(), request.getNewCursor());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    private ResponseEntity resultFilter(InternalApiRequest result){
        switch (result.getCode()){
            case 404:
                return new ResponseEntity(result, HttpStatus.NOT_FOUND);
            case 400:
                return new ResponseEntity(result, HttpStatus.BAD_REQUEST);
            case 500:
                return new ResponseEntity(result, HttpStatus.INTERNAL_SERVER_ERROR);
            default:
                return new ResponseEntity(result, HttpStatus.OK);
        }
    }

}
