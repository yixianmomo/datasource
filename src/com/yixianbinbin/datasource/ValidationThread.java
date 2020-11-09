package com.yixianbinbin.datasource;

import java.util.Iterator;


/**
 * Created by Administrator on 2020/11/9.
 */
public class ValidationThread implements Runnable {

    private StarDataSource dataSource = null;

    public ValidationThread(StarDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run() {
        Iterator<DBUtil> iter = dataSource.getPool().iterator();
        DBUtil item = null;
        while (iter.hasNext()) {
            item = iter.next();
            try {
                // 空闲可用的连接
                if (item.isIdle()) {
                    if (((System.currentTimeMillis() - item.getIdleBeginTime().getTime())/1000) > dataSource.getDataSourceConfig().getMaxIdleSeconds()) {
                        DebugLog.info("规定时间内没业务,准备删除");
                        dataSource.removeObject(item);
                    }
                    if(dataSource.getDataSourceConfig().isTestValidation() && !dataSource.sendTestSql(item)){
                        DebugLog.info("测试连接成员关闭,准备删除");
                        dataSource.removeObject(item);
                    }
                }
            } catch (Exception e) {
                dataSource.removeObject(item);
            }
        }
    }
}
