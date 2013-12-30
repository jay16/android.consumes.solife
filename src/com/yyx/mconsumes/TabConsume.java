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

import com.yyx.mconsumes.db.ConsumeDao;

import android.content.Context;
import android.content.Intent;
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
import java.text.ParseException;

public class TabConsume extends BaseActivity {
	SharedPreferences sharedPreferences;
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.tab_consume);
		
		//初始化创建日期时间
		EditText editText_consume_form_created_at = (EditText) findViewById(R.id.editText_consume_form_created_at);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		editText_consume_form_created_at.setText(dateFormat.format(new Date()));
	    //consume_form
		Button button_consume_form_submit =(Button)findViewById(R.id.button_consume_form_submit); 
		button_consume_form_submit.setOnClickListener(button_consume_form_submit_listener); 
		Button button_date_add =(Button)findViewById(R.id.button_date_add); 
		button_date_add.setOnClickListener(button_date_add_listener);
		Button button_date_plus =(Button)findViewById(R.id.button_date_plus); 
		button_date_plus.setOnClickListener(button_date_plus_listener);
	}
	
	/* 创建consume submit监听对象  
     * */
	Button.OnClickListener button_consume_form_submit_listener = new Button.OnClickListener() {
		public void onClick(View v){  
			EditText editText_consume_form_value = (EditText) findViewById(R.id.editText_consume_form_value);
			EditText editText_consume_form_created_at = (EditText) findViewById(R.id.editText_consume_form_created_at);
			EditText editText_consume_form_msg   = (EditText) findViewById(R.id.editText_consume_form_msg);
			
			String volue = editText_consume_form_value.getText().toString();
			String created_at = editText_consume_form_created_at.getText().toString();
			String msg   = editText_consume_form_msg.getText().toString();
			Integer user_id    = -1;
			Integer consume_id = -1;
			long    row_id     = (long)-1;
	        Boolean sync = false;
			
			//登陆用户密码及密码
			//String created_at = "2013-12-29 9:1:1";
			String login_email = "";
			String [] ret_array = {"0", "login email is empty!"};

            Toast.makeText(TabConsume.this, "this is three", 0).show();
			//login email不存在则提示用户无登陆
			sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
			if(sharedPreferences.contains("current_user") && 
			   !sharedPreferences.getString("current_user","").equals("")){
				login_email = sharedPreferences.getString("current_user","");
				ret_array = consume_create(login_email,volue,created_at,msg);
			} 
			
	        String ret_str;
	        if(ret_array[0].equals("1")){
	            ret_str = "创建成功";
	            sync = true;
	        } else {
	            ret_str = "创建失败:" +ret_array[1];
	            sync = false;
	        }
	        ConsumeDao consumeDao = new ConsumeDao(TabConsume.this);
	        try {
	          row_id = consumeDao.insertRecord(user_id,consume_id,volue,msg, created_at,sync);
	        } catch (Exception e) {
			  e.printStackTrace();
		    }
            
			//界面切换
			//显示记录记录
			Intent intent = new Intent(TabConsume.this,ConsumeItem.class);
			intent.putExtra("row_id", row_id);  
			startActivity(intent);
	        Toast.makeText(TabConsume.this, ret_str, 0).show();
		}
	};
	
	/* 日期加一天   */
	Button.OnClickListener button_date_add_listener = new Button.OnClickListener() {
		public void onClick(View v){  		
			EditText editText_consume_form_created_at = (EditText) findViewById(R.id.editText_consume_form_created_at);
			String created_at = editText_consume_form_created_at.getText().toString();
			editText_consume_form_created_at.setText(get_date(created_at,1));
			
			//控件重新赋值后刷新界面
			v.invalidate();
		}
	};
	/* 日期减一天   */
	Button.OnClickListener button_date_plus_listener = new Button.OnClickListener() {
		public void onClick(View v){  		
			EditText editText_consume_form_created_at = (EditText) findViewById(R.id.editText_consume_form_created_at);
			String created_at = editText_consume_form_created_at.getText().toString();
			editText_consume_form_created_at.setText(get_date(created_at,-1));
			
			//控件重新赋值后刷新界面
			v.invalidate();
		}
	};
	
	public static String get_date(String date_string, Integer num) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
		    date = dateFormat.parse(date_string);
		} catch (ParseException e) {
		    e.printStackTrace();
		}
        String ret_string = dateFormat.format(new Date(date.getTime()+num*24*60*60*1000));
        return ret_string;
	}
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
