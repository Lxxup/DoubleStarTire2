package com.ds.tire.addoil;

import org.json.JSONException;
import org.json.JSONStringer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ds.tire.util.Constant;
import com.ds.tire.util.SpUtils;

public class DBOperate
{
    private DBHelper       dbHelper;
    private Context        context;
    private SQLiteDatabase db  = null;
    private final String   TAG = "DBOperate";
    
    // 实例化DBHelper工具类
    public DBOperate(Context context)
    {
        this.context = context;
        this.dbHelper = new DBHelper(context);
    }
    
    public void open()
    {
        db = dbHelper.getWritableDatabase();
        
    }
    
    public void close()
    {
        if (db != null)
        {
            db.close();
        }
    }
    
    /**
     * 函数功能：获取记录总数  @author 李小敏  @version 2013-11-21 上午11:22:55 
     */
    public int getRecordNum()
    {
        String userId = SpUtils.getString(context, SpUtils.ACCOUNT, "");
        Cursor cursor = db.rawQuery("select count(*) from " + Constant.ADD_OIL_DB, null);
        cursor.moveToFirst();
        Log.d(TAG, "getRecordNum:" + cursor.getInt(0));
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }
    
    /**
     * 函数功能：根据起止时间获取该时间段内的加油信息jsonArrray格式  @author 李小敏
     *  @version 2013-11-21 上午11:22:16 
     */
    public String getAddOilInfo(String startTime, String endTime)
    {
        Log.d(TAG, "start:" + startTime + " end:" + endTime);
        String res = null;
        String userId = SpUtils.getString(context, SpUtils.ACCOUNT, "");
        Cursor cursor = db.rawQuery("select * from " + Constant.ADD_OIL_DB + " where user_id=? and date(add_time)>=date(?) and date(add_time)<date(?)", new String[] { userId, startTime, endTime });
        cursor.moveToFirst();
        Log.d(TAG, "cursor:" + cursor.getCount());
        JSONStringer msg = new JSONStringer();
        try
        {
            msg.array();
            while (!cursor.isAfterLast())
            {
                msg.object().key("station_name").value(cursor.getString(cursor.getColumnIndex("station"))).key("oil_species").value(cursor.getInt(cursor.getColumnIndex("oiltype"))).key("mileage").value(cursor.getString(cursor.getColumnIndex("mileage"))).key("amount").value(cursor.getString(cursor.getColumnIndex("ammount"))).key("if_fill").value(cursor.getString(cursor.getColumnIndex("is_full"))).key("datetime").value(cursor.getString(cursor.getColumnIndex("add_time"))).endObject();
                cursor.moveToNext();
            }
            msg.endArray();
            res = msg.toString();
        }
        catch (JSONException e)
        {
            Log.d("TAG", "Json error!");
            e.printStackTrace();
        }
        cursor.close();
        return res;
    }
    
    /**
     * 函数功能：获取平均油耗  @author 李小敏  @version 2013-11-21 上午11:21:38 
     */
    public Oil_AVG getOilAvg()
    {
        Oil_AVG res = null;
        int end_mileage = 0;
        String end_time = "";
        double base_count = 0;
        int start_mileage = 0;
        String start_time = "";
        String userId = SpUtils.getString(context, SpUtils.ACCOUNT, "");
        Cursor cursor1 = db.rawQuery("select sum(ammount) from " + Constant.ADD_OIL_DB + " where user_id=?", new String[] { userId });
        cursor1.moveToFirst();
        double count = cursor1.getDouble(0);
        Log.d(TAG, "count:" + count);
        cursor1.close();
        Cursor cursor2 = db.rawQuery("select * from " + Constant.ADD_OIL_DB + " where user_id=? and mileage=(select max(mileage) from " + Constant.ADD_OIL_DB + ")", new String[] { userId });
        cursor2.moveToFirst();
        if (cursor2.getCount() > 0)
        {
            end_time = cursor2.getString(cursor2.getColumnIndex("add_time"));
            end_mileage = cursor2.getInt(cursor2.getColumnIndex("mileage"));
        }
        cursor2.close();
        Cursor cursor3 = db.rawQuery("select * from " + Constant.ADD_OIL_DB + " where user_id=? and mileage=(select min(mileage) from " + Constant.ADD_OIL_DB + ")", new String[] { userId });
        cursor3.moveToFirst();
        if (cursor3.getCount() > 0)
        {
            start_time = cursor3.getString(cursor3.getColumnIndex("add_time"));
            start_mileage = cursor3.getInt(cursor3.getColumnIndex("mileage"));
            base_count = Double.valueOf(cursor3.getString(cursor3.getColumnIndex("ammount")));
        }
        cursor3.close();
        
        if (start_mileage != end_mileage)
        {
            res = new Oil_AVG();
            res.setEnd_time(end_time);
            res.setStart_time(start_time);
            res.setOil_avg((count - base_count) * 1.0 / (end_mileage - start_mileage));
        }
        Log.d(TAG, "start:" + start_time + " end:" + end_time);
        return res;
    }
    
    /**
     * 函数功能：添加加油信息  @author 李小敏  @version 2013-11-21 上午11:20:48 
     */
    public long insertAddOilInfo(String time, String mileage, int oilType, String ammount, String station, int isFull)
    {
        long flag = -1;
        String userId = SpUtils.getString(context, SpUtils.ACCOUNT, "");
        Cursor cursor = db.rawQuery("select count(*) from " + Constant.ADD_OIL_DB + " where user_id=? and add_time = ?", new String[] { userId, time });
        cursor.moveToFirst();
        long count = cursor.getLong(0);
        if (count == 0)
        {
            ContentValues values = new ContentValues();
            values.put("user_id", userId);
            values.put("add_time", time);
            values.put("mileage", mileage);
            values.put("oiltype", oilType);
            values.put("ammount", ammount);
            values.put("station", station);
            values.put("is_full", isFull);
            
            flag = db.insert(Constant.ADD_OIL_DB, null, values);
            Log.d(TAG, values.toString());
        }
        cursor.close();
        Log.d(TAG, "数据插入：" + flag);
        return flag;
    }
    
}
