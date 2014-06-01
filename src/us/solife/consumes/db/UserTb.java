package us.solife.consumes.db;

import java.util.ArrayList;

import us.solife.consumes.entity.ConsumeInfo;
import us.solife.consumes.entity.UserInfo;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class UserTb {
	public static final String KEY_ROWID = "id";

	private Context context;
	public ConsumeDatabaseHelper consumeDatabaseHelper;
	static UserTb UserDao;

	private UserTb(Context context) {
		this.context = context;
		this.consumeDatabaseHelper = new ConsumeDatabaseHelper(this.context);
	}

	public static UserTb get_user_tb(Context context) {
		if (UserDao != null) {
		} else {
			UserDao = new UserTb(context);
		}
		return UserDao;
	}
	

    /**
     * @return
     */
	public UserInfo get_record_with_email(String email) {
		SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
		Cursor cursor = database.rawQuery("select * from users where email= '"+email+"'", null);
		UserInfo user_info = new UserInfo();
		if (cursor != null) {
			cursor.moveToFirst();
			user_info = get_consume_info_from_cursor(cursor);
		}
		return user_info;
	}
	
	
	// ����һ�����Ѽ�¼
	public long insert_user(UserInfo user_info) {
		Log.w("UserTb","Before Insert User:"+user_info.to_string());
		
		SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
		database.beginTransaction();
		ContentValues values = new ContentValues();
		values.put("user_id", user_info.get_user_id());
		values.put("name", user_info.get_name());
		values.put("email", user_info.get_email());
		values.put("created_at", user_info.get_created_at());
		values.put("updated_at", user_info.get_updated_at());
		values.put("info", user_info.get_info());
		//�Ƿ��������������ͬ��
		long row_id = database.insert("users", null, values);

		database.setTransactionSuccessful();
		database.endTransaction();
		database.close();

		user_info = get_record_with_user_id(user_info.get_user_id());
		Log.w("UserTb","After Insert User:"+user_info.to_string());
		return row_id;
	}
	
	
	public long update_user(UserInfo user_info){
        Log.w("UserTB","Before Update User:"+user_info.to_string());
        
		SQLiteDatabase db = consumeDatabaseHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("name", user_info.get_name());
		values.put("email", user_info.get_email());
		values.put("created_at", user_info.get_created_at());
		values.put("updated_at", user_info.get_updated_at());
		values.put("info", user_info.get_info());
		String[] args = {String.valueOf(user_info.get_user_id())};

        long row_id = db.update("users", values, "user_id=?",args);
		user_info = get_record_with_user_id(user_info.get_user_id());
        Log.w("UserTB","After Update User:"+user_info.to_string());
        
        return row_id;
	}
	
	public long insert_or_update_user(UserInfo user_info) {
		long row_id1 = (long)-1, row_id2 = (long)-1;
		if(user_info.get_id() > 0 && user_info.get_user_id() < 0) {
			row_id1 = user_info.get_id();
			user_info.set_user_id((int) row_id1);
		} else {
			row_id1 = user_info.get_user_id();
		}
		Log.w("InsertOrUpdate", "userRowID:" + row_id1);
		UserInfo userinfo = get_record_with_user_id(row_id1);
		if(userinfo.get_email() == null || userinfo.get_email().length() == 0) {
			row_id2 = insert_user(user_info);
		} else {
			row_id2 = update_user(user_info);
		}
		return row_id2;
	}
	

	/**
    * ����row_id��ȡ������Ϣ
    * �༭��������ʱʹ��
    * @param row_id
    * @return
    */
	public UserInfo get_record_with_user_id(long user_id) {
	    SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
		Cursor cursor = database.rawQuery("select * from users where user_id= "+user_id, null);
		UserInfo user_info = new UserInfo();
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			user_info.set_id(cursor.getInt(cursor.getColumnIndex("id")));
			user_info.set_user_id(cursor.getInt(cursor.getColumnIndex("user_id")));
			user_info.set_name(cursor.getString(cursor.getColumnIndex("name")));
			user_info.set_email(cursor.getString(cursor.getColumnIndex("email")));
			user_info.set_created_at(cursor.getString(cursor.getColumnIndex("created_at")));
			user_info.set_updated_at(cursor.getString(cursor.getColumnIndex("updated_at")));
			user_info.set_info(cursor.getString(cursor.getColumnIndex("info")));
		}
		return user_info;
	}
	
	public ArrayList<Integer> get_unsync_user_list() {
		ArrayList<Integer> user_ids = new ArrayList<Integer>();
		
		SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
		String sql = "select distinct user_id from consumes where user_id > 0 " +
		" and user_id not in (select distinct user_id from users)";
		Cursor cursor = database.rawQuery(sql, null);

      
		if (cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				user_ids.add(cursor.getInt(cursor.getColumnIndex("user_id")));
			}
		}
		cursor.close();
		database.close();
		return user_ids;
	}
	
	public ArrayList<UserInfo> get_user_list() {
		
		SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
		String sql = "select * from users where user_id > 0 ";
		Cursor cursor = database.rawQuery(sql, null);

		ArrayList<UserInfo> user_infos = new ArrayList<UserInfo>();
		if (cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				user_infos.add(get_consume_info_from_cursor(cursor));
			}
		}
		cursor.close();
		database.close();
		return user_infos;
	}
	
	public UserInfo get_consume_info_from_cursor(Cursor cursor){
		UserInfo user_info = new UserInfo();
		
		user_info.set_id(cursor.getInt(cursor.getColumnIndex("id")));
		user_info.set_user_id(cursor.getInt(cursor.getColumnIndex("user_id")));
		user_info.set_name(cursor.getString(cursor.getColumnIndex("name")).toString());
		user_info.set_email(cursor.getString(cursor.getColumnIndex("email")).toString());
		user_info.set_created_at(cursor.getString(cursor.getColumnIndex("created_at"))); 
		user_info.set_updated_at(cursor.getString(cursor.getColumnIndex("updated_at"))); 
		
		return user_info;
	}
}