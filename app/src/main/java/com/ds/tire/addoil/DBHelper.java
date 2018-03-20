package com.ds.tire.addoil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ds.tire.util.Constant;

public class DBHelper extends SQLiteOpenHelper
{
    
    public DBHelper(Context context)
    {
        super(context, Constant.DBNAME, null, Constant.DBVERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        try
        {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + Constant.ADD_OIL_DB + "(user_id varchar(20) ,add_time char(20),mileage integer,ammount varchar(10),oiltype integer,station varchar(50),is_full integer,primary key (user_id,add_time))");
        }
        catch (Exception e)
        {
            Log.i("TAG", "Failed to create Table");
        }
        
        Log.i("TAG", "Create Table Success");
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + Constant.ADD_OIL_DB);
        onCreate(db);
        Log.i("DB", "Upgrade Table Success");
    }
}
