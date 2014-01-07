package us.solife.consumes.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ConsumeDatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "solife.db";
	private static final String DATABASE_TABLE = "consumes"; 
	private static final int DATABASE_VERSION = 1;
	
	//consume消费记录表
	String tbConsumeText = "create table "+ DATABASE_TABLE +"(" +
			"id integer primary key autoincrement, " +
			"user_id integer," +
			"consume_id integer," +
			"volue double NOT NULL,"+
			"msg varchar(200) DEFAULT ''," +
			"created_at varchar(100)," +
			"updated_at varchar(100) DEFAULT ''," +
			"sync boolean DEFAULT false)";

	public ConsumeDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(tbConsumeText);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("drop table if exists consumes");
	}

}
