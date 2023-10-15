package org.datapool.controllers;

import com.google.gson.internal.LinkedTreeMap;
import org.datapool.dto.DatapoolStrategy;
import org.datapool.dto.GlobalToken;
import org.datapool.dto.UpdateCacheInfoRequest;
import org.datapool.dto.api.internal.CacheDataPage;
import org.datapool.dto.api.internal.CacheMetadata;
import org.datapool.dto.api.internal.UpdateSummary;
import org.datapool.dto.commons.InternalApiRequest;
import org.datapool.dto.db.DataSource;
import org.datapool.dto.db.Role;
import org.datapool.dto.metadata.Message;
import org.datapool.jwt.GlobalTokenService;
import org.datapool.model.CacheMetadataKey;
import org.datapool.model.CacheStatus;
import org.datapool.model.CreateCacheRequest;
import org.datapool.model.CreateJdbcCacheRequest;
import org.datapool.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.datapool.dto.api.internal.LoadFromJdbcRq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.datapool.Utils.resultFilter;

@RestController
@RequestMapping("/api/v1/cache")
public class CacheRestController {
    @Autowired
    private GlobalTokenService globalTokenService;
    @Autowired
    private CacheManagerService cacheManagerService;
    @Autowired
    private PermissionValidator permissionValidator;
    @Autowired
    private DataSourceService dataSourceService;
    @Autowired
    private CacheLogService cacheLogService;
    @Autowired
    private DatapoolService datapoolService;


    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/datapool/data/{project}/{cacheName}")
    public ResponseEntity<InternalApiRequest> getDataPage(
            @RequestHeader String token,
            @PathVariable String project,
            @PathVariable String cacheName,
            @RequestParam Long page
    ){
        InternalApiRequest result = new InternalApiRequest();
        page -=1;
        try {
            GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
        } catch (Exception e){
            result.setSuccess(false);
            result.setMessage("Invalid token");
            result.setResult(new Message("Invalid token"));
            return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        }
        GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
        CacheDataPage dataPage = new CacheDataPage();
        if (permissionValidator.checkProjectPermission(globalToken.getUserId(), project)){
            result = cacheManagerService.getCacheData(project, cacheName);
            if (result.isSuccess()){
                Map cacheInfo = (Map) result.getResult();
                LinkedTreeMap metadata = (LinkedTreeMap) cacheInfo.get("metadata");
                Double size = (Double) metadata.get("cacheSize");
                double pages = Math.ceil(size / dataPage.getSize());
                Long totalPages = (long) pages;
                LinkedTreeMap metaInfo = (LinkedTreeMap) metadata.get("cacheMetadata");
                LinkedTreeMap columns = (LinkedTreeMap) metaInfo.get("columns");
                ArrayList<String> columnsSet = new ArrayList(List.of(columns.keySet().toArray()));
                ArrayList fcolumns = new ArrayList();
                fcolumns.add("hash");
                fcolumns.add("key");
                for (String column : columnsSet){
                    fcolumns.add(column);
                }
                Long startKey = (long) (page * dataPage.getSize());
                Long endKey = startKey + dataPage.getSize();
                result = datapoolService.getDataPage(
                        project,
                        cacheName,
                        startKey,
                        endKey
                );
                if (result.isSuccess()){
                    dataPage.setColumns(fcolumns);
                    dataPage.setCurrentPage(page);
                    dataPage.setTotalPage(totalPages);
                    dataPage.setData((List<Map>) result.getResult());
                    result.setResult(dataPage);
                    return new ResponseEntity<>(result, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            result.setSuccess(false);
            result.setMessage("Not found project or permissions.");
            result.setResult(new Message("Not found project or permissions."));
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/datapool")
    public ResponseEntity<InternalApiRequest> datapoolCall(
            @RequestParam String project,
            @RequestHeader String token,
            @RequestParam DatapoolStrategy strategy,
            @RequestParam String cacheName,
            @RequestParam(required = false) Long key
    ){
        InternalApiRequest result = new InternalApiRequest();
        if (key == null){
            key = -1l;
        }
        try {
            GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
        } catch (Exception e){
            result.setSuccess(false);
            result.setMessage("Invalid token");
            result.setResult(new Message("Invalid token"));
            return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        }
        GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
        if (permissionValidator.checkProjectPermission(globalToken.getUserId(), project)){
            result = datapoolService.getData(project, cacheName, strategy, key, globalToken.getEmail());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.setSuccess(false);
            result.setMessage("Not found project or permissions.");
            result.setResult(new Message("Not found project or permissions."));
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/{project}")
    public ResponseEntity<InternalApiRequest> getCaches(
            @PathVariable String project,
            @RequestHeader String token
    ){
        InternalApiRequest result = new InternalApiRequest();
        try {
            GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
        } catch (Exception e){
            result.setSuccess(false);
            result.setMessage("Invalid token");
            result.setResult(new Message("Invalid token"));
            return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        }
        GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
        if (permissionValidator.checkProjectPermission(globalToken.getUserId(), project)){
            result = cacheManagerService.getCaches(project);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.setSuccess(false);
            result.setMessage("Not found project or permissions.");
            result.setResult(new Message("Not found project or permissions."));
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/summary")
    public ResponseEntity<InternalApiRequest> getCacheData(
            @RequestBody UpdateSummary body,
            @RequestHeader String token
    ) {
        InternalApiRequest result = new InternalApiRequest();
        try {
            GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
        } catch (Exception e){
            result.setSuccess(false);
            result.setMessage("Invalid token");
            result.setResult(new Message("Invalid token"));
            return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        }
        GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
        if (permissionValidator.checkProjectPermission(globalToken.getUserId(), body.getProject())){
            result = cacheManagerService.updateCacheSummary(body);
            return resultFilter(result);
        } else {
            result.setSuccess(false);
            result.setMessage("Not found project or permissions.");
            result.setResult(new Message("Not found project or permissions."));
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/info")
    public ResponseEntity<InternalApiRequest> updateCacheDescription(
            @RequestBody UpdateCacheInfoRequest request,
            @RequestHeader String token
    ){
        InternalApiRequest result = new InternalApiRequest();
        try {
            GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
        } catch (Exception e){
            result.setSuccess(false);
            result.setMessage("Invalid token");
            result.setResult(new Message("Invalid token"));
            return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        }
        GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
        if (permissionValidator.checkProjectPermission(globalToken.getUserId(), request.getKey().getProject(), Role.TEAMMATE) | permissionValidator.checkProjectPermission(globalToken.getUserId(), request.getKey().getProject(), Role.ADMIN)){
            result = cacheManagerService.updateCacheDescription(request);
            return resultFilter(result);
        } else {
            result.setSuccess(false);
            result.setMessage("Not found project or permissions.");
            result.setResult(new Message("Not found project or permissions."));
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/{project}/{cacheName}")
    public ResponseEntity<InternalApiRequest> getCacheData(
            @PathVariable String project,
            @PathVariable String cacheName,
            @RequestHeader String token
    ){
        InternalApiRequest result = new InternalApiRequest();
        try {
            GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
        } catch (Exception e){
            result.setSuccess(false);
            result.setMessage("Invalid token");
            result.setResult(new Message("Invalid token"));
            return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        }
        GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
        if (permissionValidator.checkProjectPermission(globalToken.getUserId(), project)){
            result = cacheManagerService.getCacheData(project, cacheName);
            return resultFilter(result);
        } else {
            result.setSuccess(false);
            result.setMessage("Not found project or permissions.");
            result.setResult(new Message("Not found project or permissions."));
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/update/cursor/{project}/{cacheName}")
    public ResponseEntity<InternalApiRequest> updateCacheCursor(
            @PathVariable String project,
            @PathVariable String cacheName,
            @RequestParam(required = true) Long cursor,
            @RequestHeader String token
    ){
        InternalApiRequest result = new InternalApiRequest();
        try {
            GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
        } catch (Exception e){
            result.setSuccess(false);
            result.setMessage("Invalid token");
            result.setResult(new Message("Invalid token"));
            return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        }
        GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
        if (permissionValidator.checkProjectPermission(globalToken.getUserId(), project)){
            if (cursor < 0){
                result.setSuccess(false);
                result.setMessage("cursor must be > 0");
                result.setResult(new Message("cursor must be > 0"));
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            } else {
                result = cacheManagerService.updateCacheCursor(project, cacheName, cursor);
                return new ResponseEntity<>(result, HttpStatus.OK);
            }

        } else {
            result.setSuccess(false);
            result.setMessage("Not found project or permissions.");
            result.setResult(new Message("Not found project or permissions."));
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/status")
    public ResponseEntity<InternalApiRequest> getCacheData(
            @RequestBody CacheStatus request,
            @RequestHeader String token
    ){
        InternalApiRequest result = new InternalApiRequest();
        try {
            GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
        } catch (Exception e){
            result.setSuccess(false);
            result.setMessage("Invalid token");
            result.setResult(new Message("Invalid token"));
            return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        }
        GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
        if (
                permissionValidator.checkProjectPermission(globalToken.getUserId(), request.getKey().getProject(), Role.ADMIN)
                        |
                permissionValidator.checkProjectPermission(globalToken.getUserId(), request.getKey().getProject(), Role.TEAMMATE)
        ){
            result = cacheManagerService.changeCacheStatus(request);
            return new ResponseEntity<>(
                    cacheManagerService.getCacheData(request.getKey().getProject(), request.getKey().getCacheName()),
                    HttpStatus.OK
            );
        } else {
            result.setSuccess(false);
            result.setMessage("Not found project or permissions.");
            result.setResult(new Message("Not found project or permissions."));
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @DeleteMapping("/{project}/{cacheName}")
    public ResponseEntity<InternalApiRequest> deleteCache(
            @PathVariable String project,
            @PathVariable String cacheName,
            @RequestHeader String token
    ){
        InternalApiRequest result = new InternalApiRequest();
        try {
            GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
            if (
                    permissionValidator.checkProjectPermission(globalToken.getUserId(), project, Role.ADMIN)
                            |
                            permissionValidator.checkProjectPermission(globalToken.getUserId(), project, Role.TEAMMATE)
            ){
                result = cacheManagerService.deleteCache(project, cacheName);
                if (result.isSuccess()){
                    cacheLogService.dropCache(
                            project,
                            cacheName,
                            globalToken.getEmail(),
                            result
                    );
                }
                return new ResponseEntity<>(cacheManagerService.getCaches(project), HttpStatus.OK);
            } else {
                result.setSuccess(false);
                result.setMessage("Not found project or permissions.");
                result.setResult(new Message("Not found project or permissions."));
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e){
            result.setSuccess(false);
            result.setMessage("Invalid token");
            result.setResult(new Message("Invalid token"));
            return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/create/jdbc")
    public ResponseEntity<InternalApiRequest<CacheMetadata>> createCache(
            @RequestBody LoadFromJdbcRq request,
            @RequestHeader String token
    ){
        InternalApiRequest result = new InternalApiRequest();
        try {
            GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
            if (
                    permissionValidator.checkProjectPermission(globalToken.getUserId(), request.getKey().getProject(), Role.ADMIN)
                            |
                    permissionValidator.checkProjectPermission(globalToken.getUserId(), request.getKey().getProject(), Role.TEAMMATE)
            ){
                result = dataSourceService.getDataSourceById(request.getDatasourceId(), globalToken.getUserId());
                if (result.isSuccess()){
                    DataSource dataSource = (DataSource) result.getResult();
                    try {
                        result = cacheManagerService.createJdbcCache(request, dataSource);
                    } catch (Exception e){
                        e.printStackTrace();
                        result.setSuccess(false);
                        result.setMessage("internal error. Go to logs!");
                        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                } else {
                    return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
                }
                if (result.isSuccess()){
                    cacheLogService.createJdbcCache(
                            request,
                            globalToken.getEmail(),
                            result,
                            CacheLogService.EventType.CREATE
                    );
                }
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                result.setSuccess(false);
                result.setMessage("invalid token");
                return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
            }

        } catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("invalid token");
            return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/reload/{projectId}/{cacheName}")
    public ResponseEntity<InternalApiRequest<CacheMetadata>> reloadJdbcCache(
            @PathVariable String projectId,
            @PathVariable String cacheName,
            @RequestHeader String token
    ){
        InternalApiRequest result = new InternalApiRequest();
        try {
            GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
            if (
                    permissionValidator.checkProjectPermission(globalToken.getUserId(), projectId, Role.ADMIN)
                            |
                    permissionValidator.checkProjectPermission(globalToken.getUserId(), projectId, Role.TEAMMATE)
            ){
                result = cacheManagerService.getCacheData(projectId, cacheName);
                if (result.isSuccess()){
                    HashMap data = (HashMap) result.getResult();
                    String status = (String) ((LinkedTreeMap)((LinkedTreeMap) data.get("metadata")).get("cacheMetadata")).get("status");
                    if (status.equals("READY")){
                        result = cacheLogService.getEventProps(projectId, cacheName);
                        if (result.isSuccess()){
                            LinkedTreeMap props = (LinkedTreeMap) result.getResult();
                            CreateJdbcCacheRequest queryBody = new CreateJdbcCacheRequest();
                            queryBody.setCacheName((String) ((LinkedTreeMap)props.get("cache")).get("cacheName"));
                            queryBody.setName((String) ((LinkedTreeMap)props.get("cache")).get("cacheName"));
                            result = dataSourceService.getDataSourceById((String) props.get("datasourceId"), globalToken.getUserId());
                            CacheMetadataKey metadataKey = new CacheMetadataKey();
                            metadataKey.setCacheName(cacheName);
                            metadataKey.setProject(projectId);
                            LoadFromJdbcRq.Type type = parceSourceType((String) props.get("type"));
                            if (type!=null){
                                if (result.isSuccess()){
                                    DataSource dataSource = (DataSource) result.getResult();
                                    LoadFromJdbcRq request = new LoadFromJdbcRq()
                                            .setDatasourceId((String) props.get("datasourceId"))
                                            .setEntity((String) props.get("entity"))
                                            .setKey(metadataKey)
                                            .setType(type);
                                    result = cacheManagerService.reloadJdbcCache(request, dataSource);
                                }
                            }
                        }
                    }
                }
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                result.setSuccess(false);
                result.setMessage("invalid token");
                return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
            }
        } catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("invalid token");
            return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        }
    }

    public LoadFromJdbcRq.Type parceSourceType(String type){
        for (LoadFromJdbcRq.Type type1 : LoadFromJdbcRq.Type.values()){
            if (type1.name().equals(type)) return type1;
        }
        return null;
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/create/runtime")
    public ResponseEntity<InternalApiRequest<CacheMetadata>> createRuntimeCache(
            @RequestBody CreateCacheRequest request,
            @RequestHeader String token
    ){
        InternalApiRequest result = new InternalApiRequest();
        try {
            GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
        } catch (Exception e){
            result.setSuccess(false);
            result.setMessage("Invalid token");
            result.setResult(new Message("Invalid token"));
            return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        }
        GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
        if (
                permissionValidator.checkProjectPermission(globalToken.getUserId(), request.getCache().getProject(), Role.ADMIN)
                        |
                permissionValidator.checkProjectPermission(globalToken.getUserId(), request.getCache().getProject(), Role.TEAMMATE)
        ){
            result = cacheManagerService.createRuntimeCache(request);
            cacheLogService.createRuntimeCache(
                    request,
                    globalToken.getEmail(),
                    result,
                    CacheLogService.EventType.CREATE
            );
            return new ResponseEntity<>(cacheManagerService.getCaches(request.getCache().getProject()), HttpStatus.OK);
        } else {
            result.setSuccess(false);
            result.setMessage("Invalid token");
            result.setResult(new Message("Invalid token"));
            return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        }
    }

}
