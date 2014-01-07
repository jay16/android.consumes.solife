package us.solife.consumes;

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

import com.yyx.mconsumes.R;

import android.widget.Toast;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TabUser extends BaseActivity {
	SharedPreferences sharedPreferences;

	@Override
	public void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.tab_user);

		TextView  textView_main_header = (TextView)findViewById(R.id.textView_main_header);
		textView_main_header.setText("我的资料");
		
		//加载用户信息
		sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
		//Integer current_user_id      = sharedPreferences.getInt("current_user_id",-1);
		String current_user_name     = sharedPreferences.getString("current_user_name","");
		String current_user_email    = sharedPreferences.getString("current_user_email","");
		String current_user_province = sharedPreferences.getString("current_user_province","");
	    //login
		TextView  textView_Current_User_Name = (TextView)findViewById(R.id.textView_User_Name); 
		TextView  textView_Current_User_Email = (TextView)findViewById(R.id.textView_User_Email); 
		TextView  textView_Current_User_Province = (TextView)findViewById(R.id.textView_User_Province); 
		textView_Current_User_Name.setText(current_user_name);
		textView_Current_User_Email.setText(current_user_email);
		textView_Current_User_Province.setText(current_user_province);
	}
	
}
