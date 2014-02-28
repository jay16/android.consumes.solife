package us.solife.consumes;

import us.solife.consumes.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;




public class TabAbout extends BaseActivity {
	SharedPreferences sharedPreferences;
	
	@Override
	public void init() { // TODO Auto-generated method stub
		setContentView(R.layout.tab_about);

		TextView  textView_main_header = (TextView)findViewById(R.id.textView_main_header);
		textView_main_header.setText("For My Love - 小何");
		

		Button btn_back = (Button)findViewById(R.id.menu_btn_back);
		btn_back.setVisibility(View.GONE);
	}
	
	public void exit_application(View v) {  
    	this.finish();
    	MainTabActivity.instance.finish();//关闭Main 这个Activity
      }  
}