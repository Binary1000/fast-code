package com.cnqisoft.fastcode;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.pool.DruidPooledResultSet;
import com.alibaba.fastjson.JSON;
import com.mysql.jdbc.ResultSetImpl;
import com.mysql.jdbc.RowData;

import javax.sql.DataSource;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

/**
 * @author Binary
 */
public class DBUtil {

    private static final ThreadLocal<Connection> threadLocal;

    private static final String FIND_ONE_LIMIT = " LIMIT 1";

    private static final DataSource dataSource;

    private DBUtil() {
    }

    static {
        threadLocal = new ThreadLocal<>();
        try(
                InputStream inputStream = DBUtil.class.getResourceAsStream("/db_config.properties");
        ) {
            Properties properties = new Properties();
            properties.load(inputStream);
            dataSource = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("连接池初始化失败！");
        }
    }

    public static Connection getConnection() throws SQLException {
        Connection threadLocalConnection = threadLocal.get();
        return threadLocalConnection == null ? dataSource.getConnection() : threadLocalConnection;
    }


    public static void startTransaction() throws SQLException {
        Connection connection;
        if (threadLocal.get() == null) {
            connection = getConnection();
            threadLocal.set(connection);
        } else {
            connection = threadLocal.get();
        }
        connection.setAutoCommit(false);
    }


