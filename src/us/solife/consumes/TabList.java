package us.solife.consumes;

import java.util.ArrayList;
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
import us.solife.consumes.db.ConsumeDao;
import us.solife.consumes.entity.ConsumeInfo;
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

public class TabList extends BaseActivity{
	ListView listView;
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

		TextView  textView_main_header = (TextView)findViewById(R.id.textView_list_header);
		textView_main_header.setText("消费列表");
		
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
		setViewList();
	}
	
	public void setViewList() {
		listView = (ListView) findViewById(R.id.consumeListView);
		//获取数据库的时候应该单独开一条线程  以为是耗时的操作  这个demo数据库简单  我就放在主线程了

        consumeInfos = consumeDao.getAllRecords(TabList.this);
		if (consumeInfos != null && consumeInfos.size() != 0) {
			listView.setAdapter(new ConsumeListAdapter(consumeInfos,TabList.this));
			listView.setClickable(true);
			listView.setOnItemClickListener(new OnItemClickListener(){
				 @Override
		         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					    String item_id = ((TextView) view.findViewById(R.id.item_id)).getText().toString();
						//ConsumeDao consumeDao = ConsumeDao.getConsumeDao(getApplicationContext());
						//Cursor cursor = consumeDao.getRecordByRowId(id);
						//String msg    = cursor.getString(cursor.getColumnIndex("msg"));
						//Toast.makeText(TabList.this, msg, 0).show();

						// 界面切换
						// 显示记录记录
						Intent intent = new Intent(TabList.this, ConsumeItem.class);
						intent.putExtra("row_id", Long.parseLong(item_id));
						intent.putExtra("from_page", "TabList");
						startActivity(intent);
				 }
			});
			//Toast.makeText(TabList.this, consumeInfos.size() + "", 0).show();
		} else {
			Toast.makeText(TabList.this, "No Data", 0).show();
		}
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
					
					TextView textView1 = (TextView) findViewById(R.id.item_id);		
					textView1.setText(msg.obj.toString());
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
				setViewList();
			}
		};
		//同步本地数据至服务器
		Button.OnClickListener imageButton_refresh_listener = new Button.OnClickListener(){//创建监听对象  
			public void onClick(View v){  
				consumeInfos = consumeDao.getUnSyncRecords(TabList.this);
				
				sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
				
		    	Toast.makeText(TabList.this, "更新未同步数据", 0).show();
		    	
				if (sharedPreferences.contains("current_user_email")
						&& !sharedPreferences.getString("current_user_email", "").equals("")) {
					String login_email = sharedPreferences.getString("current_user_email", "");
					
					Toast.makeText(TabList.this, "共有"+consumeInfos.size()+"条数据未同步", 0).show();
					
					for(int i = 0; i < consumeInfos.size(); i++) {
						ConsumeInfo info = consumeInfos.get(i);
						String[] ret_array = consume_create(login_email, Double.toString(info.getVolue()), info.getCreated_at(), info.getMsg());
						if (ret_array[0].equals("1")) {
							consumeDao.updateUnSyncRecordByRowId(info.getId(),info.getUser_id(),info.getConsume_id());
							Toast.makeText(TabList.this, "更新第"+i+"笔数据", 0).show();
						} else {
							Toast.makeText(TabList.this, "update-" + info.getId()+"-fail!", 0).show();
						}	
					}
					Toast.makeText(TabList.this, "更新未同步数据完毕", 0).show();
					setViewList();
			    } else {
			    	Toast.makeText(TabList.this, "获取用户信息失败", 0).show();
			    }
		};
		
		public  String[] consume_create(String login_email, String value,
				String created_at, String msg) {
			// Step One 从服务器接口中获取当前账号和密码的配对情况
			String[] ret_array = { "0", "no return" };
			Integer ret = 0;
			String ret_info = "no return";

			String http_url = "http://solife.us/api/consumes/create?";
			// 创建httpRequest对象
			HttpPost httpRequest = new HttpPost(http_url);
			// HttpGet httpRequest =new HttpGet(httpUrl);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("email", login_email));
			params.add(new BasicNameValuePair("consume[volue]", value));
			params.add(new BasicNameValuePair("consume[created_at]", created_at));
			params.add(new BasicNameValuePair("consume[msg]", msg));

			try {
				// 设置字符集
				HttpEntity httpentity = new UrlEncodedFormEntity(params, "gb2312");
				// 请求httpRequest
				httpRequest.setEntity(httpentity);
				// 取得HttpClinet对象
				HttpClient httpclient = new DefaultHttpClient();
				// 请求HttpClient,取得HttpResponse
				HttpResponse httpResponse = httpclient.execute(httpRequest);
				// 请求成功
				if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					// 取得返回的字符串
					String strResult = EntityUtils.toString(httpResponse
							.getEntity());
					JSONObject jsonObject = new JSONObject(strResult);
					// 获取返回值
					ret = jsonObject.getInt("ret");
					ret_info = jsonObject.getString("ret_info");
					ret_array[0] = ret.toString();
					ret_array[1] = ret_info;
				}
			} catch (Exception e) {
				ret_array[1] = e.getMessage().toString();
				return ret_array;
			}
			return ret_array;
		}
	};
}
