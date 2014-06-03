package us.solife.consumes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//import java.util.HashMap;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;



//import org.apache.http.client.methods.HttpPost; 
import org.apache.commons.httpclient.HttpException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import us.solife.iconsumes.R;
import us.solife.consumes.util.LoadingDialog;
import us.solife.consumes.util.NetUtils;
//import org.json.JSONException;

//import us.solife.consumes.BaseActivity.DataCallback;
//import us.solife.consumes.db.ConsumeDao;
//import us.solife.consumes.entity.ConsumeInfo;


import us.solife.consumes.util.ToolUtils;
import android.widget.Toast;
import android.net.Uri;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
//import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
//import android.widget.TextView;
import android.widget.ProgressBar;

public class Login extends BaseActivity {
	SharedPreferences sharedPreferences;

	private ProgressBar loading_progress_bar;
	private LoadingDialog loading_dialog;
	private Button button_submit;
	private EditText editText_login_email;
	private EditText editText_login_pwd ;
	private final static Pattern emailer = Pattern.compile("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$");
	@Override
	public void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.login_form);
		
	    //login
		button_submit =(Button)findViewById(R.id.login_login_btn); 
		button_submit.setOnClickListener(button_login_listener); 
    	button_submit.setEnabled(false);
    	button_submit.setClickable(false);

		editText_login_email = (EditText) findViewById(R.id.user_info_email);
		editText_login_pwd   = (EditText) findViewById(R.id.user_info_pwd);
		editText_login_email.addTextChangedListener(text_watcher);
    	
		loading_progress_bar = (ProgressBar) findViewById(R.id.login_form_loading_progress);
		if(loading_dialog != null) loading_dialog.dismiss();
	}
	
	/*用户登陆
     * 服务器检测用户的账号和密码是否一致，并返回结果
     * */
	Button.OnClickListener button_login_listener = new Button.OnClickListener(){//创建监听对象  
		public void onClick(View v){  
        	button_submit.setEnabled(false);
        	button_submit.setClickable(false);
	    	loading_progress_bar.setVisibility(View.VISIBLE);
	    	
            loading_dialog = new LoadingDialog(Login.this);	
			loading_dialog.setLoadText("下载数据中...");	
			loading_dialog.show();
	    	
			//登陆用户密码及密码
			String login_email = editText_login_email.getText().toString();
			String login_pwd   = editText_login_pwd.getText().toString();
			
    		sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);	
            //Editor Editor = sharedPreferences.edit();
            
            String token = ToolUtils.generate_user_token(login_email, login_pwd);
			String[] ret_array = { "-1", "fail" };
			try {
				ret_array = NetUtils.get_user_info(sharedPreferences,token, getApplicationContext());
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        String ret_str;
	        if(ret_array[0].equals("1")){
	            ret_str = "登陆成功";	
	            
				//后台同步更新未同步的数据
				try {
					NetUtils.get_self_records_with_del(Login.this,token);
					NetUtils.get_user_friends_info(getApplicationContext(),token, true);
					NetUtils.get_friend_records(Login.this, token);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				startActivity(new Intent(Login.this,Main.class));
				
	        } else {
	            ret_str = "登陆失败:" +ret_array[1];
				if(loading_dialog != null) loading_dialog.dismiss();
		    	loading_progress_bar.setVisibility(View.GONE);
	        }
			Toast.makeText(Login.this, ret_str, 0).show();
			
        	button_submit.setEnabled(true);
        	button_submit.setClickable(true);
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
            if(s.length()>0 && emailer.matcher(s).matches()) {
            	button_submit.setEnabled(true);
            	button_submit.setClickable(true);
            } else {
            	button_submit.setEnabled(false);
            	button_submit.setClickable(false);
            }
	    }
	     
	};

}
