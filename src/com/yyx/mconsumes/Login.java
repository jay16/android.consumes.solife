package com.yyx.mconsumes;

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
import org.json.JSONException;

import android.widget.Toast;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
//import android.widget.TextView;

public class Login extends BaseActivity {
	SharedPreferences sharedPreferences;
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.login);
		
	    //login
		Button button_submit =(Button)findViewById(R.id.button_submit); 
		button_submit.setOnClickListener(button_login_listener); 
	}
	
	/*用户登陆
     * 服务器检测用户的账号和密码是否一致，并返回结果
     * */
	Button.OnClickListener button_login_listener = new Button.OnClickListener(){//创建监听对象  
		public void onClick(View v){  
			EditText editText_login_email = (EditText) findViewById(R.id.editText_Email);
			EditText editText_login_pwd   = (EditText) findViewById(R.id.editText_Pwd);
			//登陆用户密码及密码
			String login_email = editText_login_email.getText().toString();
			String login_pwd   = editText_login_pwd.getText().toString();
			
    		sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);	
            Editor Editor = sharedPreferences.edit();
			String [] ret_array = Login(Editor, login_email,login_pwd);
			
	        String ret_str;
	        if(ret_array[0].equals("1")){
	            ret_str = "登陆成功";	
				startActivity(new Intent(Login.this,MainTabActivity.class));
	        } else {
	            ret_str = "登陆失败:" +ret_array[1];
	        }
			Toast.makeText(Login.this, ret_str, 0).show();
		}
	};
	
    public static String [] Login(Editor Editor,String email ,String password) {
    	
        //Step One  从服务器接口中获取当前账号和密码的配对情况
    	String [] ret_array = {"0","no return"};
        Integer ret     = 0;
        String ret_info = "no return";
        
        String httpUrl="http://solife.us/api/users/login?email="+email+"&password="+password;
        //httpGet 连接对象
        HttpGet httpRequest =new HttpGet(httpUrl);
        
        try {
            //取得HttpClinet对象
            HttpClient httpclient=new DefaultHttpClient();
            // 请求HttpClient,取得HttpResponse
            HttpResponse  httpResponse=httpclient.execute(httpRequest);
            //请求成功
            if(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK)
            {
                //取得返回的字符串
                String strResult=EntityUtils.toString(httpResponse.getEntity());
                
                JSONObject jsonObject = new JSONObject(strResult) ;
                //获取返回值,并判断是否正确
                //actionResult=jsonObject.getBoolean("ActionResult");
                ret      = jsonObject.getInt("ret");
                ret_info = jsonObject.getString("ret_info");
                String name = jsonObject.getString("user_name");
                ret_array[0] = ret.toString();
                ret_array[1] = ret_info;
                if(ret.toString().equals("1")) {
					Editor.putBoolean("is_login", true);
					Editor.putString("current_user", email);
					Editor.commit();
                } else {
        			Editor.putBoolean("is_login", false);
        			Editor.commit();
                }
            }
        }
        catch(Exception e) {
        	ret_array[1] = e.getMessage().toString();
			Editor.putBoolean("is_login", false);
			Editor.commit();
        	return ret_array;
            
        }
        return ret_array;
    }


}
