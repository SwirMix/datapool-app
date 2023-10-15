package org.datapool.ant.service;

public class AmberWorkerConfig {
    private String project;
    private String token;
    private String url;
    private int threads = 10;
    private ExportTask task;

    public ExportTask getTask() {
        return task;
    }

    public AmberWorkerConfig setTask(ExportTask task) {
        this.task = task;
        return this;
    }

    public AmberWorkerConfig(String project, String token, String url){
        this.project = project;
        this.url = url;
        this.token = token;
    }

    public int getThreads() {
        return threads;
    }

    public AmberWorkerConfig setThreads(int threads) {
        this.threads = threads;
        return this;
    }

    public String getProject() {
        return project;
    }


    public String getToken() {
        return token;
    }


    public String getUrl() {
        return url;
    }

}
