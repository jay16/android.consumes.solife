package us.solife.consumes.db;

import java.util.ArrayList;
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

public class ConsumeDao {
	public static final String KEY_ROWID = "id";
	private static final String DATABASE_TABLE = "consumes";

	private long current_user_id;
	private Context context;
	public ConsumeDatabaseHelper consumeDatabaseHelper;
	static ConsumeDao consumeDao;

	private ConsumeDao(Context context,Long user_id) {
		this.current_user_id = user_id;
		this.context = context;
		this.consumeDatabaseHelper = new ConsumeDatabaseHelper(context);
	}

	public static ConsumeDao getConsumeDao(Context context,Long user_id) {
		if (consumeDao != null) {
		} else {
			consumeDao = new ConsumeDao(context,user_id);

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

			insertRecord(info.getUser_id(), info.getConsume_id(), info.getVolue(),
					info.getMsg(), info.getCreated_at(),info.getUpdated_at(),true);
		}

	}

	//取得所有消费记录
	public ArrayList<ConsumeInfo> getAllRecords(Context context) {
		// public Integer getAllRecords(Context context) {
		SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
		Cursor cursor = database.rawQuery("select * from consumes where length(created_at)>=10 order by created_at desc", null);

	
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
			//sqlite substr(str,begin_index=1,length)
			//java substring(begin_index=0,<end_index)
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
			sql = sql +" and user_id = " + current_user_id +" group by substr(created_at,0,11) order by  substr(created_at,0,11) desc";
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
		String sql = "select * from consumes where substr(created_at,0,11) = '" + day + "' order by created_at asc";
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
			    long sync = cursor.getLong(cursor.getColumnIndex("sync"));
			    
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

    public ArrayList<ConsumeInfo> getDetailRecords(String day) {
		SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
		String sql = "select * from consumes where created_at like '" + day + "%'";
		sql += " order by created_at asc";
		Cursor cursor = database.rawQuery(sql, null);

      
		ArrayList<ConsumeInfo> consumeInfos = new ArrayList<ConsumeInfo>();
		if (cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				ConsumeInfo consumeInfo = new ConsumeInfo();
				
				int id = cursor.getInt(cursor.getColumnIndex("id"));
				int user_id    = cursor.getInt(cursor.getColumnIndex("user_id"));
				int consume_id = cursor.getInt(cursor.getColumnIndex("consume_id"));
				double volue = cursor.getDouble(cursor.getColumnIndex("volue"));
				String msg   = cursor.getString(cursor.getColumnIndex("msg")).toString();
				String created_at = cursor.getString(cursor.getColumnIndex("created_at")).toString();
			    String updated_at = cursor.getString(cursor.getColumnIndex("updated_at"));
			    long sync = cursor.getLong(cursor.getColumnIndex("sync"));
			    
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
	//取得所有未同步至服务器的消费记录
	public ArrayList<ConsumeInfo> getUnSyncRecords() {
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
	
	public ArrayList<ConsumeInfo> user_consume_list() {
		ArrayList<ConsumeInfo> consumeInfos = new ArrayList<ConsumeInfo>();
		String sql;
		String y_m_d = ToolUtils.getStandardDate();
		String week = "00";
		try {
			int i = ToolUtils.getWeekNumber(y_m_d)-1;
			if(i<10) {
			  week = "0"+i;
			} else {
			  week = ""+i;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//消费记录天数
		sql = "select count(distinct substr(created_at,0,11)) as volue,'消费记录天数' as created_at from consumes ";
		sql += "where user_id="+current_user_id;
		addDataToArrayBySql(consumeInfos,sql);
		
		//单笔最大消费记录
		sql = "select volue,substr(created_at,0,11) as created_at from consumes ";
		sql += " where length(created_at)>=10 and user_id="+current_user_id + " order by volue desc";
		addDataToArrayBySql(consumeInfos,sql);
		
		//单天最大消费记录
		sql = "select sum(volue) as volue,substr(created_at,0,11) as created_at from consumes ";
		sql += " where length(created_at)>=10 and user_id="+current_user_id + " group by substr(created_at,0,11) order by sum(volue) desc";
		addDataToArrayBySql(consumeInfos,sql);
	
		//单周消费记录
		sql = "select sum(volue) as volue,strftime('%W',created_at) as created_at from consumes ";
		sql += " where strftime('%W',created_at)='"+week+"' and user_id="+current_user_id;
		sql += " group by strftime('%W',created_at) order by strftime('%W',created_at) desc";
		addDataToArrayBySql(consumeInfos,sql);
		
		//单月消费记录
		sql = "select sum(volue) as volue,substr(created_at,0,8) as created_at from consumes ";
		sql += " where length(created_at)>=10 and substr(created_at,0,8)='"+y_m_d.substring(0,7)+"' ";
		sql += "  and user_id="+current_user_id +" group by substr(created_at,0,8)";
		addDataToArrayBySql(consumeInfos,sql);

		//单年消费记录
		sql = "select sum(volue) as volue,substr(created_at,0,5) as created_at from consumes ";
		sql += " where length(created_at)>=10 and substr(created_at,0,5)='"+y_m_d.substring(0,4)+"' ";
		sql += " and user_id="+current_user_id +"  group by substr(created_at,0,5)";
		addDataToArrayBySql(consumeInfos,sql);
	
		//所有消费记录
		sql = "select sum(volue) as volue,'所有消费记录' as created_at from consumes ";
		sql += " where user_id="+current_user_id;
		addDataToArrayBySql(consumeInfos,sql);
		
		return consumeInfos;
	}
	
	public void addDataToArrayBySql(ArrayList<ConsumeInfo> consumeInfos, String sql) {
		ConsumeInfo consumeInfo = new ConsumeInfo();
		double volue;
		String created_at;
		Cursor cursor;		    

		SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
		cursor = database.rawQuery(sql, null);
		
		if(cursor.getCount()>0){
			cursor.moveToFirst();
			volue = cursor.getDouble(cursor.getColumnIndex("volue"));
			created_at = cursor.getString(cursor.getColumnIndex("created_at")).toString();
			consumeInfo.setVolue(volue); consumeInfo.setCreated_at(created_at);
		} else {
			consumeInfo.setVolue((double)0); consumeInfo.setCreated_at("0000/00/00 00:00");
		}
		consumeInfos.add(consumeInfo);
	}
	//根据不同参数group by 
	//year month week day
	public ArrayList<ConsumeInfo> getConsumeItemList(String ShowType) {
		ArrayList<ConsumeInfo> consumeInfos = new ArrayList<ConsumeInfo>();
		String sql;
        if(ShowType.equals("year")){
        	sql = "select sum(volue) as volue,substr(created_at,0,5) as created_at from consumes ";
        	sql += " where length(created_at)>=10 group by substr(created_at,0,5) order by substr(created_at,0,5) desc";
        } else if(ShowType.equals("month")){
        	sql = "select sum(volue) as volue,substr(created_at,0,8) as created_at from consumes ";
        	sql += " where length(created_at)>=10 group by substr(created_at,0,8) order by substr(created_at,0,8) desc";
        }else if(ShowType.equals("week")){
        	sql = "select sum(volue) as volue,strftime('%W',created_at) as created_at from consumes ";
        	sql += " where length(created_at)>=10 group by strftime('%W',created_at) order by strftime('%W',created_at) desc";
        } else {
        	sql = "select sum(volue) as volue,substr(created_at,0,11) as created_at from consumes ";
            sql += " where length(created_at)>=10 group by substr(created_at,0,11) order by created_at desc";
        }
		double volue;
		String created_at;
		Cursor cursor;		    

		SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
		cursor = database.rawQuery(sql, null);
		
		if(cursor.getCount()>0){
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				ConsumeInfo consumeInfo = new ConsumeInfo();
				volue = cursor.getDouble(cursor.getColumnIndex("volue"));
				created_at = cursor.getString(cursor.getColumnIndex("created_at")).toString();
				consumeInfo.setVolue(volue); consumeInfo.setCreated_at(created_at);
				consumeInfos.add(consumeInfo);
			}
		}
		return consumeInfos;
	}
	
}
