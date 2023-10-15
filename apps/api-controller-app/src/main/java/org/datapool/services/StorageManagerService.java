package org.datapool.services;

import org.apache.ignite.Ignition;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.configuration.ClientConfiguration;
import org.datapool.ExcludeCache;
import org.datapool.csv.FileApiService;
import org.datapool.csv.FileInfo;
import org.datapool.dto.TaskInfo;
import org.datapool.dto.commons.InternalApiRequest;
import org.datapool.dto.metadata.CacheMetadata;
import org.datapool.dto.metadata.CacheMetadataKey;
import org.datapool.dto.storage.CacheToCsvRequest;
import org.datapool.dto.storage.CsvToCache;
import org.datapool.services.tasks.CacheToCsvTask;
import org.datapool.services.tasks.CsvToCacheTask;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class StorageManagerService extends FileApiService {
    protected ClientConfiguration cfg;
    protected ExecutorService executorService;
    protected Map<String, TaskInfo> tasks = new ConcurrentHashMap<>();

    public StorageManagerService(String storage, ClientConfiguration cfg, int executor){
        super(storage);
        this.cfg = cfg;
        this.executorService = Executors.newFixedThreadPool(executor);
    }

    protected IgniteClient getIgniteClient(){
       return Ignition.startClient(cfg);
    }

    public InternalApiRequest export(CsvToCache request, FileInfo fileInfo){
        InternalApiRequest result = new InternalApiRequest();
        try {
            CacheMetadata metadata = (CacheMetadata) getIgniteClient().cache(ExcludeCache.METADATA_CACHE.name()).get(new CacheMetadataKey(request.getCacheName(), request.getProject()));
            CsvToCacheTask task = new CsvToCacheTask(Ignition.startClient(cfg), initParcer(fileInfo), metadata);
            executorService.submit(task);
        } catch (Exception e){
            e.printStackTrace();
            result.setCode(500);
            result.setSuccess(false);
            result.setMessage("Internal error.");
            result.setResult(request);
        }
        result.setCode(200);
        result.setSuccess(true);
        result.setMessage("Success task created.");
        result.setResult(fileInfo);
        return result;
    }
    public InternalApiRequest importCacheToCsv(CacheToCsvRequest request){
        InternalApiRequest result = new InternalApiRequest();
        IgniteClient client = Ignition.startClient(cfg);
        CacheMetadata metadata = (CacheMetadata) client.cache(ExcludeCache.METADATA_CACHE.name()).get(request.getCache());
        if (metadata!=null){
            if (request.getEndId() > metadata.getSize()){
                request.setEndId(metadata.getSize());
            }
            FileInfo fileInfo = new FileInfo()
                    .setFileName(request.getTargetFileName())
                    .setProject(request.getCache().getProject())
                    .setCreateDate(System.currentTimeMillis());
            File target = super.buildResultPath(fileInfo.getFileName(), fileInfo.getProject(), fileInfo.getCreateDate()).toFile();
            CacheToCsvTask task = new CacheToCsvTask(client, request, target);
            Future future = executorService.submit(task);

            tasks.put(fileInfo.getId(), new TaskInfo(request, fileInfo, future));
            result.setCode(200);
            result.setSuccess(true);
            result.setMessage("CREATED");
            result.setResult(fileInfo);
        } else {
            result.setCode(400);
            result.setSuccess(false);
            result.setMessage("Invalid cache");
            result.setResult(request);
        }
        return result;
    }

    public InternalApiRequest<TaskInfo> checkTask(String id){
        InternalApiRequest result = new InternalApiRequest();
        if (tasks.containsKey(id)){
            TaskInfo future = tasks.get(id);
            if (future!=null){
                result.setResult(future);
                result.setCode(200);
                result.setSuccess(true);
            }
        } else {
            result.setCode(404);
            result.setResult(null);
            result.setSuccess(true);
        }
        return result;
    }

    public void clearDoneTasks(){
        for (TaskInfo taskInfo : tasks.values()){
            if (taskInfo.getFuture().isDone()){
                tasks.remove(taskInfo.getFileInfo().getId());
            }
        }
    }

}
