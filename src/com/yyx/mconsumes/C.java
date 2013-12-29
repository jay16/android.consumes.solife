package com.yyx.mconsumes;

import android.content.Context;
//import android.content.Intent;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class C extends BaseActivity {
	SharedPreferences sharedPreferences;

	@Override
	public void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.c);
		
		//sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
		//String current_user = sharedPreferences.getString("current_user","");
	    //login
		//TextView  textView_Current_User_Email = (TextView)findViewById(R.id.textView_Current_User_Email); 
		//textView_Current_User_Email.setText(current_user);
	}
}
