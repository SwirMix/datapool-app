package org.datapool.jdbc;

import org.datapool.jdbc.dto.PgTableCreate;
import org.datapool.jdbc.dto.TableObject;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface JdbcGateway {
    public List<TableObject> getTables() throws SQLException;
    public TableObject getTableInfo(String tableName) throws SQLException;

    public boolean checkTableName(String tableName) throws SQLException;
    public boolean createTable(PgTableCreate request) throws SQLException;
    public boolean insertRequest(TableObject tableObject, Map<String, String> data) throws SQLException;

}
