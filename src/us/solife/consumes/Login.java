package us.solife.consumes;

import java.util.ArrayList;
//import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

//import org.apache.http.client.methods.HttpPost; 
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import us.solife.consumes.R;
import us.solife.consumes.util.NetUtils;
//import org.json.JSONException;

//import us.solife.consumes.BaseActivity.DataCallback;
//import us.solife.consumes.db.ConsumeDao;
//import us.solife.consumes.entity.ConsumeInfo;


import android.widget.Toast;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
import android.view.View;
//import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
//import android.widget.TextView;

public class Login extends BaseActivity {
	SharedPreferences sharedPreferences;
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.login_form);
		
	    //login
		Button button_submit =(Button)findViewById(R.id.login_login_btn); 
		button_submit.setOnClickListener(button_login_listener); 
	}
	
	/*用户登陆
     * 服务器检测用户的账号和密码是否一致，并返回结果
     * */
	Button.OnClickListener button_login_listener = new Button.OnClickListener(){//创建监听对象  
		public void onClick(View v){  
			EditText editText_login_email = (EditText) findViewById(R.id.user_info_email);
			EditText editText_login_pwd   = (EditText) findViewById(R.id.user_info_pwd);
			//登陆用户密码及密码
			String login_email = editText_login_email.getText().toString();
			String login_pwd   = editText_login_pwd.getText().toString();
			
    		sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);	
            Editor Editor = sharedPreferences.edit();
            
			String [] ret_array = NetUtils.get_user_info(sharedPreferences,login_email);
	        String ret_str;
	        if(ret_array[0].equals("1")){
	            ret_str = "登陆成功";	
				startActivity(new Intent(Login.this,Main.class));
	        } else {
	            ret_str = "登陆失败:" +ret_array[1];
	        }
			Toast.makeText(Login.this, ret_str, 0).show();
		}
	};

}
