package org.datapool.dto.storage;

import org.datapool.csv.FileInfo;
import org.datapool.dto.metadata.CacheMetadataKey;

public class ImportRequest {
    private CacheMetadataKey metadataKey;
    private FileInfo fileInfo;
    private Type type;

    public Type getType() {
        return type;
    }

    public ImportRequest setType(Type type) {
        this.type = type;
        return this;
    }

    public CacheMetadataKey getMetadataKey() {
        return metadataKey;
    }

    public ImportRequest setMetadataKey(CacheMetadataKey metadataKey) {
        this.metadataKey = metadataKey;
        return this;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public ImportRequest setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
        return this;
    }

    public enum Type {
        CSV,
        CACHE
    }
}
