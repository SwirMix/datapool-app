package org.datapool.dto;

public class ProjectInfo{
    private String project;
    private int count;

    public String getProject() {
        return project;
    }

    public ProjectInfo setProject(String project) {
        this.project = project;
        return this;
    }

    public int getCount() {
        return count;
    }

    public ProjectInfo setCount(int count) {
        this.count = count;
        return this;
    }
}
