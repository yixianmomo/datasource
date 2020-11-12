package com.yixianbinbin.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by Administrator on 2020/10/22.
 */
public class DBConnection {

    private Connection connection = null;
    private DBConfig dbConfig = null;
    private boolean isIdle = false;
    private Date idleBeginTime = null;

    public DBConnection(DBConfig dbConfig) {
        this.dbConfig = dbConfig;
        open();
    }


    public void open() {
        try {
            if (null == connection) {
                Class.forName(dbConfig.getDbDriver());
                connection = DriverManager.getConnection(String.format("jdbc:sqlserver://%s:%s;DatabaseName=%s", dbConfig.getDbHost(), dbConfig.getDbPort(), dbConfig.getDbName()),
                        dbConfig.getUser(),dbConfig.getPassword());
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new YxbbDataSourceException("数据库连接异常");
        }
    }

    public void close() {
        try {
            if (null != connection) {
                connection.close();
                connection = null;
            }
        } catch (SQLException e) {

        }
    }

    public Connection getConnection() {
        return connection;
    }

    public DBConfig getDbConfig() {
        return dbConfig;
    }

    public boolean isIdle() {
        return isIdle;
    }

    public void setIdle(boolean idle) {
        isIdle = idle;
    }

    public Date getIdleBeginTime() {
        return idleBeginTime;
    }

    public void setIdleBeginTime(Date idleBeginTime) {
        this.idleBeginTime = idleBeginTime;
    }
}
