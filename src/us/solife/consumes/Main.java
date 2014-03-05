package us.solife.consumes;

import java.util.ArrayList;


import us.solife.consumes.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TabHost;
import android.widget.Toast;

public class Main extends TabActivity  {
	public static Main instance = null;
	private ImageView mTabImg;// 动画图片
	private ImageView mTab1,mTab2,mTab3,mTab4,mTab5;
	private int zero = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int one, two, three, four;//单个水平动画位移
	int index = 0;
	int preIndex = 0;
	private static final int DIALOG_EXIT = 1;
	TabHost tabHost;
	//这个是将xml中的布局显示在屏幕上的关键类
	
	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		instance = this;
		
    	tabHost = getTabHost();
    	tabHost.addTab(tabHost.newTabSpec("TabChart").setIndicator("TabChart")
    	.setContent(new Intent(this, TabChart.class)));
    	tabHost.addTab(tabHost.newTabSpec("TabList").setIndicator("TabList")
    	.setContent(new Intent(this, TabList.class)));
    	tabHost.addTab(tabHost.newTabSpec("TabUser").setIndicator("TabUser")
    	.setContent(new Intent(this, TabUser.class)));
    	tabHost.addTab(tabHost.newTabSpec("TabAbout").setIndicator("TabAbout")
    	.setContent(new Intent(this, TabAbout.class)));


        mTab1 = (ImageView) findViewById(R.id.main_footbar_chart);
        mTab2 = (ImageView) findViewById(R.id.main_footbar_list);
        mTab3 = (ImageView) findViewById(R.id.main_footbar_add);
        mTab4 = (ImageView) findViewById(R.id.main_footbar_user);
        mTab5 = (ImageView) findViewById(R.id.main_footbar_more);
        
