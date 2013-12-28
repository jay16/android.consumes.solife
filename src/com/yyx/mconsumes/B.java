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
	
	/* ����consume
     * */
	Button.OnClickListener button_consume_form_submit_listener = new Button.OnClickListener(){//������������  
		public void onClick(View v){  
			EditText editText_consume_form_value = (EditText) findViewById(R.id.editText_consume_form_value);
			EditText editText_consume_form_msg   = (EditText) findViewById(R.id.editText_consume_form_msg);
			//DatePicker datePicker_consume_form_created_at = (DatePicker) findViewById(R.id.datePicker_consume_form_created_at);
			//��½�û����뼰����
			String value = editText_consume_form_value.getText().toString();
			String msg   = editText_consume_form_msg.getText().toString();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        String created_at   =  dateFormat.format(new Date());
	        String [] ret_array = consume_create(value,created_at,msg);
	        String ret_str;
	        if(ret_array[0].equals("1")){
	            ret_str = "�����ɹ�";			
	        } else {
	            ret_str = "����ʧ��:" +ret_array[1];
	        }
            Toast.makeText(B.this, ret_str, 0).show();
		}
	};
	
    public static String [] consume_create(String value ,String created_at,String msg)
    {
    	
        //Step One  �ӷ������ӿ��л�ȡ��ǰ�˺ź������������
    	String [] ret_array = {"0","no return"};
        Integer ret     = 0;
        String ret_info = "no return";
        
        String httpUrl  = "http://solife.us/api/consumes/create?consume[volue]="+value+"&consume[created_at]="+created_at+"&consume[msg]="+msg;
        //httpGet ���Ӷ���
        HttpGet httpRequest =new HttpGet(httpUrl);
        
        try
        {
            //ȡ��HttpClinet����
            HttpClient httpclient=new DefaultHttpClient();
            // ����HttpClient,ȡ��HttpResponse
            HttpResponse  httpResponse=httpclient.execute(httpRequest);
            //����ɹ�
            if(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK)
            {
                //ȡ�÷��ص��ַ���
                String strResult=EntityUtils.toString(httpResponse.getEntity());
                
                JSONObject jsonObject = new JSONObject(strResult) ;
                //��ȡ����ֵ,���ж��Ƿ���ȷ
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
