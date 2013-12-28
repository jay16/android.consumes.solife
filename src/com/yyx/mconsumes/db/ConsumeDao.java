package com.yyx.mconsumes.db;

import java.util.ArrayList;

import com.yyx.mconsumes.entity.ConsumeInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ConsumeDao {
	private Context context;
	public ConsumeDatabaseHelper consumeDatabaseHelper;

	public ConsumeDao(Context context) {
		this.context = context;
		this.consumeDatabaseHelper = new ConsumeDatabaseHelper(context);
	}

	//插入当前登陆用户所有消费记录
	public void insertAllRecord(Context context,ArrayList<ConsumeInfo> consumeInfos) {
		SQLiteDatabase database = consumeDatabaseHelper.getWritableDatabase();
		database.beginTransaction();
		database.execSQL("delete from consumes");
		for (int i = 0; i < consumeInfos.size(); i++) {
			ConsumeInfo info = consumeInfos.get(i);
			ContentValues values = new ContentValues();
			values.put("created_at", info.getCreated_at());
			values.put("id", info.getId());
			values.put("msg", info.getMsg());
			values.put("updated_at", info.getUpdated_at());
			values.put("volue", info.getVolue());
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

			String date = cursor.getString(cursor.getColumnIndex("created_at"));
			int id = cursor.getInt(cursor.getColumnIndex("id"));
			double amount = cursor.getDouble(cursor.getColumnIndex("volue"));
			String msg = cursor.getString(cursor.getColumnIndex("msg"));
			String update_date = cursor.getString(cursor
					.getColumnIndex("updated_at"));
			consumeInfo.setCreated_at(date);
			consumeInfo.setId(id);
			consumeInfo.setMsg(msg);
			consumeInfo.setVolue(amount);
			consumeInfo.setUpdated_at(update_date);
			consumeInfos.add(consumeInfo);
		}
		cursor.close();
		database.close();
		return consumeInfos;

	}
}
