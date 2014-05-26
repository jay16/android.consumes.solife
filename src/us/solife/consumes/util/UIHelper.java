package us.solife.consumes.util;

import java.io.File;
import java.util.HashMap;

import org.apache.commons.httpclient.HttpStatus;
import org.json.JSONException;

import us.solife.consumes.ConsumeItem;
import us.solife.consumes.Main;
import us.solife.iconsumes.R;
import us.solife.consumes.ConsumeForm;
import us.solife.consumes.TabList;
import us.solife.consumes.api.ApiClient;
import us.solife.consumes.api.URLs;
import us.solife.consumes.entity.ConsumeInfo;
import us.solife.consumes.entity.CurrentUser;
import us.solife.consumes.entity.UpdateInfo;
import us.solife.consumes.parse.UpdateInfoParse;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
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
	 * 编辑/删除消费记录
	 * @param context
	 */
	public static void showCommentInfoOptionDialog(final Context context,final ConsumeInfo consume_info)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(R.drawable.ic_dialog_menu);
		builder.setTitle(context.getString(R.string.select)+"[￥"+consume_info.get_value()+"]");

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
				//activity.finish();

				Intent intent = new Intent(activity, Main.class);
				activity.startActivity(intent);
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
		builder.setTitle(context.getString(R.string.delete)+"[￥"+consume_info.get_value()+"]");
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
				CurrentUser current_user = CurrentUser.get_current_user(context, consume_info.get_user_id());
				current_user.destroy_record(consume_info.get_id());
				Log.w("UIHelper","Delete YES");
				Intent intent = new Intent(context, ConsumeItem.class);
				String y_m_d = (String) consume_info.get_created_at().subSequence(0,10);
				Log.w("UIHelper","After Delete => "+y_m_d);
				intent.putExtra("created_at",  y_m_d);
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
	/**
	 * 消费圈明细
	 * @param context
	 */
	public static void FriendsConsumeItemDialog(final Context context, final ConsumeInfo consume_info)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("[￥"+consume_info.get_value()+"]");
		builder.setMessage(consume_info.get_remark());
		builder.create().show();
	}
	
	public static void push_notice(Context context, String title, String content, Integer uid) {
    	
		NotificationManager mNotificationManager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);    
		//定义内容  
		int notificationIcon=R.drawable.favicon;  
		CharSequence notificationTitle=title;  
		long when = System.currentTimeMillis();  
		  
		Notification notification=new Notification(notificationIcon, notificationTitle, when);  
		  
		notification.defaults=Notification.DEFAULT_ALL;  
		  
		Intent intent= new Intent(context,Main.class);  
		PendingIntent pendingIntent=PendingIntent.getActivity(context, 0, intent, 0);  
		notification.setLatestEventInfo(context,title, content,pendingIntent);  
		  
		if(notification!=null)  
		{  
		    Log.e("notifacation", "notifacation is ok");  
		    mNotificationManager.notify(1000+uid, notification);  
		} 
	}

}
