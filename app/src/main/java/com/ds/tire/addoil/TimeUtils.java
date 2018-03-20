package com.ds.tire.addoil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils
{
    // 格式化时间
    public static Calendar getDate(String time)
    {
        Date date = null;
        Calendar c = Calendar.getInstance();
        if (time != null && !time.equals(""))
        {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try
            {
                date = format.parse(time);
                c.setTime(date);
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
        }
        
        return c;
    }
    
    public static String timeToDate(String time)
    {
        String res = "";
        if (time != null && !time.equals(""))
        {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            try
            {
                Date date = format.parse(time);
                res = format1.format(date);
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
        }
        
        return res;
    }
    
    @SuppressWarnings("deprecation")
    public static String formatTime(int year, int month, int day)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(year, month, day);
        return format.format(date);
    }
}
