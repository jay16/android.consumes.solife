package us.solife.consumes.db;

import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

import us.solife.consumes.TabList;
import us.solife.consumes.entity.ConsumeInfo;
import us.solife.consumes.entity.UserInfo;


import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;
import us.solife.consumes.util.NetUtils;
import us.solife.consumes.util.ToolUtils;

import java.util.Calendar;
import java.text.ParseException;

public class UserDao {
	public static final String KEY_ROWID = "id";
	private static final String DATABASE_TABLE = "consumes";

	private Context context;
	public ConsumeDatabaseHelper consumeDatabaseHelper;
	static UserDao UserDao;

	private UserDao(Context context) {
		this.context = context;
		this.consumeDatabaseHelper = new ConsumeDatabaseHelper(context);
	}

	public static UserDao getConsumeDao(Context context) {
		if (UserDao != null) {
		} else {
			UserDao = new UserDao(context);

		}
		return UserDao;

	}
	
	// 插入一笔消费记录
	public long insertRecord(UserInfo user_info) {
		SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
		database.beginTransaction();

		ContentValues values = new ContentValues();
		values.put("user_id", user_info.getUser_id());
		values.put("name", user_info.getName());
		values.put("email", user_info.getEmail());
		values.put("gravatar", user_info.getGravatar());
		values.put("created_at", user_info.getCreated_at());
		values.put("updated_at", user_info.getUpdated_at());
		//是否与服务器数据已同步
		long rowid = database.insert("users", "created_at", values);

		database.setTransactionSuccessful();
		database.endTransaction();
		database.close();

		return rowid;
	}
}