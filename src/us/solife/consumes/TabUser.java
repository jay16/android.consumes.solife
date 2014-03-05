package us.solife.consumes;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;

import android.content.Context;
//import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

//import org.apache.http.client.methods.HttpPost; 

import us.solife.consumes.R;
import us.solife.consumes.entity.ConsumeInfo;
import us.solife.consumes.entity.CurrentUser;
import us.solife.consumes.util.NetUtils;
import us.solife.consumes.util.ToolUtils;


import android.widget.Toast;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.ParseException;

public class TabUser extends BaseActivity {
	SharedPreferences sharedPreferences;
	CurrentUser       current_user;

	@Override
	public void init(){
		// TODO Auto-generated method stub
		setContentView(R.layout.tab_user);

	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

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

		String standard_date = ToolUtils.getStandardDate();
		String week_name = ToolUtils.getWeekName(standard_date);
		int week_number = ToolUtils.getWeekNumber(standard_date);
		//当前日期信息
		TextView  textView_current_date = (TextView)findViewById(R.id.textView_current_date); 	
		textView_current_date.setText("当前日期:"+standard_date+" "+week_name+" 第"+week_number+"周");
		//textView_current_date.setText(current_user_gravatar);

	        
		ImageView image_view = (ImageView)findViewById(R.id.imageView_current_user_gravatar_image);
		//String picDirStr = Environment.getExternalStorageDirectory().getAbsolutePath();
		String picDirStr = Environment.getExternalStorageDirectory()+File.separator;
		picDirStr += "solife"+File.separator+ current_user_email.replace("@","_")+".jpg";
		 
		//textView_current_date.setText(picDirStr);
		File picDir = new File(picDirStr);
        if(!picDir.exists()){
        	Toast.makeText(TabUser.this, "图片不存在", 0).show();
        } else {
    		Bitmap bitmap = NetUtils.getLoacalBitmap(picDirStr); //从本地取图片(在cdcard中获取)  //
    		image_view.setImageBitmap(bitmap); //设置Bitmap
        	Toast.makeText(TabUser.this, "图片存在", 0).show();
        }
		
	    //login
		TextView  textView_user_name     = (TextView)findViewById(R.id.textView_current_user_name); 
		TextView  textView_user_email    = (TextView)findViewById(R.id.textView_current_user_email); 
		TextView  textView_user_register = (TextView)findViewById(R.id.textView_current_user_registeration_time); 
		TextView  textView_user_area     = (TextView)findViewById(R.id.textView_current_user_area); 
		textView_user_name.setText(current_user_name+"["+current_user_id+"]");
		textView_user_email.setText(current_user_email);
		textView_user_register.setText("注册日期:"+current_user_register);
		textView_user_area.setText(current_user_area);
	}
	public void initControls() {
		BigDecimal volue;

		sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
		long current_user_id = sharedPreferences.getLong("current_user_id", -1);
		current_user = CurrentUser.getCurrentUser(getApplication(),Integer.parseInt(String.valueOf(current_user_id)));
		
		ArrayList<ConsumeInfo> consumeinfos = current_user.consume_info_list();
		ConsumeInfo consumeInfo = new ConsumeInfo();
		
		TextView  consume_day_number = (TextView)findViewById(R.id.consume_day_number); 
		consumeInfo = consumeinfos.get(0);
		consume_day_number.setText((int)consumeInfo.get_volue()+"天");
		
		TextView  consume_max_value_by_once = (TextView)findViewById(R.id.consume_max_value_by_once); 
		consumeInfo = consumeinfos.get(1);
	    volue = new BigDecimal(consumeInfo.get_volue()).setScale(1, BigDecimal.ROUND_HALF_UP);
		consume_max_value_by_once.setText("￥"+volue);
		
		TextView  consume_max_value_by_day = (TextView)findViewById(R.id.consume_max_value_by_day); 
		consumeInfo = consumeinfos.get(2);
		volue = new BigDecimal(consumeInfo.get_volue()).setScale(1, BigDecimal.ROUND_HALF_UP);
		consume_max_value_by_day.setText("￥"+volue);
		
		TextView  consume_accumulate_value_by_week = (TextView)findViewById(R.id.consume_accumulate_value_by_week); 
		consumeInfo = consumeinfos.get(3);
		volue = new BigDecimal(consumeInfo.get_volue()).setScale(1, BigDecimal.ROUND_HALF_UP);
		consume_accumulate_value_by_week.setText("￥"+volue);
		
		TextView  consume_accumulate_value_by_month = (TextView)findViewById(R.id.consume_accumulate_value_by_month);
		consumeInfo = consumeinfos.get(4);
		volue = new BigDecimal(consumeInfo.get_volue()).setScale(1, BigDecimal.ROUND_HALF_UP);
		consume_accumulate_value_by_month.setText("￥"+volue);
		
		TextView  consume_accumulate_value_by_year = (TextView)findViewById(R.id.consume_accumulate_value_by_year);
		consumeInfo = consumeinfos.get(5);
		volue = new BigDecimal(consumeInfo.get_volue()).setScale(1, BigDecimal.ROUND_HALF_UP);
		consume_accumulate_value_by_year.setText("￥"+volue);
		
		TextView  consume_accumulate_value_by_all = (TextView)findViewById(R.id.consume_accumulate_value_by_all);
		consumeInfo = consumeinfos.get(6);
		volue = new BigDecimal(consumeInfo.get_volue()).setScale(1, BigDecimal.ROUND_HALF_UP);
		consume_accumulate_value_by_all.setText("￥"+volue);
		
		ImageButton imageButton_refresh  = (ImageButton) findViewById(R.id.imageButton_user_info_refresh);
		imageButton_refresh.setOnClickListener(imageButton_user_info_refresh_listener);
	}		
	
	//同步本地数据至服务器
	Button.OnClickListener imageButton_user_info_refresh_listener = new Button.OnClickListener() {//创建监听对象  
		public void onClick(View v){  
			sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
			
	    	Toast.makeText(TabUser.this, "开始更新用户信息", 0).show();
	    	
			if (sharedPreferences.contains("current_user_email")
					&& !sharedPreferences.getString("current_user_email", "").equals("")) {
				String email = sharedPreferences.getString("current_user_email", "");
				
				String [] ret_array = NetUtils.get_user_info(sharedPreferences,email);
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
		    } else {
		    	Toast.makeText(TabUser.this, "配置信息不完善", 0).show();
		    }
	  }
	};
}
