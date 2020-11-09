package com.yixianbinbin.datasource;

/**
 * Created by Administrator on 2020/11/9.
 */
public class StarDataSourceException extends RuntimeException {

    public StarDataSourceException() {
        super();
    }

    public StarDataSourceException(String message) {
        super(message);
    }

    public StarDataSourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public StarDataSourceException(Throwable cause) {
        super(cause);
    }

    protected StarDataSourceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
