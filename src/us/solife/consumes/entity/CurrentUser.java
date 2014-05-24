package us.solife.consumes.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import us.solife.consumes.db.ConsumeDatabaseHelper;
import us.solife.consumes.util.ToolUtils;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * 登陆用户信息
 * @author jay (http://solife.us/resume)
 * @version 1.0
 * @created 2014-02-25
 */
public class CurrentUser {
	public static final String KEY_ROWID = "id";
	private static final String DT_CONSUME = "consumes";

	private int    user_id;
	private Context context;
	public ConsumeDatabaseHelper consumeDatabaseHelper;
	static CurrentUser consume_user;
	
	private CurrentUser(Context context,int user_id) {
		this.user_id = user_id;
		this.context = context;
		this.consumeDatabaseHelper = new ConsumeDatabaseHelper(context);
	}

	public static CurrentUser get_current_user(Context context,int user_id) {
		if (consume_user != null) {
		} else {
			consume_user = new CurrentUser(context,user_id);

		}
		return consume_user;
	}
	
    /**
     * 根据row_id获取消费信息
     * 编辑消费消费时使用
     * @param row_id
     * @return
     */
	public ConsumeInfo get_record(long row_id) {
		SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
		Cursor cursor = database.rawQuery("select * from consumes where id = "+row_id + " and user_id = "+ user_id, null);
        ConsumeInfo consume_info = new ConsumeInfo();
		if (cursor != null) {
			cursor.moveToFirst();
			consume_info = get_consume_info_from_cursor(cursor);
		}
		return consume_info;
	}

	/**
	 *  创建消费记录
	 * @param value
	 * @param remark
	 * @param created_at
	 * @return
	 */
	public long insert_record(Double value, String remark, String created_at) {
		SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
		database.beginTransaction();

		ContentValues values = new ContentValues();
		values.put("user_id", user_id);
		values.put("consume_id", -1);
		values.put("value", value);
		values.put("remark", remark);
		values.put("created_at", created_at);
		values.put("updated_at", created_at);
		//是否与服务器数据已同步
		values.put("sync", false);
		values.put("state", "create");
		/*
		 * sync & state
		 * sync:false state: create 待同步创建
		 * sync:false state: update 待同步更新
		 * sync:false state: delete 待同步删除
		 */
		long row_id = database.insert("consumes", null, values);

		database.setTransactionSuccessful();
		database.endTransaction();
		database.close();
		
		ConsumeInfo consume_info = get_record(row_id);
        Log.w("CurrentUser","创建数据:"+consume_info.to_string());
        
		return row_id;
	}
	
	public long update_record(long row_id, Double value, String remark,String created_at){
		SQLiteDatabase db = consumeDatabaseHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("user_id", user_id);
		cv.put("value", value);
		cv.put("remark", remark);
		cv.put("created_at", created_at);
		cv.put("sync", false);
		cv.put("state", "update");
		String[] args = {String.valueOf(row_id)};

        db.update(DT_CONSUME, cv, "id=?",args);
        
		ConsumeInfo consume_info = get_record(row_id);
        Log.w("CurrentUser","更新数据1:"+consume_info.to_string());
        
        return row_id;
	}
	
	public long update_record(ConsumeInfo consume_info){
		SQLiteDatabase db = consumeDatabaseHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("user_id", user_id);
		cv.put("consume_id", consume_info.get_consume_id());
		cv.put("value", consume_info.get_value());
		cv.put("remark", consume_info.get_remark());
		cv.put("created_at", consume_info.get_created_at());
		cv.put("sync", consume_info.get_sync());
		cv.put("state", consume_info.get_state());
		String[] args = {String.valueOf(consume_info.get_id())};
		//log调试用

		long row_id = db.update(DT_CONSUME, cv, "id=?",args);
		
		consume_info = get_record(consume_info.get_id());
        Log.w("CurrentUser","更新数据2:"+consume_info.to_string());
		return row_id;
	}
	/**
	 * 直接删除
	 * @param row_id
	 */
	public void delete_record(long row_id) {	
		SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
		database.beginTransaction();
		database.execSQL("delete from consumes where id = "+row_id + " and user_id = " + user_id);
		database.setTransactionSuccessful();
		database.endTransaction();
		database.close();
		//log调试用
        Log.w("CurrentUser","删除数据:"+row_id);
	}
	
	/**
	 * 通过状态判断
	 * 删除或修改状态
	 * @param row_id
	 */
	public void destroy_record(long row_id) {	
		ConsumeInfo consume_info = get_record(row_id);
		long sync = consume_info.get_sync();
		//未同步时直接删除即可
		if(sync == (long)0) {
			delete_record(row_id);
	        Log.w("CurrentUser","删除数据:"+consume_info.to_string());
		//同步时修改状态
		//sync: false, state: "delete"
		} else {
		  consume_info.set_sync((long)0);
		  consume_info.set_state("delete");
		  update_record(consume_info);
	      Log.w("CurrentUser","修改状态:"+consume_info.to_string());
		}
	}
	//取得所有消费记录
	public ArrayList<ConsumeInfo> get_all_records(Context context) {
		// public Integer getAllRecords(Context context) {
		SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
		String sql = "select * from consumes where user_id = " + user_id + " and state <> 'delete'" + 
	      " order by created_at desc";
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

	//取得所有消费记录
	//按天分组
    public ArrayList<ConsumeInfo> get_consumes_with_day(Context context,String show_type,String day) {
			// public Integer getAllRecords(Context context) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String y_m_d = df.format(new Date());
			SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
			String sql = "select substr(created_at,0,11) as created_at,count(*) as count,sum(value) as value " +
			  "from consumes where user_id = " + user_id + " ";

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

			ArrayList<ConsumeInfo> consumeInfos = new ArrayList<ConsumeInfo>();
			if (cursor.getCount() > 0) {
				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
					ConsumeInfo consumeInfo = new ConsumeInfo();
					
					int count = cursor.getInt(cursor.getColumnIndex("count"));
					double value = cursor.getDouble(cursor.getColumnIndex("value"));
					String created_at = cursor.getString(cursor.getColumnIndex("created_at")).toString();
				    
					consumeInfo.set_id(count);
					consumeInfo.set_value(value);
					consumeInfo.set_created_at(created_at);
					consumeInfos.add(consumeInfo);
				}
			}
			cursor.close();
			database.close();
			return consumeInfos;
		}


    /**
     * 查看某年/月/天的具体消费记录明细
     * ConsumeItem中调用
     * @param day
     */
    public ArrayList<ConsumeInfo> get_consume_items_with_date(String day) {
		SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
		String sql = "select * from consumes where user_id = " + user_id +
		" and created_at like '" + day + "%' and state <> 'delete' order by created_at asc";
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


	
	/**
	 * cursor内容存入consumeInfo
	 * @param cursor
	 * @return
	 */
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
	 * 总结用户消费消息
	 * @return
	 */
	public ArrayList<ConsumeInfo> consume_info_list() {
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
			e.printStackTrace();
		}
		
		//消费记录天数
		sql = "select count(distinct substr(created_at,0,11)) as value,'消费记录天数' as created_at from consumes ";
		sql += "where user_id="+user_id;
		add_data_to_array_with_sql(consumeInfos,sql);
		
		//单笔最大消费记录
		sql = "select value,substr(created_at,0,11) as created_at from consumes ";
		sql += " where length(created_at)>=10 and user_id="+user_id + " order by value desc";
		add_data_to_array_with_sql(consumeInfos,sql);
		
		//单天最大消费记录
		sql = "select sum(value) as value,substr(created_at,0,11) as created_at from consumes ";
		sql += " where length(created_at)>=10 and user_id="+user_id + " group by substr(created_at,0,11) order by sum(value) desc";
		add_data_to_array_with_sql(consumeInfos,sql);
	
		//单周消费记录
		sql = "select sum(value) as value,strftime('%W',created_at) as created_at from consumes ";
		sql += " where strftime('%W',created_at)='"+week+"' and user_id="+user_id;
		sql += " group by strftime('%W',created_at) order by strftime('%W',created_at) desc";
		add_data_to_array_with_sql(consumeInfos,sql);
		
		//单月消费记录
		sql = "select sum(value) as value,substr(created_at,0,8) as created_at from consumes ";
		sql += " where length(created_at)>=10 and substr(created_at,0,8)='"+y_m_d.substring(0,7)+"' ";
		sql += "  and user_id="+user_id +" group by substr(created_at,0,8)";
		add_data_to_array_with_sql(consumeInfos,sql);

		//单年消费记录
		sql = "select sum(value) as value,substr(created_at,0,5) as created_at from consumes ";
		sql += " where length(created_at)>=10 and substr(created_at,0,5)='"+y_m_d.substring(0,4)+"' ";
		sql += " and user_id="+user_id +"  group by substr(created_at,0,5)";
		add_data_to_array_with_sql(consumeInfos,sql);
	
		//所有消费记录
		sql = "select sum(value) as value,'所有消费记录' as created_at from consumes ";
		sql += " where user_id="+user_id;
		add_data_to_array_with_sql(consumeInfos,sql);
		
		return consumeInfos;
	}
	
	public void add_data_to_array_with_sql(ArrayList<ConsumeInfo> consumeInfos, String sql) {
		ConsumeInfo consumeInfo = new ConsumeInfo();
		double value;
		String created_at;
		Cursor cursor;		    

		SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
		cursor = database.rawQuery(sql, null);
		
		if(cursor.getCount()>0){
			cursor.moveToFirst();
			value = cursor.getDouble(cursor.getColumnIndex("value"));
			created_at = cursor.getString(cursor.getColumnIndex("created_at")).toString();
			consumeInfo.set_value(value); consumeInfo.set_created_at(created_at);
		} else {
			consumeInfo.set_value((double)0); consumeInfo.set_created_at("0000/00/00 00:00");
		}
		consumeInfos.add(consumeInfo);
	}
	/**
	 * 消费记录列表
	 * 根据选项展示方式不同
	 * year month week day
	 */
	public ArrayList<ConsumeInfo> get_all_consume_item(String show_type) {
		ArrayList<ConsumeInfo> consumeInfos = new ArrayList<ConsumeInfo>();
		String sql;
        if(show_type.equals("year")){
        	sql = "select sum(value) as value,substr(created_at,0,5) as created_at from consumes ";
        	sql += " where user_id = " + user_id + " and state <> 'delete' group by substr(created_at,0,5) order by substr(created_at,0,5) desc";
        } else if(show_type.equals("month")){
        	sql = "select sum(value) as value,substr(created_at,0,8) as created_at from consumes ";
        	sql += " where user_id = " + user_id + " and state <> 'delete' group by substr(created_at,0,8) order by substr(created_at,0,8) desc";
        }else if(show_type.equals("week")){
        	sql = "select sum(value) as value,strftime('%W',created_at) as created_at from consumes ";
        	sql += " where user_id = " + user_id + " and state <> 'delete' group by strftime('%W',created_at) order by strftime('%W',created_at) desc";
        }else {
        	sql = "select sum(value) as value,substr(created_at,0,11) as created_at from consumes ";
            sql += " where user_id = " + user_id + " and state <> 'delete' group by substr(created_at,0,11) order by created_at desc";
        }
		double value;
		String created_at;
		Cursor cursor;		    

		SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
		cursor = database.rawQuery(sql, null);
		
		if(cursor.getCount()>0){
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				ConsumeInfo consumeInfo = new ConsumeInfo();
				value = cursor.getDouble(cursor.getColumnIndex("value"));
				created_at = cursor.getString(cursor.getColumnIndex("created_at")).toString();
				consumeInfo.set_value(value); consumeInfo.set_created_at(created_at);
				consumeInfos.add(consumeInfo);
			}
		}
		return consumeInfos;
	}
	
    public UserInfo get_current_user_info() {
		SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
		String sql = "select * from users where user_id = " + user_id;
		Cursor cursor = database.rawQuery(sql, null);
    	UserInfo user_info = new UserInfo();
    	
		if(cursor.getCount()>0){
			cursor.moveToFirst();
			
		    user_info.set_id(cursor.getInt(cursor.getColumnIndex("id")));
		    user_info.set_user_id(cursor.getInt(cursor.getColumnIndex("user_id")));
		    user_info.set_name(cursor.getString(cursor.getColumnIndex("name")).toString());
		    user_info.set_email(cursor.getString(cursor.getColumnIndex("email")).toString());
		    user_info.set_area(cursor.getString(cursor.getColumnIndex("area")));
		    user_info.set_gravatar(cursor.getString(cursor.getColumnIndex("gravatar")));
		    user_info.set_created_at(cursor.getString(cursor.getColumnIndex("created_at")));
		    user_info.set_updated_at(cursor.getString(cursor.getColumnIndex("updated_at")));
		    user_info.set_sync(cursor.getLong(cursor.getColumnIndex("sync")));
		    user_info.set_state(cursor.getString(cursor.getColumnIndex("state")));
		}
    	return user_info;
    }
    


}
