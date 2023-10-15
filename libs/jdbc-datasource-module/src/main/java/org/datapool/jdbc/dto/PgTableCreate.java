package org.datapool.jdbc.dto;

import java.util.HashMap;
import java.util.Map;

public class PgTableCreate {
    private String tableName;
    private Map<String, String> columns = new HashMap<>();

    public void addColumn(String name, String type){
        columns.put(name, type);
    }

    public String getTableName() {
        return tableName;
    }

    public PgTableCreate setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public Map<String, String> getColumns() {
        return columns;
    }

    public PgTableCreate setColumns(Map<String, String> columns) {
        this.columns = columns;
        return this;
    }
}
