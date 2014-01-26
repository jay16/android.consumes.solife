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
import us.solife.consumes.adapter.ConsumeItemListAdapter;

public class ConsumeItem extends BaseActivity {
	ListView listView;
	ConsumeDao             consumeDao;
	ArrayList<ConsumeInfo> consumeInfos;
	String from_page;
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.consume_item);

		Cursor cursor;
		Float volue = (float)0;
		
		Intent intent = getIntent();
	    String day = intent.getStringExtra("created_at");

		consumeDao = ConsumeDao.getConsumeDao(getApplicationContext());

		consumeInfos = consumeDao.getDayDetailRecords(day);
		ConsumeInfo  consumeInfo;
		for(int i=0; i<consumeInfos.size(); i++ ) {
			consumeInfo = consumeInfos.get(i);
			volue = volue + (float)consumeInfo.getVolue();

		}
		TextView consume_item_value = (TextView) findViewById(R.id.consume_item_value);
		TextView consume_item_created_at = (TextView) findViewById(R.id.consume_item_created_at);
		
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd");
		String week = "F";
		try {
		    Date date = (Date)sdf.parse(day);
		    Calendar calendar = Calendar.getInstance();
		    calendar.setTime(date);  
		    int week_index = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		    String[] weeks = {"日","一","二","三","四","五","六"};
		    week =  weeks[week_index];
		} catch (ParseException e) {
			e.printStackTrace();
		}catch(java.text.ParseException e){
			e.printStackTrace();
		}

		//消费值四舍五入，保留一位小数
		BigDecimal value = new BigDecimal(volue).setScale(1, BigDecimal.ROUND_HALF_UP);
		consume_item_value.setText(value+"元");
		consume_item_created_at.setText(day+" 周"+week);
		
		//显示明细
		setViewList(day);
		
		Button button_back = (Button) findViewById(R.id.button_back);
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
		consumeInfos = consumeDao.getDayDetailRecords(day);
		
		listView.setAdapter(new ConsumeItemListAdapter(consumeInfos,ConsumeItem.this));
		listView.setClickable(false);

		listView.invalidate();
	}
	
	public void consume_item_edit(View v) {
		TextView consume_item_created_at = (TextView) v.findViewById(R.id.consume_item_created_at);
		
		Toast.makeText(ConsumeItem.this, consume_item_created_at.getText(), 0).show();
	}
}
