package com.yixianbinbin.datasource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Administrator on 2020/11/6.
 */
public class YxbbDataSource implements ObjectPool<DBConnection> {

    private PooledFactory<DBConnection> factory = null;
    private YxbbDataSourceConfig dataSourceConfig = null;
    private CopyOnWriteArraySet<DBConnection> pool = new CopyOnWriteArraySet<>();
    private ScheduledExecutorService checkThread = Executors.newSingleThreadScheduledExecutor();
    private int idleSize = 0;
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

    public boolean sendTestSql(DBConnection dbConnection) throws SQLException {
        Connection conn = dbConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(dataSourceConfig.getTestSql());
        return ps.execute();
    }


    private DBConnection getIdleObject(boolean setNotIdle) {
        DBConnection item = null;
        try {
            idleLock.lock();
            Iterator<DBConnection> iter = pool.iterator();
            while (iter.hasNext()) {
                item = iter.next();
                if (item.isIdle()) {
                    if (setNotIdle) {
                        item.setIdle(false);
                        item.setIdleBeginTime(null);
                        decrementIdle();
                    }
                    break;
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
        dbConnection = getIdleObject(true);
        if (null != dbConnection) {
//            DebugLog.info("拿到空闲连接,当前连接数:" + ";线程:" + Thread.currentThread().getName());
            return dbConnection;
        }
        try {
            borrowLock.lock();
            if (getCurrSize() < dataSourceConfig.getMaxPoolSize()) {
                dbConnection = factory.makeObject();
                pool.add(dbConnection);
//                DebugLog.info("创建连接,当前连接数:" + ";线程:" + Thread.currentThread().getName());
            }
        } catch (Exception e) {

        } finally {
            borrowLock.unlock();
        }
        // 只能等待空闲连接
        if (null == dbConnection) {
            for (int i = 0; i < 10; i++) {
                dbConnection = getIdleObject(true);
//                DebugLog.info(Thread.currentThread().getName() + "第" + i + "次循环,结果:" + (dbConnection != null));
                if (null == dbConnection) {
                    threadWait(500);
                } else {
//                    DebugLog.info("拿到空闲连接,当前连接数:" + ";线程:" + Thread.currentThread().getName());
                    break;
                }
            }
        }

        if (null == dbConnection) {
            throw new YxbbDataSourceException("没有可用连接");
        }
        return dbConnection;
    }

    private void threadWait(int v) {
        try {
            Thread.sleep(v);
        } catch (InterruptedException e) {

        }
    }

    @Override
    public void returnObject(DBConnection var1) {
        try {
            returnLock.lock();
            if (getNumIdle() < dataSourceConfig.getMaxIdle()) {
                if (factory.validateObject(var1)) {
                    var1.setIdle(true);
                    var1.setIdleBeginTime(new Date());
                    incrementIdle();
//                    DebugLog.info("放入连接,当前连接数:" + getCurrSize() + "空闲:" + getNumIdle() + ";线程:" + Thread.currentThread().getName());
                    return;
                }
            }
            removeObject(var1);
//            currSize--;
        } catch (Exception e) {

        } finally {
            returnLock.unlock();
        }
    }


    public synchronized void removeObject(DBConnection var1) {
        factory.destroyObject(var1);
        pool.remove(var1);
//        DebugLog.info("移除连接,当前连接数:" + ";线程:" + Thread.currentThread().getName());
    }

    @Override
    public synchronized int getNumActive() {
        return getCurrSize() - idleSize;
    }

    @Override
    public synchronized int getNumIdle() {
//        return Long.valueOf(pool.stream().filter((item) -> item.isIdle()).count()).intValue();
        return idleSize;
    }

    public synchronized void incrementIdle() {
        idleSize = idleSize + 1;
    }

    public synchronized void decrementIdle() {
        idleSize = idleSize - 1;
    }

    public synchronized int getCurrSize() {
        return pool.size();
    }

    public PooledFactory<DBConnection> getFactory() {
        return factory;
    }

    public YxbbDataSourceConfig getDataSourceConfig() {
        return dataSourceConfig;
    }

    public CopyOnWriteArraySet<DBConnection> getPool() {
        return pool;
    }
}
