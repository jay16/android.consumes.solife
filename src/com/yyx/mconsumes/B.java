package com.yyx.mconsumes;

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

import android.content.Context;
import android.content.SharedPreferences;
//import android.content.SharedPreferences.Editor;
import android.widget.Toast;
import android.view.View;
import android.widget.Button;
//import android.widget.CalendarView;
//import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class B extends BaseActivity {
	SharedPreferences sharedPreferences;
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.b);

		
		Toast.makeText(B.this, "this is one", 0).show();
	    //consume_form
		Button button_consume_form_submit =(Button)findViewById(R.id.button_consume_form_submit); 
		button_consume_form_submit.setOnClickListener(button_consume_form_submit_listener); 
	}
	
	/* 创建consume submit监听对象  
     * */
	Button.OnClickListener button_consume_form_submit_listener = new Button.OnClickListener() {
		public void onClick(View v){  
            Toast.makeText(B.this, "this is two", 0).show();
            
			EditText editText_consume_form_value = (EditText) findViewById(R.id.editText_consume_form_value);
			EditText editText_consume_form_msg   = (EditText) findViewById(R.id.editText_consume_form_msg);
			//DatePicker datePicker_consume_form_created_at = (DatePicker) findViewById(R.id.datePicker_consume_form_created_at);
			//登陆用户密码及密码
			String consume_value = editText_consume_form_value.getText().toString();
			String consume_msg   = editText_consume_form_msg.getText().toString();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        String consume_created_at   =  dateFormat.format(new Date());
			//String consume_created_at = "2013-12-29 9:1:1";
			String login_email = "";
			String [] ret_array = {"0", "login email is empty!"};

            Toast.makeText(B.this, "this is three", 0).show();
			//login email不存在则提示用户无登陆
			sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
			if(sharedPreferences.contains("login_email") && 
			   !sharedPreferences.getString("login_email","").equals("")){
				login_email = sharedPreferences.getString("login_email","");
				ret_array = consume_create(login_email,consume_value,consume_created_at,consume_msg);
			} 
			
	        String ret_str;
	        if(ret_array[0].equals("1")){
	            ret_str = "创建成功";			
	        } else {
	            ret_str = "创建失败:" +ret_array[1];
	        }
            Toast.makeText(B.this, ret_str, 0).show();
		}
	};
	
    public static String [] consume_create(String login_email, String value ,String created_at,String msg) {
        //Step One  从服务器接口中获取当前账号和密码的配对情况
    	String [] ret_array = {"0","no return"};
        Integer ret     = 0;
        String ret_info = "no return";
        
        String http_url  = "http://solife.us/api/consumes/create?";//?email="+login_email+"&consume[volue]="+value+"&consume[created_at]="+created_at+"&consume[msg]="+msg;
        //创建httpRequest对象  
        HttpPost httpRequest = new HttpPost(http_url); 
        //HttpGet httpRequest =new HttpGet(httpUrl); 
        List<NameValuePair> params = new ArrayList<NameValuePair>();  
        params.add(new BasicNameValuePair("email", login_email));  
        params.add(new BasicNameValuePair("consume[volue]", value));  
        params.add(new BasicNameValuePair("consume[created_at]", created_at));  
        params.add(new BasicNameValuePair("consume[msg]", msg));  
        
        try {        	
        	//设置字符集  
            HttpEntity httpentity = new UrlEncodedFormEntity(params, "gb2312");
            //请求httpRequest  
            httpRequest.setEntity(httpentity);
            //取得HttpClinet对象
            HttpClient httpclient=new DefaultHttpClient();
            // 请求HttpClient,取得HttpResponse
            HttpResponse  httpResponse=httpclient.execute(httpRequest);
            //请求成功
            if(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK) {
                //取得返回的字符串
                String strResult=EntityUtils.toString(httpResponse.getEntity());
                JSONObject jsonObject = new JSONObject(strResult) ;
                //获取返回值
                ret      = jsonObject.getInt("ret");
                ret_info = jsonObject.getString("ret_info");
                ret_array[0] = ret.toString();
                ret_array[1] = ret_info;
            }
        }
        catch(Exception e) {
        	ret_array[1] = e.getMessage().toString();
        	return ret_array;
        }
        return ret_array;
    }


}
