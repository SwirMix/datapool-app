package org.datapool.services;

import com.google.gson.internal.LinkedTreeMap;
import org.datapool.dto.api.internal.LoadFromJdbcRq;
import org.datapool.model.CreateCacheRequest;
import org.datapool.dto.commons.InternalApiRequest;
import org.datapool.dto.db.CacheLog;
import org.datapool.dto.db.CacheLogEntity;
import org.datapool.repository.CacheLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CacheLogService {
    @Autowired
    private CacheLogRepository logRepository;

    public InternalApiRequest selectCacheAction(String project, Pageable page){
        InternalApiRequest result = new InternalApiRequest();
        try {
            Page<CacheLog> events = logRepository.findByProjectId(project, page);
            result.setResult(events);
            result.setSuccess(true);
            result.setMessage("OK");
        } catch (Exception e){
            e.printStackTrace();
            result.setMessage("Internal error. Go to logs.");
            result.setSuccess(false);
            result.setResult(new ArrayList<CacheLog>());
        }
        return result;
    }

    public InternalApiRequest getEventProps(String projectId, String cacheNane){
        InternalApiRequest result = new InternalApiRequest();

        CacheLog logEvent = logRepository.getLastDataView(projectId, cacheNane, "CREATE");
        if (logEvent!=null){
            CacheLogEntity logEntity = logEvent.getData();
            LinkedTreeMap props = logEntity.getProps();
            result.setResult(props);
            result.setSuccess(true);
            result.setCode(200);
            result.setMessage(logEvent.getId());
            return result;
        } else {
            result.setCode(404);
            result.setMessage("Not found event");
            result.setSuccess(false);
        }
        return result;
    }

    public void createRuntimeCache(CreateCacheRequest request, String userEmail, InternalApiRequest result, EventType type){
        try {
            CacheLogEntity props = new CacheLogEntity();
            props.setMessage(result.getMessage());
            props.setSuccess(result.isSuccess());
            props.setType(type.name());
            props.setUserEmail(userEmail);
            props.setProps(convertCreateCacheRequest(request));
            CacheLog message = new CacheLog();
            message.setType(type.name());
            message.setId(UUID.randomUUID().toString());
            message.setCacheName(request.getCache().getCacheName());
            message.setProjectId(request.getCache().getProject());
            message.setDate(new Date());
            message.setData(props);
            logRepository.save(message);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void createJdbcCache(LoadFromJdbcRq request, String userEmail, InternalApiRequest result, EventType type){
        try {
            CacheLogEntity props = new CacheLogEntity();
            props.setMessage(result.getMessage());
            props.setSuccess(result.isSuccess());
            props.setType(type.name());
            props.setUserEmail(userEmail);
            props.setProps(convertJdbcCreateRequest(request));
            CacheLog message = new CacheLog();
            message.setType(type.name());
            message.setId(UUID.randomUUID().toString());
            message.setCacheName(request.getKey().getCacheName());
            message.setProjectId(request.getKey().getProject());
            message.setDate(new Date());
            message.setData(props);
            logRepository.save(message);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void dropCache(String project, String cacheName, String email, InternalApiRequest result){
        try {
            CacheLog message = new CacheLog();
            CacheLog lastCreate = logRepository.getLastDataView(project, cacheName, EventType.CREATE.name());
            message.setProjectId(project);
            message.setCacheName(cacheName);
            message.setId(UUID.randomUUID().toString());
            message.setType(EventType.DELETE.name());
            message.setDate(new Date());

            CacheLogEntity entity = new CacheLogEntity();
            entity.setType(EventType.DELETE.name());
            entity.setUserEmail(lastCreate.getData().getUserEmail());
            entity.setMessage(result.getMessage());
            entity.setProps(lastCreate.getData().getProps());
            entity.setSuccess(true);
            message.setData(entity);
            logRepository.save(message);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    protected LinkedTreeMap convertJdbcCreateRequest(LoadFromJdbcRq request){
        LinkedTreeMap props = new LinkedTreeMap();
        props.put("datasourceId", request.getDatasourceId());
        props.put("entity", request.getEntity());
        props.put("type", request.getType().name());
        props.put("cache", request.getKey());
        return props;
    }

    protected LinkedTreeMap convertCreateCacheRequest(CreateCacheRequest request){
        LinkedTreeMap props = new LinkedTreeMap();
        props.put("cache", request.getCache());
        props.put("columns", request.getColumns());
        props.put("cacheType", request.getCacheType());
        props.put("type", "RUNTIME");
        props.put("entity", "manual request");
        props.put("datasourceId", "none");
        return props;
    }

    public enum EventType {
        CREATE,
        DELETE,
    }
}
