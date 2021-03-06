package us.solife.consumes.db;

import java.util.ArrayList;

import us.solife.consumes.entity.ConsumeInfo;
import us.solife.consumes.entity.TagInfo;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TagTb {
	public static final String KEY_ROWID = "id";

	private Context context;
	public ConsumeDatabaseHelper consumeDatabaseHelper;
	static TagTb TagDao;

	private TagTb(Context context) {
		this.context = context;
		this.consumeDatabaseHelper = new ConsumeDatabaseHelper(this.context);
	}

	public static TagTb getTagTb(Context context) {
		if (TagDao == null) TagDao = new TagTb(context);

		return TagDao;
	}

	/**
	 *  插入当前登陆用户所有消费记录
	 *  插入前会先清空实体表
	 * @param consumeInfos
	 */
	public void insertAllTag(ArrayList<TagInfo> tagInfos, Boolean isTruncate) {
		if(isTruncate) truncate_table();
		
		for (int i = 0; i < tagInfos.size(); i++) {
			TagInfo taginfo = tagInfos.get(i);
			//log调试用
            Log.w("TagDao",taginfo.to_string());
            taginfo.set_tag_id((int)taginfo.get_id());
            taginfo.set_sync((long)1);
            insertTag(taginfo);
			//insert_record(info.get_user_id(), (int) info.get_id(), info.get_value(),
			//		info.get_remark(), info.get_created_at(),true);
		}

	}

	public void truncate_table(){
		SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
		database.beginTransaction();
		database.execSQL("delete from tags");
		database.setTransactionSuccessful();
		database.endTransaction();
		database.close();
		
	}

    /**
     * @return
     */
	public ArrayList<TagInfo> getTagWithKlass(Integer klass) {
		SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
		String sql = "select * from tags where klass = "+klass;
		Log.w("KlassSQL", sql);
		Cursor cursor = database.rawQuery(sql, null);

		ArrayList<TagInfo> tag_infos = new ArrayList<TagInfo>();
		if (cursor != null && cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) 
			  tag_infos.add(get_tag_info_from_cursor(cursor));
		}
		Log.w("TagKlassCount", "size:" + tag_infos.size());
		return tag_infos;
	}
	
	public TagInfo findOrCreateTag(TagInfo tagInfo) {
		SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
		String sql = "select * from tags where klass = " + tagInfo.get_klass();
		sql += " and label = '" + tagInfo.get_label()+"'";
		sql += " and user_id = " + tagInfo.get_user_id();
		Log.w("findOrCreateTag", sql);
		Cursor cursor = database.rawQuery(sql, null);


		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();  
			tagInfo = get_tag_info_from_cursor(cursor);
		} else {
			insertTag(tagInfo);
		}
		
		return tagInfo;
	}
	
	public long insertTag(TagInfo tag_info) {
		SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
		database.beginTransaction();
        Log.w("InsertTag","before:"+tag_info.to_string());
		ContentValues values = new ContentValues();
		values.put("user_id", tag_info.get_user_id());
		values.put("tag_id", tag_info.get_tag_id());
		values.put("label", tag_info.get_label());
		values.put("klass", tag_info.get_klass());
		values.put("created_at", tag_info.get_created_at());
		values.put("updated_at", tag_info.get_updated_at());
		//是否与服务器数据已同步
		values.put("sync", tag_info.get_sync());
		values.put("state", tag_info.get_state());
		long rowid = database.insert("tags", null, values);
		database.setTransactionSuccessful();
		database.endTransaction();
		database.close();

		//log调试用
		tag_info = get_record(rowid);
        Log.w("InsertTag","after:"+tag_info.to_string());
		return rowid;
	}
	//取得指定消费记录
	public TagInfo get_record(long rowid) {
		SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
		String sql = "select * from tags where id = " + rowid;
		Cursor cursor = database.rawQuery(sql, null);
	
		TagInfo tag_info = new TagInfo();
		if (cursor !=null && cursor.getCount() > 0) {
			cursor.moveToFirst(); 
			tag_info= get_tag_info_from_cursor(cursor);
		}
		cursor.close();
		database.close();
		return tag_info;
	}
	
	public TagInfo get_tag_info_from_cursor(Cursor cursor) {
		TagInfo tag_info = new TagInfo();
		tag_info.set_id(cursor.getInt(cursor.getColumnIndex("id")));
		tag_info.set_user_id(cursor.getInt(cursor.getColumnIndex("user_id")));
		tag_info.set_tag_id(cursor.getInt(cursor.getColumnIndex("tag_id")));
		tag_info.set_label(cursor.getString(cursor.getColumnIndex("label")));
		tag_info.set_klass(cursor.getInt(cursor.getColumnIndex("klass")));
		tag_info.set_created_at(cursor.getString(cursor.getColumnIndex("created_at")));
		tag_info.set_updated_at(cursor.getString(cursor.getColumnIndex("updated_at")));
		tag_info.set_sync(cursor.getLong(cursor.getColumnIndex("sync")));
		tag_info.set_state(cursor.getString(cursor.getColumnIndex("state")));
		Log.w("FromCursor", tag_info.to_string());
		return tag_info;
	}
	

	  
	//取得所有未同步至服务器的消费记录
	public ArrayList<TagInfo> get_unsync_tags() {
		// public Integer getAllRecords(Context context) {
		SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
		Cursor cursor = database.rawQuery("select * from tags where sync = 0 ", null);
		
		ArrayList<TagInfo> tag_infos = new ArrayList<TagInfo>();
		TagInfo tag_info;
		if (cursor !=null && cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				tag_info = get_tag_info_from_cursor(cursor);
				Log.w("UnsyncTags", tag_info.to_string());
				tag_infos.add(tag_info);
			}
		} else {
			Log.w("UNsyncTag", "isEmpty!");
		}
		cursor.close();
		database.close();
		return tag_infos;
	}

}
