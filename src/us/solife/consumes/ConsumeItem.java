package us.solife.consumes;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import us.solife.consumes.adapter.ConsumeListAdapter;
import us.solife.consumes.db.ConsumeDao;
import us.solife.consumes.entity.ConsumeInfo;

import com.yyx.mconsumes.R;
import android.database.Cursor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ParseException;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import us.solife.consumes.db.ConsumeDao;
import us.solife.consumes.entity.ConsumeInfo;
import us.solife.consumes.util.ToolUtils;
import us.solife.consumes.adapter.ConsumeItemListAdapter;

public class ConsumeItem extends BaseActivity {
	ListView listView;
	ConsumeDao             consumeDao;
	ArrayList<ConsumeInfo> consumeInfos;
	SharedPreferences sharedPreferences;
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.consume_item);
		TextView  textView_main_header = (TextView)findViewById(R.id.textView_main_header);
		textView_main_header.setText("消费明细");
		
		Cursor cursor;
		Float volue = (float)0;
		
		Intent intent = getIntent();
	    String day = intent.getStringExtra("created_at");

		sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
		long current_user_id = sharedPreferences.getLong("current_user_id", -1);
		consumeDao = ConsumeDao.getConsumeDao(ConsumeItem.this,current_user_id);
		consumeInfos = consumeDao.getDetailRecords(day);
		
		for(int i=0; i<consumeInfos.size(); i++ ) {
			ConsumeInfo  consumeInfo = consumeInfos.get(i);
			volue = volue + (float)consumeInfo.getVolue();
		}
		
		TextView consume_item_value = (TextView) findViewById(R.id.consume_item_value);
		TextView consume_item_created_at = (TextView) findViewById(R.id.consume_item_created_at);
		TextView consume_item_created_at_week = (TextView) findViewById(R.id.consume_item_created_at_week);
		
		//消费值四舍五入，保留一位小数
		BigDecimal value = new BigDecimal(volue).setScale(1, BigDecimal.ROUND_HALF_UP);
		consume_item_value.setText(value+"元");
		consume_item_created_at.setText(day);
		
		String week = ToolUtils.getWeekName(day);
		consume_item_created_at_week.setText(week);
		
		//显示明细
		setViewList(day);
		
		Button button_back = (Button) findViewById(R.id.btn_back);
		button_back.setOnClickListener(button_back_listener);
	}

	/*
	 * 创建consume submit监听对象
	 */
	Button.OnClickListener button_back_listener = new Button.OnClickListener() {
		public void onClick(View v) {
			
			startActivity(new Intent(ConsumeItem.this, MainTabActivity.class));
			//ConsumeItem.this.finish();

		}
	};
	public void setViewList(String day) {
		listView = (ListView) findViewById(R.id.consume_item_list_view);
		consumeInfos = consumeDao.getDetailRecords(day);
		
		listView.setAdapter(new ConsumeItemListAdapter(consumeInfos,ConsumeItem.this));
		listView.setClickable(false);

		listView.invalidate();
	}
	
	public void consume_item_edit(View v) {
		TextView consume_item_msg = (TextView) v.findViewById(R.id.consume_item_msg);
		
		Toast.makeText(ConsumeItem.this, consume_item_msg.getText(), 0).show();
	}
	
}
