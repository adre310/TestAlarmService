package com.example.sms.timer.spy;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "sms.db";
    private static final int DATABASE_VERSION = 1;

	public MyDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL("CREATE TABLE sms (_id INTEGER PRIMARY KEY, type TEXT,modified INTEGER, address TEXT, date INTEGER, body TEXT)");		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
