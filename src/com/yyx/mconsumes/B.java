package com.yyx.mconsumes;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
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

	    //consume_form
		Button button_consume_form_submit =(Button)findViewById(R.id.button_consume_form_submit); 
		button_consume_form_submit.setOnClickListener(button_consume_form_submit_listener); 
	}
	
	/* 创建consume
     * */
	Button.OnClickListener button_consume_form_submit_listener = new Button.OnClickListener(){//创建监听对象  
		public void onClick(View v){  
			EditText editText_consume_form_value = (EditText) findViewById(R.id.editText_consume_form_value);
			EditText editText_consume_form_msg   = (EditText) findViewById(R.id.editText_consume_form_msg);
			//DatePicker datePicker_consume_form_created_at = (DatePicker) findViewById(R.id.datePicker_consume_form_created_at);
			//登陆用户密码及密码
			String value = editText_consume_form_value.getText().toString();
			String msg   = editText_consume_form_msg.getText().toString();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        String created_at   =  dateFormat.format(new Date());
	        String [] ret_array = consume_create(value,created_at,msg);
	        String ret_str;
	        if(ret_array[0].equals("1")){
	            ret_str = "创建成功";			
	        } else {
	            ret_str = "创建失败:" +ret_array[1];
	        }
            Toast.makeText(B.this, ret_str, 0).show();
		}
	};
	
    public static String [] consume_create(String value ,String created_at,String msg)
    {
    	
        //Step One  从服务器接口中获取当前账号和密码的配对情况
    	String [] ret_array = {"0","no return"};
        Integer ret     = 0;
        String ret_info = "no return";
        
        String httpUrl  = "http://solife.us/api/consumes/create?consume[volue]="+value+"&consume[created_at]="+created_at+"&consume[msg]="+msg;
        //httpGet 连接对象
        HttpGet httpRequest =new HttpGet(httpUrl);
        
        try
        {
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
                ret_array[0] = ret.toString();
                ret_array[1] = ret_info;
            }
        }
        catch(Exception e)
        {
        	ret_array[1] = e.getMessage().toString();
        	return ret_array;
        }
        return ret_array;
    }


}
