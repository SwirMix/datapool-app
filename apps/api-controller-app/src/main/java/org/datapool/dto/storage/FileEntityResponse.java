package org.datapool.dto.storage;

import org.datapool.csv.FileInfo;

public class FileEntityResponse {
    private FileInfo fileInfo;
    private String url;

    public FileEntityResponse setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
        return this;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public String getUrl() {
        return url;
    }

    public FileEntityResponse setUrl(String url) {
        this.url = url;
        return this;
    }
}
