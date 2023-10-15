package org.datapool.dto.commons.dto.commons;

public class CreateCsvCacheRequest extends CreateCacheRq {
    private String fileName;
    private long createDate;
    private String[] columns;

    public String getFileName() {
        return fileName;
    }

    public CreateCsvCacheRequest setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public long getCreateDate() {
        return createDate;
    }

    public CreateCsvCacheRequest setCreateDate(long createDate) {
        this.createDate = createDate;
        return this;
    }

    public String[] getColumns() {
        return columns;
    }

    public CreateCsvCacheRequest setColumns(String[] columns) {
        this.columns = columns;
        return this;
    }
}
