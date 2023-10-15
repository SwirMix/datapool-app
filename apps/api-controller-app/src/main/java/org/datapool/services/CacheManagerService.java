package org.datapool.services;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import org.datapool.api.*;
import org.datapool.dto.CacheColumnDesc;
import org.datapool.dto.UpdateCacheInfo;
import org.datapool.dto.UpdateCacheInfoRequest;
import org.datapool.dto.api.internal.CacheDescriptionItem;
import org.datapool.dto.api.internal.LoadFromJdbcRq;
import org.datapool.dto.api.internal.UpdateSummary;
import org.datapool.dto.commons.InternalApiRequest;
import org.datapool.dto.db.Cache;
import org.datapool.dto.db.CacheKey;
import org.datapool.dto.db.CacheLog;
import org.datapool.dto.db.DataSource;
import org.datapool.dto.metadata.CacheMetadata;
import org.datapool.dto.metadata.Message;
import org.datapool.model.*;
import org.datapool.model.Status;
import org.datapool.repository.CacheLogRepository;
import org.datapool.repository.CacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.*;

import static org.datapool.Utils.cacheManagerResponseConverter;

@Service
public class CacheManagerService {
    @Autowired
    @Qualifier("cacheManagerServer")
    private Retrofit retrofit;
    @Autowired
    private CacheLogService logService;
    @Autowired
    private CacheLogRepository cacheLogRepository;
    @Autowired
    private CacheRepository cacheRepository;
    private InfoServiceRestControllerApi infoServiceRestControllerApi;
    private CacheCreatorControllerApi cacheCreatorControllerApi;
    private JdbcGatewayControllerApi jdbcGatewayControllerApi;
    private ImportControllerApi importControllerApi;
    private UserTokenControllerApi tokenControllerApi;
    private Health healthApi;

    private Health getHealthApi(){
        if (healthApi == null){
            healthApi = retrofit.create(Health.class);
        }
        return healthApi;
    }

    private UserTokenControllerApi getTokenControllerApi(){
        if (tokenControllerApi == null){
            tokenControllerApi = retrofit.create(UserTokenControllerApi.class);
        }
        return tokenControllerApi;
    }
    private ImportControllerApi getImportControllerApi(){
        if (importControllerApi==null){
            importControllerApi = retrofit.create(ImportControllerApi.class);
        }
        return importControllerApi;
    }

    private InfoServiceRestControllerApi getInfoServiceApi(){
        if (infoServiceRestControllerApi==null){
            infoServiceRestControllerApi = retrofit.create(InfoServiceRestControllerApi.class);
        }
        return infoServiceRestControllerApi;
    }

    private JdbcGatewayControllerApi getJdbcGatewayControllerApi(){
        if (jdbcGatewayControllerApi==null){
            jdbcGatewayControllerApi = retrofit.create(JdbcGatewayControllerApi.class);
        }
        return jdbcGatewayControllerApi;
    }

    private CacheCreatorControllerApi getCacheCreatorService(){
        if (cacheCreatorControllerApi==null){
            cacheCreatorControllerApi = retrofit.create(CacheCreatorControllerApi.class);
        }
        return cacheCreatorControllerApi;
    }

    public Status getHealth() throws IOException {
        return getHealthApi().status().execute().body();
    }

