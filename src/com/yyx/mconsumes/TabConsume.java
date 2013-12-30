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
		
		//��ʼ����������ʱ��
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
	
	/* ����consume submit��������  
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
			
			//��½�û����뼰����
			//String created_at = "2013-12-29 9:1:1";
			String login_email = "";
			String [] ret_array = {"0", "login email is empty!"};

            Toast.makeText(TabConsume.this, "this is three", 0).show();
			//login email����������ʾ�û��޵�½
			sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
			if(sharedPreferences.contains("current_user") && 
			   !sharedPreferences.getString("current_user","").equals("")){
				login_email = sharedPreferences.getString("current_user","");
				ret_array = consume_create(login_email,volue,created_at,msg);
			} 
			
	        String ret_str;
	        if(ret_array[0].equals("1")){
	            ret_str = "�����ɹ�";
	            sync = true;
	        } else {
	            ret_str = "����ʧ��:" +ret_array[1];
	            sync = false;
	        }
	        ConsumeDao consumeDao = new ConsumeDao(TabConsume.this);
	        try {
	          row_id = consumeDao.insertRecord(user_id,consume_id,volue,msg, created_at,sync);
	        } catch (Exception e) {
			  e.printStackTrace();
		    }
            
			//�����л�
			//��ʾ��¼��¼
			Intent intent = new Intent(TabConsume.this,ConsumeItem.class);
			intent.putExtra("row_id", row_id);  
			startActivity(intent);
	        Toast.makeText(TabConsume.this, ret_str, 0).show();
		}
	};
	
	/* ���ڼ�һ��   */
	Button.OnClickListener button_date_add_listener = new Button.OnClickListener() {
		public void onClick(View v){  		
			EditText editText_consume_form_created_at = (EditText) findViewById(R.id.editText_consume_form_created_at);
			String created_at = editText_consume_form_created_at.getText().toString();
			editText_consume_form_created_at.setText(get_date(created_at,1));
			
			//�ؼ����¸�ֵ��ˢ�½���
			v.invalidate();
		}
	};
	/* ���ڼ�һ��   */
	Button.OnClickListener button_date_plus_listener = new Button.OnClickListener() {
		public void onClick(View v){  		
			EditText editText_consume_form_created_at = (EditText) findViewById(R.id.editText_consume_form_created_at);
			String created_at = editText_consume_form_created_at.getText().toString();
			editText_consume_form_created_at.setText(get_date(created_at,-1));
			
			//�ؼ����¸�ֵ��ˢ�½���
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
        //Step One  �ӷ������ӿ��л�ȡ��ǰ�˺ź������������
    	String [] ret_array = {"0","no return"};
        Integer ret     = 0;
        String ret_info = "no return";
        
        String http_url  = "http://solife.us/api/consumes/create?";//?email="+login_email+"&consume[volue]="+value+"&consume[created_at]="+created_at+"&consume[msg]="+msg;
        //����httpRequest����  
        HttpPost httpRequest = new HttpPost(http_url); 
        //HttpGet httpRequest =new HttpGet(httpUrl); 
        List<NameValuePair> params = new ArrayList<NameValuePair>();  
        params.add(new BasicNameValuePair("email", login_email));  
        params.add(new BasicNameValuePair("consume[volue]", value));  
        params.add(new BasicNameValuePair("consume[created_at]", created_at));  
        params.add(new BasicNameValuePair("consume[msg]", msg));  
        
        try {        	
        	//�����ַ���  
            HttpEntity httpentity = new UrlEncodedFormEntity(params, "gb2312");
            //����httpRequest  
            httpRequest.setEntity(httpentity);
            //ȡ��HttpClinet����
            HttpClient httpclient=new DefaultHttpClient();
            // ����HttpClient,ȡ��HttpResponse
            HttpResponse  httpResponse=httpclient.execute(httpRequest);
            //����ɹ�
            if(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK) {
                //ȡ�÷��ص��ַ���
                String strResult=EntityUtils.toString(httpResponse.getEntity());
                JSONObject jsonObject = new JSONObject(strResult) ;
                //��ȡ����ֵ
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