    private static void commitOrRollback(boolean success) {
        Connection connection = threadLocal.get();
        try {
            if (connection != null) {
                if (success) {
                    connection.commit();
                } else {
                    connection.rollback();
                }
                connection.setAutoCommit(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadLocal.remove();
            closeConnection(connection);
        }
    }

    /**
     * 提交事务
     */
    public static void commit() {
        commitOrRollback(true);
    }

    /**
     * 回滚事务
     */
    public static void rollback() {
        commitOrRollback(false);
    }

    /**
     * 关闭连接
     *
     * @param connection 数据库连接
     */
    public static void closeConnection(Connection connection) {
        try {
            if (connection != null && connection.getAutoCommit()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void release(Connection connection, ResultSet resultSet, Statement statement) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        release(connection, statement);
    }

    public static void release(Connection connection, Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        closeConnection(connection);
    }

    /**
     * 统计select的条数
     *
     * @param sql        执行的sql
     * @param parameters 参数
     * @return 条数
     * @throws SQLException sql异常
     */
    public static long count(String sql, Object... parameters) throws SQLException {
        sql = String.format("SELECT count(*) as number FROM ( %s ) a", sql);
        return (long) DBUtil.findOne(sql, parameters).get("number");
    }

    /**
     * 查询sql的结果是否存在
     *
     * @param sql        执行的sql
     * @param parameters 参数
     * @return 是否存在
     * @throws SQLException sql异常
     */
    public static boolean isExist(String sql, Object... parameters) throws SQLException {
        sql = String.format("SELECT EXISTS ( %s ) AS is_exist", sql);
        return (Long) DBUtil.findOne(sql, parameters).get("is_exist") == 1;
    }

    public static String querySqlToJson(String sql, Object... parameters) throws SQLException {
        try (
                DBEntity dbEntity = getDBQueryEntity(sql, parameters)
        ) {
            ResultSet resultSet = dbEntity.getResultSet();
            return resultToJson(resultSet);
        }
    }

    public static <T> T findOne(String originalSql, Class<T> clazz, Object... parameters) throws SQLException {
        String sql = originalSql + FIND_ONE_LIMIT;
        try (
                DBEntity dbEntity = getDBQueryEntity(sql, parameters)
        ) {
            ResultSet resultSet = dbEntity.getResultSet();
            ResultSetMetaData metaData = resultSet.getMetaData();
            if (resultSet.next()) {
                int columnCount = metaData.getColumnCount();
                return getRowData(resultSet, metaData, columnCount, clazz);
            }
        }
        return null;
    }

    public static Map<Object, Object> findOne(String originalSql, Object... parameters) throws SQLException {
        String sql = originalSql + FIND_ONE_LIMIT;
        try (
                DBEntity dbEntity = getDBQueryEntity(sql, parameters)
        ) {
            ResultSet resultSet = dbEntity.getResultSet();
            ResultSetMetaData metaData = resultSet.getMetaData();
            if (resultSet.next()) {
                int columnCount = metaData.getColumnCount();
                return getRowData(resultSet, metaData, columnCount);
            }
        }
        return Collections.emptyMap();
    }

    /**
     * 执行查询sql (select)
     *
     * @param sql        执行的sql
     * @param parameters 参数
     * @return 成功后的list
     * @throws SQLException sql异常
     */
    public static List<Map<Object, Object>> querySqlToList(String sql, Object... parameters) throws SQLException {
        try (
                DBEntity dbEntity = getDBQueryEntity(sql, parameters)
        ) {
            return resultSetToList(dbEntity.getResultSet());
        }
    }

    public static <T> List<T> querySqlToList(String sql, Class<T> clazz, Object... parameters) throws SQLException {
        try (
                DBEntity dbEntity = getDBQueryEntity(sql, parameters)
        ) {
            return resultSetToList(dbEntity.getResultSet(), clazz);
        }
    }

    /**
     * 执行更新sql（insert, update, delete, create table, drop table...等等)
     *
     * @param sql        执行的sql
     * @param parameters 参数
     * @return 执行insert操作成功后的自增id
     * @throws SQLException sql异常
     */
    public static long executeUpdate(String sql, Object... parameters) throws SQLException {
        try (
                DBEntity dbEntity = getDBUpdateEntity(sql, parameters)
        ) {
            ResultSet generatedKeys = dbEntity.getResultSet();
            if (generatedKeys.next()) {
                return generatedKeys.getLong(1);
            }
        }
        return -1;
    }

    public static void batchUpdate(String sql, Object[] parameters) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            for (Object parameter : parameters) {
                preparedStatement.setObject(1, parameter);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } finally {
            release(connection, preparedStatement);
        }
    }

    public static void batchUpdate(String sql, List<Object[]> parameterList) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            for (Object[] parameters : parameterList) {
                setParameters(preparedStatement, parameters);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } finally {
            release(connection, preparedStatement);
        }
    }

    public static Map<String, Map<Object, Object>> querySqlToMap(String sql, String key) throws SQLException {
        try (
                DBEntity dbEntity = getDBQueryEntity(sql)
        ) {
            return getStringMapMap(key, dbEntity);
        }
    }

    private static Map<String, Map<Object, Object>> getStringMapMap(String key, DBEntity dbEntity) throws SQLException {
        ResultSet resultSet = dbEntity.getResultSet();
        ResultSetMetaData md = resultSet.getMetaData();
        int columnCount = md.getColumnCount();
        Map<String, Map<Object, Object>> map = new HashMap<>();
        while (resultSet.next()) {
            Map<Object, Object> rowData = getRowDataWithoutKey(resultSet, md, columnCount, key);
            map.put(resultSet.getString(key), rowData);
        }
        return map;
    }

    public static void dropTableIfExists(String tableName) throws SQLException {
        String sql = "DROP TABLE IF EXISTS " + tableName;
        executeUpdate(sql);
    }

    public static Map<String, Map<Object, Object>> querySqlToMap(String sql, String key, Object... parameters) throws SQLException {
        try (
                DBEntity dbEntity = getDBQueryEntity(sql, parameters)
        ) {
            return getStringMapMap(key, dbEntity);
        }
    }

    public static Map<String, List<Map<Object, Object>>> querySqlToMapList(String sql, String key, Object... parameters) throws SQLException {
        try (
                DBEntity entity = getDBQueryEntity(sql, parameters)
        ) {
            ResultSet resultSet = entity.getResultSet();
            ResultSetMetaData md = resultSet.getMetaData();
            int columnCount = md.getColumnCount();
            Map<String, List<Map<Object, Object>>> map = new HashMap<>();
            return getStringListMap(key, resultSet, md, columnCount, map);
        }
    }


    public static String querySqlToMapJson(String sql, String key) throws SQLException {
        return JSON.toJSONString(querySqlToMap(sql, key));
    }

    public static void deleteBySqlList(String[] sqlList) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = DBUtil.getConnection();
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            for (String sql : sqlList) {
                statement.addBatch(sql);
            }
            statement.executeBatch();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            release(connection, statement);
        }
    }

