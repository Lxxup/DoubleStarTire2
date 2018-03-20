package com.ds.tire.addoil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CurrentTimeUtils
{
    public static String getCurrentTime()
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");// 设置日期格式
        String time = df.format(new Date());
        return time;
    }
}
