package com.ds.tire.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

	private DBHelper helper;
	private SQLiteDatabase db;

	public DBManager(Context context) {
		helper = new DBHelper(context);
		db = helper.getWritableDatabase();
	}

	public void add(List<ReData> sdatas) {
		db.beginTransaction();
		try {
			for (ReData sdata : sdatas) {
				db.execSQL("INSERT INTO data VALUES(null,?, ?, ?, ?)",
						new Object[] { sdata.mId, sdata.qiya,sdata.wendu, sdata.jsd});
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	

	public void deleteOldPerson(ReData person) {
		db.delete("mId", "mId=?",
				new String[] { String.valueOf(person.mId) });
	}

	public List<ReData> query() {
		ArrayList<ReData> sdatas = new ArrayList<ReData>();
		Cursor c = queryTheCursor();
		while (c.moveToNext()) {
			ReData adata = new ReData();
			adata._id = c.getInt(c.getColumnIndex("_id"));
			adata.mId = c.getString(c.getColumnIndex("mId"));
			adata.qiya = c.getInt(c.getColumnIndex("qiya"));
			adata.wendu = c.getInt(c.getColumnIndex("wendu"));
			adata.jsd = c.getInt(c.getColumnIndex("jsd"));
			sdatas.add(adata);
		}
		c.close();
		return sdatas;
	}
//	public String search(String id) {
//		Cursor cursor = db.rawQuery("select * from data where mId=?",
//				new String[] {id});
//		        cursor.moveToFirst();
//				while (cursor.moveToNext()) {
//				String 姓名= cursor.getString(cursor.getColumnIndex("qiya"));
//				String 年龄= cursor.getString(cursor.getColumnIndex("wendu"));
//				String 性别 = cursor.getString(cursor.getColumnIndex("jsd"));
//				｝
//		return sdatas;
//	}

	public Cursor queryTheCursor() {
		Cursor c = db.rawQuery("SELECT * FROM stada", null);
		return c;
	}

	public void closeDB() {
		db.close();
	}

}
