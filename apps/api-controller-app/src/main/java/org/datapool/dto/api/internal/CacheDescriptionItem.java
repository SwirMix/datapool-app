package org.datapool.dto.api.internal;

import java.util.Map;

/*
    {
      "lastJobSuccess": false,
      "name": "TC3-GRANT-MASTER-MEGA-OVER-BIT",
      "project": "aa6eb28e-5d9a-4fd9-b37a-d440bf4a0f26",
      "size": 95000000,
      "cacheId": "aa6eb28e-5d9a-4fd9-b37a-d440bf4a0f26_TC3-GRANT-MASTER-MEGA-OVER-BIT",
      "type": "REPSISTENCE",
      "columns": {
        "id": "text",
        "login": "text",
        "password": "text"
      },
      "status": "READY",
      "consumers": 5
    },
 */
public class CacheDescriptionItem {
    private String lastUpdateDate = "";
    private String name;
    private String project;
    private int size;
    private String cacheId;
    private String type;
    private Map<String,String> columns;
    private String status;
    private Double cursor = 0.0;
    private int consumers = 0;

    public Double getCursor() {
        return cursor;
    }

    public CacheDescriptionItem setCursor(Double cursor) {
        this.cursor = cursor;
        return this;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public CacheDescriptionItem setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
        return this;
    }

    public String getName() {
        return name;
    }

    public CacheDescriptionItem setName(String name) {
        this.name = name;
        return this;
    }

    public String getProject() {
        return project;
    }

    public CacheDescriptionItem setProject(String project) {
        this.project = project;
        return this;
    }

    public int getSize() {
        return size;
    }

    public CacheDescriptionItem setSize(int size) {
        this.size = size;
        return this;
    }

    public String getCacheId() {
        return cacheId;
    }

    public CacheDescriptionItem setCacheId(String cacheId) {
        this.cacheId = cacheId;
        return this;
    }

    public String getType() {
        return type;
    }

    public CacheDescriptionItem setType(String type) {
        this.type = type;
        return this;
    }

    public Map<String, String> getColumns() {
        return columns;
    }

    public CacheDescriptionItem setColumns(Map<String, String> columns) {
        this.columns = columns;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public CacheDescriptionItem setStatus(String status) {
        this.status = status;
        return this;
    }

    public int getConsumers() {
        return consumers;
    }

    public CacheDescriptionItem setConsumers(int consumers) {
        this.consumers = consumers;
        return this;
    }
}
