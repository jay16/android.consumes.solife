package us.solife.consumes.util;

import java.io.File;

import us.solife.consumes.ConsumeItem;
import us.solife.consumes.R;
import us.solife.consumes.ConsumeForm;
import us.solife.consumes.TabList;
import us.solife.consumes.db.CurrentUser;
import us.solife.consumes.entity.ConsumeInfo;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class UIHelper {

	public final static int LISTVIEW_ACTION_INIT = 0x01;
	public final static int LISTVIEW_ACTION_REFRESH = 0x02;
	public final static int LISTVIEW_ACTION_SCROLL = 0x03;
	public final static int LISTVIEW_ACTION_CHANGE_CATALOG = 0x04;
	
	public final static int LISTVIEW_DATA_MORE = 0x01;
	public final static int LISTVIEW_DATA_LOADING = 0x02;
	public final static int LISTVIEW_DATA_FULL = 0x03;
	public final static int LISTVIEW_DATA_EMPTY = 0x04;
	
	public final static int LISTVIEW_DATATYPE_NEWS = 0x01;
	public final static int LISTVIEW_DATATYPE_BLOG = 0x02;
	public final static int LISTVIEW_DATATYPE_POST = 0x03;
	public final static int LISTVIEW_DATATYPE_TWEET = 0x04;
	public final static int LISTVIEW_DATATYPE_ACTIVE = 0x05;
	public final static int LISTVIEW_DATATYPE_MESSAGE = 0x06;
	public final static int LISTVIEW_DATATYPE_COMMENT = 0x07;
	
	public final static int REQUEST_CODE_FOR_RESULT = 0x01;
	public final static int REQUEST_CODE_FOR_REPLY = 0x02;
	
	/**
	 * 评论操作选择框
	 * @param context
	 */
	public static void showCommentInfoOptionDialog(final Context context,final ConsumeInfo consume_info)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(R.drawable.ic_dialog_menu);
		builder.setTitle(context.getString(R.string.select)+"[￥"+consume_info.get_volue()+"]");

		builder.setItems(R.array.consume_item_edit_array,new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface arg0, int arg1) {
				Intent intent;
				switch (arg1) {
					case 0://编辑
						intent = new Intent(context, ConsumeForm.class);
						intent.putExtra("action",  "update");
						intent.putExtra("row_id",  consume_info.get_id());
						context.startActivity(intent);
						break;
					case 1://删除
						UIHelper.deleteConsumeOptionDialog(context,consume_info);
						break;
				}				
			}
		});
		builder.create().show();
	}
	

	/**
	 * 点击返回监听事件
	 * @param activity
	 * @return
	 */
	public static View.OnClickListener finish(final Activity activity)
	{
		return new View.OnClickListener() {
			public void onClick(View v) {
				activity.finish();
			}
		};
	}	
	
	/**
	 * 删除消费记录对话框
	 * @param context
	 */
	public static void deleteConsumeOptionDialog(final Context context, final ConsumeInfo consume_info)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(R.drawable.ic_dialog_menu);
		builder.setTitle(context.getString(R.string.delete)+"[￥"+consume_info.get_volue()+"]");
		builder.setItems(R.array.delete_options,new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface arg0, int arg1) {
				/*
				switch (arg1) {
					case 0://删除
						Log.w("UIHelper","deleteConsumeOptionDialog YES");
						break;
					case 1://取消
						Log.w("UIHelper","deleteConsumeOptionDialog NO");
						break;
				}
				*/				
			}
		});

		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				CurrentUser current_user = CurrentUser.getCurrentUser(context, consume_info.get_user_id());
				current_user.destroy_record(consume_info.get_id());
				Log.w("UIHelper","Delete YES");
				Intent intent = new Intent(context, ConsumeItem.class);
				intent.putExtra("created_at",  consume_info.get_created_at().subSequence(1,10));
				context.startActivity(intent);
				dialog.dismiss();
			}
		});
		builder.setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				Log.w("UIHelper","Delete NO");
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
}
