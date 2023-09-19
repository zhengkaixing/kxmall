package com.kxmall.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by admin on 2019/7/1.
 */
public class GeneratorUtil {

    private static AtomicInteger orderIdCount = new AtomicInteger();

    private static final SimpleDateFormat ORDER_ID_FORMAT = new SimpleDateFormat("yyyyMMHHmmss");

    public static String genSixVerifyCode() {
        String time = System.nanoTime() + "";
        return time.substring(time.length() - 6);
    }

    public static String genSessionId() {
        return UUID.randomUUID().toString().replace("-","").toUpperCase();
    }


    public static String genOrderId() {
        int i = orderIdCount.incrementAndGet() % 1000;
        if (i < 1000)
            i += 1000;
        return ORDER_ID_FORMAT.format(new Date()) + i;
    }

    public static String genFileName(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String genUUId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
