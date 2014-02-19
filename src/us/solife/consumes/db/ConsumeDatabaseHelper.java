package us.solife.consumes.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ConsumeDatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "solife.db";
	private static final String DATABASE_TABLE_CONSUME = "consumes"; 
	private static final String DATABASE_TABLE_USER    = "users"; 
	private static final int DATABASE_VERSION = 1;
	
	//consume消费记录表
	String tb_consumes = "create table "+ DATABASE_TABLE_CONSUME +"(" +
			"id integer primary key autoincrement, " +
			"user_id integer," +
			"consume_id integer," +
			"volue double NOT NULL,"+
			"msg varchar(200) DEFAULT ''," +
			"created_at varchar(100)," +
			"updated_at varchar(100) DEFAULT ''," +
			"sync boolean DEFAULT false)";
	
	//user用户记录表
	String tb_users = "create table "+ DATABASE_TABLE_USER +"(" +
			"id integer primary key autoincrement, " +
			"user_id integer," +
			"name varchar(100) DEFAULT ''," +
			"email varchar(100) DEFAULT ''," +
			"gravatar varchar(200) DEFAULT ''," +
			"created_at varchar(100)," +
			"updated_at varchar(100) DEFAULT '')";

	public ConsumeDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(tb_consumes);
		db.execSQL(tb_users);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("drop table if exists consumes");
		db.execSQL("drop table if exists users");
	}

}
