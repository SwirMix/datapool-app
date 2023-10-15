package org.datapool.csv;

public class FileInfo {
    private String project;
    private String fileName;
    private long createDate = System.currentTimeMillis();
    private long size;
    private String[] columns;
    private long rows = 0;

    public String getId(){
        return project + "_" + createDate + "_" + fileName;
    }

    public String[] getColumns() {
        return columns;
    }

    public FileInfo setColumns(String[] columns) {
        this.columns = columns;
        return this;
    }

    public long getRows() {
        return rows;
    }

    public FileInfo setRows(long rows) {
        this.rows = rows;
        return this;
    }

    public long getSize() {
        return size;
    }

    public FileInfo setSize(long size) {
        this.size = size;
        return this;
    }

    public long getCreateDate() {
        return createDate;
    }

    public FileInfo setCreateDate(long createDate) {
        this.createDate = createDate;
        return this;
    }

    public FileInfo(){

    }
    public FileInfo(String project, String fileName, long timestamp){
        this.fileName = fileName;
        this.project = project;
        this.createDate = timestamp;
    }


    public FileInfo(String project, String fileName){
        this.fileName = fileName;
        this.project = project;
    }

    public String getProject() {
        return project;
    }

    public FileInfo setProject(String project) {
        this.project = project;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public FileInfo setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }
}
