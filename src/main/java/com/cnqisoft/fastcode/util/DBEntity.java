package com.cnqisoft.fastcode.util;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBEntity implements Closeable {

    private final Connection connection;

    private final PreparedStatement preparedStatement;

    private ResultSet resultSet;

    public DBEntity(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
        this.connection = connection;
        this.preparedStatement = preparedStatement;
        this.resultSet = resultSet;
    }

    public DBEntity(Connection connection, PreparedStatement preparedStatement) {
        this.connection = connection;
        this.preparedStatement = preparedStatement;
    }

    public Connection getConnection() {
        return connection;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }


    public PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }

    @Override
    public void close() {
        DBUtil.release(connection, resultSet, preparedStatement);
    }
}
