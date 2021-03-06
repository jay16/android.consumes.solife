package us.solife.consumes;

import java.util.ArrayList;

import us.solife.iconsumes.R;
import us.solife.consumes.api.Gravatar;
import us.solife.consumes.db.UserTb;
import us.solife.consumes.entity.UserInfo;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TabAbout extends BaseActivity implements OnClickListener {
	SharedPreferences sharedPreferences;
	
	@Override
	public void init() throws NameNotFoundException { // TODO Auto-generated method stub
		setContentView(R.layout.tab_about);

		TextView  textView_main_header = (TextView)findViewById(R.id.textView_main_header);
		textView_main_header.setText("设置");

		Button btn_back = (Button)findViewById(R.id.menu_btn_back);
		btn_back.setVisibility(View.GONE);
		
		UserTb user_table = UserTb.get_user_tb(TabAbout.this);
		ArrayList<Integer> user_ids = user_table.get_unsync_user_list();
		//ArrayList<UserInfo> user_infos = user_table.get_user_list();
		String dd = "" + user_ids.size() + ">";
		for(int i=0;i< user_ids.size();i++) {
			//dd += user_infos.get(i).to_string() + "\n";
			dd += "-" + user_ids.get(i);
		}
		
		//TextView  textView1 = (TextView)findViewById(R.id.textView1);
		//textView1.setText(dd);
		
		RelativeLayout tab_about_info = (RelativeLayout)findViewById(R.id.tab_about_info);
		RelativeLayout tab_about_msg  = (RelativeLayout)findViewById(R.id.tab_about_msg);
		RelativeLayout tab_about_feedback = (RelativeLayout)findViewById(R.id.tab_about_feedback);
		RelativeLayout tab_about_solife  = (RelativeLayout)findViewById(R.id.tab_about_solife);
		TextView tab_about_version  = (TextView)findViewById(R.id.tab_about_version);
		TextView tab_about_client  = (TextView)findViewById(R.id.tab_about_client);
		
		final PackageManager pm = getApplicationContext().getPackageManager();
		PackageInfo packInfo = pm.getPackageInfo("us.solife.iconsumes", PackageManager.GET_ACTIVITIES);
		final String version = packInfo.versionName;
		tab_about_version.setText("版本: " + version);
		tab_about_client.setText("手机: " + Build.BRAND+"_"+Build.MODEL);
		tab_about_info.setOnClickListener(new View.OnClickListener(){
		     @Override
		     public void onClick(View v){
					Toast.makeText(getApplication(), "[个人信息]待完善", 0).show();
		     }
		 });
		tab_about_msg.setOnClickListener(new View.OnClickListener(){
		     @Override
		     public void onClick(View v){
					Toast.makeText(getApplication(), "[系统通知]待完善", 0).show();
		     }
		 });
		tab_about_feedback.setOnClickListener(new View.OnClickListener(){
		     @Override
		     public void onClick(View v){
					Toast.makeText(getApplication(), "[帮助与反馈]待完善", 0).show();
		     }
		 });
		tab_about_solife.setOnClickListener(new View.OnClickListener(){
		     @Override
		     public void onClick(View v){
					Toast.makeText(getApplication(), "[关于SOLife]待完善", 0).show();
		     }
		 });
	}

	@SuppressLint("ShowToast")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tab_about_info:
			Toast.makeText(getApplication(), "[个人信息]待完善", 0).show();
			break;
		case R.id.tab_about_msg:
			Toast.makeText(getApplication(), "[系统通知]待完善", 0).show();
			break;
		case R.id.tab_about_feedback:
			Toast.makeText(getApplication(), "[帮助与反馈]待完善", 0).show();
			break;
		case R.id.tab_about_solife:
			Toast.makeText(getApplication(), "[关于SOLife]待完善", 0).show();
			break;
	    default:
	    	break;
		}
		
	}  
	
	public void exit_application(View v) {  
    	this.finish();
    	Main.instance.finish();//关闭Main 这个Activity
    }
}