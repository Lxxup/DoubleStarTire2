package com.ds.tire.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONStringer;



import android.R.bool;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

public class DBOperate {
	private DBHelper dbHelper;
	private Context context;
	private SQLiteDatabase db = null;
	private final String TAG = "TAG";

	// 实例化DBHelper工具类
	public DBOperate(Context context) {
		this.context = context;
		this.dbHelper = new DBHelper(context);
	}

	public void open() {
		db = dbHelper.getWritableDatabase();

	}

	public void close() {
		if (db != null) {
			db.close();
		}
	}

	
	/*
	 * 插入传感器ID
	 */
	public long insertMID(String mid,String selected) {
		long flag = -1;
	//	Cursor cursor = db.rawQuery("select * from mid", null);
		Cursor cursor = db.rawQuery("select * from mid  where mId=? ",
				new String[] { mid });
		cursor.moveToFirst();
		try {
			if (cursor.getCount() == 0) {
						ContentValues values = new ContentValues();
						values.put("mId", mid);
						values.put("selected", selected);
						Log.d(TAG,
								"shuju" +mid
										+ selected);
						flag = db.insert("mid", null, values);
					
			} else {
				
			}
		} catch (Exception e) {
		
		}
		
		return flag;
	}
//	public long insertMID(String mid,String selected,String reliability) {
//		long flag = -1;
//	//	Cursor cursor = db.rawQuery("select * from mid", null);
//		Cursor cursor = db.rawQuery("select * from mid where mId=?",new String[] {
//				mid});
//		cursor.moveToFirst();
//		try {
//			if (cursor.getCount() == 0) {
//						ContentValues values = new ContentValues();
//						values.put("mId", mid);
//						values.put("selected", selected);
//						values.put("reliability", reliability);
////						values.put("yaqiang", yaqiang);
////						values.put("wendu", wendu);
////						values.put("jiasudu", jiasudu);
//						Log.d(TAG,
//								"shuju" +mid
//										+ selected);
//						flag = db.insert("mid", null, values);
//					
//			} else {
//				
//			}
//		} catch (Exception e) {
//		
//		}
//		
//		return flag;
//	}
	
	
	/*
	 * 获取传感器
	 */
	public String getMID() {
		String res = null;
//		Cursor cursor = db.rawQuery("select distinct mId from mid where reliability=?",
//				new String[] {"1"});
		Cursor cursor = db.rawQuery("select distinct mId from mid ",
				null);
		cursor.moveToFirst();
		Log.d(TAG, "攻击航" + cursor.getCount());
		if (cursor.getCount() > 0) {
			JSONStringer msg = new JSONStringer();
			try {
				msg.array();
				while (!cursor.isAfterLast()) {
					msg.object().key("mId").value(cursor.getString(cursor.getColumnIndex("mId"))).endObject();
					cursor.moveToNext();
				}
				msg.endArray();
				res = msg.toString();
				Log.d(TAG, "从数据库中读取的设备号：" + res);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		cursor.close();
		return res;
	}
	/*
	 * 判断是否有相同传感器
	 */
	public  boolean getSAMEMID(String id) {
		
		Cursor cursor = db.rawQuery("select * from mid where mId=?",new String[] {
				id});
 	    cursor.moveToFirst();
 	    Log.d(TAG, "数据行数：" + cursor.getCount());
		if (cursor.getCount() > 0) {					
			try {
				//String s=cursor.getString(cursor.getColumnIndex("reliability"));
				Log.d(TAG,"读取的数据库中的reliability：：："+cursor.getString(cursor.getColumnIndex("reliability")));
				if(cursor.getString(cursor.getColumnIndex("reliability")).equals("0")){
					updateSAMEMID(id,"1");	
					
				}else if(cursor.getString(cursor.getColumnIndex("reliability")).equals("1")){
					
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			cursor.close();
			
			return true;
			
		}else{
			cursor.close();
			return false;
		}
		
		
	}
	/*
	 * 获取传感器数据
	 */	 
	public String getData(String id) {
		String res = null;
		Cursor cursor = db.rawQuery("select * from mid where mId=?",
				new String[] {id});
		cursor.moveToFirst();
		Log.d(TAG, "攻击航" + cursor.getCount());
		if (cursor.getCount() > 0) {
			try {
				
				while (!cursor.isAfterLast()) {
					String yaqiang= cursor.getString(cursor.getColumnIndex("yaqiang"));
					String wendu= cursor.getString(cursor.getColumnIndex("wendu"));
					String jiasudu = cursor.getString(cursor.getColumnIndex("jiasudu"));
				}
				
				Log.d(TAG, "从数据库中读取的传感器数据：" + res);
			} catch (Exception e) {
				e.printStackTrace();
			}

			JSONStringer msg = new JSONStringer();
			try {
				msg.array();
				while (!cursor.isAfterLast()) {
					msg.object().key("yaqiang").value(cursor.getString(cursor.getColumnIndex("yaqiang"))).endObject();
					cursor.moveToNext();
				}
				msg.endArray();
				res = msg.toString();
				Log.d(TAG, "从数据库中读取的传感器数据：" + res);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		cursor.close();
		return res;
	}
	
	/**
	 * 2.更新同步标志位
	 * 
	 * @param person
	 */
	public void updateSynflag(String user_id, String time) {
		ContentValues values = new ContentValues();
		values.put("synflag", 1);
		db.update("record", values, "user_id=? and time = ?", new String[] {
				user_id, time });

	}
	/*
	 * 更新传感器可靠性状态*/

	public void updateMID(String mId,String selected) {
		ContentValues values = new ContentValues();
		values.put("selected",selected);
		db.update("mid", values, "mId=? ", new String[] {
				mId });
	}
	/*
	 * 更新传感器状态*/

	public void updateSAMEMID(String mId,String reliability) {
		ContentValues values = new ContentValues();
		values.put("reliability",reliability);
		db.update("mid", values, "mId=? ", new String[] {
				mId });
	}
	/*
	 * 更新传感器数据*/

	public void updateData(String mId,int yaqiang,int wendu,int jiasudu ) {
		ContentValues values = new ContentValues();
		values.put("yaqiang",yaqiang);
		values.put("wendu",wendu);
		values.put("jiasudu",jiasudu);
		db.update("mid", values, "mId=? ", new String[] {
				mId });
	}
	/**
	 * 3.删除(注销用户使用)
	 * 
	 * @param id
	 */
	public void deletemid() {
		db.delete("mid", null, null);
	}

		
}
