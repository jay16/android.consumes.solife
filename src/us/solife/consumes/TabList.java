package us.solife.consumes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.httpclient.HttpException;
import org.json.JSONException;

import us.solife.iconsumes.R;
import us.solife.consumes.adapter.ListViewConsumeAdapter;
import us.solife.consumes.api.URLs;
import us.solife.consumes.db.ConsumeTb;
import us.solife.consumes.entity.ConsumeInfo;
import us.solife.consumes.entity.CurrentUser;
import us.solife.consumes.util.LoadingDialog;
import us.solife.consumes.util.NetUtils;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import us.solife.consumes.parse.ConsumeListParse;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout.LayoutParams;

/**
 * �������Ѽ�¼�б�
 * @author jay (http://solife.us/resume)
 * @version 1.0
 * @created 2014-02-25
 */
public class TabList extends BaseActivity{
	private Context mContext;
	ListView listView;
    Spinner  spinner=null;  
	int      precursor;
	int      index;
	//String url = "http://solife.us/api/consumes/list";
	SharedPreferences      preferences;
	ArrayList<ConsumeInfo> consume_infos;
	ConsumeTb             consumeDao;
	CurrentUser           current_user;
	SharedPreferences sharedPreferences;

	private LinearLayout mTopLayout;
	private LinearLayout tablist_1;
	private LinearLayout tablist_2;
	private LinearLayout tablist_3;
	private LinearLayout tablist_4;
	private TextView mTopText;
	private PopupWindow mPopupWindow;
	private ProgressBar loading_progress_bar;
	private LoadingDialog loading_dialog;
	private String current_user_token;
	private Long current_user_id;

	@Override
	public void init() { // TODO Auto-generated method stub
		setContentView(R.layout.tab_list);
		mContext = context;
		mTopText = (TextView)findViewById(R.id.home_top_text);
		mTopLayout = (LinearLayout)findViewById(R.id.menu_consume_item_list);
		loading_progress_bar = (ProgressBar) findViewById(R.id.tab_list_loading_progress);
		

		sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
		current_user_token = sharedPreferences.getString("current_user_token", "");
		current_user_id = sharedPreferences.getLong("current_user_id", -1);
		
		/**
		 * ���Ѽ�¼�б�չʾ��ʽ
		 * ��/��/��/��
		spinner = (Spinner)findViewById(R.id.Spinnered);  
		ArrayAdapter <CharSequence> adapter = ArrayAdapter.createFromResource(this,  
                R.array.consume_item_list_view_array, android.R.layout.simple_spinner_item);  
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);  
        spinner.setSelection(3,true);
        spinner.setPrompt("�б���ʾ��ʽ"); 
        spinner.setOnItemSelectedListener(new SpinnerOnItemSelectListener()); 
		 */

        /**
         * ��ʼ���������ݿ�
         */
		sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
		long current_user_id = sharedPreferences.getLong("current_user_id", -1);
		consumeDao = ConsumeTb.get_record_tb(getApplication());
		current_user = CurrentUser.get_current_user(getApplication(),Integer.parseInt(String.valueOf(current_user_id)));
		/**
		 * ͬ��/�������ݰ�ť
		 */
		ImageButton imageButton_download = (ImageButton) findViewById(R.id.imageButton_download);		
		ImageButton imageButton_refresh  = (ImageButton) findViewById(R.id.imageButton_refresh);
		imageButton_download.setOnClickListener(imageButton_download_listener);
		imageButton_refresh.setOnClickListener(imageButton_refresh_listener);
		

		if(loading_dialog != null)	loading_dialog.dismiss();
	}
	
