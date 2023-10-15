package org.datapool.jdbc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.datapool.jdbc.dto.PgTableCreate;
import org.datapool.jdbc.dto.TableObject;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.datapool.jdbc.dto.PgQueries.*;

public class JdbcGatewayImpl implements JdbcGateway {
    protected HikariConfig config;
    protected HikariDataSource dataSource;
    protected String schema;

    public String getSchema() {
        return schema;
    }

    public JdbcGatewayImpl setSchema(String schema) {
        this.schema = schema;
        return this;
    }

    public HikariConfig getConfig() {
        return config;
    }

    public JdbcGatewayImpl(HikariConfig config, String schema){
        this.config = config;
        dataSource = new HikariDataSource(config);
        this.schema = schema;
    }

    @Override
    public List<TableObject> getTables() throws SQLException {
        List<TableObject> tables = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()){
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(String.format(GET_TABLES.query, schema));
            while (resultSet.next()){
                String tableName = resultSet.getString(1);
                tables.add(new TableObject().setTableName(tableName));
            }
            resultSet.close();
            for (TableObject tableObject : tables){
                resultSet = statement.executeQuery(
                        String.format(GET_TABLE_COLUMNS.query, tableObject.getTableName(), schema)
                );
                while (resultSet.next()){
                    for (int i = 1; i < resultSet.getMetaData().getColumnCount(); i++){
                        String column = resultSet.getString(1);
                        String type = resultSet.getString(2);
                        tableObject.addColumn(column, type);
                    }
                }
            }
            statement.close();
            connection.close();
        }
        return tables;
    }

    @Override
    public TableObject getTableInfo(String tableName) throws SQLException {
        TableObject tableObject = new TableObject();
        tableObject.setTableName(tableName);
        try (Connection connection = dataSource.getConnection()){
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    String.format(GET_TABLE_COLUMNS.query, tableObject.getTableName(), schema)
            );
            while (resultSet.next()){
                for (int i = 1; i < resultSet.getMetaData().getColumnCount(); i++){
                    String column = resultSet.getString(1);
                    String type = resultSet.getString(2);
                    tableObject.addColumn(column, type);
                }
            }
            connection.close();
        }
        return tableObject;
    }

    @Override
    public boolean checkTableName(String tableName) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(String.format(GET_TABLES.query, schema));
            while (resultSet.next()) {
                if (resultSet.getString(1).equals(tableName)) return true;
            }
            connection.close();
        }
        return false;
    }

    /**
     * create table datapool.projects (
     *     id varchar(36) primary key,
     *     name varchar(128) not null,
     *     description varchar(500) default '',
     *     created date
     * );
     * @param request
     * @return
     * @throws SQLException
     */
    @Override
    public boolean createTable(PgTableCreate request) throws SQLException {
        if (!checkTableName(request.getTableName())){
            StringBuilder builder = new StringBuilder();
            builder.append("create table " + schema + "." + request.getTableName() + " (");
            String[] columns = request.getColumns().keySet().toArray(new String[0]);
            for (int index = 0; index < columns.length; index++){
                if (index == columns.length-1){
                    builder.append(columns[index] + " " + request.getColumns().get(columns[index]) + " )");
                } else {
                    builder.append(columns[index] + " " + request.getColumns().get(columns[index]) + ",");
                }
            }
            try (Connection connection = dataSource.getConnection()){
                String sql = builder.toString();
                System.out.println(sql);
                Statement statement = connection.createStatement();
                statement.executeUpdate(sql);
                statement.execute("commit");
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean insertRequest(TableObject tableObject, Map<String, String> data) throws SQLException {
        StringBuilder builder = new StringBuilder();
        builder.append("insert into " + schema + "." + tableObject.getTableName() + " (");
        String[] columns = tableObject.getColumns().keySet().toArray(new String[0]);
        for (int index = 0; index < columns.length; index++){
            if (index == columns.length-1){
                builder.append(columns[index] + " ) values (");
            } else {
                builder.append(columns[index] + ",");
            }
        }

        for (int index = 0; index < columns.length; index++){
            if (index == columns.length-1){
                builder.append( "'" + data.get(columns[index]) + "'" + " )");
            } else {
                builder.append( "'" + data.get(columns[index]) + "'" + ",");
            }
        }

        try (Connection connection = dataSource.getConnection()){
            String sql = builder.toString();
            System.out.println(sql);
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            statement.execute("commit");
        }
        return true;
    }
}
