package us.solife.consumes;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;

import android.content.Context;
//import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

//import org.apache.http.client.methods.HttpPost; 

import us.solife.iconsumes.R;
import us.solife.consumes.api.Gravatar;
import us.solife.consumes.entity.ConsumeInfo;
import us.solife.consumes.entity.CurrentUser;
import us.solife.consumes.util.NetUtils;
import us.solife.consumes.util.ToolUtils;


import android.widget.Toast;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;

import org.json.JSONException;

public class TabUser extends BaseActivity {
	SharedPreferences sharedPreferences;
	CurrentUser       current_user;
	Context mContext;

	@Override
	public void init(){
		// TODO Auto-generated method stub
		setContentView(R.layout.tab_user);
		mContext = context;

	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		String network  = NetUtils.network_type(getApplicationContext());
		TextView  textView_network    = (TextView)findViewById(R.id.textView_current_network); 
		textView_network.setText("当前网络: " + network);
		
		try {
			initUserInfo();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initControls();
	}
	
	public void initUserInfo()  throws ParseException{
		//加载用户信息
		sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
		//Integer current_user_id      = sharedPreferences.getInt("current_user_id",-1);
		String current_user_name     = sharedPreferences.getString("current_user_name","");
		String current_user_email    = sharedPreferences.getString("current_user_email","");
		String current_user_area = sharedPreferences.getString("current_user_province","");
		String current_user_register = sharedPreferences.getString("current_user_register","");
		String current_user_gravatar = sharedPreferences.getString("current_user_gravatar","");
		long current_user_id = sharedPreferences.getLong("current_user_id",-1);

		String standard_date = ToolUtils.get_standard_date();
		String week_name = ToolUtils.get_week_name(standard_date);
		int week_number = ToolUtils.get_week_number(standard_date);
		//当前日期信息
		TextView  textView_current_date = (TextView)findViewById(R.id.textView_current_date); 	
		textView_current_date.setText("当前日期:"+standard_date+" "+week_name+" 第"+week_number+"周");
		//textView_current_date.setText(current_user_gravatar);

	        
		ImageView image_view = (ImageView)findViewById(R.id.imageView_current_user_gravatar_image);

		String picDirStr = Gravatar.gravatar_path(current_user_email);
		File picDir = new File(picDirStr); 
		if(picDir.exists()) {
    		Bitmap bitmap = NetUtils.getLoacalBitmap(picDirStr); //从本地取图片(在cdcard中获取)
    		image_view.setImageBitmap(bitmap); //设置Bitmap
        } else if(!ToolUtils.has_sdcard()){
        	Toast.makeText(TabUser.this, "存储卡已移除，头像图片读取失败", 0).show();
        } else {
        	Toast.makeText(TabUser.this, "头像图片不存在", 0).show();
        }
	    //login
		TextView  textView_user_name     = (TextView)findViewById(R.id.textView_current_user_name); 
		TextView  textView_user_email    = (TextView)findViewById(R.id.textView_current_user_email); 
		TextView  textView_user_register = (TextView)findViewById(R.id.textView_current_user_registeration_time); 
		TextView  textView_user_area     = (TextView)findViewById(R.id.textView_current_user_area); 
		textView_user_name.setText(current_user_name);
		textView_user_email.setText(current_user_email);
		textView_user_register.setText("注册日期:"+current_user_register);
		textView_user_area.setText(current_user_area);
	}
	public void initControls() {
		BigDecimal volue;

		sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
		long current_user_id = sharedPreferences.getLong("current_user_id", -1);
		current_user = CurrentUser.get_current_user(getApplication(),Integer.parseInt(String.valueOf(current_user_id)));
		
		ArrayList<ConsumeInfo> consumeinfos = current_user.consume_info_list();
		ConsumeInfo consumeInfo = new ConsumeInfo();
		
		TextView  consume_day_number = (TextView)findViewById(R.id.consume_day_number); 
		consumeInfo = consumeinfos.get(0);
		consume_day_number.setText((int)consumeInfo.get_value()+"天");
		
		TextView  consume_max_value_by_once = (TextView)findViewById(R.id.consume_max_value_by_once); 
		consumeInfo = consumeinfos.get(1);
	    volue = new BigDecimal(consumeInfo.get_value()).setScale(1, BigDecimal.ROUND_HALF_UP);
		consume_max_value_by_once.setText("￥"+volue);
		
		TextView  consume_max_value_by_day = (TextView)findViewById(R.id.consume_max_value_by_day); 
		consumeInfo = consumeinfos.get(2);
		volue = new BigDecimal(consumeInfo.get_value()).setScale(1, BigDecimal.ROUND_HALF_UP);
		consume_max_value_by_day.setText("￥"+volue);
		
		TextView  consume_accumulate_value_by_week = (TextView)findViewById(R.id.consume_accumulate_value_by_week); 
		consumeInfo = consumeinfos.get(3);
		volue = new BigDecimal(consumeInfo.get_value()).setScale(1, BigDecimal.ROUND_HALF_UP);
		consume_accumulate_value_by_week.setText("￥"+volue);
		
		TextView  consume_accumulate_value_by_month = (TextView)findViewById(R.id.consume_accumulate_value_by_month);
		consumeInfo = consumeinfos.get(4);
		volue = new BigDecimal(consumeInfo.get_value()).setScale(1, BigDecimal.ROUND_HALF_UP);
		consume_accumulate_value_by_month.setText("￥"+volue);
		
		TextView  consume_accumulate_value_by_year = (TextView)findViewById(R.id.consume_accumulate_value_by_year);
		consumeInfo = consumeinfos.get(5);
		volue = new BigDecimal(consumeInfo.get_value()).setScale(1, BigDecimal.ROUND_HALF_UP);
		consume_accumulate_value_by_year.setText("￥"+volue);
		
		TextView  consume_accumulate_value_by_all = (TextView)findViewById(R.id.consume_accumulate_value_by_all);
		consumeInfo = consumeinfos.get(6);
		volue = new BigDecimal(consumeInfo.get_value()).setScale(1, BigDecimal.ROUND_HALF_UP);
		consume_accumulate_value_by_all.setText("￥"+volue);
		
		ImageButton imageButton_refresh  = (ImageButton) findViewById(R.id.imageButton_user_info_refresh);
		imageButton_refresh.setOnClickListener(imageButton_user_info_refresh_listener);
	}		
	
	//同步本地数据至服务器
	Button.OnClickListener imageButton_user_info_refresh_listener = new Button.OnClickListener() {//创建监听对象  
		public void onClick(View v){  
			sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
			
	    	Toast.makeText(TabUser.this, "开始更新用户信息", 0).show();
	    	
			if (sharedPreferences.contains("current_user_token")
					&& !sharedPreferences.getString("current_user_token", "").equals("")) {
				String token = sharedPreferences.getString("current_user_token", "");
				Log.w("Token", token);
				
				try {
					NetUtils.get_user_friends_info(getApplicationContext(), token);
	                NetUtils.get_friend_records(getApplicationContext(), token);
					String [] ret_array = NetUtils.get_user_info(sharedPreferences,token, getApplicationContext());
					if(ret_array[0].equals("1")){
					    try {
							initUserInfo();
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Toast.makeText(TabUser.this, "更新成功", 0).show();
					} else {
				    	Toast.makeText(TabUser.this, "更新失败:"+ret_array[1], 0).show();
					}				
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    } else {
		    	Toast.makeText(TabUser.this, "配置信息不完善", 0).show();
		    }			
	  }
	};
}
