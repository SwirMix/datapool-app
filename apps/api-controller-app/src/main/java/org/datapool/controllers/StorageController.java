package org.datapool.controllers;

import org.datapool.csv.FileInfo;
import org.datapool.dto.GlobalToken;
import org.datapool.dto.TaskInfo;
import org.datapool.dto.commons.CreateCacheRequest;
import org.datapool.dto.commons.InternalApiRequest;
import org.datapool.dto.commons.dto.metadata.Message;
import org.datapool.dto.db.Role;
import org.datapool.dto.metadata.CacheMetadata;
import org.datapool.dto.storage.CacheToCsvRequest;
import org.datapool.dto.storage.CsvToCache;
import org.datapool.dto.storage.FileEntityResponse;
import org.datapool.jwt.GlobalTokenService;
import org.datapool.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/v1/storage/")
public class StorageController {
    @Autowired
    private StorageManagerService fileApiService;
    @Autowired
    private GlobalTokenService globalTokenService;
    @Autowired
    private PermissionValidator permissionValidator;
    @Autowired
    private AccountServiceImpl accountService;
    @Autowired
    private CacheManagerService cacheManagerService;
    @Autowired
    private CacheLogService cacheLogService;
    @Autowired
    private ProjectServiceImpl projectService;
    private static final String TIMESTAMP_REGEXP = "_([\\s\\S]+?)_";
    private static final String FILE_DOWNLOAD_PATTERN = "api/v1/storage/%s/%s/download";

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/import")
    public ResponseEntity<InternalApiRequest> cacheToCsv(
            @RequestBody CacheToCsvRequest body,
            @RequestHeader String token
    ){
        InternalApiRequest result = new InternalApiRequest();
        try {
            GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
            if (permissionValidator.checkProjectPermission(globalToken.getUserId(), body.getCache().getProject())){
                result = fileApiService.importCacheToCsv(body);
            }
            return new ResponseEntity(result, HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("invalid token");
            return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping(value = "/{project}/upload", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }, method = RequestMethod.POST)
    public ResponseEntity uploadFile(
            @RequestParam("file") MultipartFile file,
            @PathVariable String project,
            RedirectAttributes redirectAttributes,
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
                result = fileApiService.uploadFile(file, project);
                redirectAttributes.addFlashAttribute("message",
                        "You successfully uploaded " + file.getOriginalFilename() + "!");
            } else {
                result.setSuccess(false);
                result.setMessage("invalid permissions.");
                return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
            }
            return new ResponseEntity(result, HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("invalid token");
            return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/{project}")
    public ResponseEntity getCsvFiles(
            @PathVariable String project,
            @RequestHeader String token
    ){
        InternalApiRequest result = new InternalApiRequest();
        try {
            GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
            if (permissionValidator.checkProjectPermission(globalToken.getUserId(), project)){
                try {
                    result = fileApiService.getProjectFiles(project);
                    if (result.isSuccess()){
                        List<FileInfo> files = (List<FileInfo>) result.getResult();
                        List<FileEntityResponse> finalData = new ArrayList<>();
                        for (FileInfo fileInfo : files){
                            finalData.add(buildResponse(fileInfo, FILE_DOWNLOAD_PATTERN));
                        }
                        result.setResult(finalData);
                    }

                    if (!result.isSuccess()){
                        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
                    }
                    return new ResponseEntity<>(result, HttpStatus.OK);
                } catch (Exception e){
                    e.printStackTrace();
                    result.setSuccess(false);
                    result.setMessage("Go to log");
                    return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
                }

            }
        } catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("invalid token");
            return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/{project}/{id}")
    public ResponseEntity getCsvInfo(
            @PathVariable String project,
            @PathVariable String id,
            @RequestHeader String token
    ){
        InternalApiRequest result = new InternalApiRequest();

        try {
            GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
            if (permissionValidator.checkProjectPermission(globalToken.getUserId(), project)){

                FileInfo fileInfo = new FileInfo();
                Matcher matcher = Pattern.compile(TIMESTAMP_REGEXP).matcher(id);
                matcher.find();
                long timestamp = Long.parseLong(id.substring(matcher.start(), matcher.end()).replace("_", ""));
                String fileName = id.replace(project + "_" + timestamp + "_", "");
                fileInfo.setFileName(fileName);
                fileInfo.setCreateDate(timestamp);
                fileInfo.setProject(project);
                try {
                    result = fileApiService.checkCsv(fileInfo);
                    if (!result.isSuccess()){
                        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
                    }
                    result.setResult(buildResponse((FileInfo) result.getResult(), FILE_DOWNLOAD_PATTERN));
                    return new ResponseEntity<>(result, HttpStatus.OK);
                } catch (Exception e){
                    e.printStackTrace();
                    result.setSuccess(false);
                    result.setMessage("Go to log");
                    return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("invalid token");
            return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/task/{project}/{id}")
    public ResponseEntity checkTask(
            @PathVariable String project,
            @PathVariable String id,
            @RequestHeader String token
    ){
        InternalApiRequest result = new InternalApiRequest();

        try {
            GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
            if (permissionValidator.checkProjectPermission(globalToken.getUserId(), project)){
                try {
                    result = fileApiService.checkTask(id);
                    if (!result.isSuccess()){
                        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
                    }
                    TaskInfo taskInfo = (TaskInfo) result.getResult();
                    if (taskInfo.getFuture().isDone()){
                        taskInfo.setDownloadUrl(String.format(FILE_DOWNLOAD_PATTERN, taskInfo.getFileInfo().getProject(), taskInfo.getFileInfo().getId()));
                    }
                    result.setResult(taskInfo);
                    return new ResponseEntity<>(result, HttpStatus.OK);
                } catch (Exception e){
                    e.printStackTrace();
                    result.setSuccess(false);
                    result.setMessage("Go to log");
                    return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("invalid token");
            return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/{project}/{id}/download")
    public ResponseEntity downloadCsvFile(
            @PathVariable String project,
            @PathVariable String id,
            @RequestHeader String token
    ){
        InternalApiRequest result = new InternalApiRequest();
        GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
        FileInfo fileInfo = new FileInfo();
        Matcher matcher = Pattern.compile(TIMESTAMP_REGEXP).matcher(id);
        matcher.find();
        long timestamp = Long.parseLong(id.substring(matcher.start(), matcher.end()).replace("_", ""));
        String fileName = id.replace(project + "_" + timestamp + "_", "");
        fileInfo.setFileName(fileName);
        fileInfo.setCreateDate(timestamp);
        fileInfo.setProject(project);
        try {
            File file = fileApiService.buildResultPath(fileName, project, timestamp).toFile();
            if (file.exists()){
                InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
                HttpHeaders header = new HttpHeaders();
                header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
                header.add("Cache-Control", "no-cache, no-store, must-revalidate");
                header.add("Pragma", "no-cache");
                header.add("Expires", "0");
                return ResponseEntity.ok()
                        .headers(header)
                        .contentLength(file.length())
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(resource);
            } else {
                result.setResult(new Message("not found file with id: " + id));
                result.setSuccess(false);
                result.setMessage("not found file with id: " + id);
                result.setCode(404);
                return new ResponseEntity(result, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("Go to log");
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @DeleteMapping("/{project}/delete/{fileId}")
    public ResponseEntity deleteFile(
            @PathVariable String project,
            @PathVariable String fileId,
            @RequestHeader String token
    ){
        InternalApiRequest result = new InternalApiRequest();
        GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
        if (
                permissionValidator.checkProjectPermission(globalToken.getUserId(), project, Role.ADMIN)
                        |
                permissionValidator.checkProjectPermission(globalToken.getUserId(), project, Role.TEAMMATE)
        ){
            try {
                try {
                    FileInfo fileInfo = new FileInfo();
                    Matcher matcher = Pattern.compile(TIMESTAMP_REGEXP).matcher(fileId);
                    matcher.find();
                    long timestamp = Long.parseLong(fileId.substring(matcher.start(), matcher.end()).replace("_", ""));
                    String fileName = fileId.replace(project + "_" + timestamp + "_", "");
                    fileInfo.setFileName(fileName);
                    fileInfo.setCreateDate(timestamp);
                    fileInfo.setProject(project);
                    result = fileApiService.deleteFile(fileInfo);
                    if (!result.isSuccess()){
                        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
                    }
                    return new ResponseEntity<>(result, HttpStatus.OK);
                } catch (Exception e){
                    e.printStackTrace();
                    result.setSuccess(false);
                    result.setMessage("Go to log");
                    return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } catch (Exception e){
                e.printStackTrace();
                result.setSuccess(false);
                result.setMessage("invalid token");
                return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
            }
        } else {
            result.setSuccess(false);
            result.setMessage("invalid token");
            return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/export")
    public ResponseEntity exportToCache(
            @RequestBody CsvToCache request,
            @RequestHeader String token
    ){
        InternalApiRequest result = new InternalApiRequest();
        GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
        if (
                permissionValidator.checkProjectPermission(globalToken.getUserId(), request.getProject(), Role.ADMIN)
                        |
                permissionValidator.checkProjectPermission(globalToken.getUserId(), request.getProject(), Role.TEAMMATE)
        ){
            try {
                try {
                    CreateCacheRequest cacheRequest = new CreateCacheRequest();
                    cacheRequest.setCacheType(CacheMetadata.CacheType.PERSISTENCE);

                    FileInfo fileInfo = new FileInfo();
                    Matcher matcher = Pattern.compile(TIMESTAMP_REGEXP).matcher(request.getFileId());
                    matcher.find();
                    long timestamp = Long.parseLong(request.getFileId().substring(matcher.start(), matcher.end()).replace("_", ""));
                    String fileName = request.getFileId().replace(request.getProject() + "_" + timestamp + "_", "");
                    fileInfo.setFileName(fileName);
                    fileInfo.setCreateDate(timestamp);
                    fileInfo.setProject(request.getProject());
                    result = fileApiService.checkCsv(fileInfo);
                    Map<String, String> columns = new HashMap<>();
                    if (result.isSuccess()){
                        fileInfo = (FileInfo) result.getResult();
                        for (String column : fileInfo.getColumns()){
                            columns.put(column, "text");
                        }

                        result = cacheManagerService.getCacheData(request.getProject(), request.getCacheName());
                        if (result.isSuccess()){
                            result = new InternalApiRequest();
                            result.setSuccess(false);
                            result.setMessage("Cache already exist. Rename!");
                            result.setCode(400);
                            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
                        }

                        org.datapool.model.CreateCacheRequest createCacheRequest = new org.datapool.model.CreateCacheRequest();
                        org.datapool.model.CacheMetadataKey cacheMetadataKey = new org.datapool.model.CacheMetadataKey();
                        cacheMetadataKey.setProject(request.getProject());
                        cacheMetadataKey.setCacheName(request.getCacheName());
                        createCacheRequest.setCacheType(org.datapool.model.CreateCacheRequest.CacheTypeEnum.PERSISTENCE);
                        createCacheRequest.setCache(cacheMetadataKey);
                        createCacheRequest.setColumns(columns);
                        result = cacheManagerService.createRuntimeCache(createCacheRequest);
                        org.datapool.model.CreateCacheRequest dtoRequest = new org.datapool.model.CreateCacheRequest();
                        dtoRequest.setColumns(columns);
                        dtoRequest.setCache(cacheMetadataKey);
                        dtoRequest.setCacheType(org.datapool.model.CreateCacheRequest.CacheTypeEnum.PERSISTENCE);
                        cacheLogService.createRuntimeCache(
                                dtoRequest,
                                globalToken.getEmail(),
                                result,
                                CacheLogService.EventType.CREATE
                        );
                        if (result.isSuccess()){
                            result = fileApiService.export(request, fileInfo);
                        }
                    }
                    if (!result.isSuccess()){
                        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
                    }
                    return new ResponseEntity<>(result, HttpStatus.OK);
                } catch (Exception e){
                    e.printStackTrace();
                    result.setSuccess(false);
                    result.setMessage("Go to log");
                    return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } catch (Exception e){
                e.printStackTrace();
                result.setSuccess(false);
                result.setMessage("invalid token");
                return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
            }
        } else {
            result.setSuccess(false);
            result.setMessage("invalid token");
            return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        }
    }


    public static FileEntityResponse buildResponse(FileInfo fileInfo, String pattern){
        FileEntityResponse body = new FileEntityResponse()
                .setFileInfo(fileInfo).setUrl(String.format(pattern, fileInfo.getProject(), fileInfo.getId()));
        return body;
    }
}
