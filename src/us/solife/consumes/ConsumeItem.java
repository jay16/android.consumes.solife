package us.solife.consumes;

import us.solife.consumes.db.ConsumeDao;

import com.yyx.mconsumes.R;
import android.database.Cursor;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ConsumeItem extends BaseActivity {
	String from_page;
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.consume_item);

		Cursor cursor;
		Float volue;
		String msg, created_at, updated_at, sync;

		TextView textView_consume_volue = (TextView) findViewById(R.id.textView_consume_volue);
		TextView textView_consume_msg = (TextView) findViewById(R.id.textView_consume_msg);
		TextView textView_consume_created_at = (TextView) findViewById(R.id.textView_consume_created_at);

		Intent intent = getIntent();
		Long row_id = intent.getLongExtra("row_id", (long) -1);
	    from_page = intent.getStringExtra("from_page");

		ConsumeDao consumeDao = ConsumeDao.getConsumeDao(getApplicationContext());
        
		if(row_id != (long)-1) {
			cursor = consumeDao.getRecordByRowId(row_id);
			volue  = cursor.getFloat(cursor.getColumnIndex("volue"));
			msg    = cursor.getString(cursor.getColumnIndex("msg"));
			created_at = cursor.getString(cursor.getColumnIndex("created_at"));
			sync       = cursor.getString(cursor.getColumnIndex("sync"));
			textView_consume_volue.setText(volue.toString());
			textView_consume_msg.setText(msg);
			textView_consume_created_at.setText(created_at);
		} else {
			textView_consume_msg.setText("Fail with row id:"+row_id);
		}
		

		Button button_back = (Button) findViewById(R.id.button_back);
		button_back.setOnClickListener(button_back_listener);
	}

	/*
	 * 创建consume submit监听对象
	 */
	Button.OnClickListener button_back_listener = new Button.OnClickListener() {
		public void onClick(View v) {
			Intent intent;
			if(from_page.equals("TabList")){
		      intent = new Intent(ConsumeItem.this, TabList.class);
			} else if (from_page.equals("TabConsume")){
				intent = new Intent(ConsumeItem.this, MainTabActivity.class);
			} else {
				intent = new Intent(ConsumeItem.this, MainTabActivity.class);
			}
				
			startActivity(intent);
		}
	};
	
}
