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
public class StarDataSource implements ObjectPool<DBUtil> {

    private PooledFactory<DBUtil> factory = null;
    private StarDataSourceConfig dataSourceConfig = null;
    private CopyOnWriteArraySet<DBUtil> pool = new CopyOnWriteArraySet<>();
    private ScheduledExecutorService checkThread = Executors.newSingleThreadScheduledExecutor();
    //    private int currSize = 0;
    private ReentrantLock borrowLock = new ReentrantLock();
    private ReentrantLock returnLock = new ReentrantLock();
    private ReentrantLock idleLock = new ReentrantLock();

    public StarDataSource(PooledFactory<DBUtil> factory) {
        this.factory = factory;
        this.dataSourceConfig = new StarDataSourceConfig();
        startValidationThread();
    }

    public StarDataSource(PooledFactory<DBUtil> factory, StarDataSourceConfig dataSourceConfig) {
        this.factory = factory;
        this.dataSourceConfig = dataSourceConfig;
        startValidationThread();
    }

    private void startValidationThread() {
        checkThread.scheduleAtFixedRate(new ValidationThread(this), 1L, dataSourceConfig.getTestIntervalMinutes(), TimeUnit.MINUTES);
    }

    public boolean sendTestSql(DBUtil dbUtil) {
        try {
            Connection conn = dbUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(dataSourceConfig.getTestSql());
            ps.executeQuery();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }


    private DBUtil getIdleObject() {
        DBUtil item = null;
        try {
            idleLock.lock();
            Iterator<DBUtil> iter = pool.iterator();
            while (iter.hasNext()) {
                item = iter.next();
                if (item.isIdle()) {
                    if (!factory.validateObject(item)) {
                        factory.activateObject(item);
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

    private synchronized void setObjectNotIdle(DBUtil dbUtil) {
        dbUtil.setIdle(false);
        dbUtil.setIdleBeginTime(null);
    }

    public synchronized void setObjectIdle(DBUtil dbUtil) {
        dbUtil.setIdle(true);
        dbUtil.setIdleBeginTime(new Date());
    }


    @Override
    public DBUtil borrowObject() {
        DBUtil dbUtil = null;
        dbUtil = getIdleObject();
        if (null != dbUtil) {
            setObjectNotIdle(dbUtil);
            return dbUtil;
        }
        try {
            borrowLock.lock();
            if (getCurrSize() < dataSourceConfig.getMaxPoolSize()) {
                dbUtil = factory.makeObject();
//                currSize = currSize++;
                setObjectNotIdle(dbUtil);
                addObject(dbUtil);
                DebugLog.info("准备创建连接,当前连接数:" + getCurrSize() + ";线程:" + Thread.currentThread().getName());
            }
        } catch (Exception e) {

        } finally {
            borrowLock.unlock();
        }
        // 只能等待空闲连接
        if (null == dbUtil) {
            for (int i = 0; i < 10; i++) {
                dbUtil = getIdleObject();
                DebugLog.info(Thread.currentThread().getName() + "第" + i + "次循环,结果:" + (dbUtil != null));
                if (null == dbUtil) {
                    threadWait(500);
                } else {
                    setObjectNotIdle(dbUtil);
                    break;
                }
            }
        }

        if (null == dbUtil) {
            throw new StarDataSourceException("没有可用连接");
        }
        return dbUtil;
    }

    private void threadWait(int v) {
        try {
            Thread.sleep(v);
        } catch (InterruptedException e) {

        }
    }

    @Override
    public void returnObject(DBUtil var1) {
        try {
            returnLock.lock();
            if (getNumIdle() < dataSourceConfig.getMaxIdle()) {
                if (factory.validateObject(var1)) {
                    setObjectIdle(var1);
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


    public synchronized void addObject(DBUtil dbUtil) {
        pool.add(dbUtil);
    }


    public synchronized void removeObject(DBUtil var1) {
        factory.destroyObject(var1);
        pool.remove(var1);
    }

    @Override
    public synchronized int getNumActive() {
        return Long.valueOf(pool.stream().filter((item) -> !item.isIdle()).count()).intValue();
    }

    @Override
    public synchronized int getNumIdle() {
        return Long.valueOf(pool.stream().filter((item) -> item.isIdle()).count()).intValue();
    }

    public synchronized int getCurrSize() {
        return pool.size();
    }

    public PooledFactory<DBUtil> getFactory() {
        return factory;
    }

    public StarDataSourceConfig getDataSourceConfig() {
        return dataSourceConfig;
    }

    public CopyOnWriteArraySet<DBUtil> getPool() {
        return pool;
    }
}
