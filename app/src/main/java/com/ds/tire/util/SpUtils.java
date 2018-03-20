package com.ds.tire.util;

import android.R.string;
import android.content.Context;
import android.content.SharedPreferences;

public class SpUtils
{
    public static final String SETTING       = "setting.xml";
    public static final String ACCOUNT       = "account";
    public static final String MUMBER        = "number";
    public static final String AIDFLAGONE    = "aidflagone";
    public static final String TIRE_PRESSURE = "tire_pressure";
    public static final String WHEELS_COUNT  = "wheels_count";
    public static final String INSTALLED_COUNT = "installed_count";
    public static final String WHEELS_ID     = "wheels_ID";
//    public static final String WENDUTOP= null;
//    public static final String WENDUBOT= null;
//    public static final String YAQIANGTOP= null;
//    public static final String YAQIANGBOT= null;
    
    public static String getString(Context context, String key, String def)
    {
        SharedPreferences sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        return sp.getString(key, def);
    }
    
    public static void setString(Context context, String key, String val)
    {
        SharedPreferences sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString(key, val);
        e.commit();
    }
    
    public static boolean getBoolean(Context context, String key, boolean def)
    {
        SharedPreferences sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        return sp.getBoolean(key, def);
    }
    
    public static void setBoolean(Context context, String key, boolean val)
    {
        SharedPreferences sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putBoolean(key, val);
        e.commit();
    }
    
    public static int getInteger(Context context, String key, int def)
    {
        SharedPreferences sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        return sp.getInt(key, def);
    }
    
    public static void setInteger(Context context, String key, int val)
    {
        SharedPreferences sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putInt(key, val);
        e.commit();
    }
    
}
