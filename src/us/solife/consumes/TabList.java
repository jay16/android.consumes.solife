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
 * �������Ѽ�¼�б�
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
		 * ���Ѽ�¼�б�չʾ��ʽ
		 * ��/��/��/��
		 */
		spinner = (Spinner)findViewById(R.id.Spinnered);  
		ArrayAdapter <CharSequence> adapter = ArrayAdapter.createFromResource(this,  
                R.array.plants_array, android.R.layout.simple_spinner_item);  
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);  
        spinner.setSelection(3,true);
        spinner.setPrompt("�б���ʾ��ʽ"); 
        spinner.setOnItemSelectedListener(new SpinnerOnItemSelectListener()); 
		
        /**
         * ��ʼ���������ݿ�
         */
		sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
		long current_user_id = sharedPreferences.getLong("current_user_id", -1);
		consumeDao = ConsumeDao.getConsumeDao(TabList.this,current_user_id);
		
		/**
		 * ͬ��/�������ݰ�ť
		 */
		ImageButton imageButton_download = (ImageButton) findViewById(R.id.imageButton_download);		
		ImageButton imageButton_refresh  = (ImageButton) findViewById(R.id.imageButton_refresh);
		imageButton_download.setOnClickListener(imageButton_download_listener);
		imageButton_refresh.setOnClickListener(imageButton_refresh_listener);
	}
	
	/**
	 * ÿ����ת���ý���ʱ���ظ���
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		setViewList("day");
	}
	/**
	 * �����б�spinner�����Ӧ
	 */
	class SpinnerOnItemSelectListener implements OnItemSelectedListener{  
		
		/**
		 * �����Ӧ�������
		 */
	    @Override  
	    public void onItemSelected(AdapterView<?> AdapterView, View view, int position, long arg3) {  
	        String selected = AdapterView.getItemAtPosition(position).toString();  
	        Toast.makeText(TabList.this, selected, 0).show();
	        
	        //��������б�spinner��ͬѡ���Ӧ��ͬ��Ϣ
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
	 * ��ʼ����ͼ�ؼ�
	 * @param: ShowType ��ʾ��ʽ
	 * year/month/week/day
	 */
	public void setViewList(String ShowType) {

		listView = (ListView) findViewById(R.id.consumeListView);
        consumeInfos = consumeDao.getConsumeItemList(ShowType);
        
        //�����Ѽ�¼ʱ��ʾ����
		if (consumeInfos == null && consumeInfos.size() == 0) {
			Toast.makeText(TabList.this, "No Data", 0).show();
			return;
		}
		
		//listView.addFooterView(lvQuestion_footer);//��ӵײ���ͼ  ������setAdapterǰ
		listView.setAdapter(new ListViewConsumeAdapter(consumeInfos,TabList.this));
		listView.setClickable(true);
		listView.setOnItemClickListener(new OnItemClickListener(){
			 @Override
	         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//���ͷ�����ײ�����Ч
        		if(position == 0) return;
        		
                ConsumeInfo consumeinfo = consumeInfos.get(position);
        		if(consumeinfo == null){
	        		//�ж��Ƿ���TextView
	        		if(view instanceof TextView){
	        			consumeinfo = (ConsumeInfo)view.getTag();
	        		}else{
	        			TextView tv = (TextView)view.findViewById(R.id.TextView_item_value);
	        			consumeinfo = (ConsumeInfo)tv.getTag();
	        		}
        		}
        		if(consumeinfo == null) return;
        		
                //��ʾ��������
				Toast.makeText(TabList.this, "["+consumeinfo.getCreated_at()+"]���Ѽ�¼", 0).show();
				
				// �����л�����ʾ�����¼
				Intent intent = new Intent(TabList.this, ConsumeItem.class);
				intent.putExtra("created_at",  consumeinfo.getCreated_at());
				startActivity(intent);
			 }
		});
		listView.invalidate();
	}

	// ���-���ݲ���
		Handler handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case 1000:
					Toast.makeText(TabList.this, "���ݿ�ͬ���ɹ�", 0).show();
					break;
				case 2000:
					Toast.makeText(TabList.this, "���ݿ�ͬ��ʧ�ܣ�", 0).show();
					Toast.makeText(TabList.this, msg.obj.toString(), 0).show();

					break;
				case 3000:
					Toast.makeText(TabList.this, "����:δ�������磡", 0).show();
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
						Toast.makeText(TabList.this, "ͬ������"+arrayList.size(), 0).show();
						
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
									Toast.makeText(TabList.this, "��ʼͬ������2", 0).show();
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
						Toast.makeText(TabList.this, "ͬ������Ϊ��1��", 0).show();
					}

				} else {
					Toast.makeText(getApplicationContext(), "��ȡ����Ϊ��2", 0).show();
				}

			}
		};
		
		//ͬ�����ݿ�������
		Button.OnClickListener imageButton_download_listener = new Button.OnClickListener(){//������������  
			public void onClick(View v){  
				Toast.makeText(TabList.this, "��ʼͬ������1", 0).show();
				
				ConsumeListParse consumeListParse = new ConsumeListParse();
				getDataFromServer(getApplicationContext(), consumeListParse, url, callback);
				setViewList("day");
			}
		};
		//ͬ������������������
		Button.OnClickListener imageButton_refresh_listener = new Button.OnClickListener(){//������������  
			public void onClick(View v){  
				consumeInfos = consumeDao.getUnSyncRecords();
				sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
				
		    	//Toast.makeText(TabList.this, "����δͬ������", 0).show();
		    	
				if (sharedPreferences.contains("current_user_email")
						&& !sharedPreferences.getString("current_user_email", "").equals("")) {
					String login_email = sharedPreferences.getString("current_user_email", "");
					long current_user_id = sharedPreferences.getLong("current_user_id", -1);
					

			    	Toast.makeText(TabList.this, "��������", 0).show();
					//��̨ͬ������δͬ��������
					NetUtils.upload_unsync_consumes_background(TabList.this,login_email,current_user_id);
					Toast.makeText(TabList.this, "�������", 0).show();
					setViewList("day");
			    } else {
			    	Toast.makeText(TabList.this, "����ʧ��", 0).show();
			    }
		};
	
	
	};
	
	//���ñ������Ҳఴť������
	public void btnmainright(View v) {  
		Intent intent = new Intent (TabList.this,MenuConsumeItemListType.class);			
		startActivity(intent);	
		//Toast.makeText(getApplicationContext(), "����˹��ܰ�ť", Toast.LENGTH_LONG).show();
      }  
}