	/**
	 * ÿ����ת���ý���ʱ���ظ���
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		init_view_list("day");
		setListener();
    	if(loading_dialog != null) loading_dialog.dismiss();
	}


	private void setListener() {
		mTopLayout.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// ��ʾ�˵�
				//Intent intent = new Intent (getApplication(),TabListMenu.class);			
				//startActivity(intent);	
				Context context = TabList.this;
				LayoutInflater mLayoutInflater = (LayoutInflater) context  
	                    .getSystemService(LAYOUT_INFLATER_SERVICE);  
	            View mPopView = mLayoutInflater.inflate(  
	                    R.layout.menu_tab_list_header, null); 

	    		if (mPopupWindow == null) {
					mPopupWindow = new PopupWindow(mPopView, LayoutParams.FILL_PARENT,
							LayoutParams.WRAP_CONTENT);
					//mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
					tablist_1 = (LinearLayout)mPopView.findViewById(R.id.tablist_1);
					tablist_2 = (LinearLayout)mPopView.findViewById(R.id.tablist_2);
					tablist_3 = (LinearLayout)mPopView.findViewById(R.id.tablist_3);
					
					tablist_1.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {				
							if (mPopupWindow.isShowing()) 
								mPopupWindow.dismiss();
							Log.w("TabList","tablist_1");
							init_view_list("year");
						}
					});
					tablist_2.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {				
							if (mPopupWindow.isShowing()) 
								mPopupWindow.dismiss();
							Log.w("TabList","tablist_2");
							init_view_list("month");
						}
					});
					tablist_3.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {				
							if (mPopupWindow.isShowing()) 
								mPopupWindow.dismiss();
							Log.w("TabList","tablist_3");
							init_view_list("day");
						}
					});
	    		}
				if (mPopupWindow.isShowing()) {
					mPopupWindow.dismiss();
				} else {
					mPopupWindow.showAsDropDown(mTopLayout, 0, -10);
				}
				Log.w("TabList","mTopLayout");
			}
		});
		
	}
	
	/**
	 * ��ʼ����ͼ�ؼ�
	 * @param: ShowType ��ʾ��ʽ
	 * year/month/week/day
	 */
	public void init_view_list(String show_type) {

		listView = (ListView) findViewById(R.id.consumeListView);
        consume_infos = current_user.get_all_consume_item(show_type);
        String top_text = "��";
        if(show_type.equals("year"))
        	top_text = "��";
        else if(show_type.equals("month"))
        	top_text = "��";
        else if(show_type.equals("day"))
        	top_text = "��";
        else
        	top_text = "��";
        mTopText.setText("�����б�["+top_text+"]");
        
        //�����Ѽ�¼ʱ��ʾ����
		if (consume_infos == null && consume_infos.size() == 0) {
			Toast.makeText(TabList.this, "No Data", 0).show();
			return;
		}
		
		//listView.addFooterView(lvQuestion_footer);//��ӵײ���ͼ  ������setAdapterǰ
		listView.setAdapter(new ListViewConsumeAdapter(consume_infos,TabList.this));
		listView.setClickable(true);
		listView.setOnItemClickListener(new OnItemClickListener(){
			 @Override
	         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ConsumeInfo consumeinfo = consume_infos.get(position);
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
				Toast.makeText(TabList.this, "["+consumeinfo.get_created_at()+"]���Ѽ�¼", 0).show();
				
				// �����л�����ʾ�����¼
				Intent intent = new Intent(TabList.this, ConsumeItem.class);
				intent.putExtra("created_at",  consumeinfo.get_created_at());
				startActivity(intent);
			 }
		});
		listView.invalidate();
	}
	
	/**
	 * �����б�spinner�����Ӧ
	class SpinnerOnItemSelectListener implements OnItemSelectedListener{  
	    @Override  
	    public void onItemSelected(AdapterView<?> AdapterView, View view, int position, long arg3) {  
	        String selected = AdapterView.getItemAtPosition(position).toString();  
	        Toast.makeText(TabList.this, selected, 0).show();
	        
	        //��������б�spinner��ͬѡ���Ӧ��ͬ��Ϣ
	        switch(position) {
	        case 0:
	        	init_view_list("year"); break;
	        case 1:
	        	init_view_list("month"); break;
	        case 2:
	        	init_view_list("week"); break;
	        case 3:
	        	init_view_list("day"); break;
	        default:
		        init_view_list("day"); break;
	        }
	    }
	    
	    @Override  
	    public void onNothingSelected(AdapterView<?> arg0) {  
	        // TODO Auto-generated method stub   
	        Toast.makeText(TabList.this, "NothingSelected", 0).show();
	    }  
	      
	}

	 */
	// ���-���ݲ���
		Handler handler = new Handler() {
			public void handleMessage(android.os.Message message) {
				switch (message.what) {
				case 1000:
					Toast.makeText(TabList.this, "ͬ������"+ ((ArrayList<ConsumeInfo>)message.obj).size(), 0).show();
					Toast.makeText(TabList.this, "���ݿ�ͬ���ɹ�", 0).show();
					break;
				case 2000:
					//Toast.makeText(TabList.this, "���ݿ�ͬ��ʧ�ܣ�", 0).show();
					Toast.makeText(TabList.this, message.obj.toString(), 0).show();

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
					final ArrayList<ConsumeInfo> consume_infos = (ArrayList<ConsumeInfo>) paramObject.get("consume_infos");
					
					if (consume_infos != null && consume_infos.size() != 0) {
						Toast.makeText(TabList.this, "ͬ������"+consume_infos.size(), 0).show();
						
						Message message = new Message();
						try {
							Toast.makeText(TabList.this, "��ʼͬ������2", 0).show();
							//log������
				            Log.w("TabList callback","�����б�����:"+consume_infos.size());
							consumeDao.insert_all_record(consume_infos, true);
				            Log.w("TabList callback","�����б�������ݿ����");
							message.what = 1000;
							message.obj  = consume_infos;
							handler.sendMessage(message);
						} catch (Exception e) {
							message.what = 2000;
							message.obj = e;
							handler.sendMessage(message);
							e.printStackTrace();
						}

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
				Toast.makeText(TabList.this, "����������...", 0).show();

		    	loading_progress_bar.setVisibility(View.VISIBLE);
		    	if(loading_dialog != null) loading_dialog.dismiss();
			    loading_dialog = new LoadingDialog(TabList.this);
				loading_dialog.setLoadText("����������...");			
				loading_dialog.show();
				
				//ConsumeListParse consumeListParse = new ConsumeListParse();
				//getDataFromServer(getApplicationContext(), consumeListParse, URLs.CONSUME_LIST, callback);
				try {
					NetUtils.download_all_records(TabList.this,current_user_token);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				init_view_list("day");
				
				loading_progress_bar.setVisibility(View.GONE);
				if(loading_dialog != null) loading_dialog.dismiss();
			}
		};
		//ͬ������������������
		Button.OnClickListener imageButton_refresh_listener = new Button.OnClickListener(){//������������  
			public void onClick(View v){  
				consume_infos = consumeDao.get_unsync_records();
				
		    	//Toast.makeText(TabList.this, "����δͬ������", 0).show();
		    	loading_progress_bar.setVisibility(View.VISIBLE);
		    	if(loading_dialog != null) loading_dialog.dismiss();
			    loading_dialog = new LoadingDialog(TabList.this);
				loading_dialog.setLoadText("ͬ��������...");	
				loading_dialog.show();

				NetUtils.sync_upload_record_background(TabList.this,current_user_token);
				Toast.makeText(TabList.this, "ͬ�����", 0).show();
				init_view_list("day");
				
				loading_progress_bar.setVisibility(View.GONE);
				if(loading_dialog != null) loading_dialog.dismiss();
		};
	};
	
	public void chk_list_click() {
		if (mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
		}
	};
	
}
