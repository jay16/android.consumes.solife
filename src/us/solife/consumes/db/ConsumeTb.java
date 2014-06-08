package us.solife.consumes.db;

import java.util.ArrayList;
import android.util.Log;
import java.util.Date;
import java.text.SimpleDateFormat;

import us.solife.consumes.TabList;
import us.solife.consumes.entity.ConsumeInfo;


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

/**
 * 消费记录实体表
 * @author jay (http://solife.us/resume)
 * @version 1.0
 * @created 2014-02-25
 */
public class ConsumeTb {
	public static final String KEY_ROWID = "id";
	private static final String DATABASE_TABLE = "consumes";

	private Context context;
	public ConsumeDatabaseHelper consumeDatabaseHelper;
	static ConsumeTb consumeDao;

	private ConsumeTb(Context context) {
		this.context = context;
		this.consumeDatabaseHelper = new ConsumeDatabaseHelper(context);
	}

	public static ConsumeTb get_consume_tb(Context context) {
		if (consumeDao != null) {
		} else {
			consumeDao = new ConsumeTb(context);
		}
		return consumeDao;
	}

	/**
	 *  插入当前登陆用户所有消费记录
	 *  插入前会先清空实体表
	 * @param consumeInfos
	 */
	public void insert_all_record(ArrayList<ConsumeInfo> consumeInfos, Boolean isTruncate) {
		if(isTruncate) truncate_table();
		
		for (int i = 0; i < consumeInfos.size(); i++) {
			ConsumeInfo consume_info = consumeInfos.get(i);
			//log调试用
            Log.w("ConsumeDao",consume_info.to_string());
            consume_info.set_consume_id((int)consume_info.get_id());
            consume_info.set_sync((long)1);
            insert_record(consume_info);
			//insert_record(info.get_user_id(), (int) info.get_id(), info.get_value(),
			//		info.get_remark(), info.get_created_at(),true);
		}

	}

	public void truncate_table(){
		SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
		database.beginTransaction();
		database.execSQL("delete from consumes");
		database.setTransactionSuccessful();
		database.endTransaction();
		database.close();
		
	}

	// 插入一笔消费记录
	public long insert_record(ConsumeInfo consume_info) {
		SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
		database.beginTransaction();
        Log.w("InsertRecord","before:"+consume_info.to_string());
		ContentValues values = new ContentValues();
		values.put("user_id", consume_info.get_user_id());
		values.put("consume_id", consume_info.get_consume_id());
		values.put("value", consume_info.get_value());
		values.put("klass", consume_info.get_klass());
		values.put("ymdhms", consume_info.get_ymdhms());
		values.put("remark", consume_info.get_remark());
		values.put("tags_list", consume_info.get_tags_list());
		values.put("created_at", consume_info.get_created_at());
		values.put("updated_at", consume_info.get_updated_at());
		//是否与服务器数据已同步
		values.put("sync", consume_info.get_sync());
		values.put("state", consume_info.get_state());
		long rowid = database.insert("consumes", null, values);
		database.setTransactionSuccessful();
		database.endTransaction();
		database.close();

		//log调试用
		consume_info = get_record(rowid);
        Log.w("InsertRecord","after:"+consume_info.to_string());
		return rowid;
	}
	//取得指定消费记录
	public ConsumeInfo get_record(long rowid) {
		// public Integer getAllRecords(Context context) {
		SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
		//String sql = "select * from consumes where user_id not null and state <> 'delete' " +
		String sql = "select * from consumes where id = " + rowid;
		Cursor cursor = database.rawQuery(sql, null);
	
		ConsumeInfo consume_info = new ConsumeInfo();
		if (cursor.getCount() > 0) {
			cursor.moveToFirst(); 
			consume_info= get_consume_info_from_cursor(cursor);
		}
		cursor.close();
		database.close();
		return consume_info;
	}
	
	//取得所有消费记录
	public ArrayList<ConsumeInfo> get_all_records() {
		// public Integer getAllRecords(Context context) {
		SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
		//String sql = "select * from consumes where user_id not null and state <> 'delete' " +
		String sql = "select * from consumes order by created_at desc";
		Cursor cursor = database.rawQuery(sql, null);
	
		ArrayList<ConsumeInfo> consumeInfos = new ArrayList<ConsumeInfo>();
		if (cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
	
				consumeInfos.add(get_consume_info_from_cursor(cursor));
			}
		}
		cursor.close();
		database.close();
		return consumeInfos;
	}


  
	//取得所有未同步至服务器的消费记录
	public ArrayList<ConsumeInfo> get_unsync_records() {
		// public Integer getAllRecords(Context context) {
		SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
		Cursor cursor = database.rawQuery("select id,user_id,consume_id,value,remark,ymdhms,klass,tags_list,created_at,updated_at,sync,state from consumes where sync = 0", null);
		
		ArrayList<ConsumeInfo> consumeInfos = new ArrayList<ConsumeInfo>();
		if (cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				consumeInfos.add(get_consume_info_from_cursor(cursor));
			}
		}
		cursor.close();
		database.close();
		return consumeInfos;
	}


	
	public ConsumeInfo get_consume_info_from_cursor(Cursor cursor){
		ConsumeInfo consume_info = new ConsumeInfo();
	    
	    consume_info.set_id(cursor.getInt(cursor.getColumnIndex("id")));
	    consume_info.set_user_id(cursor.getInt(cursor.getColumnIndex("user_id")));
	    consume_info.set_consume_id(cursor.getInt(cursor.getColumnIndex("consume_id")));
		consume_info.set_value(cursor.getDouble(cursor.getColumnIndex("value")));
		consume_info.set_ymdhms(cursor.getString(cursor.getColumnIndex("ymdhms")));
		consume_info.set_tags_list(cursor.getString(cursor.getColumnIndex("tags_list")));
		consume_info.set_klass(cursor.getInt(cursor.getColumnIndex("klass")));
		consume_info.set_remark(cursor.getString(cursor.getColumnIndex("remark")).toString());
		consume_info.set_created_at(cursor.getString(cursor.getColumnIndex("created_at")).toString());
		//consume_info.set_updated_at(cursor.getString(cursor.getColumnIndex("updated_at")).toString());
		consume_info.set_sync(cursor.getLong(cursor.getColumnIndex("sync")));
		consume_info.set_state(cursor.getString(cursor.getColumnIndex("state")));
		
		return consume_info;
	}
    /**
     * 取得朋友圈的最大消费记录id
     * 以此来获取最新朋友消费信息
     * @return
     */
	public Integer get_friends_max_consume_id() {
		SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
		String sql = "select max(consume_id) as consume_id from consumes where user_id in "
				+ "(select distinct user_id from users where info = 'friend')";
		Cursor cursor = database.rawQuery(sql, null);
		Integer consume_id = -1;
		if(cursor != null && cursor.getCount()>0){
			cursor.moveToFirst();
		    consume_id = cursor.getInt(cursor.getColumnIndex("consume_id"));
		}
		return consume_id;
	}
    /**
     * @return
     */
	public String get_friends_ids() {
		SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
		String sql = "select distinct user_id from users where info = 'friend'";
		String ids = "";
		Cursor cursor = database.rawQuery(sql, null);
		if(cursor != null && cursor.getCount()>0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				ids = ids + cursor.getInt(cursor.getColumnIndex("user_id")) + ",";
				Log.w("FriendId", ids);
			}
			
			ids = ids.substring(0, ids.length()-1);
		}
		return ids;
	}
}
