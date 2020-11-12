package com.yixianbinbin.datasource;

import java.io.Serializable;

/**
 * Created by Administrator on 2020/11/9.
 */
public class YxbbDataSourceConfig implements Serializable {

    private static final long serialVersionUID = 1221405269125826567L;
    private int maxIdle = 5;//最大空闲数
    private int maxPoolSize = 10;//最大连接池大小
    private int maxIdleSeconds = 3600;//最大空闲秒数
    private boolean testValidation = true;
    private String testSql = "select 1";//测试连接
    private int testIntervalMinutes = 10;//间隔分钟数

    public YxbbDataSourceConfig() {
    }

    public YxbbDataSourceConfig(int maxIdle, int maxPoolSize) {
        this.maxIdle = maxIdle;
        this.maxPoolSize = maxPoolSize;
    }

    public YxbbDataSourceConfig(int maxIdle, int maxPoolSize, int maxIdleSeconds, String testSql, int testIntervalMinutes) {
        this.maxIdle = maxIdle;
        this.maxPoolSize = maxPoolSize;
        this.maxIdleSeconds = maxIdleSeconds;
        this.testSql = testSql;
        this.testIntervalMinutes = testIntervalMinutes;
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
