package us.solife.consumes.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import us.solife.consumes.entity.ConsumeInfo;
import us.solife.consumes.entity.UserInfo;
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

	private long    user_id;
	private Context context;
	public ConsumeDatabaseHelper consumeDatabaseHelper;
	static CurrentUser consume_user;
	
	private CurrentUser(Context context,Long user_id) {
		this.user_id = user_id;
		this.context = context;
		this.consumeDatabaseHelper = new ConsumeDatabaseHelper(context);
	}

	public static CurrentUser getCurrentUser(Context context,Long user_id) {
		if (consume_user != null) {
		} else {
			consume_user = new CurrentUser(context,user_id);

		}
		return consume_user;
	}
	


	// 当前登陆用户新建一笔消费记录
	public long insert_record(Double volue, String msg, String created_at) {
		SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
		database.beginTransaction();

		ContentValues values = new ContentValues();
		values.put("user_id", user_id);
		values.put("consume_id", -1);
		values.put("volue", volue);
		values.put("msg", msg);
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
		long rowid = database.insert("consumes", null, values);
		
		//log调试用
        Log.w("ConsumeDao","插入数据库动作完成id:["+rowid+"]");
		database.setTransactionSuccessful();
		database.endTransaction();
		database.close();

		return rowid;
	}
	
	public long update_record(Integer row_id, Double volue, String msg,String created_at){
		SQLiteDatabase db = consumeDatabaseHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("user_id", user_id);
		cv.put("volue", volue);
		cv.put("msg", msg);
		cv.put("created_at", created_at);
		cv.put("sync", false);
		cv.put("state", "update");
		String[] args = {String.valueOf(row_id)};

		return db.update(DT_CONSUME, cv, "id=?",args);
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
			String sql = "select substr(created_at,0,11) as created_at,count(*) as count,sum(volue) as volue " +
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
					double volue = cursor.getDouble(cursor.getColumnIndex("volue"));
					String created_at = cursor.getString(cursor.getColumnIndex("created_at")).toString();
				    
					consumeInfo.set_id(count);
					consumeInfo.set_volue(volue);
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
     * 根据row_id获取消费信息
     * 编辑消费消费时使用
     * @param row_id
     * @return
     */
	public ConsumeInfo get_record_with_rowid(Integer row_id) {
		SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
		Cursor cursor = database.rawQuery("select * from consumes where id="+row_id, null);
        ConsumeInfo consume_info = new ConsumeInfo();
		if (cursor != null) {
			cursor.moveToFirst();
			consume_info = get_consume_info_from_cursor(cursor);
		}
		return consume_info;
	}
	
	/**
	 * cursor内容存入consumeInfo
	 * @param cursor
	 * @return
	 */
	public ConsumeInfo get_consume_info_from_cursor(Cursor cursor){
		ConsumeInfo consumeInfo = new ConsumeInfo();
		
		int id = cursor.getInt(cursor.getColumnIndex("id"));
		double volue = cursor.getDouble(cursor.getColumnIndex("volue"));
		String msg = cursor.getString(cursor.getColumnIndex("msg")).toString();
		String created_at = cursor.getString(cursor.getColumnIndex("created_at")).toString();
	    String updated_at = cursor.getString(cursor.getColumnIndex("updated_at"));
	    Long sync         = cursor.getLong(cursor.getColumnIndex("sync"));
	    
	    consumeInfo.set_id(id);
		consumeInfo.set_volue(volue);
		consumeInfo.set_msg(msg);
		consumeInfo.set_created_at(created_at);
		consumeInfo.set_updated_at(updated_at);
		consumeInfo.set_sync(sync);
		
		return consumeInfo;
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
		sql = "select count(distinct substr(created_at,0,11)) as volue,'消费记录天数' as created_at from consumes ";
		sql += "where user_id="+user_id;
		add_data_to_array_with_sql(consumeInfos,sql);
		
		//单笔最大消费记录
		sql = "select volue,substr(created_at,0,11) as created_at from consumes ";
		sql += " where length(created_at)>=10 and user_id="+user_id + " order by volue desc";
		add_data_to_array_with_sql(consumeInfos,sql);
		
		//单天最大消费记录
		sql = "select sum(volue) as volue,substr(created_at,0,11) as created_at from consumes ";
		sql += " where length(created_at)>=10 and user_id="+user_id + " group by substr(created_at,0,11) order by sum(volue) desc";
		add_data_to_array_with_sql(consumeInfos,sql);
	
		//单周消费记录
		sql = "select sum(volue) as volue,strftime('%W',created_at) as created_at from consumes ";
		sql += " where strftime('%W',created_at)='"+week+"' and user_id="+user_id;
		sql += " group by strftime('%W',created_at) order by strftime('%W',created_at) desc";
		add_data_to_array_with_sql(consumeInfos,sql);
		
		//单月消费记录
		sql = "select sum(volue) as volue,substr(created_at,0,8) as created_at from consumes ";
		sql += " where length(created_at)>=10 and substr(created_at,0,8)='"+y_m_d.substring(0,7)+"' ";
		sql += "  and user_id="+user_id +" group by substr(created_at,0,8)";
		add_data_to_array_with_sql(consumeInfos,sql);

		//单年消费记录
		sql = "select sum(volue) as volue,substr(created_at,0,5) as created_at from consumes ";
		sql += " where length(created_at)>=10 and substr(created_at,0,5)='"+y_m_d.substring(0,4)+"' ";
		sql += " and user_id="+user_id +"  group by substr(created_at,0,5)";
		add_data_to_array_with_sql(consumeInfos,sql);
	
		//所有消费记录
		sql = "select sum(volue) as volue,'所有消费记录' as created_at from consumes ";
		sql += " where user_id="+user_id;
		add_data_to_array_with_sql(consumeInfos,sql);
		
		return consumeInfos;
	}
	
	public void add_data_to_array_with_sql(ArrayList<ConsumeInfo> consumeInfos, String sql) {
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
			consumeInfo.set_volue(volue); consumeInfo.set_created_at(created_at);
		} else {
			consumeInfo.set_volue((double)0); consumeInfo.set_created_at("0000/00/00 00:00");
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
        	sql = "select sum(volue) as volue,substr(created_at,0,5) as created_at from consumes ";
        	sql += " where user_id = " + user_id + " and state <> 'delete' group by substr(created_at,0,5) order by substr(created_at,0,5) desc";
        } else if(show_type.equals("month")){
        	sql = "select sum(volue) as volue,substr(created_at,0,8) as created_at from consumes ";
        	sql += " where user_id = " + user_id + " and state <> 'delete' group by substr(created_at,0,8) order by substr(created_at,0,8) desc";
        }else if(show_type.equals("week")){
        	sql = "select sum(volue) as volue,strftime('%W',created_at) as created_at from consumes ";
        	sql += " where user_id = " + user_id + " and state <> 'delete' group by strftime('%W',created_at) order by strftime('%W',created_at) desc";
        }else {
        	sql = "select sum(volue) as volue,substr(created_at,0,11) as created_at from consumes ";
            sql += " where user_id = " + user_id + " and state <> 'delete' group by substr(created_at,0,11) order by created_at desc";
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
				consumeInfo.set_volue(volue); consumeInfo.set_created_at(created_at);
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
