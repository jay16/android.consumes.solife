package us.solife.consumes;

import java.util.ArrayList;
import java.util.HashMap;

import us.solife.consumes.BaseActivity.DataCallback;
import us.solife.consumes.db.ConsumeDao;
import us.solife.consumes.entity.ConsumeInfo;
import us.solife.consumes.parseJson.ConsumeListParse;

//����consume�Զ�������
import com.yyx.mconsumes.R;

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
import android.widget.AdapterView;
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
	// ���-���ݲ���
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// intent���AndroidӦ�õĸ������֮���ͨѶ
			// ר���ṩ���������õ������Ϣ��ʵ�ֵ������뱻������֮��Ľ���
			Intent intent = new Intent(splashActivity.this, MainTabActivity.class);
			switch (msg.what) {
			case 1000:
				Toast.makeText(splashActivity.this, "���ݿ����ɹ�", 0).show();
				Editor Editor = sharedPreferences.edit();
				Editor.putBoolean("isDownLoad", true);
				Editor.commit();
				intent.putExtra("flag", 1000);
				startActivity(intent);
				break;
			case 2000:
				Toast.makeText(splashActivity.this, "���ݿ��ʼ��ʧ�ܣ�", 0).show();
				intent.putExtra("flag", 2000);
				startActivity(intent);
				break;

			case 3000:
				Toast.makeText(splashActivity.this, "����:δ�������磡", 0).show();
				break;
			default:
				break;
			}
			finish();
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// �������䲻�ɵ������������ִ���
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
				promot.setText("���ݿⱾ�ػ���......");
				if (arrayList != null && arrayList.size() != 0) {
					new Thread() {
						public void run() {
							Message message = new Message();
							try {
								ConsumeDao consumeDao = ConsumeDao.getConsumeDao(splashActivity.this,(long)-1);
								consumeDao.insertAllRecord(splashActivity.this, arrayList);
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
					ConsumeDao consumeDao = ConsumeDao.getConsumeDao(splashActivity.this,(long)-1);
				}

			} else {
				Toast.makeText(splashActivity.this, "��ȡ����ʧ��", 0).show();
			}

		}
	};

	// ��дBaseActivity�е�Init()
	@Override
	public void init() {
		// TODO Auto-generated method stub
		// ��ȡ�ؼ�
		promot = (TextView) findViewById(R.id.init);
		backGround = (ImageView) findViewById(R.id.backGround);
		AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f, 1.0f);
		// alphaAnimation.setDuration(1000);
		backGround.setAnimation(alphaAnimation);
		sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);

		// �ж��Ƿ��Ѿ���½
		// δ��½��ֱ����ʾ��½����
		Intent intent;
		if (sharedPreferences.contains("is_login")
				&& sharedPreferences.getBoolean("is_login", false)) {
			intent = new Intent(splashActivity.this, MainTabActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
		} else {
			intent = new Intent(splashActivity.this, Login.class);
		}
		
		startActivity(intent);
		finish();
		// boolean isDownLoad = sharedPreferences.getBoolean("isDownLoad",
		// false);
		// if (isDownLoad) {
		// promot.setText("������......");
		// new Thread() {
		// public void run() {
		// try {
		// sleep(5000);
		// startActivity(new Intent(splashActivity.this,MainTabActivity.class));
		// finish();
		// } //catch (InterruptedException e) {
		// TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// };
		// }.start();
		// } else {
		// ConsumeListParse consumeListParse = new ConsumeListParse();
		// getdataFromServer(getApplicationContext(), consumeListParse, url, callback);
		// }

	}
}
