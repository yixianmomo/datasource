package com.yixianbinbin.datasource;

/**
 * Created by Administrator on 2020/11/6.
 */
public class DefaultPooledFactory implements PooledFactory<DBConnection> {

    private DBConfig dbConfig = new DBConfig();

    public DefaultPooledFactory(DBConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    @Override
    public synchronized DBConnection makeObject()  {
        return new DBConnection(dbConfig);
    }

    @Override
    public synchronized void destroyObject(DBConnection var1)  {
        if(null != var1) {
            var1.close();
        }
    }

    @Override
    public synchronized boolean validateObject(DBConnection var1) {
        return var1 != null && var1.getConnection() != null;
    }

    @Override
    public synchronized void activateObject(DBConnection var1)  {
        if (null == var1) {
            throw new YxbbDataSourceException("连接实例为null");
        }
        // 如果断开就重连
        if (null == var1.getConnection()) {
            var1.open();
        }
    }

    @Override
    public synchronized void passivateObject(DBConnection var1)  {
        if (null != var1) {
            var1.close();
        }
        var1 = null;
    }
}
