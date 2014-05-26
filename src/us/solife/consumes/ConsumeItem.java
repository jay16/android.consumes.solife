package us.solife.consumes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import us.solife.iconsumes.R;
import us.solife.consumes.entity.ConsumeInfo;
import us.solife.consumes.entity.CurrentUser;

import android.database.Cursor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ParseException;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import us.solife.consumes.entity.ConsumeInfo;
import us.solife.consumes.util.ToolUtils;
import us.solife.consumes.util.UIHelper;
import us.solife.consumes.adapter.ListViewConsumeItemAdapter;

/**
 * 消费记录明细
 * @author jay (http://solife.us/resume)
 * @version 1.0
 * @created 2014-02-25
 */
public class ConsumeItem extends BaseActivity {
	ListView  listView;
	ConsumeInfo            consume_info;
	CurrentUser            current_user;
	ArrayList<ConsumeInfo> consume_infos;
	SharedPreferences      shared_preferences;
	Spinner                spinner;
	private Button mBack;
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.consume_item);
		TextView  textView_main_header = (TextView)findViewById(R.id.textView_main_header);
		textView_main_header.setText("消费明细");
		
		Float volue = (float)0;
		
		Intent intent = getIntent();
	    String day = intent.getStringExtra("created_at");

		shared_preferences = getSharedPreferences("config", Context.MODE_PRIVATE);
		long current_user_id = shared_preferences.getLong("current_user_id", -1);
		current_user = CurrentUser.get_current_user(ConsumeItem.this,Integer.parseInt(String.valueOf(current_user_id)));
		consume_infos = current_user.get_consume_items_with_date(day);
		
		for(int i=0; i<consume_infos.size(); i++ ) {
			consume_info = consume_infos.get(i);
			volue = volue + (float)consume_info.get_value();
		}
		
		TextView consume_item_value = (TextView) findViewById(R.id.consume_item_value);
		TextView consume_item_created_at = (TextView) findViewById(R.id.consume_item_created_at);
		TextView consume_item_created_at_week = (TextView) findViewById(R.id.consume_item_created_at_week);
		
		//消费值四舍五入，保留一位小数
		BigDecimal value = new BigDecimal(volue).setScale(1, BigDecimal.ROUND_HALF_UP);
		consume_item_value.setText(value+"元");
		consume_item_created_at.setText(day);
		
		String week = ToolUtils.get_week_name(day);
		consume_item_created_at_week.setText(week);
		
		//显示明细
		init_view_list(day);
		
		mBack = (Button) findViewById(R.id.menu_btn_back);
    	mBack.setOnClickListener(UIHelper.finish(this));
		//button_back.setOnClickListener(button_back_listener);
	}

	/**
	 * 初始化视图控件
	 * @param day
	 */
	public void init_view_list(String day) {
		consume_infos = current_user.get_consume_items_with_date(day);
		
		listView = (ListView) findViewById(R.id.consume_item_list_view);
		listView.setAdapter(new ListViewConsumeItemAdapter(consume_infos,ConsumeItem.this));
		//listView.setClickable(true);

	}
	
	/*
	 * 创建consume submit监听对象
	 */
	Button.OnClickListener button_back_listener = new Button.OnClickListener() {
		public void onClick(View v) {
			
			startActivity(new Intent(ConsumeItem.this, Main.class));
			//ConsumeItem.this.finish();

		}
	};
	
	/**
	 * 编辑/删除消费记录
	 * 点击消费明细时响应
	 * @param view
	 */
	public void consume_item_onclick(View view) {
		//判断是否是TextView
		if(view instanceof TextView){
			consume_info = (ConsumeInfo)view.getTag();
		} else {
			TextView tv = (TextView)view.findViewById(R.id.consume_item_msg);
			consume_info = (ConsumeInfo)tv.getTag();
		}
		if(consume_info == null) return;

        //提示消费内容
		Toast.makeText(ConsumeItem.this, "长按可编辑[￥"+consume_info.get_value()+"]", 0).show();
		//TextView consume_item_msg = (TextView) v.findViewById(R.id.consume_item_msg);
		//Toast.makeText(ConsumeItem.this, consume_item_msg.getText(), 0).show();
	}

	@Override
	public void onBackPressed() {
		this.finish();
		Intent intent;
		intent = new Intent(getApplicationContext(), Main.class);
		intent.putExtra("currIndex", 1);
		startActivity(intent);	
	}
	
}
