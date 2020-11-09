package com.yixianbinbin.datasource;

import java.io.Serializable;

/**
 * Created by Administrator on 2020/11/6.
 */
public class DBConfig implements Serializable {

    private static final long serialVersionUID = 8837968297438495945L;
    private String dbDriver;
    private String dbHost;
    private int dbPort;
    private String dbName;
    private String user;
    private String password;

    public DBConfig() {
    }

    public DBConfig(String dbDriver, String dbHost, int dbPort, String dbName, String user, String password) {
        this.dbDriver = dbDriver;
        this.dbHost = dbHost;
        this.dbPort = dbPort;
        this.dbName = dbName;
        this.user = user;
        this.password = password;
    }

    public String getDbDriver() {
        return dbDriver;
    }

    public void setDbDriver(String dbDriver) {
        this.dbDriver = dbDriver;
    }

    public String getDbHost() {
        return dbHost;
    }

    public void setDbHost(String dbHost) {
        this.dbHost = dbHost;
    }

    public int getDbPort() {
        return dbPort;
    }

    public void setDbPort(int dbPort) {
        this.dbPort = dbPort;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
