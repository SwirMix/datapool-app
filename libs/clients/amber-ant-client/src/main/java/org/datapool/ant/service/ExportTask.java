package org.datapool.ant.service;

import java.io.File;

public class ExportTask {
    private String cacheName;
    private long startId;
    private long endId;
    private long batch = 100;
    private File output;

    public long getBatch() {
        return batch;
    }

    public ExportTask setBatch(long batch) {
        this.batch = batch;
        return this;
    }

    public String getCacheName() {
        return cacheName;
    }

    public ExportTask setCacheName(String cacheName) {
        this.cacheName = cacheName;
        return this;
    }

    public long getStartId() {
        return startId;
    }

    public ExportTask setStartId(long startId) {
        this.startId = startId;
        return this;
    }

    public long getEndId() {
        return endId;
    }

    public ExportTask setEndId(long endId) {
        this.endId = endId;
        return this;
    }

    public File getOutput() {
        return output;
    }

    public ExportTask setOutput(File output) {
        this.output = output;
        return this;
    }
}
