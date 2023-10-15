package org.datapool.jdbc.dto;

public enum PgQueries {
    GET_TABLES("SELECT table_name FROM information_schema.tables WHERE table_schema = '%s'"),
    GET_TABLE_COLUMNS("SELECT column_name, data_type FROM information_schema.columns WHERE table_name = '%s' and table_schema = '%s' ORDER BY ordinal_position");

    public String query;
    PgQueries(String query){
        this.query = query;
    }
}
