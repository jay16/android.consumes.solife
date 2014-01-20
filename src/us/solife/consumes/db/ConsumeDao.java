package us.solife.consumes.db;

import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

import us.solife.consumes.TabList;
import us.solife.consumes.entity.ConsumeInfo;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class ConsumeDao {
	public static final String KEY_ROWID = "id";
	private static final String DATABASE_TABLE = "consumes";

	private Context context;
	public ConsumeDatabaseHelper consumeDatabaseHelper;
	static ConsumeDao consumeDao;

	private ConsumeDao(Context context) {
		this.context = context;
		this.consumeDatabaseHelper = new ConsumeDatabaseHelper(context);
	}

	public static ConsumeDao getConsumeDao(Context context) {
		if (consumeDao != null) {
		} else {
			consumeDao = new ConsumeDao(context);

		}
		return consumeDao;

	}

	// 插入当前登陆用户所有消费记录
	public void insertAllRecord(Context context, ArrayList<ConsumeInfo> consumeInfos) {
		SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
		database.beginTransaction();
		database.execSQL("delete from consumes");
		database.setTransactionSuccessful();
		database.endTransaction();
		database.close();
		for (int i = 0; i < consumeInfos.size(); i++) {
			ConsumeInfo info = consumeInfos.get(i);
			/*
			ContentValues values = new ContentValues();
			values.put("user_id", info.getUser_id());
			values.put("consume_id", info.getConsume_id());
			values.put("volue", info.getVolue());
			values.put("msg", info.getMsg());
			values.put("created_at", info.getCreated_at());
			values.put("updated_at", info.getUpdated_at());
			// 与服务器数据已同步
			values.put("sync", (long)1);
			database.insert("consumes", "created_at", values);
			*/
			insertRecord(info.getUser_id(), info.getConsume_id(), info.getVolue(),
					info.getMsg(), info.getCreated_at(),info.getUpdated_at(),true);
		}

		//database.setTransactionSuccessful();
		//database.endTransaction();
		//database.close();

	}

	//取得所有消费记录
	public ArrayList<ConsumeInfo> getAllRecords(Context context) {
		// public Integer getAllRecords(Context context) {
		SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
		Cursor cursor = database.rawQuery("select * from consumes order by created_at desc", null);

		// Cursor cursor = database.query(true, DATABASE_TABLE, new String[] {
		// "volue","msg","created_at" }, null, null, null, null, null, null);
		ArrayList<ConsumeInfo> consumeInfos = new ArrayList<ConsumeInfo>();
		if (cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				ConsumeInfo consumeInfo = new ConsumeInfo();
				
				int id = cursor.getInt(cursor.getColumnIndex("id"));
				int user_id    = cursor.getInt(cursor.getColumnIndex("user_id"));
				int consume_id = cursor.getInt(cursor.getColumnIndex("consume_id"));
				double volue = cursor.getDouble(cursor.getColumnIndex("volue"));
				String msg = cursor.getString(cursor.getColumnIndex("msg")).toString();
				String created_at = cursor.getString(cursor.getColumnIndex("created_at")).toString();
			    String updated_at = cursor.getString(cursor.getColumnIndex("updated_at"));
			    Long sync         = cursor.getLong(cursor.getColumnIndex("sync"));
			    
				consumeInfo.setId(id);
				consumeInfo.setUser_id(user_id);
				consumeInfo.setConsume_id(consume_id);
				consumeInfo.setVolue(volue);
				consumeInfo.setMsg(msg);
				consumeInfo.setCreated_at(created_at);
				consumeInfo.setUpdated_at(updated_at);
				consumeInfo.setSync(sync);
				consumeInfos.add(consumeInfo);
			}
		}
		cursor.close();
		database.close();
		return consumeInfos;
	}

	//取得所有消费记录
	//按天分组
    public ArrayList<ConsumeInfo> getRecordsAsDay(Context context,String show_type,String day) {
			// public Integer getAllRecords(Context context) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String y_m_d = df.format(new Date());
			SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
			String sql = "select substr(created_at,0,11) as created_at,count(*) as count,sum(volue) as volue " +
					"from consumes where length(created_at)>=10 ";
			if(show_type.equals("day")){
				  sql = sql + " and substr(created_at,0,11)='"+day+"' ";
			} else if(show_type.equals("month")){
			  sql = sql + " and substr(created_at,0,8)='"+y_m_d.substring(0,7)+"' ";
			} else if(show_type.equals("year")) {
				  sql = sql + " and substr(created_at,0,5)='"+y_m_d.substring(0,4)+"' ";
			} else if(show_type.equals("all")) {
				
			} else {
				  sql = sql + " and substr(created_at,0,8)='"+y_m_d.substring(0,7)+"' ";
			}
			sql = sql +" group by substr(created_at,0,11) order by  substr(created_at,0,11) desc";
			Cursor cursor = database.rawQuery(sql, null);

			// Cursor cursor = database.query(true, DATABASE_TABLE, new String[] {
			// "volue","msg","created_at" }, null, null, null, null, null, null);
			ArrayList<ConsumeInfo> consumeInfos = new ArrayList<ConsumeInfo>();
			if (cursor.getCount() > 0) {
				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
					ConsumeInfo consumeInfo = new ConsumeInfo();
					
					int count = cursor.getInt(cursor.getColumnIndex("count"));
					//int user_id    = cursor.getInt(cursor.getColumnIndex("user_id"));
					//int consume_id = cursor.getInt(cursor.getColumnIndex("consume_id"));
					double volue = cursor.getDouble(cursor.getColumnIndex("volue"));
					//String msg = cursor.getString(cursor.getColumnIndex("msg")).toString();
					String created_at = cursor.getString(cursor.getColumnIndex("created_at")).toString();
				    //String updated_at = cursor.getString(cursor.getColumnIndex("count"));
				    
					consumeInfo.setId(count);
					//consumeInfo.setUser_id(user_id);
					//consumeInfo.setConsume_id(consume_id);
					consumeInfo.setVolue(volue);
					//consumeInfo.setMsg(msg);
					consumeInfo.setCreated_at(created_at);
					//consumeInfo.setUpdated_at(updated_at);
					//consumeInfo.setSync(sync);
					consumeInfos.add(consumeInfo);
				}
			}
			cursor.close();
			database.close();
			return consumeInfos;
		}

    public ArrayList<ConsumeInfo> getDayDetailRecords(String day) {
		SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
		String sql = "select * from consumes where substr(created_at,0,11) = '" + day + "' order by created_at desc";
		Cursor cursor = database.rawQuery(sql, null);


		ArrayList<ConsumeInfo> consumeInfos = new ArrayList<ConsumeInfo>();
		if (cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				ConsumeInfo consumeInfo = new ConsumeInfo();
				
				int id = cursor.getInt(cursor.getColumnIndex("id"));
				int user_id    = cursor.getInt(cursor.getColumnIndex("user_id"));
				int consume_id = cursor.getInt(cursor.getColumnIndex("consume_id"));
				double volue = cursor.getDouble(cursor.getColumnIndex("volue"));
				String msg = cursor.getString(cursor.getColumnIndex("msg")).toString();
				String created_at = cursor.getString(cursor.getColumnIndex("created_at")).toString();
			    String updated_at = cursor.getString(cursor.getColumnIndex("updated_at"));
			    
				consumeInfo.setId(id);
				consumeInfo.setUser_id(user_id);
				consumeInfo.setConsume_id(consume_id);
				consumeInfo.setVolue(volue);
				consumeInfo.setMsg(msg);
				consumeInfo.setCreated_at(created_at);
				consumeInfo.setUpdated_at(updated_at);
				//consumeInfo.setSync(sync);
				consumeInfos.add(consumeInfo);
			}
		}
		cursor.close();
		database.close();
		return consumeInfos;
	}


	//取得所有未同步至服务器的消费记录
	public ArrayList<ConsumeInfo> getUnSyncRecords(Context context) {
		// public Integer getAllRecords(Context context) {
		SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
		Cursor cursor = database.rawQuery("select * from consumes where sync = 0", null);
		
		ArrayList<ConsumeInfo> consumeInfos = new ArrayList<ConsumeInfo>();
		if (cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				ConsumeInfo consumeInfo = new ConsumeInfo();
				
				int id = cursor.getInt(cursor.getColumnIndex("id"));
				double volue = cursor.getDouble(cursor.getColumnIndex("volue"));
				String msg = cursor.getString(cursor.getColumnIndex("msg")).toString();
				String created_at = cursor.getString(cursor.getColumnIndex("created_at")).toString();
			    String updated_at = cursor.getString(cursor.getColumnIndex("updated_at"));
			    Long sync         = cursor.getLong(cursor.getColumnIndex("sync"));
			    
			    consumeInfo.setId(id);
				consumeInfo.setVolue(volue);
				consumeInfo.setMsg(msg);
				consumeInfo.setCreated_at(created_at);
				consumeInfo.setUpdated_at(updated_at);
				consumeInfo.setSync(sync);
				consumeInfos.add(consumeInfo);
			}
		}
		cursor.close();
		database.close();
		return consumeInfos;
	}
	// 插入一笔消费记录
	public long insertRecord(Integer user_id, Integer consume_id, Double volue,
			String msg, String created_at, String updated_at, Boolean sync) {
		SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
		database.beginTransaction();

		ContentValues values = new ContentValues();
		values.put("user_id", user_id);
		values.put("consume_id", consume_id);
		values.put("volue", volue);
		values.put("msg", msg);
		values.put("created_at", created_at);
		values.put("updated_at", created_at);
		//是否与服务器数据已同步
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
				"volue", "msg", "created_at","sync" }, "id=" + row_id, null, null,
				null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}
	
	public int updateUnSyncRecordByRowId(long row_id,int user_id, int consume_id) {
		SQLiteDatabase db = consumeDatabaseHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("user_id", user_id);
		cv.put("consume_id", user_id);
		cv.put("sync", true);
		String[] args = {String.valueOf(row_id)};

		return db.update(DATABASE_TABLE, cv, "id=?",args);
	}
}
