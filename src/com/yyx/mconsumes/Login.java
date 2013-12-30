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
	
	/*�û���½
     * ����������û����˺ź������Ƿ�һ�£������ؽ��
     * */
	Button.OnClickListener button_login_listener = new Button.OnClickListener(){//������������  
		public void onClick(View v){  
			EditText editText_login_email = (EditText) findViewById(R.id.editText_Email);
			EditText editText_login_pwd   = (EditText) findViewById(R.id.editText_Pwd);
			//��½�û����뼰����
			String login_email = editText_login_email.getText().toString();
			String login_pwd   = editText_login_pwd.getText().toString();
			
    		sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);	
            Editor Editor = sharedPreferences.edit();
			String [] ret_array = Login(Editor, login_email,login_pwd);
			
	        String ret_str;
	        if(ret_array[0].equals("1")){
	            ret_str = "��½�ɹ�";	
				startActivity(new Intent(Login.this,MainTabActivity.class));
	        } else {
	            ret_str = "��½ʧ��:" +ret_array[1];
	        }
			Toast.makeText(Login.this, ret_str, 0).show();
		}
	};
	
    public static String [] Login(Editor Editor,String email ,String password) {
    	
        //Step One  �ӷ������ӿ��л�ȡ��ǰ�˺ź������������
    	String [] ret_array = {"0","no return"};
        Integer ret     = 0;
        String ret_info = "no return";
        
        String httpUrl="http://solife.us/api/users/login?email="+email+"&password="+password;
        //httpGet ���Ӷ���
        HttpGet httpRequest =new HttpGet(httpUrl);
        
        try {
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
