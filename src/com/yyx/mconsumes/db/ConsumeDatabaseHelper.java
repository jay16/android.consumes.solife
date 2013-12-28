package com.yyx.mconsumes.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ConsumeDatabaseHelper extends SQLiteOpenHelper {
	String dbText = "create table consumes (consume_id integer primary key autoincrement, created_at varchar(100),id integer,msg varchar(200) DEFAULT '',updated_at varchar(100) DEFAULT '',volue double NOT NULL)";

	public ConsumeDatabaseHelper(Context context) {
		super(context, "consumes.db", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(dbText);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("drop table if exists consumes");
	}

}
