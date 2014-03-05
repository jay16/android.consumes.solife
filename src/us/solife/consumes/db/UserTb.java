package us.solife.consumes.db;

import us.solife.consumes.entity.UserInfo;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class UserTb {
	public static final String KEY_ROWID = "id";

	private Context context;
	public ConsumeDatabaseHelper consumeDatabaseHelper;
	static UserTb UserDao;

	private UserTb(Context context) {
		this.context = context;
		this.consumeDatabaseHelper = new ConsumeDatabaseHelper(this.context);
	}

	public static UserTb getConsumeDao(Context context) {
		if (UserDao != null) {
		} else {
			UserDao = new UserTb(context);

		}
		return UserDao;

	}
	
	// 插入一笔消费记录
	public long insertRecord(UserInfo user_info) {
		SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
		database.beginTransaction();

		ContentValues values = new ContentValues();
		values.put("user_id", user_info.get_user_id());
		values.put("name", user_info.get_name());
		values.put("email", user_info.get_email());
		values.put("gravatar", user_info.get_gravatar());
		values.put("created_at", user_info.get_created_at());
		values.put("updated_at", user_info.get_updated_at());
		//是否与服务器数据已同步
		long rowid = database.insert("users", "created_at", values);

		database.setTransactionSuccessful();
		database.endTransaction();
		database.close();

		return rowid;
	}
}