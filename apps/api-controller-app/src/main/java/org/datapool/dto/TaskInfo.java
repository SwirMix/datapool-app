package org.datapool.dto;

import org.datapool.csv.FileInfo;
import org.datapool.dto.storage.CacheToCsvRequest;

import java.util.concurrent.Future;

public class TaskInfo {
    private CacheToCsvRequest request;
    private FileInfo fileInfo;
    private Future future;
    private String downloadUrl;
    private long createTimestamp;

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public TaskInfo setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
        return this;
    }

    public long getCreateTimestamp() {
        return createTimestamp;
    }

    public TaskInfo setCreateTimestamp(long createTimestamp) {
        this.createTimestamp = createTimestamp;
        return this;
    }

    public TaskInfo(CacheToCsvRequest request, FileInfo fileInfo, Future future){
        this.fileInfo = fileInfo;
        this.future = future;
        this.request = request;
        this.createTimestamp = System.currentTimeMillis();
    }

    public CacheToCsvRequest getRequest() {
        return request;
    }

    public TaskInfo setRequest(CacheToCsvRequest request) {
        this.request = request;
        return this;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public TaskInfo setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
        return this;
    }

    public Future getFuture() {
        return future;
    }

    public TaskInfo setFuture(Future future) {
        this.future = future;
        return this;
    }
}
