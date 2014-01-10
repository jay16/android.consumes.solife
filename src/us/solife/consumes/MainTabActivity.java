package us.solife.consumes;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import us.solife.consumes.db.ConsumeDao;

import com.yyx.mconsumes.R;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
//import android.widget.CompoundButton;
//import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toast;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Button;
//import android.app.AlertDialog;
//import android.content.DialogInterface;

public class MainTabActivity extends TabActivity implements OnClickListener {
	TabHost tabHost;
	RadioButton aBt, bBt, cBt, dBt, eBt;
	int index = 0;
	RadioButton[] buttons;
	int preIndex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		buttons = new RadioButton[5];
		
		tabHost = getTabHost();
		tabHost.addTab(tabHost.newTabSpec("TabChart").setIndicator("TabChart")
				.setContent(new Intent(this, TabChart.class)));
		tabHost.addTab(tabHost.newTabSpec("TabList").setIndicator("TabList")
				.setContent(new Intent(this, TabList.class)));
		tabHost.addTab(tabHost.newTabSpec("TabUser").setIndicator("TabUser")
				.setContent(new Intent(this, TabUser.class)));
		tabHost.addTab(tabHost.newTabSpec("TabConsume").setIndicator("TabConsume")
				.setContent(new Intent(this, TabConsume.class)));
		tabHost.addTab(tabHost.newTabSpec("TabAbout").setIndicator("TabAbout")
				.setContent(new Intent(this, TabAbout.class)));
		aBt = (RadioButton) findViewById(R.id.main_footbar_chart);
		bBt = (RadioButton) findViewById(R.id.main_footbar_list);
		cBt = (RadioButton) findViewById(R.id.main_footbar_user);
		dBt = (RadioButton) findViewById(R.id.main_footbar_add);
		eBt = (RadioButton) findViewById(R.id.main_footbar_more);
		buttons[0] = aBt;
		buttons[1] = bBt;
		buttons[2] = cBt;
		buttons[3] = dBt;
		buttons[4] = eBt;
		aBt.setOnClickListener(this);
		bBt.setOnClickListener(this);
		cBt.setOnClickListener(this);
		dBt.setOnClickListener(this);
		eBt.setOnClickListener(this);
		aBt.setChecked(true);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch (v.getId()) {
		case R.id.main_footbar_chart:

			index = 0;
			buttons[0].setChecked(true);
			buttons[1].setChecked(false);
			buttons[2].setChecked(false);
			buttons[3].setChecked(false);
			buttons[4].setChecked(false);
			tabHost.setCurrentTabByTag("TabChart");
			
			break;
		case R.id.main_footbar_list:

			index = 1;
			buttons[0].setChecked(false);
			buttons[1].setChecked(true);
			buttons[2].setChecked(false);
			buttons[3].setChecked(false);
			buttons[4].setChecked(false);
			tabHost.setCurrentTabByTag("TabList");

			break;
		case R.id.main_footbar_user:
			index = 2;
			buttons[0].setChecked(false);
			buttons[1].setChecked(false);
			buttons[2].setChecked(true);
			buttons[3].setChecked(false);
			buttons[4].setChecked(false);
			
			tabHost.setCurrentTabByTag("TabUser");

			break;
		case R.id.main_footbar_add:
			index = 3;
			buttons[0].setChecked(false);
			buttons[1].setChecked(false);
			buttons[2].setChecked(false);
			buttons[3].setChecked(true);
			buttons[4].setChecked(false);
			tabHost.setCurrentTabByTag("TabConsume");

			break;
		case R.id.main_footbar_more:
			index = 4;
			buttons[0].setChecked(false);
			buttons[1].setChecked(false);
			buttons[2].setChecked(false);
			buttons[3].setChecked(false);
			buttons[4].setChecked(true);
			tabHost.setCurrentTabByTag("TabAbout");

			break;
			

		default:
			break;
		}
	}


    
}