        mTabImg = (ImageView) findViewById(R.id.img_tab_now);
        mTab1.setOnClickListener(new MyOnClickListener(0));
        mTab2.setOnClickListener(new MyOnClickListener(1));
        mTab3.setOnClickListener(new MyOnClickListener(2));
        mTab4.setOnClickListener(new MyOnClickListener(3));
        mTab5.setOnClickListener(new MyOnClickListener(4));
        Display currDisplay = getWindowManager().getDefaultDisplay();//获取屏幕当前分辨率
        int displayWidth = currDisplay.getWidth();
        int displayHeight = currDisplay.getHeight();
        one = displayWidth/5; //设置水平动画平移大小
        two = one*2;
        three = one*3;
        four = one*4;

	}

  /**
	 * 头标点击监听
	 */
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}
		@Override
		public void onClick(View v) {
			img_bg_animation(index);
		}
	};
    
	 /* 页卡切换监听(原作者:D.Winter)
	 */
	public void img_bg_animation(Integer arg0) {
		Animation animation = null;
		switch (arg0) {
		case 0:
			mTab1.setImageDrawable(getResources().getDrawable(R.drawable.widget_bar_chart));	
			tabHost.setCurrentTabByTag("TabChart");
			if (currIndex == 1) {
				animation = new TranslateAnimation(one, 0, 0, 0);
				mTab2.setImageDrawable(getResources().getDrawable(R.drawable.widget_bar_list));
			} else if (currIndex == 2) {
				animation = new TranslateAnimation(two, 0, 0, 0);
				mTab3.setImageDrawable(getResources().getDrawable(R.drawable.widget_bar_add));
			} else if (currIndex == 3) {
				animation = new TranslateAnimation(three, 0, 0, 0);
				mTab4.setImageDrawable(getResources().getDrawable(R.drawable.widget_bar_user));
			} else if (currIndex == 4) {
				animation = new TranslateAnimation(four, 0, 0, 0);
				mTab5.setImageDrawable(getResources().getDrawable(R.drawable.widget_bar_more));
			}
			break;
		case 1:
			mTab2.setImageDrawable(getResources().getDrawable(R.drawable.widget_bar_list));	
			tabHost.setCurrentTabByTag("TabList");
			if (currIndex == 0) {
				animation = new TranslateAnimation(zero, one, 0, 0);
				mTab1.setImageDrawable(getResources().getDrawable(R.drawable.widget_bar_chart));
			} else if (currIndex == 2) {
				animation = new TranslateAnimation(two, one, 0, 0);
				mTab3.setImageDrawable(getResources().getDrawable(R.drawable.widget_bar_add));
			} else if (currIndex == 3) {
				animation = new TranslateAnimation(three, one, 0, 0);
				mTab4.setImageDrawable(getResources().getDrawable(R.drawable.widget_bar_user));
			} else if (currIndex == 4) {
				animation = new TranslateAnimation(four, one, 0, 0);
				mTab5.setImageDrawable(getResources().getDrawable(R.drawable.widget_bar_more));
			}
			break;
		case 2:
			mTab3.setImageDrawable(getResources().getDrawable(R.drawable.widget_bar_add));
			Intent intent;
			intent = new Intent(getApplicationContext(), ConsumeForm.class);
			intent.putExtra("action", "create");
			intent.putExtra("row_id", (long)-1);	
			startActivity(intent);	
			if (currIndex == 0) {
				animation = new TranslateAnimation(zero, two, 0, 0);
				mTab1.setImageDrawable(getResources().getDrawable(R.drawable.widget_bar_chart));
			} else if (currIndex == 1) {
				animation = new TranslateAnimation(one, two, 0, 0);
				mTab2.setImageDrawable(getResources().getDrawable(R.drawable.widget_bar_list));
			} else if (currIndex == 3) {
				animation = new TranslateAnimation(three, two, 0, 0);
				mTab4.setImageDrawable(getResources().getDrawable(R.drawable.widget_bar_user));
			} else if (currIndex == 4) {
				animation = new TranslateAnimation(four, two, 0, 0);
				mTab5.setImageDrawable(getResources().getDrawable(R.drawable.widget_bar_more));
			}
			break;
		case 3:
			mTab4.setImageDrawable(getResources().getDrawable(R.drawable.widget_bar_user));
			tabHost.setCurrentTabByTag("TabUser");
			if (currIndex == 0) {
				animation = new TranslateAnimation(zero, three, 0, 0);
				mTab1.setImageDrawable(getResources().getDrawable(R.drawable.widget_bar_chart));
			} else if (currIndex == 1) {
				animation = new TranslateAnimation(one, three, 0, 0);
				mTab2.setImageDrawable(getResources().getDrawable(R.drawable.widget_bar_list));
			} else if (currIndex == 2) {
				animation = new TranslateAnimation(two, three, 0, 0);
				mTab3.setImageDrawable(getResources().getDrawable(R.drawable.widget_bar_add));
			} else if (currIndex == 4) {
				animation = new TranslateAnimation(four, three, 0, 0);
				mTab5.setImageDrawable(getResources().getDrawable(R.drawable.widget_bar_more));
			}
			break;
		case 4:
			mTab5.setImageDrawable(getResources().getDrawable(R.drawable.widget_bar_more));
			tabHost.setCurrentTabByTag("TabAbout");
			if (currIndex == 0) {
				animation = new TranslateAnimation(zero, four, 0, 0);
				mTab1.setImageDrawable(getResources().getDrawable(R.drawable.widget_bar_chart));
			} else if (currIndex == 1) {
				animation = new TranslateAnimation(one, four, 0, 0);
				mTab2.setImageDrawable(getResources().getDrawable(R.drawable.widget_bar_list));
			} else if (currIndex == 2) {
				animation = new TranslateAnimation(two, four, 0, 0);
				mTab3.setImageDrawable(getResources().getDrawable(R.drawable.widget_bar_add));
			} else if (currIndex == 3) {
				animation = new TranslateAnimation(three, four, 0, 0);
				mTab4.setImageDrawable(getResources().getDrawable(R.drawable.widget_bar_user));
			}
			break;
		}
		currIndex = arg0;
		animation.setFillAfter(true);// True:图片停在动画结束位置
		animation.setDuration(150);
		mTabImg.startAnimation(animation);
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
	 * 退出到Home窗口
	 */
	@Override
	public void onBackPressed() {
		Toast.makeText(getApplication(),"back", 0).show();
	}

}
