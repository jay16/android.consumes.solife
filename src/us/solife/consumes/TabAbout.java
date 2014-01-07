package us.solife.consumes;

import android.content.Context;
import android.widget.TextView;

import com.yyx.mconsumes.R;


public class TabAbout extends BaseActivity {
	@Override
	public void init() { // TODO Auto-generated method stub
		setContentView(R.layout.tab_about);

		TextView  textView_main_header = (TextView)findViewById(R.id.textView_main_header);
		textView_main_header.setText("For My Love - Ð¡ºÎ");
	}
}