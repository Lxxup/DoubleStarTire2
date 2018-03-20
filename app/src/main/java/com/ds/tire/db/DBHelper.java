package com.ds.tire.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "sensor.db";
	private static final int DATABASE_VERSION = 1;

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS sdata"
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT,mId VARCHAR, qiya INTEGER, wendu INTEGER, jsd INTEGER)");
		db.execSQL("CREATE TABLE IF NOT EXISTS mid"
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT,mId VARCHAR, selected VARCHAR, reliability VARCHAR)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("ALTER TABLE sdata ADD COLUMN other STRING");
		db.execSQL("DROP TABLE IF EXISTS mid");
		onCreate(db);
	}

}
