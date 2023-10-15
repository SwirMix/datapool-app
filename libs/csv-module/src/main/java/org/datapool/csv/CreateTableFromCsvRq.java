package org.datapool.csv;

public class CreateTableFromCsvRq {
    private String tableName;
    private FileInfo fileInfo;

    public String getTableName() {
        return tableName;
    }

    public CreateTableFromCsvRq setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public CreateTableFromCsvRq setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
        return this;
    }
}
