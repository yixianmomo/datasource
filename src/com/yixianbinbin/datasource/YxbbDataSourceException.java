package com.yixianbinbin.datasource;

/**
 * Created by Administrator on 2020/11/9.
 */
public class YxbbDataSourceException extends RuntimeException {

    public YxbbDataSourceException() {
        super();
    }

    public YxbbDataSourceException(String message) {
        super(message);
    }

    public YxbbDataSourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public YxbbDataSourceException(Throwable cause) {
        super(cause);
    }

    protected YxbbDataSourceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
