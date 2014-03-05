package us.solife.consumes;

import us.solife.consumes.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.Toast;

public class MainTabActivity extends TabActivity implements OnClickListener,OnItemClickListener {
	public static MainTabActivity instance = null;
	TabHost tabHost;
	RadioButton aBt, bBt, cBt, dBt, eBt;
	int index = 0;
	RadioButton[] buttons;
	int preIndex = 0;
	private static final int DIALOG_EXIT = 1;

	//����ǽ�xml�еĲ�����ʾ����Ļ�ϵĹؼ���
    private LayoutInflater inflater; 
    private View layout;	
    private LinearLayout mClose;
    private LinearLayout mCloseBtn;
    private PopupWindow menuWindow;
	private boolean menu_display = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		instance = this;
		
		
		buttons = new RadioButton[5];
		
		tabHost = getTabHost();
		tabHost.addTab(tabHost.newTabSpec("TabChart").setIndicator("TabChart")
				.setContent(new Intent(this, TabChart.class)));
		tabHost.addTab(tabHost.newTabSpec("TabList").setIndicator("TabList")
				.setContent(new Intent(this, TabList.class)));
		tabHost.addTab(tabHost.newTabSpec("TabUser").setIndicator("TabUser")
				.setContent(new Intent(this, TabUser.class)));
		tabHost.addTab(tabHost.newTabSpec("TabAbout").setIndicator("TabAbout")
				.setContent(new Intent(this, TabAbout.class)));
		setDefaultTab(1);
		//tabHost.addTab(tabHost.newTabSpec("TabConsume").setIndicator("TabConsume")
		//		.setContent(new Intent(this, TabConsume.class)));
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
		bBt.setChecked(true);
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
		case R.id.main_footbar_more:
			index = 3;
			buttons[0].setChecked(false);
			buttons[1].setChecked(false);
			buttons[2].setChecked(false);
			buttons[3].setChecked(true);
			buttons[4].setChecked(false);
			tabHost.setCurrentTabByTag("TabAbout");

			break;
		case R.id.main_footbar_add:
			index = 4;
			buttons[0].setChecked(false);
			buttons[1].setChecked(false);
			buttons[2].setChecked(false);
			buttons[3].setChecked(false);
			buttons[4].setChecked(true);
			/*
			tabHost.setCurrentTabByTag("TabConsume");
			*/
            //��ת���������Ѽ�¼����
			Intent intent;
			intent = new Intent(getApplicationContext(), ConsumeForm.class);
			intent.putExtra("action",  "create");
			intent.putExtra("row_id",  (long)-1);
			startActivity(intent);
			break;
			
		default:
			break;
		}
	}
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater infalter = getMenuInflater();
		infalter.inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	/**
	 * open menu
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final int itemId = item.getItemId();
		switch(itemId){
		case R.id.menu_update_version:
			new Thread(new CheckVersionTask(this)).start();
			break;
		case R.id.menu_exit:
			showDialog(DIALOG_EXIT);
			break;
			
		}
		return super.onOptionsItemSelected(item);
	}

    @Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_EXIT:
			return new AlertDialog.Builder(this)
			.setTitle(R.string.prompt_ts)
			.setMessage(R.string.ok_exit)
			.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					android.os.Process.killProcess(android.os.Process.myPid());
				}
			})
			.setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			})
			.create();
		}
		return null;
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		switch (position) {
		}
	}
	/**
	 * �˳���Home����
	 */
	@Override
	public void onBackPressed() {
		Toast.makeText(getApplication(),"back", 0).show();
	}

}
