package com.yixianbinbin.datasource;

/**
 * Created by Administrator on 2020/11/6.
 */
public class DefaultPooledFactory implements PooledFactory<DBUtil> {

    private DBConfig dbConfig = new DBConfig();

    public DefaultPooledFactory(DBConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    @Override
    public synchronized DBUtil makeObject()  {
        return new DBUtil(dbConfig);
    }

    @Override
    public synchronized void destroyObject(DBUtil var1)  {
        if(null != var1) {
            var1.close();
        }
    }

    @Override
    public synchronized boolean validateObject(DBUtil var1) {
        return var1 != null && var1.getConnection() != null;
    }

    @Override
    public synchronized void activateObject(DBUtil var1)  {
        if (null == var1) {
            throw new StarDataSourceException("连接实例为null");
        }
        // 如果断开就重连
        if (null == var1.getConnection()) {
            var1.open();
        }
    }

    @Override
    public synchronized void passivateObject(DBUtil var1)  {
        if (null != var1) {
            var1.close();
        }
        var1 = null;
    }
}
