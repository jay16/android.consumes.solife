package us.solife.consumes;

import java.util.ArrayList;
import java.util.HashMap;

import us.solife.consumes.adapter.ListViewConsumeAdapter;
import us.solife.consumes.db.ConsumeDao;
import us.solife.consumes.entity.ConsumeInfo;
import us.solife.consumes.util.NetUtils;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import com.yyx.mconsumes.R;
import android.widget.ArrayAdapter;
import us.solife.consumes.parseJson.ConsumeListParse;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * 个人消费记录列表
 * @author jay (http://solife.us/resume)
 * @version 1.0
 * @created 2014-02-25
 */
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

		/**
		 * 消费记录列表展示方式
		 * 年/月/周/天
		 */
		spinner = (Spinner)findViewById(R.id.Spinnered);  
		ArrayAdapter <CharSequence> adapter = ArrayAdapter.createFromResource(this,  
                R.array.plants_array, android.R.layout.simple_spinner_item);  
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);  
        spinner.setSelection(3,true);
        spinner.setPrompt("列表显示方式"); 
        spinner.setOnItemSelectedListener(new SpinnerOnItemSelectListener()); 
		
        /**
         * 初始化本地数据库
         */
		sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
		long current_user_id = sharedPreferences.getLong("current_user_id", -1);
		consumeDao = ConsumeDao.getConsumeDao(TabList.this,current_user_id);
		
		/**
		 * 同步/下载数据按钮
		 */
		ImageButton imageButton_download = (ImageButton) findViewById(R.id.imageButton_download);		
		ImageButton imageButton_refresh  = (ImageButton) findViewById(R.id.imageButton_refresh);
		imageButton_download.setOnClickListener(imageButton_download_listener);
		imageButton_refresh.setOnClickListener(imageButton_refresh_listener);
	}
	
	/**
	 * 每当跳转到该界面时加载该项
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		setViewList("day");
	}
	/**
	 * 下拉列表spinner点击响应
	 */
	class SpinnerOnItemSelectListener implements OnItemSelectedListener{  
		
		/**
		 * 点击响应具体操作
		 */
	    @Override  
	    public void onItemSelected(AdapterView<?> AdapterView, View view, int position, long arg3) {  
	        String selected = AdapterView.getItemAtPosition(position).toString();  
	        Toast.makeText(TabList.this, selected, 0).show();
	        
	        //点击下拉列表spinner不同选项，响应不同信息
	        switch(position) {
	        case 0:
	        	setViewList("year"); break;
	        case 1:
	        	setViewList("month"); break;
	        case 2:
	        	setViewList("week"); break;
	        case 3:
	        	setViewList("day"); break;
	        default:
		        setViewList("day"); break;
	        }
	    }
	    
	    @Override  
	    public void onNothingSelected(AdapterView<?> arg0) {  
	        // TODO Auto-generated method stub   
	        Toast.makeText(TabList.this, "NothingSelected", 0).show();
	    }  
	      
	}  	

	/**
	 * 初始化视图控件
	 * @param: ShowType 显示格式
	 * year/month/week/day
	 */
	public void setViewList(String ShowType) {

		listView = (ListView) findViewById(R.id.consumeListView);
        consumeInfos = consumeDao.getConsumeItemList(ShowType);
        
        //无消费记录时提示错误
		if (consumeInfos == null && consumeInfos.size() == 0) {
			Toast.makeText(TabList.this, "No Data", 0).show();
			return;
		}
		
		//listView.addFooterView(lvQuestion_footer);//添加底部视图  必须在setAdapter前
		listView.setAdapter(new ListViewConsumeAdapter(consumeInfos,TabList.this));
		listView.setClickable(true);
		listView.setOnItemClickListener(new OnItemClickListener(){
			 @Override
	         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//点击头部、底部栏无效
        		if(position == 0) return;
        		
                ConsumeInfo consumeinfo = consumeInfos.get(position);
        		if(consumeinfo == null){
	        		//判断是否是TextView
	        		if(view instanceof TextView){
	        			consumeinfo = (ConsumeInfo)view.getTag();
	        		}else{
	        			TextView tv = (TextView)view.findViewById(R.id.TextView_item_value);
	        			consumeinfo = (ConsumeInfo)tv.getTag();
	        		}
        		}
        		if(consumeinfo == null) return;
        		
                //提示消费内容
				Toast.makeText(TabList.this, "["+consumeinfo.getCreated_at()+"]消费记录", 0).show();
				
				// 界面切换，显示具体记录
				Intent intent = new Intent(TabList.this, ConsumeItem.class);
				intent.putExtra("created_at",  consumeinfo.getCreated_at());
				startActivity(intent);
			 }
		});
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
									sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
									long current_user_id = sharedPreferences.getLong("current_user_id", -1);
									ConsumeDao consumeDao = ConsumeDao.getConsumeDao(TabList.this,current_user_id);
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
				setViewList("day");
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
					long current_user_id = sharedPreferences.getLong("current_user_id", -1);
					

			    	Toast.makeText(TabList.this, "更新数据", 0).show();
					//后台同步更新未同步的数据
					NetUtils.upload_unsync_consumes_background(TabList.this,login_email,current_user_id);
					Toast.makeText(TabList.this, "更新完毕", 0).show();
					setViewList("day");
			    } else {
			    	Toast.makeText(TabList.this, "更新失败", 0).show();
			    }
		};
	
	
	};
	
	//设置标题栏右侧按钮的作用
	public void btnmainright(View v) {  
		Intent intent = new Intent (TabList.this,MenuConsumeItemListType.class);			
		startActivity(intent);	
		//Toast.makeText(getApplicationContext(), "点击了功能按钮", Toast.LENGTH_LONG).show();
      }  
}
