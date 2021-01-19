package com.yixianbinbin.datasource;

import java.io.Serializable;

/**
 * Created by Administrator on 2020/11/6.
 */
public class DBConfig implements Serializable {

    private static final long serialVersionUID = 8837968297438495945L;
    private String driverClassName;
    private String jdbcUrl;
    private String user;
    private String password;

    public DBConfig() {
    }

    public DBConfig(String driverClassName,String jdbcUrl, String user, String password) {
        this.driverClassName = driverClassName;
        this.jdbcUrl = jdbcUrl;
        this.user = user;
        this.password = password;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
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
