package com.ds.tire.util;

import android.content.Context;
import android.telephony.TelephonyManager;

public final class PhoneUtils
{
    private PhoneUtils()
    {
    }
    
    public static String getDeviceId(Context context)
    {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }
}
