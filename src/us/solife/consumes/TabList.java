package us.solife.consumes;

import java.util.ArrayList;
import android.widget.SimpleAdapter;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import us.solife.consumes.adapter.ConsumeListAdapter;
//import us.solife.consumes.adapter.Display;
import us.solife.consumes.db.ConsumeDao;
import us.solife.consumes.entity.ConsumeInfo;
import us.solife.consumes.util.NetUtils;
import android.database.Cursor;
import android.content.Context;
import android.content.Intent;
//import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.os.Handler;
import android.os.Message;
import android.os.Looper;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import com.yyx.mconsumes.R;
import android.widget.ArrayAdapter;
import us.solife.consumes.BaseActivity.DataCallback;
import us.solife.consumes.db.ConsumeDao;
import us.solife.consumes.entity.ConsumeInfo;
import us.solife.consumes.parseJson.ConsumeListParse;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
//import android.view.Display;

public class TabList extends BaseActivity{
	ListView listView;
    Spinner spinner=null;  
	int      precursor;
	int      index;
	String url = "http://solife.us/api/consumes/list";
	SharedPreferences      preferences;
	ArrayList<ConsumeInfo> consumeInfos;
	ConsumeDao             consumeDao;
	SharedPreferences sharedPreferences;

	@Override
	public void init() { // TODO Auto-generated method stub
		setContentView(R.layout.tab_list);

		//下拉列表
		spinner = (Spinner)findViewById(R.id.Spinnered);  
		ArrayAdapter <CharSequence> adapter = ArrayAdapter.createFromResource(this,  
                R.array.plants_array, android.R.layout.simple_spinner_item);  
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);  
        spinner.setPrompt("列表显示方式"); 
        spinner.setOnItemSelectedListener(new SpinnerOnItemSelectListener());  
		//TextView  textView_main_header = (TextView)findViewById(R.id.textView_list_header);
		//textView_main_header.setText("消费列表");
		
		consumeDao = ConsumeDao.getConsumeDao(TabList.this);
		
