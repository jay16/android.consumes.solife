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
 * ���Ѽ�¼ʵ���
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

	public static ConsumeTb get_record_tb(Context context) {
		if (consumeDao != null) {
		} else {
			consumeDao = new ConsumeTb(context);
		}
		return consumeDao;
	}

	/**
	 *  ���뵱ǰ��½�û��������Ѽ�¼
	 *  ����ǰ�������ʵ���
	 * @param consumeInfos
	 */
	public void insert_all_record(ArrayList<ConsumeInfo> consumeInfos, Boolean isTruncate) {
		if(isTruncate) truncate_table();
		
		for (int i = 0; i < consumeInfos.size(); i++) {
			ConsumeInfo info = consumeInfos.get(i);
			//log������
            Log.w("ConsumeDao",info.to_string());
            
			insert_record(info.get_user_id(), (int) info.get_id(), info.get_value(),
					info.get_remark(), info.get_created_at(),true);
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
	// ����һ�����Ѽ�¼
	public long insert_record(Integer user_id, Integer consume_id, Double value,
			String remark, String created_at, Boolean sync) {
		SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
		database.beginTransaction();

		ContentValues values = new ContentValues();
		values.put("user_id", user_id);
		values.put("consume_id", consume_id);
		values.put("value", value);
		values.put("remark", remark);
		values.put("created_at", created_at);
		//�Ƿ��������������ͬ��
		values.put("sync", sync);
		long rowid = database.insert("consumes", null, values);
		//log������
        Log.w("ConsumeDao","�������ݿ⶯�����id:["+rowid+"]");
		database.setTransactionSuccessful();
		database.endTransaction();
		database.close();

		return rowid;
	}
	
	//ȡ���������Ѽ�¼
	public ArrayList<ConsumeInfo> get_all_records(Context context) {
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


  
	//ȡ������δͬ���������������Ѽ�¼
	public ArrayList<ConsumeInfo> get_unsync_records() {
		// public Integer getAllRecords(Context context) {
		SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
		Cursor cursor = database.rawQuery("select * from consumes where sync = 0", null);
		
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
		consume_info.set_remark(cursor.getString(cursor.getColumnIndex("remark")).toString());
		consume_info.set_created_at(cursor.getString(cursor.getColumnIndex("created_at")).toString());
		consume_info.set_sync(cursor.getLong(cursor.getColumnIndex("sync")));
		consume_info.set_state(cursor.getString(cursor.getColumnIndex("state")));
		
		return consume_info;
	}
    /**
     * ȡ������Ȧ��������Ѽ�¼id
     * �Դ�����ȡ��������������Ϣ
     * @return
     */
	public Integer get_friends_max_consume_id(Integer user_id) {
		SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
		String sql = "select max(consume_id) as consume_id from consumes where user_id <> " + user_id;
		Cursor cursor = database.rawQuery(sql, null);
		Integer consume_id = -1;
		if(cursor.getCount()>0){
			cursor.moveToFirst();
		    consume_id = cursor.getInt(cursor.getColumnIndex("consume_id"));
		}
		return consume_id;
	}
}
