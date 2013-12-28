package com.yyx.mconsumes;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
//import android.widget.CompoundButton;
//import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TabHost;
//import android.widget.Toast;
import android.widget.EditText;
//import android.widget.TextView;
import android.widget.Button;
//import android.app.AlertDialog;
//import android.content.DialogInterface;

public class MainTabActivity extends TabActivity implements OnClickListener {
	TabHost tabHost;
	RadioButton mainBt, bBt, cBt, dBt;
	int index = 0;
	RadioButton[] buttons;
	int preIndex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		buttons = new RadioButton[4];
  
	   //TextView resultText = (EditText) this.findViewById(R.id.editText1);
		
		tabHost = getTabHost();
		tabHost.addTab(tabHost.newTabSpec("main").setIndicator("main")
				.setContent(new Intent(this, MainRecordActivity.class)));
		tabHost.addTab(tabHost.newTabSpec("B").setIndicator("B")
				.setContent(new Intent(this, B.class)));
		tabHost.addTab(tabHost.newTabSpec("C").setIndicator("C")
				.setContent(new Intent(this, C.class)));
		tabHost.addTab(tabHost.newTabSpec("D").setIndicator("D")
				.setContent(new Intent(this, D.class)));
		mainBt = (RadioButton) findViewById(R.id.main_footbar_news);
		bBt = (RadioButton) findViewById(R.id.main_footbar_question);
		cBt = (RadioButton) findViewById(R.id.main_footbar_tweet);
		dBt = (RadioButton) findViewById(R.id.main_footbar_active);
		buttons[0] = mainBt;
		buttons[1] = bBt;
		buttons[2] = cBt;
		buttons[3] = dBt;
		bBt.setOnClickListener(this);
		cBt.setOnClickListener(this);
		dBt.setOnClickListener(this);
		mainBt.setOnClickListener(this);
		mainBt.setChecked(true);

	    //login
		Button button_submit =(Button)findViewById(R.id.button_submit); 
		button_submit.setOnClickListener(button_login_listener); 
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch (v.getId()) {
		case R.id.main_footbar_news:

			index = 0;
			buttons[0].setChecked(true);
			buttons[1].setChecked(false);
			buttons[2].setChecked(false);
			buttons[3].setChecked(false);
			tabHost.setCurrentTabByTag("main");

			break;
		case R.id.main_footbar_question:

			index = 1;
			buttons[1].setChecked(true);
			buttons[0].setChecked(false);
			buttons[2].setChecked(false);
			buttons[3].setChecked(false);
			tabHost.setCurrentTabByTag("B");

			break;
		case R.id.main_footbar_tweet:
			index = 2;
			buttons[2].setChecked(true);
			buttons[0].setChecked(false);
			buttons[1].setChecked(false);
			buttons[3].setChecked(false);
			tabHost.setCurrentTabByTag("C");

			break;
		case R.id.main_footbar_active:
			index = 3;
			buttons[3].setChecked(true);
			buttons[1].setChecked(false);
			buttons[2].setChecked(false);
			buttons[0].setChecked(false);
			tabHost.setCurrentTabByTag("D");

			break;

		default:
			break;
		}
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
			Login(login_email,login_pwd);
		}
	};
	
    public static Boolean Login(String Account ,String PassWord)
    {
    	
        //Step One  �ӷ������ӿ��л�ȡ��ǰ�˺ź������������
        Boolean actionResult=false;   
        String httpUrl="http://solife.us/api/consumes/create?Account="+Account+"&PassWord="+PassWord;
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
                Integer ret = jsonObject.getInt("ret");
                String ret_info = jsonObject.getString("ret_info");
            }
        }
        catch(Exception e)
        {
            return false;
            
        }
        return actionResult;
    }
    
}