		ImageButton imageButton_download = (ImageButton) findViewById(R.id.imageButton_download);		
		ImageButton imageButton_refresh  = (ImageButton) findViewById(R.id.imageButton_refresh);
		imageButton_download.setOnClickListener(imageButton_download_listener);
		imageButton_refresh.setOnClickListener(imageButton_refresh_listener);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		//Display display = this.getWindowManager().getDefaultDisplay();
		//int width = display.getWidth();
		setViewList("month","");
	}
	
	class SpinnerOnItemSelectListener implements OnItemSelectedListener{  
		  
	    @Override  
	    public void onItemSelected(AdapterView<?> AdapterView, View view, int position, long arg3) {  
	        // TODO Auto-generated method stub  
	        String selected = AdapterView.getItemAtPosition(position).toString();  
	        Toast.makeText(TabList.this, selected, 0).show();
	        
	        //点击下拉列表spinner不同选项，响应不同信息
	        if(position == 0) {
	        	setViewList("month","");
	        } else if(position == 1) {
	        	setViewList("year","");
	        } else if(position == 2) {
	        	setViewList("all","");
	        } else {
	        	setViewList("month","");
	        }
	    }
	    @Override  
	    public void onNothingSelected(AdapterView<?> arg0) {  
	        // TODO Auto-generated method stub   
	        Toast.makeText(TabList.this, "NothingSelected", 0).show();
	    }  
	      
	}  	

	
	public void setViewList(String ShowType,String day) {
		listView = (ListView) findViewById(R.id.consumeListView);
		//获取数据库的时候应该单独开一条线程  以为是耗时的操作  这个demo数据库简单  我就放在主线程了

        consumeInfos = consumeDao.getRecordsAsDay(TabList.this,ShowType,day);
        
		if (consumeInfos != null && consumeInfos.size() != 0) {
			listView.setAdapter(new ConsumeListAdapter(consumeInfos,TabList.this));
			listView.setClickable(true);
			listView.setOnItemClickListener(new OnItemClickListener(){
				 @Override
		         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					    String created_at = ((TextView) view.findViewById(R.id.TextView_item_date)).getText().toString();


						Toast.makeText(TabList.this, created_at, 0).show();
						// 界面切换
						// 显示记录记录
						Intent intent = new Intent(TabList.this, ConsumeItem.class);
						intent.putExtra("created_at", created_at);
						startActivity(intent);
				 }
			});
		} else {
			Toast.makeText(TabList.this, "No Data", 0).show();
		}
		listView.invalidate();
	}

	// 句柄-数据插入
		Handler handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case 1000:
					Toast.makeText(TabList.this, "数据库同步成功", 0).show();
					break;
				case 2000:
					Toast.makeText(TabList.this, "数据库同步失败！", 0).show();
					Toast.makeText(TabList.this, msg.obj.toString(), 0).show();

					break;
				case 3000:
					Toast.makeText(TabList.this, "错误:未连接网络！", 0).show();
					break;
				default:
					break;
				}
			};
		};
		DataCallback<HashMap<String, Object>> callback = new DataCallback<HashMap<String, Object>>() {

			@Override
			public void processData(HashMap<String, Object> paramObject, boolean paramBoolean) {
				// TODO Auto-generated method stub
				boolean result = (Boolean) paramObject.get("result");
				if (result) {
					final ArrayList<ConsumeInfo> arrayList = (ArrayList<ConsumeInfo>) paramObject.get("consumeInfos");
					
					if (arrayList != null && arrayList.size() != 0) {
						Toast.makeText(TabList.this, "同步数据"+arrayList.size(), 0).show();
						
						/*String tmp = "";
						for(int i = 0;i < arrayList.size(); i ++){
							tmp = tmp + arrayList.get(i).to_string();
						}
						TextView textView1 = (TextView) findViewById(R.id.textView1);		
						textView1.setText(tmp);*/
						
						//new Thread() {
						//	public void run() {

								//Looper.prepare();  
								Message message = new Message();
								try {
									Toast.makeText(TabList.this, "开始同步数据2", 0).show();
									ConsumeDao consumeDao = ConsumeDao.getConsumeDao(TabList.this);
									consumeDao.insertAllRecord(TabList.this, arrayList);
									message.what = 1000;
									message.obj  = arrayList;
									handler.sendMessage(message);
								} catch (Exception e) {
									message.what = 2000;
									message.obj = e;
									handler.sendMessage(message);
									e.printStackTrace();
								}
								//Looper.loop(); 
						//	};
						//}.start();

					} else {
						//ConsumeDao consumeDao = ConsumeDao.getConsumeDao(TabList.this);
						Toast.makeText(TabList.this, "同步数据为空1！", 0).show();
					}

				} else {
					Toast.makeText(getApplicationContext(), "获取数据为空2", 0).show();
				}

			}
		};
		
		//同步数据库至本地
		Button.OnClickListener imageButton_download_listener = new Button.OnClickListener(){//创建监听对象  
			public void onClick(View v){  
				Toast.makeText(TabList.this, "开始同步数据1", 0).show();
				
				ConsumeListParse consumeListParse = new ConsumeListParse();
				getDataFromServer(getApplicationContext(), consumeListParse, url, callback);
				setViewList("month","");
			}
		};
		//同步本地数据至服务器
		Button.OnClickListener imageButton_refresh_listener = new Button.OnClickListener(){//创建监听对象  
			public void onClick(View v){  
				consumeInfos = consumeDao.getUnSyncRecords();
				sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
				
		    	//Toast.makeText(TabList.this, "更新未同步数据", 0).show();
		    	
				if (sharedPreferences.contains("current_user_email")
						&& !sharedPreferences.getString("current_user_email", "").equals("")) {
					String login_email = sharedPreferences.getString("current_user_email", "");
					
					//后台同步更新未同步的数据
					NetUtils.upload_unsync_consumes_background(TabList.this,login_email);
					setViewList("month","");
			    } else {
			    	Toast.makeText(TabList.this, "获取用户信息失败", 0).show();
			    }
		};
	
	
	};
}
