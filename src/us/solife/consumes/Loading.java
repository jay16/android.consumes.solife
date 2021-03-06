package us.solife.consumes;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import us.solife.iconsumes.R;
import us.solife.consumes.BaseActivity.DataCallback;
import us.solife.consumes.api.URLs;
import us.solife.consumes.db.ConsumeTb;
import us.solife.consumes.entity.ConsumeInfo;
import us.solife.consumes.parse.ConsumeListParse;
import us.solife.consumes.recevier.TimerService;

//引用consume自定义的类包

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class Loading extends BaseActivity {
	SharedPreferences sharedPreferences;
	ImageView backGround;
	TextView promot;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// 以下两句不可调换，否则会出现错误
		setContentView(R.layout.loading);
		super.onCreate(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	// 重写BaseActivity中的Init()
	@Override
	public void init() {
		// TODO Auto-generated method stub
		sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);

		// 判断是否已经登陆
		// 未登陆则直接显示登陆界面
		Intent intent;
		if (sharedPreferences.contains("is_login")
				&& sharedPreferences.getBoolean("is_login", false)) {
			
			Log.w("TOKEN",sharedPreferences.getString("current_user_token", "NOSET"));
			
			intent = new Intent(Loading.this, Main.class);
			//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
			//如果这个activity已经启动了，就不产生新的activity，
			//而只是把这个activity实例加到栈顶来就可以了
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			
			//循环定时执行
			Intent intent1 =new Intent(getApplication(), TimerService.class);
		    intent1.setAction(sharedPreferences.getString("current_user_token", ""));
		    PendingIntent sender=PendingIntent.getBroadcast(getApplication(), 0, intent1, 0);
		    //开始时间
		    long now =SystemClock.elapsedRealtime();
		    //long now = System.currentTimeMillis();  

		    AlarmManager am=(AlarmManager)getSystemService(ALARM_SERVICE);//5秒一个周期，不停的发送广播
		    am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, now, 600000, sender);
		} else {
			intent = new Intent(Loading.this, Login.class);
		}
		
		//创建基本目录文件夹
		File storage_base = new File(URLs.STORAGE_BASE);
		if(!storage_base.exists()) storage_base.mkdir(); 
		File storage_apk = new File(URLs.STORAGE_APK);
		if(!storage_apk.exists()) storage_apk.mkdir(); 
		File storage_gravatar = new File(URLs.STORAGE_GRAVATAR);
		if(!storage_gravatar.exists()) storage_gravatar.mkdir(); 
	    
		startActivity(intent);
		finish();

	}
}
