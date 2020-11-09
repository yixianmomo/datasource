package com.yixianbinbin.datasource;

/**
 * Created by Administrator on 2020/11/6.
 */
public interface PooledFactory<T> {

    T makeObject();

    void destroyObject(T var1);

    boolean validateObject(T var1);

    void activateObject(T var1);

    void  passivateObject(T var1);

}
