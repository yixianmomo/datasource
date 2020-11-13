package com.yixianbinbin.datasource;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

/**
 * Created by Administrator on 2020/11/6.
 */
public class YxbbDataSource implements DataSource, ObjectPool<DBConnection> {

    private PooledFactory<DBConnection> factory = null;
    private YxbbDataSourceConfig dataSourceConfig = null;
    private ConcurrentLinkedQueue<DBConnection> pool = new ConcurrentLinkedQueue<>();
    private int createSize = 0;
    private ScheduledExecutorService checkThread = Executors.newSingleThreadScheduledExecutor();
    private ReentrantLock borrowLock = new ReentrantLock();
    private ReentrantLock returnLock = new ReentrantLock();
    private ReentrantLock idleLock = new ReentrantLock();


    public YxbbDataSource(PooledFactory<DBConnection> factory) {
        this.factory = factory;
        this.dataSourceConfig = new YxbbDataSourceConfig();
        startValidationThread();
    }

    public YxbbDataSource(PooledFactory<DBConnection> factory, YxbbDataSourceConfig dataSourceConfig) {
        this.factory = factory;
        this.dataSourceConfig = dataSourceConfig;
        startValidationThread();
    }

    private void startValidationThread() {
        checkThread.scheduleAtFixedRate(new ClearIdleThread(this), 1L, dataSourceConfig.getTestIntervalMinutes(), TimeUnit.MINUTES);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return borrowObject();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return null;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    public boolean sendTestSql(DBConnection dbConnection) throws SQLException {
        PreparedStatement ps = dbConnection.prepareStatement(dataSourceConfig.getTestSql());
        return ps.execute();
    }


    private DBConnection getIdleObject() {

        DBConnection item = null;
        try {
            idleLock.lock();
            if (!pool.isEmpty()) {
                item = pool.poll();
                if (!factory.validateObject(item)) {
                    factory.activateObject(item);
                }
            }
        } catch (Exception e) {

        } finally {
            idleLock.unlock();
        }
        return item;
    }


    @Override
    public DBConnection borrowObject() {
        DBConnection dbConnection = null;
        dbConnection = getIdleObject();
        if (null != dbConnection) {
//            DebugLog.info("拿到空闲连接,当前连接数:" + ";线程:" + Thread.currentThread().getName());
            return dbConnection;
        }
        try {
            borrowLock.lock();
            if (getCredateSize() < dataSourceConfig.getMaxPoolSize()) {
                dbConnection = factory.makeObject();
                incrementSize();
//                DebugLog.info("创建连接,当前连接数:" + ";线程:" + Thread.currentThread().getName());
            }
        } catch (Exception e) {

        } finally {
            borrowLock.unlock();
        }
        if (null == dbConnection) {
            throw new YxbbDataSourceException("无法获取连接");
        }
        return dbConnection;
    }


    @Override
    public void returnObject(DBConnection var1) {
        try {
            returnLock.lock();
            if (pool.size() < dataSourceConfig.getMaxIdle()) {
                if (factory.validateObject(var1)) {
                    pool.add(var1);
//                    DebugLog.info("放入连接,当前连接数:" + getCurrSize() + "空闲:" + getNumIdle() + ";线程:" + Thread.currentThread().getName());
                    return;
                }
            }
            removeObject(var1);
        } catch (Exception e) {

        } finally {
            returnLock.unlock();
        }
    }

    public synchronized void removeObject(DBConnection var1) {
        factory.destroyObject(var1);
        pool.remove(var1);
        decrementSize();
//        DebugLog.info("移除连接,当前连接数:"  + getCredateSize()+ ";线程:" + Thread.currentThread().getName());
    }

    public synchronized int getCredateSize() {
        return createSize;
    }

    public synchronized void incrementSize() {
        createSize++;
    }

    public synchronized void decrementSize() {
        createSize--;
    }

    public PooledFactory<DBConnection> getFactory() {
        return factory;
    }

    public YxbbDataSourceConfig getDataSourceConfig() {
        return dataSourceConfig;
    }

    public ConcurrentLinkedQueue<DBConnection> getPool() {
        return pool;
    }
}