    public static int getResultSetRowCount(ResultSet resultSet) throws SQLException {
        if (resultSet instanceof DruidPooledResultSet) {
            ResultSet rawResultSet = ((DruidPooledResultSet) resultSet).getRawResultSet();
            try {
                Field field = ResultSetImpl.class.getDeclaredField("rowData");
                field.setAccessible(true);
                RowData rowData = (RowData) field.get(rawResultSet);
                return rowData.size();
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    private static DBEntity getDBQueryEntity(String sql, Object... parameters) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            setParameters(preparedStatement, parameters);
            resultSet = preparedStatement.executeQuery();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return new DBEntity(connection, preparedStatement, resultSet);
    }

    private static DBEntity getDBUpdateEntity(String sql, Object... parameters) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            setParameters(preparedStatement, parameters);
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return new DBEntity(connection, preparedStatement, resultSet);
    }

    private static void setParameters(PreparedStatement preparedStatement, Object... parameters) {
        for (int i = 0; i < parameters.length; i++) {
            try {
                preparedStatement.setObject(i + 1, parameters[i]);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static Map<String, List<Map<Object, Object>>> getStringListMap(String key, ResultSet resultSet, ResultSetMetaData md, int columnCount, Map<String, List<Map<Object, Object>>> map) throws SQLException {
        while (resultSet.next()) {
            String pk = resultSet.getString(key);
            Map<Object, Object> rowData = getRowDataWithoutKey(resultSet, md, columnCount, key);
            if (map.containsKey(pk)) {
                map.get(pk).add(rowData);
            } else {
                List<Map<Object, Object>> list = new ArrayList<>();
                list.add(rowData);
                map.put(pk, list);
            }
        }
        return map;
    }

    private static List<Map<Object, Object>> resultSetToList(ResultSet resultSet) throws SQLException {
        List<Map<Object, Object>> list = new ArrayList<>();
        ResultSetMetaData md = resultSet.getMetaData();
        int columnCount = md.getColumnCount();
        while (resultSet.next()) {
            Map<Object, Object> rowData = getRowData(resultSet, md, columnCount);
            list.add(rowData);
        }
        return list;
    }

    private static <T> List<T> resultSetToList(ResultSet resultSet, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        try {
            ResultSetMetaData md = resultSet.getMetaData();
            int columnCount = md.getColumnCount();
            while (resultSet.next()) {
                T rowData = getRowData(resultSet, md, columnCount, clazz);
                list.add(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static Map<Object, Object> getRowData(ResultSet resultSet, ResultSetMetaData md, int columnCount) throws SQLException {
        Map<Object, Object> rowData = new HashMap<>(columnCount);
        for (int i = 1; i <= columnCount; i++) {
            rowData.put(md.getColumnLabel(i), resultSet.getObject(i));
        }
        return rowData;
    }

    private static <T> T getRowData(ResultSet resultSet, ResultSetMetaData md, int columnCount, Class<T> clazz) throws SQLException {
        T object = null;
        try {
            object = clazz.newInstance();
            for (int i = 1; i <= columnCount; i++) {
                Field field = clazz.getDeclaredField(md.getColumnLabel(i));
                field.setAccessible(true);
                field.set(object, resultSet.getObject(i));
            }
            return object;
        } catch (InstantiationException | IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return object;
    }


    private static Map<Object, Object> getRowDataWithoutKey(ResultSet resultSet, ResultSetMetaData md, int columnCount, String key) throws SQLException {
        Map<Object, Object> rowData = new HashMap<>(columnCount);
        for (int i = 1; i <= columnCount; i++) {
            if (md.getColumnLabel(i).equals(key)) {
                continue;
            }
            rowData.put(md.getColumnLabel(i), resultSet.getObject(i));
        }
        return rowData;
    }

    private static String resultToJson(ResultSet rs) throws SQLException {
        List<Map<Object, Object>> list = resultSetToList(rs);
        return JSON.toJSONString(list);
    }

}
