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

	public static TagTb get_tag_tb(Context context) {
		if (TagDao != null) {
		} else {
			TagDao = new TagTb(context);
		}
		return TagDao;
	}
	

    /**
     * @return
     */
	public ArrayList<TagInfo> get_tags_with_klass(Integer klass) {
		SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
		Cursor cursor = database.rawQuery("select * from tags where klass = "+klass, null);

		ArrayList<TagInfo> tag_infos = new ArrayList<TagInfo>();
		if (cursor != null && cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) 
			  tag_infos.add(get_tag_info_from_cursor(cursor));
		}
		Log.w("TagKlassCount", "size:" + tag_infos.size());
		return tag_infos;
	}
	
	public long insert_tag(TagInfo tag_info) {
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

}
