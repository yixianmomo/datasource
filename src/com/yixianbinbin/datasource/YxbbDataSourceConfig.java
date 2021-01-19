package com.yixianbinbin.datasource;

import java.io.Serializable;

/**
 * Created by Administrator on 2020/11/9.
 */
public class YxbbDataSourceConfig implements Serializable {

    private static final long serialVersionUID = 1221405269125826567L;
    private String driverClassName;
    private String jdbcUrl;
    private String user;
    private String password;
    private int maxIdle = 5;//最大空闲数
    private int maxPoolSize = 20;//最大连接池大小
    private int maxIdleSeconds = 3600;//最大空闲秒数
    private boolean testValidation = true;
    private String testSql = "select 1";//测试连接
    private int testIntervalMinutes = 1;//间隔分钟数

    public YxbbDataSourceConfig() {
    }

    public YxbbDataSourceConfig(String driverClassName, String jdbcUrl, String user, String password, int maxIdle, int maxPoolSize) {
        this.driverClassName = driverClassName;
        this.jdbcUrl = jdbcUrl;
        this.user = user;
        this.password = password;
        this.maxIdle = maxIdle;
        this.maxPoolSize = maxPoolSize;
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

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getMaxIdleSeconds() {
        return maxIdleSeconds;
    }

    public void setMaxIdleSeconds(int maxIdleSeconds) {
        this.maxIdleSeconds = maxIdleSeconds;
    }

    public String getTestSql() {
        return testSql;
    }

    public void setTestSql(String testSql) {
        this.testSql = testSql;
    }

    public int getTestIntervalMinutes() {
        return testIntervalMinutes;
    }

    public void setTestIntervalMinutes(int testIntervalMinutes) {
        this.testIntervalMinutes = testIntervalMinutes;
    }

    public boolean isTestValidation() {
        return testValidation;
    }

    public void setTestValidation(boolean testValidation) {
        this.testValidation = testValidation;
    }
}