    public InternalApiRequest<CacheMetadata> createJdbcCache(LoadFromJdbcRq request, DataSource dataSource){
        InternalApiRequest result = new InternalApiRequest();
        Call<InternalApiRequest> call = null;
        PostgresJdbcProps props = new PostgresJdbcProps();
        switch (dataSource.getProperties().getType()){
            case POSTGRESQL:
                props.setPassword(dataSource.getProperties().getProperties().getProperty("password"));
                props.setUsername(dataSource.getProperties().getProperties().getProperty("username"));
                props.setUrl(dataSource.getProperties().getProperties().getProperty("url"));
                props.setDriverClassName("org.postgresql.Driver");
                props.setSchema(dataSource.getProperties().getProperties().getProperty("schema"));
                break;
        }
        switch (request.getType()){
            case DEFAULT:
                CreateDefaultJdbcCache body = new CreateDefaultJdbcCache();
                body.setName(request.getKey().getCacheName());
                body.setProjectId(request.getKey().getProject());
                body.setTableName(request.getEntity());
                body.setProperties(props);
                body.setId(dataSource.getId());
                call = getImportControllerApi().createJdbcCache(body);
                break;
            case QUERY:
                CreateJdbcCacheRequest queryBody = new CreateJdbcCacheRequest();
                queryBody.setName(request.getKey().getCacheName());
                queryBody.setProjectId(request.getKey().getProject());
                queryBody.setQuery(request.getEntity());
                queryBody.setProperties(props);
                queryBody.setId(dataSource.getId());
                queryBody.setCacheName(request.getKey().getCacheName());
                call = getImportControllerApi().createJdbcCache(queryBody);
                break;
        }
        try {
            InternalApiRequest object = call.execute().body();
            if (object!=null){
                if (object.isSuccess()){
                    result.setMessage(object.getMessage());
                    result.setSuccess(object.isSuccess());
                    result.setResult(object.getResult());
                    Cache cache = new Cache();
                    cache.setTitle(request.getKey().getCacheName());
                    cache.setSummary("Hello my Friend!");
                    cache.setContent("[]");
                    cache.setId(new CacheKey()
                            .setProjectId(request.getKey().getProject())
                            .setCacheName(request.getKey().getCacheName())
                    );
                    cacheRepository.save(cache);
                    return cacheManagerResponseConverter(object);
                }
            } else {
                result.setMessage("Internal error.");
                result.setSuccess(false);
                result.setResult(new Message("Slave service error"));
                return result;
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        result.setMessage("Internal error.");
        result.setSuccess(false);
        result.setResult(new Message("Slave service error"));

        return result;
    }


    public InternalApiRequest<CacheMetadata> reloadJdbcCache(LoadFromJdbcRq request, DataSource dataSource){
        InternalApiRequest result = new InternalApiRequest();
        Call<InternalApiRequest> call = null;
        PostgresJdbcProps props = new PostgresJdbcProps();
        switch (dataSource.getProperties().getType()){
            case POSTGRESQL:
                props.setPassword(dataSource.getProperties().getProperties().getProperty("password"));
                props.setUsername(dataSource.getProperties().getProperties().getProperty("username"));
                props.setUrl(dataSource.getProperties().getProperties().getProperty("url"));
                props.setDriverClassName("org.postgresql.Driver");
                props.setSchema(dataSource.getProperties().getProperties().getProperty("schema"));
                break;
        }
        switch (request.getType()){
            case DEFAULT:
                CreateDefaultJdbcCache body = new CreateDefaultJdbcCache();
                body.setName(request.getKey().getCacheName());
                body.setProjectId(request.getKey().getProject());
                body.setTableName(request.getEntity());
                body.setProperties(props);
                body.setId(dataSource.getId());
                call = getImportControllerApi().createJdbcCache(body);
                break;
            case QUERY:
                CreateJdbcCacheRequest queryBody = new CreateJdbcCacheRequest();
                queryBody.setName(request.getKey().getCacheName());
                queryBody.setProjectId(request.getKey().getProject());
                queryBody.setQuery(request.getEntity());
                queryBody.setProperties(props);
                queryBody.setId(dataSource.getId());
                queryBody.setCacheName(request.getKey().getCacheName());
                call = getImportControllerApi().reloadJdbcCache(queryBody);
                break;
        }
        try {
            InternalApiRequest object = call.execute().body();
            if (object!=null){
                if (object.isSuccess()){
                    result.setMessage(object.getMessage());
                    result.setSuccess(object.isSuccess());
                    result.setResult(object.getResult());
                    return cacheManagerResponseConverter(object);
                }
            } else {
                result.setMessage("Internal error.");
                result.setSuccess(false);
                result.setResult(new Message("Slave service error"));
                return result;
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        result.setMessage("Internal error.");
        result.setSuccess(false);
        result.setResult(new Message("Slave service error"));

        return result;
    }

    public InternalApiRequest createRuntimeCache(CreateCacheRequest request){
        InternalApiRequest result = new InternalApiRequest();
        try {
            Call<InternalApiRequest> call = getCacheCreatorService().createCache(request);
            InternalApiRequest object = call.execute().body();
            if (object.isSuccess()){
                result.setMessage(object.getMessage());
                result.setSuccess(object.isSuccess());
                result.setResult(object.getResult());
                Cache cache = new Cache();
                cache.setTitle(request.getCache().getCacheName());
                cache.setSummary("Hello my Friend!");
                cache.setContent("[]");
                cache.setId(new CacheKey()
                        .setProjectId(request.getCache().getProject())
                        .setCacheName(request.getCache().getCacheName())
                );
                cacheRepository.save(cache);
                return cacheManagerResponseConverter(object);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        result.setMessage("Internal error.");
        result.setSuccess(false);
        result.setResult(new Message("Slave service error"));
        return result;
    }

    public InternalApiRequest changeCacheStatus(CacheStatus cacheStatus){
        InternalApiRequest result = new InternalApiRequest();
        try {
            Call<InternalApiRequest> call = getCacheCreatorService().changeCacheState(cacheStatus);
            InternalApiRequest object = call.execute().body();
            if (object.isSuccess()){
                result.setMessage(object.getMessage());
                result.setSuccess(object.isSuccess());
                result.setResult(object.getResult());
                return cacheManagerResponseConverter(object);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        result.setMessage("Internal error.");
        result.setSuccess(false);
        result.setResult(new Message("Slave service error"));
        return result;
    }

    public InternalApiRequest getCaches(String project) {
        InternalApiRequest result = new InternalApiRequest();
        try {
            Response<InternalApiRequest> call = getInfoServiceApi()
                    .getProjectCache(project).execute();
            InternalApiRequest object = call.body();
            if (object.isSuccess()){
                result.setMessage(object.getMessage());
                result.setSuccess(object.isSuccess());
                result.setResult(object.getResult());
                InternalApiRequest responseBody = cacheManagerResponseConverter(object);
                LinkedTreeMap body = (LinkedTreeMap) responseBody.getResult();
                List<LinkedTreeMap> caches = (List<LinkedTreeMap>) body.get("caches");
                List<CacheDescriptionItem> finalArray = new ArrayList<>();
                for (LinkedTreeMap cache : caches){
                    CacheDescriptionItem descriptionItem = new CacheDescriptionItem();
                    LinkedTreeMap metadata = (LinkedTreeMap) cache.get("cacheMetadata");
                    LinkedTreeMap cacheInfo = (LinkedTreeMap) metadata.get("cache");
                    descriptionItem.setCacheId((String) cacheInfo.get("publicCacheName"));
                    descriptionItem.setColumns((Map<String, String>) metadata.get("columns"));
                    descriptionItem.setName((String) cacheInfo.get("cacheName"));
                    descriptionItem.setProject((String) cacheInfo.get("project"));
                    Double size = (Double) metadata.get("size");
                    Double cursor = (Double) cache.get("cursor");
                    descriptionItem.setSize(size.intValue());
                    descriptionItem.setCursor(cursor);
                    descriptionItem.setStatus((String) metadata.get("status"));
                    descriptionItem.setType((String) metadata.get("type"));
                    descriptionItem.setLastUpdateDate((String) metadata.get("lastUpdateDate"));

                    finalArray.add(descriptionItem);
                }
                responseBody.setResult(finalArray);
                return responseBody;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        result.setMessage("Internal error.");
        result.setSuccess(false);
        result.setResult(new Message("Slave service error. Cache-manager-service is unavailable."));
        return result;
    }

    public InternalApiRequest updateCacheCursor(String project, String cacheName, Long cursor){
        InternalApiRequest result = new InternalApiRequest();
        UpdateCursorRequest request = new UpdateCursorRequest()
                .setCacheMetadataKey(new org.datapool.dto.metadata.CacheMetadataKey(cacheName, project))
                .setNewCursor(cursor);
        Map content = new HashMap();
        try {
            Response<InternalApiRequest> call = getInfoServiceApi()
                    .updateCursor(request).execute();
            InternalApiRequest object = call.body();
            if (object.isSuccess()){
                result.setMessage(object.getMessage());
                result.setSuccess(object.isSuccess());
                result.setResult(object.getResult());
                InternalApiRequest cacheMetaResult =  cacheManagerResponseConverter(object);
                if (cacheMetaResult.isSuccess()){
                    CacheLog cacheLog = cacheLogRepository.getLastDataView(project, cacheName, "CREATE");
                    content.put("metadata", cacheMetaResult.getResult());
                    content.put("datasource", cacheLog);
                    result.setResult(content);
                    return result;
                } else {
                    return cacheMetaResult;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        result.setMessage("Internal error.");
        result.setSuccess(false);
        result.setResult(new Message("Slave service error"));
        return result;
    }

    public InternalApiRequest updateCacheSummary(UpdateSummary summary){
        InternalApiRequest result = new InternalApiRequest();
        if (summary.getCacheName()!=null && summary.getSummary()!=null){
            Optional<Cache> resultSet = cacheRepository.findById(new CacheKey(summary.getProject(), summary.getCacheName()));
            if (resultSet.isPresent()){
                Cache cache = resultSet.get();
                cache.setSummary(summary.getSummary());
                cacheRepository.save(cache);

                result.setMessage("OK");
                result.setCode(200);
                result.setSuccess(true);
                result.setResult("OK");
                return result;
            } else {
                result.setMessage("Cache summary not found!");
                result.setCode(404);
                result.setSuccess(false);
                result.setResult("");
                return result;
            }
        }
        result.setMessage("Summary or cacheName is null!");
        result.setCode(400);
        result.setSuccess(false);
        result.setResult("");
        return result;
    }

    public InternalApiRequest updateCacheDescription(UpdateCacheInfoRequest request){
        InternalApiRequest result = new InternalApiRequest();
        try {
            if(getCacheData(request.getKey().getProject(), request.getKey().getCacheName()).isSuccess()){
               Cache cache = new Cache();
               UpdateCacheInfo cacheInfo = request.getCacheInfo();
               if (cacheInfo!=null){
                    if (cacheInfo.getConditions()!=null | cacheInfo.getTitle()!=null | cacheInfo.getSummary()!=null | cacheInfo.getColumns()!=null){
                        if (cacheInfo.getColumns().size() == 0){
                            result.setMessage("Bad request");
                            result.setSuccess(false);
                            result.setCode(400);
                            result.setResult(new Message("Bad columns data."));
                            return result;
                        } else {
                            cache.setId(new CacheKey(request.getKey().getProject(), request.getKey().getCacheName()));
                            cache.setTitle(request.getCacheInfo().getTitle());
                            cache.setSummary(request.getCacheInfo().getSummary());
                            cache.setConditions(request.getCacheInfo().getConditions());
                            cache.setContent(new Gson().toJson(request.getCacheInfo().getColumns()));
                            cacheRepository.save(cache);
                            result.setResult(cache);
                            result.setCode(200);
                            result.setSuccess(true);
                            result.setMessage("OK");
                            return result;
                        }
                    } else {
                        result.setMessage("Bad request");
                        result.setSuccess(false);
                        result.setCode(400);
                        result.setResult(new Message("Some fields is null."));
                        return result;
                    }
               } else {
                   result.setMessage("Bad request");
                   result.setSuccess(false);
                   result.setCode(400);
                   result.setResult(new Message("CacheInfo us null."));
                   return result;
               }
            } else {
                result.setMessage("Cache not found");
                result.setSuccess(false);
                result.setCode(404);
                result.setResult(new Message("Cache not found. Bad request."));
                return result;
            }
        } catch (Exception e){
            result.setMessage("Internal error.");
            result.setSuccess(false);
            result.setCode(500);
            result.setResult(new Message(e.getMessage()));
            return result;
        }
    }

    public InternalApiRequest getCacheData(String project, String cacheName){
        InternalApiRequest result = new InternalApiRequest();
        Map content = new HashMap();
        try {
            Response<InternalApiRequest> call = getInfoServiceApi()
                    .getMetadata(project, cacheName).execute();
            InternalApiRequest object = call.body();
            switch (call.code()){
                case 200:
                    if (object.isSuccess()){
                        result.setMessage(object.getMessage());
                        result.setSuccess(object.isSuccess());
                        result.setResult(object.getResult());
                        InternalApiRequest cacheMetaResult = cacheManagerResponseConverter(object);
                        if (cacheMetaResult.isSuccess()){
                            CacheLog cacheLog = cacheLogRepository.getLastDataView(project, cacheName, "CREATE");
                            Optional<Cache> cache = cacheRepository.findById(new CacheKey(project, cacheName));
                            if (cache.isPresent()){
                                content.put("summary", cache.get().getSummary());
                                LinkedTreeMap columns = (LinkedTreeMap) ((LinkedTreeMap)((LinkedTreeMap) result.getResult()).get("cacheMetadata")).get("columns");
                                Cache desc = enrichmentCacheSummary(cache.get(), columns);
                                content.put("title", desc.getTitle());
                                content.put("conditions", desc.getConditions());
                                content.put("columns", new Gson().fromJson(desc.getContent(), List.class));
                            } else {
                                content.put("summary", "no data");
                            }
                            content.put("metadata", cacheMetaResult.getResult());
                            content.put("datasource", cacheLog);
                            result.setResult(content);
                            return result;
                        } else {
                            return cacheMetaResult;
                        }
                    }
                case 400:
                    result.setMessage("Bad data.");
                    result.setCode(400);
                    result.setSuccess(false);
                    return result;
                case 404:
                    result.setMessage("Bad data.");
                    result.setCode(404);
                    result.setSuccess(false);
                    return result;
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        result.setMessage("Internal error.");
        result.setSuccess(false);
        result.setResult(new Message("Slave service error"));
        return result;
    }

    private Cache enrichmentCacheSummary(Cache cache, LinkedTreeMap<String, String> columns){
        if (columns.size()>0 && cache.getContent().equals("[]")){
            List<CacheColumnDesc> columnsInfo = new ArrayList<>();
            for (String column : columns.keySet()){
                CacheColumnDesc columnDesc = new CacheColumnDesc();
                columnDesc.setName(column);
                columnDesc.setType(columns.get(column));
                columnDesc.setDesc("it's just an unrecognized column");
                columnsInfo.add(columnDesc);
            }
            cache.setContent(new Gson().toJson(columnsInfo));
            cacheRepository.save(cache);
        }
        return cache;
    }

    public InternalApiRequest deleteCache(String project, String cacheName){
        InternalApiRequest result = new InternalApiRequest();
        try {
            Response<InternalApiRequest> call = getCacheCreatorService()
                    .deleteCache(project, cacheName).execute();
            InternalApiRequest object = call.body();
            if (object.isSuccess()){
                result.setMessage(object.getMessage());
                result.setSuccess(object.isSuccess());
                result.setResult(object.getResult());
                cacheRepository.deleteById(new CacheKey(project, cacheName));
                return cacheManagerResponseConverter(object);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        result.setMessage("Internal error.");
        result.setSuccess(false);
        result.setResult(new Message("Slave service error"));
        return result;
    }

    public InternalApiRequest deleteToken(String token){
        InternalApiRequest result = new InternalApiRequest();
        try {
            Response<InternalApiRequest> call = getTokenControllerApi().deleteToken(
                    token
            ).execute();
            InternalApiRequest object = call.body();
            if (object.isSuccess()){
                result.setMessage(object.getMessage());
                result.setSuccess(object.isSuccess());
                result.setResult(object.getResult());
                return cacheManagerResponseConverter(object);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        result.setMessage("Internal error.");
        result.setSuccess(false);
        result.setResult(new Message("Slave service error"));
        return result;
    }

    public InternalApiRequest saveTokenInCache(String token, String projectId){
        InternalApiRequest result = new InternalApiRequest();
        try {
            Response<InternalApiRequest> call = getTokenControllerApi().saveRemoteToken(
                    new SaveToken(token, projectId)
            ).execute();
            InternalApiRequest object = call.body();
            if (object.isSuccess()){
                result.setMessage(object.getMessage());
                result.setSuccess(object.isSuccess());
                result.setResult(object.getResult());
                return cacheManagerResponseConverter(object);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        result.setMessage("Internal error.");
        result.setSuccess(false);
        result.setResult(new Message("Slave service error"));
        return result;
    }

    public InternalApiRequest getJdbcTables(DataSource dataSource, String driverClassName){
        InternalApiRequest result = new InternalApiRequest();
        PostgresJdbcProps props = new PostgresJdbcProps();
        props.setPassword(dataSource.getProperties().getProperties().getProperty("password"));
        props.setUsername(dataSource.getProperties().getProperties().getProperty("username"));
        props.setUrl(dataSource.getProperties().getProperties().getProperty("url"));
        props.setDriverClassName(driverClassName);
        props.setSchema(dataSource.getProperties().getProperties().getProperty("schema"));

        try {
            Response<InternalApiRequest> call = getJdbcGatewayControllerApi().getJdbcTables(props).execute();
            InternalApiRequest object = call.body();
            if (object.isSuccess()){
                result.setMessage(object.getMessage());
                result.setSuccess(object.isSuccess());
                result.setResult(object.getResult());
                return cacheManagerResponseConverter(object);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        result.setMessage("Internal error.");
        result.setSuccess(false);
        result.setResult(new Message("Slave service error"));
        return result;
    }
}
