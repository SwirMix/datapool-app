package org.datapool.dto;

import java.util.List;

public class UpdateCacheInfo {
    private String title;
    private String summary;
    private String conditions;
    private List<CacheColumnDesc> columns;

    public String getTitle() {
        return title;
    }

    public UpdateCacheInfo setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getSummary() {
        return summary;
    }

    public UpdateCacheInfo setSummary(String summary) {
        this.summary = summary;
        return this;
    }

    public String getConditions() {
        return conditions;
    }

    public UpdateCacheInfo setConditions(String conditions) {
        this.conditions = conditions;
        return this;
    }

    public List<CacheColumnDesc> getColumns() {
        return columns;
    }

    public UpdateCacheInfo setColumns(List<CacheColumnDesc> columns) {
        this.columns = columns;
        return this;
    }
}
