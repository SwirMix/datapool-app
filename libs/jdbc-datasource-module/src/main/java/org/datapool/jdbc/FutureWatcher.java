package org.datapool.jdbc;

import org.datapool.dto.commons.CreateCacheRq;
import org.datapool.dto.commons.CreateJdbcCacheRequest;
import org.datapool.dto.metadata.CacheMetadata;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class FutureWatcher<V> {
    private String uuid;
    private Future<V> future;
    private Date startTime;
    private Object request;
    private V result;

    public V getResult() {
        return result;
    }

    public FutureWatcher setResult(V result) {
        this.result = result;
        return this;
    }

    public FutureWatcher checkFuture(){
        if (future.isDone()){
            try {
                result = future.get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        return this;
    }

    public Object getRequest() {
        return request;
    }

    public FutureWatcher setRequest(Object request) {
        this.request = request;
        return this;
    }

    public FutureWatcher(String uuid, Future future){
        this.future = future;
        this.uuid = uuid;
        this.startTime = new Date();
    }

    public String getUuid() {
        return uuid;
    }

    public FutureWatcher setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public Future<V> getFuture() {
        return future;
    }

    public FutureWatcher setFuture(Future<V> future) {
        this.future = future;
        return this;
    }

    public Date getStartTime() {
        return startTime;
    }

    public FutureWatcher setStartTime(Date startTime) {
        this.startTime = startTime;
        return this;
    }
}
