package com.yyx.mconsumes;

import java.util.ArrayList;
import java.util.HashMap;

//引用自定义的类包
import com.yyx.mconsumes.BaseActivity.DataCallback;
import com.yyx.mconsumes.db.ConsumeDao;
import com.yyx.mconsumes.entity.ConsumeInfo;
import com.yyx.mconsumes.parseJson.ConsumeListParse;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class splashActivity extends BaseActivity {
	SharedPreferences sharedPreferences;
	ImageView backGround;
	String url = "http://solife.us/api/consumes/list";
	TextView promot;
	//句柄-数据插入
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			//解决Android应用的各项组件之间的通讯
			//专门提供组件互相调用的相关信息，实现调用者与被调用者之间的解耦
			Intent intent = new Intent(splashActivity.this, MainTabActivity.class);
			switch (msg.what) {
			case 1000:
				Toast.makeText(splashActivity.this, "数据库插入成功", 0).show();
				Editor Editor = sharedPreferences.edit();
				Editor.putBoolean("isDownLoad", true);
				Editor.commit();
				intent.putExtra("flag", 1000);
				startActivity(intent);
				break;
			case 2000:
				Toast.makeText(splashActivity.this, "数据库初始化失败！", 0).show();
				intent.putExtra("flag", 2000);
				startActivity(intent);

				break;

			default:
				break;

			}
			finish();

		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.splash_activity);
		super.onCreate(savedInstanceState);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	DataCallback<HashMap<String, Object>> callback = new DataCallback<HashMap<String, Object>>() {

		@Override
		public void processData(HashMap<String, Object> paramObject, boolean paramBoolean) {
			// TODO Auto-generated method stub
			boolean result = (Boolean) paramObject.get("result");
			if (result) {
				final ArrayList<ConsumeInfo> arrayList = (ArrayList<ConsumeInfo>) paramObject.get("consumeInfos");
				promot.setText("数据库本地化中......");
				if (arrayList != null && arrayList.size() != 0) {
					new Thread() {
						public void run() {
							Message message = new Message();
							try {
								ConsumeDao consumeDao = new ConsumeDao(splashActivity.this);
								consumeDao.insertAllRecord(splashActivity.this,arrayList);
								message.what = 1000;
								message.obj = arrayList;
								handler.sendMessage(message);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								message.what = 2000;
								handler.sendMessage(message);
								e.printStackTrace();
							}

						};
					}.start();

				} else {
					ConsumeDao consumeDao = new ConsumeDao(splashActivity.this);
				}

			} else {
				Toast.makeText(splashActivity.this, "获取数据失败", 0).show();
			}

		}
	};

	//重写BaseActivity中的Init()
	@Override
	public void init() {
		// TODO Auto-generated method stub
		//获取控件
		promot     = (TextView) findViewById(R.id.init);
		backGround = (ImageView) findViewById(R.id.backGround);
		AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f, 1.0f);
		//alphaAnimation.setDuration(1000);
		backGround.setAnimation(alphaAnimation);
		sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);

		boolean isDownLoad = sharedPreferences.getBoolean("isDownLoad", false);
		if (isDownLoad) {
			promot.setText("加载中......");
			//new Thread() {
				//public void run() {
					//try {
						//sleep(5000);
						//startActivity(new Intent(splashActivity.this,MainTabActivity.class));
						//finish();
					//} //catch (InterruptedException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					//}
				//};
			//}.start();
		} else {
			//ConsumeListParse consumeListParse = new ConsumeListParse();
			//getdataFromServer(getApplicationContext(), consumeListParse, url, callback);
		}

	}
}
