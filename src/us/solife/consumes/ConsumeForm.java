package us.solife.consumes;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import us.solife.iconsumes.R;
import us.solife.consumes.adapter.ListViewTagSelectAdapter;
import us.solife.consumes.db.TagTb;
import us.solife.consumes.entity.ConsumeInfo;
import us.solife.consumes.entity.CurrentUser;


import us.solife.consumes.entity.TagInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
//import android.content.SharedPreferences.Editor;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
//import android.widget.CalendarView;
//import android.widget.DatePicker;
import android.widget.EditText;
import android.text.TextWatcher;
import android.text.Editable;

import java.util.Date;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import us.solife.consumes.util.NetUtils;
import us.solife.consumes.util.ToolUtils;
import us.solife.consumes.util.UIHelper;

public class ConsumeForm extends BaseActivity {
	private SharedPreferences shared_preferences;
	private CurrentUser       current_user;
	private TextView  textView_main_header;
	private EditText editTextRecordFormValue;
	private EditText editTextRecordFormYmdhms;
	private EditText editTextRecordFormRemark;
	private TextView  textViewRecordFormTags;
	private LinearLayout linearLayoutRecordFormTag;
	private RadioGroup radioGroupRecordKlass;
	private Button buttonRecordFormSubmit;
	private Button buttonDateAdd;
	private Button buttonDatePlus;
	private Button mBack;
	private Long   row_id = (long)-1;
	private String action = "create" ;
	private Integer klass = -1;
	private Long currentUserId;
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.consume_form);
		
		radioGroupRecordKlass     = (RadioGroup)findViewById(R.id.radioGroup_consumeKlass);
		textView_main_header         = (TextView)findViewById(R.id.textView_main_header);
		editTextRecordFormValue  = (EditText) findViewById(R.id.editText_consume_form_value);
		editTextRecordFormYmdhms = (EditText) findViewById(R.id.editText_consume_form_ymdhms);
		editTextRecordFormRemark = (EditText) findViewById(R.id.editText_consume_form_remark);
		editTextRecordFormRemark.addTextChangedListener(text_watcher);
		textViewRecordFormTags   = (TextView) findViewById(R.id.textView_consume_form_tags);
		linearLayoutRecordFormTag = (LinearLayout)findViewById(R.id.linearLayout_consume_form_tags);
		
		// consume_form
		buttonDateAdd = (Button) findViewById(R.id.button_date_add);
		buttonDateAdd.setOnClickListener(button_date_add_listener);
		buttonDatePlus = (Button) findViewById(R.id.button_date_plus);
		buttonDatePlus.setOnClickListener(button_date_plus_listener);
		buttonRecordFormSubmit = (Button) findViewById(R.id.button_consume_form_submit);
		buttonRecordFormSubmit.setOnClickListener(button_consume_form_submit_listener);
		//返回主界面
    	mBack = (Button)findViewById(R.id.menu_btn_back);
    	mBack.setOnClickListener(UIHelper.finish(this));

		shared_preferences = getSharedPreferences("config", Context.MODE_PRIVATE);
	    currentUserId = shared_preferences.getLong("current_user_id", -1);
	    current_user = CurrentUser.getCurrentUser(getApplication(), currentUserId);
	}
	
	/**
	 * 每当跳转到该界面时加载该项
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		//跳转至该界面状态
		//创建/编辑
		Intent intent = getIntent();
	    if(intent.hasExtra("action")) {
		    action = intent.getStringExtra("action");
		    row_id = intent.getLongExtra("row_id",0);
	    } else {
	    	action = "create";
	    }
	    Integer radioGroupId = -1;
	    if(action.equals("update")) {
	    	radioGroupId = init_update_consume(row_id);
	    } else {
	    	radioGroupId = init_create_consume();
	    }

		radioGroupRecordKlass.setOnCheckedChangeListener(null);
		radioGroupRecordKlass.check(radioGroupId);
		radioGroupRecordKlass.setOnCheckedChangeListener(radio_group_oncheck);
		
	    TextView time = (TextView)findViewById(R.id.textView_main_time);
	    time.setText(ToolUtils.get_ymdw_date());
	}
	
	private Integer init_create_consume() {
		// 初始化创建日期时间
		editTextRecordFormYmdhms.setText(ToolUtils.get_ymdhmsw_date());
		textView_main_header.setText("创建记录");
		buttonRecordFormSubmit.setText("提交");
		textViewRecordFormTags.setText(""); 
		linearLayoutRecordFormTag.setVisibility(View.GONE);
		return R.id.radio5;
	}

	private Integer init_update_consume(Long row_id) {		
		ConsumeInfo recordInfo = current_user.findRecordById(row_id);
		Log.w("get_record", recordInfo.to_string());
		editTextRecordFormValue.setText(recordInfo.get_value()+"");
		editTextRecordFormYmdhms.setText(recordInfo.get_ymdhms());
		editTextRecordFormRemark.setText(recordInfo.get_remark());
		textViewRecordFormTags.setText(recordInfo.get_tags_list());
		textView_main_header.setText("编辑记录");
		buttonRecordFormSubmit.setText("更新");
		LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linearLayout_consume_form_tags);
        if(recordInfo.get_tags_list()==null || 
           recordInfo.get_tags_list().length() == 0) {
            linearLayout.setVisibility(View.GONE);
        } else {
        	linearLayout.setVisibility(View.VISIBLE);
            textViewRecordFormTags.setText(recordInfo.get_tags_list());
        }
        
		Integer id;
		Log.w("UpdatedRecord",recordInfo.to_string());
		switch(recordInfo.get_klass()+"") {
		case "1": id = R.id.radio1; break;
		case "2": id = R.id.radio2; break;
		case "3": id = R.id.radio3; break;
		case "4": id = R.id.radio4; break;
		case "5": id = R.id.radio5; break; 
		default: id = R.id.radio5; break;
		}
		Log.w("UpdatedRecord", "radio:"+id);
 
		return id;
	}

	RadioGroup.OnCheckedChangeListener radio_group_oncheck = new RadioGroup.OnCheckedChangeListener() { 
		 public void onCheckedChanged(RadioGroup group, int checkedId) { 
			 RadioButton radioButton = (RadioButton)findViewById(radioGroupRecordKlass.getCheckedRadioButtonId());
			 String whatIn = radioButton.getText().toString();
			 switch(whatIn) {
				 case "衣": klass = 1; break;
				 case "食": klass = 2; break;
				 case "住": klass = 3; break;
				 case "行": klass = 4; break;
				 case "其他": klass = 5; break;
				 default: klass = -1; break;
			 }
			 Log.i("whatIn", whatIn+ " - " + klass);

	         UIHelper.consumeTagFormDialog(ConsumeForm.this, Integer.valueOf(String.valueOf(row_id)), klass, whatIn, currentUserId, textViewRecordFormTags, linearLayoutRecordFormTag);
		 }
	};
		 
	private TextWatcher text_watcher = new TextWatcher(){
		 
	    @Override
	    public void afterTextChanged(Editable s) {
	        // TODO Auto-generated method stub
	    }
	 
	    @Override
	    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	        // TODO Auto-generated method stub
	    }
	 
	    @Override
	    public void onTextChanged(CharSequence s, int start, int before, int count) {
	    	String[] rows = new String[20];
	    	String row = "";
	    	String tmp = "";
	    	Boolean isAllBlank = true;
	    	Float value = (float)0;
	    	TextView  editText_consume_form_value = (TextView)findViewById(R.id.editText_consume_form_value);
	    	
            rows = s.toString().trim().split("\n");
            for (int i = 0; i < rows.length; i++) {
				String[] aa = rows[i].split("-");
				if(aa.length==0) continue;
				
				isAllBlank = false;
				
				tmp = aa[0].trim();
				if(tmp.length()==0) continue;
				  
				try {
				      //float f = Float.valueOf(tmp.trim()).floatValue();
				      float f = Float.parseFloat(tmp.trim());
				      value = value + f;
				} catch (NumberFormatException nfe) {}
            }
	    	//Toast.makeText(TabConsume.this, ""+value, 0).show();
            editText_consume_form_value.setText(Float.toString(value));
            if(isAllBlank == false) {
            	buttonRecordFormSubmit.setEnabled(true);
            	buttonRecordFormSubmit.setClickable(true);
            } else {
            	buttonRecordFormSubmit.setEnabled(false);
            	buttonRecordFormSubmit.setClickable(false);
            }
	    }
	     
	};
	/*
	 * 创建consume submit监听对象
	 */
	Button.OnClickListener button_consume_form_submit_listener = new Button.OnClickListener() {
		public void onClick(View v) {
			EditText editTextConsumeFormValue = (EditText) findViewById(R.id.editText_consume_form_value);
			EditText editTextConsumeFormYmdhms = (EditText) findViewById(R.id.editText_consume_form_ymdhms);
			EditText editTextConsumeFormRemark = (EditText) findViewById(R.id.editText_consume_form_remark);

			String value  = editTextConsumeFormValue.getText().toString().trim();
			String remark = editTextConsumeFormRemark.getText().toString().trim();
			String ymdhms = editTextConsumeFormYmdhms.getText().toString().trim();
			// 登陆用户密码及密码
			// String created_at = "2013-12-29 9:1:1";
			String token = "";
			
			// login email不存在则提示用户无登陆
			sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
			if (sharedPreferences.contains("current_user_token")
					&& !sharedPreferences.getString("current_user_token", "").equals("")) {
				token = sharedPreferences.getString("current_user_token", "");

				ConsumeInfo consume_info = new ConsumeInfo();
				try {
					if(action.equals("create")){
						consume_info.set_user_id(Integer.valueOf(String.valueOf(currentUserId)));
						consume_info.set_consume_id(-1);
                        consume_info.set_value(Double.parseDouble(value));
                        consume_info.set_remark(remark);
                        consume_info.set_ymdhms(ymdhms);
                        consume_info.set_tags_list(textViewRecordFormTags.getText().toString());
                        consume_info.set_klass(klass);
                        consume_info.set_created_at(ymdhms.substring(0, 19));
                        consume_info.set_updated_at(ymdhms.substring(0, 19));
                        consume_info.set_sync((long)0);
                        consume_info.set_state("create");
						current_user.insert_record(consume_info);
						Log.e("ConsumeForm["+action+"]",consume_info.to_string());
					} else if(action.equals("update")){
						consume_info = current_user.findRecordById(row_id);
						consume_info.set_value(Double.parseDouble(value));
						consume_info.set_remark(remark);
						consume_info.set_ymdhms(ymdhms);
                        consume_info.set_tags_list(textViewRecordFormTags.getText().toString());
                        consume_info.set_klass(klass);
						consume_info.set_created_at(ymdhms.substring(0,19));
						
						//修改未同步数据，无需修改sync,state
						if(consume_info.get_sync() == (long)1) {
							consume_info.set_sync((long)0);
							consume_info.set_state("update");
						}
						
						Log.w("BeforeUpdate", consume_info.to_string());
						current_user.update_record(consume_info);
						Log.e("ConsumeForm","Action:["+action+"]");
					} else {
						Log.e("ConsumeForm","Action Not Found:["+action+"]");
						Toast.makeText(ConsumeForm.this, "Action Not Found:["+action+"]", 0).show();
					}

					if(NetUtils.has_network(getApplicationContext())) {
					   //后台同步
					   NetUtils.sync_upload_background(ConsumeForm.this,token, currentUserId);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				// 界面切换
				// 显示记录记录
				Intent intent = new Intent(ConsumeForm.this, ConsumeItem.class);
				intent.putExtra("created_at", ymdhms.substring(0,10));
				startActivity(intent);
			} else {
				Toast.makeText(ConsumeForm.this, "Email Is Empty!", 0).show();
			}
		}
	};

	 
	/* 日期加一天 */
	Button.OnClickListener button_date_add_listener = new Button.OnClickListener() {
		public void onClick(View v) {
			EditText editText_consume_form_ymdhms = (EditText) findViewById(R.id.editText_consume_form_ymdhms);
			String created_at = editText_consume_form_ymdhms.getText().toString();
			try {
				editText_consume_form_ymdhms.setText(ToolUtils.add_dates(created_at, 1));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			// 控件重新赋值后刷新界面
			v.invalidate();
		}
	};
	/* 日期减一天 */
	Button.OnClickListener button_date_plus_listener = new Button.OnClickListener() {
		public void onClick(View v) {
			EditText editText_consume_form_ymdhms = (EditText) findViewById(R.id.editText_consume_form_ymdhms);
			String ymdhms = editText_consume_form_ymdhms.getText().toString();
			try {
				editText_consume_form_ymdhms.setText(ToolUtils.add_dates(ymdhms, -1));
			} catch (ParseException e) {
				e.printStackTrace();
			}

			// 控件重新赋值后刷新界面
			v.invalidate();
		}
	};
	
	public boolean dispatchKeyEvent(KeyEvent event) {
		if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){  
			if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
				UIHelper.finish(this);
			}
		}
		Log.w("Main","dispatchKeyEvent"+event.toString());
		return super.dispatchKeyEvent(event);  
	}

}
