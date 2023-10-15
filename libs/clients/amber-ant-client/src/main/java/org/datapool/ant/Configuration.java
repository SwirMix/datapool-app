package org.datapool.ant;

import java.util.List;

public class Configuration {
    private String project;
    private String token;
    private String datapool;
    private List<TaskConfig> tasks;
    private int threads;

    public int getThreads() {
        return threads;
    }

    public Configuration setThreads(int threads) {
        this.threads = threads;
        return this;
    }

    public String getProject() {
        return project;
    }

    public Configuration setProject(String project) {
        this.project = project;
        return this;
    }

    public String getToken() {
        return token;
    }

    public Configuration setToken(String token) {
        this.token = token;
        return this;
    }

    public String getDatapool() {
        return datapool;
    }

    public Configuration setDatapool(String datapool) {
        this.datapool = datapool;
        return this;
    }

    public List<TaskConfig> getTasks() {
        return tasks;
    }

    public Configuration setTasks(List<TaskConfig> tasks) {
        this.tasks = tasks;
        return this;
    }

    public static class TaskConfig {
        private String name;
        private String output;
        private String cache;
        private int batch;
        private int threads;
        private int startId;
        private int endId;

        public String getName() {
            return name;
        }

        public TaskConfig setName(String name) {
            this.name = name;
            return this;
        }

        public String getOutput() {
            return output;
        }

        public TaskConfig setOutput(String output) {
            this.output = output;
            return this;
        }

        public String getCache() {
            return cache;
        }

        public TaskConfig setCache(String cache) {
            this.cache = cache;
            return this;
        }

        public int getBatch() {
            return batch;
        }

        public TaskConfig setBatch(int batch) {
            this.batch = batch;
            return this;
        }

        public int getThreads() {
            return threads;
        }

        public TaskConfig setThreads(int threads) {
            this.threads = threads;
            return this;
        }

        public int getStartId() {
            return startId;
        }

        public TaskConfig setStartId(int startId) {
            this.startId = startId;
            return this;
        }

        public int getEndId() {
            return endId;
        }

        public TaskConfig setEndId(int endId) {
            this.endId = endId;
            return this;
        }
    }
}
