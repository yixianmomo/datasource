package com.yixianbinbin.datasource;

/**
 * Created by Administrator on 2020/11/6.
 */
public interface ObjectPool<T> {


    T borrowObject();

    void returnObject(T var1);


    int getNumActive();

    int getNumIdle();

    int getCurrSize();


}
