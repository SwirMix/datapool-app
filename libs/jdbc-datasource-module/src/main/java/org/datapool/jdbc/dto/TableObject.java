package org.datapool.jdbc.dto;

import org.datapool.dto.metadata.CacheStatus;

import java.util.HashMap;
import java.util.Map;

public class TableObject {
    private String tableName;
    private Map<String, String> columns = new HashMap<>();

    public TableObject addColumn(String column, String type){
        this.columns.put(column, type);
        return this;
    }

    public String getTableName() {
        return tableName;
    }

    public TableObject setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public Map<String, String> getColumns() {
        return columns;
    }

    public TableObject setColumns(Map<String, String> columns) {
        this.columns = columns;
        return this;
    }
}
