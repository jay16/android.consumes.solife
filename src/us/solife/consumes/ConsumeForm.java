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
	private EditText editText_consume_form_value;
	private EditText editText_consume_form_ymdhms;
	private EditText editText_consume_form_remark;
	private RadioGroup radioGroup_consume_klass;
	private Button button_consume_form_submit;
	private Button button_date_add;
	private Button button_date_plus;
	private Button mBack;
	private Long row_id = (long)-1;
	private String action  = "create" ;
	private Integer klass = -1;
	private Integer current_user_id;
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.consume_form);
		
		radioGroup_consume_klass = (RadioGroup)findViewById(R.id.radioGroup_consumeKlass);
		radioGroup_consume_klass.setOnCheckedChangeListener(radio_group_oncheck);
		
		textView_main_header = (TextView)findViewById(R.id.textView_main_header);
		editText_consume_form_value      = (EditText) findViewById(R.id.editText_consume_form_value);
		editText_consume_form_ymdhms = (EditText) findViewById(R.id.editText_consume_form_ymdhms);
		editText_consume_form_remark = (EditText) findViewById(R.id.editText_consume_form_remark);
		editText_consume_form_remark.addTextChangedListener(text_watcher);

		// consume_form
		button_date_add = (Button) findViewById(R.id.button_date_add);
		button_date_add.setOnClickListener(button_date_add_listener);
		button_date_plus = (Button) findViewById(R.id.button_date_plus);
		button_date_plus.setOnClickListener(button_date_plus_listener);
		button_consume_form_submit = (Button) findViewById(R.id.button_consume_form_submit);
		button_consume_form_submit.setOnClickListener(button_consume_form_submit_listener);
		//����������
    	mBack = (Button)findViewById(R.id.menu_btn_back);
    	mBack.setOnClickListener(UIHelper.finish(this));

		shared_preferences = getSharedPreferences("config", Context.MODE_PRIVATE);
	    current_user_id = Integer.parseInt(String.valueOf(shared_preferences.getLong("current_user_id", -1)));
	    current_user = CurrentUser.get_current_user(getApplication(), current_user_id);

		//��ת���ý���״̬
		//����/�༭
		Intent intent = getIntent();
	    if(intent.hasExtra("action")) {
		    action = intent.getStringExtra("action");
		    row_id = intent.getLongExtra("row_id",0);
	    } else {
	    	action = "create";
	    }
	    if(action.equals("update")){
	    	init_update_consume(row_id);
	    } else {
			init_create_consume();
	    }
	    TextView time = (TextView)findViewById(R.id.textView_main_time);
	    time.setText(ToolUtils.get_ymdw_date());
	}
	
	private void init_create_consume() {
		// ��ʼ����������ʱ��
		editText_consume_form_ymdhms.setText(ToolUtils.get_ymdhmsw_date());
		textView_main_header.setText("������¼");
		button_consume_form_submit.setText("�ύ");
	}

	private void init_update_consume(Long row_id) {		
		ConsumeInfo consume_info = current_user.get_record(row_id);
		Log.w("get_record", consume_info.to_string());
		editText_consume_form_value.setText(consume_info.get_value()+"");
		editText_consume_form_ymdhms.setText(consume_info.get_ymdhms());
		editText_consume_form_remark.setText(consume_info.get_remark());
		textView_main_header.setText("�༭��¼");
		button_consume_form_submit.setText("����");
		Integer id;
		Log.w("UpdatedRecord",consume_info.to_string());
		switch(consume_info.get_klass()+"") {
		case "1": id = R.id.radio1; break;
		case "2": id = R.id.radio2; break;
		case "3": id = R.id.radio3; break;
		case "4": id = R.id.radio4; break;
		case "5": id = R.id.radio5; break; 
		default: id = R.id.radio5; break;
		}
		Log.w("UpdatedRecord", "radio:"+id);
		radioGroup_consume_klass.check(id);
	}
	
	RadioGroup.OnCheckedChangeListener radio_group_oncheck = new RadioGroup.OnCheckedChangeListener() { 
		 public void onCheckedChanged(RadioGroup group, int checkedId) { 
			 RadioButton radioButton = (RadioButton)findViewById(radioGroup_consume_klass.getCheckedRadioButtonId());
			 String whatIn = radioButton.getText().toString();
			 switch(whatIn) {
				 case "��": klass = 1; break;
				 case "ʳ": klass = 2; break;
				 case "ס": klass = 3; break;
				 case "��": klass = 4; break;
				 case "����": klass = 5; break;
				 default: klass = -1; break;
			 }
			 Log.i("whatIn", whatIn+ " - " + klass);
			 ListView listView = (ListView)findViewById(R.id.tagListView);

			TagTb tag_tb = TagTb.get_tag_tb(context);
			ArrayList<TagInfo> tag_infos = tag_tb.get_tags_with_klass(klass);
	        if(tag_infos.size() > 0) {
	          //UIHelper.initTagListView(context, listView, tag_infos);
	          ListViewTagSelectAdapter tag_adapter = new ListViewTagSelectAdapter(tag_infos, ConsumeForm.this);
			  UIHelper.consume_tag_form(ConsumeForm.this, tag_adapter, klass, whatIn, current_user_id);
	        }
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
	    	Float value = (float)0;
	    	TextView  editText_consume_form_value = (TextView)findViewById(R.id.editText_consume_form_value);
	    	
            rows = s.toString().split("\n");
            for (int i = 0; i < rows.length; i++) {
				String[] aa = rows[i].split("-");
				if(aa.length==0) continue;
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
            if(value > 0) {
            	button_consume_form_submit.setEnabled(true);
            	button_consume_form_submit.setClickable(true);
            } else {
            	button_consume_form_submit.setEnabled(false);
            	button_consume_form_submit.setClickable(false);
            }
	    }
	     
	};
	/*
	 * ����consume submit��������
	 */
	Button.OnClickListener button_consume_form_submit_listener = new Button.OnClickListener() {
		public void onClick(View v) {
			EditText editText_consume_form_value = (EditText) findViewById(R.id.editText_consume_form_value);
			EditText editText_consume_form_ymdhms = (EditText) findViewById(R.id.editText_consume_form_ymdhms);
			EditText editText_consume_form_remark = (EditText) findViewById(R.id.editText_consume_form_remark);

			String value  = editText_consume_form_value.getText().toString();
			String remark = editText_consume_form_remark.getText().toString();
			String ymdhms = editText_consume_form_ymdhms.getText().toString();
			// ��½�û����뼰����
			// String created_at = "2013-12-29 9:1:1";
			String token = "";
			
			// login email����������ʾ�û��޵�½
			sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
			if (sharedPreferences.contains("current_user_token")
					&& !sharedPreferences.getString("current_user_token", "").equals("")) {
				token = sharedPreferences.getString("current_user_token", "");

				ConsumeInfo consume_info = new ConsumeInfo();
				try {
					if(action.equals("create")){
						consume_info.set_user_id(current_user_id);
						consume_info.set_consume_id(-1);
                        consume_info.set_value(Double.parseDouble(value));
                        consume_info.set_remark(remark);
                        consume_info.set_ymdhms(ymdhms);
                        consume_info.set_tags_list("");
                        consume_info.set_klass(klass);
                        consume_info.set_created_at(ymdhms.substring(0, 19));
                        consume_info.set_sync((long)0);
                        consume_info.set_state("create");
						current_user.insert_record(consume_info);
						Log.e("ConsumeForm["+action+"]",consume_info.to_string());
					} else if(action.equals("update")){
						consume_info = current_user.get_record(row_id);
						consume_info.set_value(Double.parseDouble(value));
						consume_info.set_remark(remark);
						consume_info.set_ymdhms(ymdhms);
                        consume_info.set_tags_list("");
                        consume_info.set_klass(klass);
						consume_info.set_created_at(ymdhms.substring(0,19));
						
						//�޸�δͬ�����ݣ������޸�sync,state
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
					   //��̨ͬ��
					   NetUtils.sync_upload_record_background(ConsumeForm.this,token);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				// �����л�
				// ��ʾ��¼��¼
				Intent intent = new Intent(ConsumeForm.this, ConsumeItem.class);
				intent.putExtra("created_at", ymdhms.substring(0,10));
				startActivity(intent);
			} else {
				Toast.makeText(ConsumeForm.this, "Email Is Empty!", 0).show();
			}
		}
	};

	 
	/* ���ڼ�һ�� */
	Button.OnClickListener button_date_add_listener = new Button.OnClickListener() {
		public void onClick(View v) {
			EditText editText_consume_form_ymdhms = (EditText) findViewById(R.id.editText_consume_form_ymdhms);
			String created_at = editText_consume_form_ymdhms.getText().toString();
			try {
				editText_consume_form_ymdhms.setText(ToolUtils.add_dates(created_at, 1));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			// �ؼ����¸�ֵ��ˢ�½���
			v.invalidate();
		}
	};
	/* ���ڼ�һ�� */
	Button.OnClickListener button_date_plus_listener = new Button.OnClickListener() {
		public void onClick(View v) {
			EditText editText_consume_form_ymdhms = (EditText) findViewById(R.id.editText_consume_form_ymdhms);
			String ymdhms = editText_consume_form_ymdhms.getText().toString();
			try {
				editText_consume_form_ymdhms.setText(ToolUtils.add_dates(ymdhms, -1));
			} catch (ParseException e) {
				e.printStackTrace();
			}

			// �ؼ����¸�ֵ��ˢ�½���
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
