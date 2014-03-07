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

import us.solife.consumes.R;
import us.solife.consumes.entity.ConsumeInfo;
import us.solife.consumes.entity.CurrentUser;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
//import android.content.SharedPreferences.Editor;
import android.widget.AdapterView;
import android.widget.ImageView;
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
import us.solife.consumes.util.UIHelper;

public class ConsumeForm extends BaseActivity {
	private SharedPreferences shared_preferences;
	private CurrentUser       current_user;
	private TextView  textView_main_header;
	private EditText editText_consume_form_value;
	private EditText editText_consume_form_created_at;
	private EditText editText_consume_form_msg;
	private Button button_consume_form_submit;
	private Button button_date_add;
	private Button button_date_plus;
	private Button mBack;
	private Long row_id = (long)-1;
	private String action  = "create" ;
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.consume_form);
		
		textView_main_header = (TextView)findViewById(R.id.textView_main_header);
		editText_consume_form_value      = (EditText) findViewById(R.id.editText_consume_form_value);
		editText_consume_form_created_at = (EditText) findViewById(R.id.editText_consume_form_created_at);
		editText_consume_form_msg = (EditText) findViewById(R.id.editText_consume_form_msg);
		editText_consume_form_msg.addTextChangedListener(text_watcher);

		// consume_form
		button_date_add = (Button) findViewById(R.id.button_date_add);
		button_date_add.setOnClickListener(button_date_add_listener);
		button_date_plus = (Button) findViewById(R.id.button_date_plus);
		button_date_plus.setOnClickListener(button_date_plus_listener);
		button_consume_form_submit = (Button) findViewById(R.id.button_consume_form_submit);
		button_consume_form_submit.setOnClickListener(button_consume_form_submit_listener);
		//返回主界面
    	mBack = (Button)findViewById(R.id.menu_btn_back);
    	mBack.setOnClickListener(UIHelper.finish(this));

		shared_preferences = getSharedPreferences("config", Context.MODE_PRIVATE);
	    long current_user_id = shared_preferences.getLong("current_user_id", -1);
	    current_user = CurrentUser.getCurrentUser(getApplication(),Integer.parseInt(String.valueOf(current_user_id)));

		//跳转至该界面状态
		//创建/编辑
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

	}
	
	private void init_create_consume() {
		// 初始化创建日期时间
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		editText_consume_form_created_at.setText(dateFormat.format(new Date()));
		textView_main_header.setText("创建记录");
		button_consume_form_submit.setText("提交");
	}

	private void init_update_consume(Long row_id) {		
		ConsumeInfo consume_info = current_user.get_record(row_id);
		editText_consume_form_created_at.setText(consume_info.get_created_at());
		editText_consume_form_msg.setText(consume_info.get_msg());
		textView_main_header.setText("编辑记录");
		button_consume_form_submit.setText("更新");
	}
	
	
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
	 * 创建consume submit监听对象
	 */
	Button.OnClickListener button_consume_form_submit_listener = new Button.OnClickListener() {
		public void onClick(View v) {
			EditText editText_consume_form_value = (EditText) findViewById(R.id.editText_consume_form_value);
			EditText editText_consume_form_created_at = (EditText) findViewById(R.id.editText_consume_form_created_at);
			EditText editText_consume_form_msg = (EditText) findViewById(R.id.editText_consume_form_msg);

			String volue = editText_consume_form_value.getText().toString();
			String msg = editText_consume_form_msg.getText().toString();
			String created_at = editText_consume_form_created_at.getText().toString();
			Integer user_id = -1;
			// 登陆用户密码及密码
			// String created_at = "2013-12-29 9:1:1";
			String login_email = "";
			String[] ret_array = { "0", "login email is empty!" };

			// login email不存在则提示用户无登陆
			sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
			if (sharedPreferences.contains("current_user_email")
					&& !sharedPreferences.getString("current_user_email", "").equals("")) {
				login_email = sharedPreferences.getString("current_user_email", "");
				
				try {
					if(action.equals("create")){
						Log.w("ConsumeForm","volue:["+volue+"] msg:["+msg+"] created_at:["+created_at+"]");
						current_user.insert_record(Double.parseDouble(volue), msg, created_at);
						Log.e("ConsumeForm","Action:["+action+"]");
					} else if(action.equals("update")){
						ConsumeInfo consume_info = current_user.get_record(row_id);
						consume_info.set_volue(Double.parseDouble(volue));
						consume_info.set_msg(msg);
						consume_info.set_created_at(created_at);
						
						//修改未同步数据，无需修改sync,state
						if(consume_info.get_sync() == (long)1) {
							consume_info.set_sync((long)0);
							consume_info.set_state("update");
						}
						
						current_user.update_record(consume_info);
						Log.e("ConsumeForm","Action:["+action+"]");
					} else {
						Log.e("ConsumeForm","Action Not Found:["+action+"]");
						Toast.makeText(ConsumeForm.this, "Action Not Found:["+action+"]", 0).show();
					}

					//后台同步
					NetUtils.upload_unsync_consumes_background(ConsumeForm.this,login_email);
				} catch (Exception e) {
					e.printStackTrace();
				}
				// 界面切换
				// 显示记录记录
				Intent intent = new Intent(ConsumeForm.this, ConsumeItem.class);
				intent.putExtra("created_at", created_at.substring(0,10));
				startActivity(intent);
			} else {
				Toast.makeText(ConsumeForm.this, "Email Is Empty!", 0).show();
			}

		}
	};

	 
	/* 日期加一天 */
	Button.OnClickListener button_date_add_listener = new Button.OnClickListener() {
		public void onClick(View v) {
			EditText editText_consume_form_created_at = (EditText) findViewById(R.id.editText_consume_form_created_at);
			String created_at = editText_consume_form_created_at.getText()
					.toString();
			editText_consume_form_created_at.setText(get_date(created_at, 1));

			// 控件重新赋值后刷新界面
			v.invalidate();
		}
	};
	/* 日期减一天 */
	Button.OnClickListener button_date_plus_listener = new Button.OnClickListener() {
		public void onClick(View v) {
			EditText editText_consume_form_created_at = (EditText) findViewById(R.id.editText_consume_form_created_at);
			String created_at = editText_consume_form_created_at.getText().toString();
			editText_consume_form_created_at.setText(get_date(created_at, -1));

			// 控件重新赋值后刷新界面
			v.invalidate();
		}
	};

	public static String get_date(String date_string, Integer num) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = dateFormat.parse(date_string);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String ret_string = dateFormat.format(new Date(date.getTime() + num
				* 24 * 60 * 60 * 1000));
		return ret_string;
	}
	
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
