package com.yixianbinbin.datasource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Administrator on 2020/11/9.
 */
public final class DebugLog {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public static void  info(String s1){
        System.out.println(LocalDateTime.now().format(formatter).concat(" ").concat(s1));
    }

}
