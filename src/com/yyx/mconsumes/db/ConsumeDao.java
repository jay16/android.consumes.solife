package com.yyx.mconsumes.db;

import java.util.ArrayList;

import com.yyx.mconsumes.entity.ConsumeInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ConsumeDao {   
	public static final String KEY_ROWID = "id";    
	private static final String DATABASE_TABLE = "consumes"; 

	private Context context;
	public ConsumeDatabaseHelper consumeDatabaseHelper;

	public ConsumeDao(Context context) {
		this.context = context;
		this.consumeDatabaseHelper = new ConsumeDatabaseHelper(context);
	}

	// 插入当前登陆用户所有消费记录
	public void insertAllRecord(Context context, ArrayList<ConsumeInfo> consumeInfos) {
		SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
		database.beginTransaction();
		database.execSQL("delete from consumes");
		for (int i = 0; i < consumeInfos.size(); i++) {
			ConsumeInfo info = consumeInfos.get(i);
			ContentValues values = new ContentValues();
			values.put("user_id", info.getUserId());
			values.put("consume_id", info.getConsumeId());
			values.put("volue", info.getVolue());
			values.put("msg", info.getMsg());
			values.put("created_at", info.getCreated_at());
			values.put("updated_at", info.getUpdated_at());
			//与服务器数据已同步
			values.put("sync", true);
			database.insert("consumes", "created_at", values);
		}

		database.setTransactionSuccessful();
		database.endTransaction();
		database.close();

	}

	public ArrayList<ConsumeInfo> getAllRecords(Context context) {
		SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
		Cursor cursor = database.rawQuery("select * from consumes", null);
		ArrayList<ConsumeInfo> consumeInfos = new ArrayList<ConsumeInfo>();
		while (cursor.moveToNext()) {
			ConsumeInfo consumeInfo = new ConsumeInfo();

			int user_id    = cursor.getInt(cursor.getColumnIndex("user_id"));
			int consume_id = cursor.getInt(cursor.getColumnIndex("consume_id"));
			double amount  = cursor.getDouble(cursor.getColumnIndex("volue"));
			String msg     = cursor.getString(cursor.getColumnIndex("msg"));
			String created_at = cursor.getString(cursor.getColumnIndex("created_at"));
			String update_at = cursor.getString(cursor.getColumnIndex("updated_at"));
			consumeInfo.setConsumeId(user_id);
			consumeInfo.setConsumeId(consume_id);
			consumeInfo.setMsg(msg);
			consumeInfo.setVolue(amount);
			consumeInfo.setUpdated_at(update_at);
			consumeInfo.setCreated_at(created_at);
			consumeInfos.add(consumeInfo);
		}
		cursor.close();
		database.close();
		return consumeInfos;
	}
	
	// 插入一笔消费记录
	public long insertRecord(Integer user_id, Integer consume_id, String volue, 
			String msg, String created_at, Boolean sync) {
		SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
		database.beginTransaction();

		ContentValues values = new ContentValues();
		values.put("user_id", user_id);
		values.put("consume_id", consume_id);
		values.put("volue", volue);
		values.put("msg", msg);
		values.put("created_at", created_at);
		values.put("updated_at", created_at);
		//与服务器数据已同步
		values.put("sync", sync);
		long rowid = database.insert("consumes", "created_at", values);

		database.setTransactionSuccessful();
		database.endTransaction();
		database.close();
		
		return rowid;
	}

	public Cursor getRecordByRowId(long row_id) {
		SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
		Cursor mCursor = database.query(true, DATABASE_TABLE, new String[] {  
                "volue","msg","created_at" },
                "id=" + row_id, null, null, null, null, null);  

        if (mCursor != null) {  
            mCursor.moveToFirst();  
        }
		
		return mCursor;
	}
}
