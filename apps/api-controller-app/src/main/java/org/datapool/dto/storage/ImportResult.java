package org.datapool.dto.storage;

public class ImportResult {
    private ImportRequest request;
    private long size = 0;
    private String message;

    public ImportRequest getRequest() {
        return request;
    }

    public ImportResult setRequest(ImportRequest request) {
        this.request = request;
        return this;
    }

    public long getSize() {
        return size;
    }

    public ImportResult setSize(long size) {
        this.size = size;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ImportResult setMessage(String message) {
        this.message = message;
        return this;
    }
}
