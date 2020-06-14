package com.utility.reflection;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

public class TableMetaExampleMain {

    private static final String DRIVER_NAME = "com.mysql.jdbc.Driver";

    private static final String USERNAME = "USERNAME";
    private static final String PASSWORD = "PASSWORD";
    private static final String SCHEMA = "main";
    private static final String HOSTNAME = "jdbc:mysql://HOSTNAME:3306";
    private static final String CONNECTION_PARAM = "?autoReconnect=true&useUnicode=true&characterEncoding=utf8&mysqlEncoding=utf8";
    private static final String TABLE_NAME = "event";

    public static void main(String[] args) throws Exception {
        new TableMetaExampleMain().run();
    }

    private void run() throws Exception {
        DataSource dataSource = this.createDataSource();
        Connection connection = this.getConnection(dataSource);
        DatabaseMetaData databaseMetaData = connection.getMetaData();

        Map<String, String> tableMeta = this.getTableMeta(databaseMetaData);
        tableMeta.forEach((k, v) -> System.out.println("Key : " + k + ", Value :" + v));

        List<Map<String, Object>> columnMetaList = this.getColumnMetaList(databaseMetaData);
        for (Map<String, Object> meta : columnMetaList) {
            meta.forEach((k, v) -> System.out.println("Key : " + k + ", Value :" + v));
        }
    }

    private Map<String, String> getTableMeta(DatabaseMetaData databaseMetaData) throws Exception {
        ResultSet rs = databaseMetaData.getTables(SCHEMA, null, TABLE_NAME, null);
        rs.next();

        Map<String, String> tableMetaMap = new HashMap<>();
        tableMetaMap.put("TABLE_CAT", rs.getString("TABLE_CAT"));
        tableMetaMap.put("TABLE_SCHEM", rs.getString("TABLE_SCHEM"));
        tableMetaMap.put("TABLE_NAME", rs.getString("TABLE_NAME"));
        tableMetaMap.put("TABLE_TYPE", rs.getString("TABLE_TYPE"));
        tableMetaMap.put("REMARKS", rs.getString("REMARKS"));

        return tableMetaMap;
    }

    private List<Map<String, Object>> getColumnMetaList(DatabaseMetaData databaseMetaData) throws Exception {
        List<Map<String, Object>> columnList = new ArrayList<>();
        ResultSet rs = databaseMetaData.getColumns(SCHEMA, null, TABLE_NAME, "%");

        while (rs.next()) {
            Map<String, Object> infoMap = new HashMap<>();
            infoMap.put("TABLE_CAT", rs.getString("TABLE_CAT"));
            infoMap.put("TABLE_SCHEM", rs.getString("TABLE_SCHEM"));
            infoMap.put("TABLE_NAME", rs.getString("TABLE_NAME"));
            infoMap.put("COLUMN_NAME", rs.getString("COLUMN_NAME"));
            infoMap.put("DATA_TYPE", rs.getInt("DATA_TYPE"));
            infoMap.put("TYPE_NAME", rs.getString("TYPE_NAME"));
            infoMap.put("COLUMN_SIZE", rs.getInt("COLUMN_SIZE"));
            infoMap.put("BUFFER_LENGTH", rs.getInt("BUFFER_LENGTH"));
            infoMap.put("DECIMAL_DIGITS", rs.getInt("DECIMAL_DIGITS"));
            infoMap.put("NUM_PREC_RADIX", rs.getInt("NUM_PREC_RADIX"));
            infoMap.put("NULLABLE", rs.getInt("NULLABLE"));
            infoMap.put("REMARKS", rs.getString("REMARKS"));
            infoMap.put("COLUMN_DEF", rs.getString("COLUMN_DEF"));
            infoMap.put("SQL_DATA_TYPE", rs.getShort("SQL_DATA_TYPE"));
            infoMap.put("SQL_DATETIME_SUB", rs.getShort("SQL_DATETIME_SUB"));
            infoMap.put("CHAR_OCTET_LENGTH", rs.getInt("CHAR_OCTET_LENGTH"));
            infoMap.put("ORDINAL_POSITION", rs.getInt("ORDINAL_POSITION"));
            infoMap.put("IS_NULLABLE", rs.getString("IS_NULLABLE"));
            infoMap.put("SCOPE_CATALOG", rs.getString("SCOPE_CATALOG"));
            infoMap.put("SCOPE_SCHEMA", rs.getString("SCOPE_SCHEMA"));
            infoMap.put("SCOPE_TABLE", rs.getString("SCOPE_TABLE"));
            infoMap.put("SOURCE_DATA_TYPE", rs.getShort("SOURCE_DATA_TYPE"));
            infoMap.put("IS_AUTOINCREMENT", rs.getString("IS_AUTOINCREMENT"));

            columnList.add(infoMap);
        }

        return columnList;
    }

    private BasicDataSource createDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(DRIVER_NAME);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setUrl(HOSTNAME);
        return dataSource;
    }

    public Connection getConnection(DataSource dataSource) throws Exception {
        return dataSource.getConnection();
    }
}
