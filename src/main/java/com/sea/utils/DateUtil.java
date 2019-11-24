package com.sea.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    //获取带毫秒时间戳
    public static String getyyyyMMddHHmmssSSS(){
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return sdf.format(d);
    }

    //获取带毫秒时间
    public static String getyyyyMMddHHmmss(){
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(d);
    }
}
