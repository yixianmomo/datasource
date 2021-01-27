package com.yxbb.datasource;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.logging.Logger;

/**
 * Created by Administrator on 2020/11/9.
 */
public final class DebugLog {

    private static boolean isLogger = false;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    static {
        try {
            Enumeration<URL> resources = ClassLoader.getSystemResources("org/slf4j/impl/StaticLoggerBinder.class");
            isLogger = resources.hasMoreElements();
        } catch (IOException e) {

        }
    }

    public static void info(String s1) {
        if (isLogger) {
            org.slf4j.LoggerFactory.getLogger(DebugLog.class).info(s1);
        } else {
            Logger.getLogger(DebugLog.class.getName()).info(s1);
        }
    }

}
