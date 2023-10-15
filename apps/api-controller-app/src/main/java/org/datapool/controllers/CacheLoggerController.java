package org.datapool.controllers;

import com.google.gson.internal.LinkedTreeMap;
import org.datapool.ApplicationConfiguration;
import org.datapool.Utils;
import org.datapool.dto.DataPoolItem;
import org.datapool.dto.GlobalToken;
import org.datapool.dto.api.internal.AuthRs;
import org.datapool.dto.commons.InternalApiRequest;
import org.datapool.dto.metadata.Message;
import org.datapool.jwt.GlobalTokenService;
import org.datapool.services.CacheLogService;
import org.datapool.services.CacheManagerService;
import org.datapool.services.DatapoolService;
import org.datapool.services.PermissionValidator;
import org.datapool.victoria.ConsumersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/log")
public class CacheLoggerController {
    @Autowired
    private GlobalTokenService globalTokenService;
    @Autowired
    private PermissionValidator permissionValidator;
    @Autowired
    private CacheLogService cacheLogService;
    @Autowired
    private ConsumersService consumersService;
    @Autowired
    private DatapoolService datapoolService;
    @Autowired
    private ApplicationConfiguration configuration;

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/consumers/{projectId}/{cache}")
    public ResponseEntity<InternalApiRequest> getConsumersData(
            @PathVariable String projectId,
            @PathVariable String cache,
            @RequestParam("start") String start,
            @RequestParam("end") String end,
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
        if (permissionValidator.checkProjectPermission(globalToken.getUserId(), projectId)){
            result = consumersService.getConsumersDataByRange(start, end, projectId, cache);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.setSuccess(false);
            result.setMessage("Not found project or permissions.");
            result.setResult(new Message("Not found project or permissions."));
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/consumers/{projectId}")
    public ResponseEntity<InternalApiRequest> getConsumersDataByProject(
            @PathVariable String projectId,
            @RequestParam("start") String start,
            @RequestParam("end") String end,
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
        if (permissionValidator.checkProjectPermission(globalToken.getUserId(), projectId)){
            result = consumersService.getConsumersDataByProjectAndRange(start, end, projectId);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.setSuccess(false);
            result.setMessage("Not found project or permissions.");
            result.setResult(new Message("Not found project or permissions."));
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/{projectId}")
    public ResponseEntity<InternalApiRequest> getEvents(
            @PathVariable String projectId,
            @RequestHeader String token,
            @RequestParam(required = false) Integer page
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
        if (permissionValidator.checkProjectPermission(globalToken.getUserId(), projectId)){
            if (page == null){
                page = 1;
            } else {
                if (page < 0){
                    page = 1;
                }
            }
            Pageable pageable = PageRequest.of(page-1, 10);
            result = cacheLogService.selectCacheAction(projectId, pageable);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.setSuccess(false);
            result.setMessage("Not found project or permissions.");
            result.setResult(new Message("Not found project or permissions."));
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }
}
