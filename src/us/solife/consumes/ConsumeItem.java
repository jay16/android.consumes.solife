package us.solife.consumes;

import java.util.ArrayList;

import us.solife.consumes.db.ConsumeDao;
import us.solife.consumes.entity.ConsumeInfo;

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
	ArrayList<ConsumeInfo> consumeInfos;
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.consume_item);

		Cursor cursor;
		Float volue = (float)0;
		String msg = "";
		String created_at = "";
		String updated_at = "";
		Long sync;

		TextView textView_row_id = (TextView) findViewById(R.id.textView_row_id);
		TextView textView_consume_volue = (TextView) findViewById(R.id.textView_consume_volue);
		TextView textView_consume_msg = (TextView) findViewById(R.id.textView_consume_msg);
		TextView textView_consume_created_at = (TextView) findViewById(R.id.textView_consume_created_at);
		TextView textView_consume_sync = (TextView) findViewById(R.id.textView_consume_sync);

		Intent intent = getIntent();
	    from_page = intent.getStringExtra("from_page");
		Long row_id = intent.getLongExtra("row_id", (long) -1);
	    String day = intent.getStringExtra("created_at");

		ConsumeDao consumeDao = ConsumeDao.getConsumeDao(getApplicationContext());
        
		if(from_page.equals("TabConsume")){
	      if(row_id != (long)-1) {
				cursor = consumeDao.getRecordByRowId(row_id);
				volue  = cursor.getFloat(cursor.getColumnIndex("volue"));
				msg    = cursor.getString(cursor.getColumnIndex("msg"));
				created_at = cursor.getString(cursor.getColumnIndex("created_at"));
				sync       = cursor.getLong(cursor.getColumnIndex("sync"));
				textView_row_id.setText(""+row_id);
				textView_consume_volue.setText(volue.toString());
				textView_consume_msg.setText(msg);
				textView_consume_created_at.setText(created_at);
				if(sync == 1){
					textView_consume_sync.setText("已同步");
				} else {
					textView_consume_sync.setText("本地保存");
				}
			} else {
				textView_consume_msg.setText("Fail with row id:"+row_id);
			}
		} else if(from_page.equals("TabList")){
			consumeInfos = consumeDao.getDayDetailRecords(day);
			ConsumeInfo  consumeInfo;
			for(int i=0; i<consumeInfos.size(); i++ ) {
				consumeInfo = consumeInfos.get(i);
				volue = volue + (float)consumeInfo.getVolue();
				if(msg.length()==0) {
					msg = consumeInfo.getMsg();
				} else {
					msg = msg + "\n----------------\n" + consumeInfo.getMsg();
				}
			}
			textView_consume_volue.setText(volue.toString());
			textView_consume_msg.setText(msg);
			textView_consume_created_at.setText(created_at);
		}



		Button button_back = (Button) findViewById(R.id.button_back);
		button_back.setOnClickListener(button_back_listener);
	}

	/*
	 * 创建consume submit监听对象
	 */
	Button.OnClickListener button_back_listener = new Button.OnClickListener() {
		public void onClick(View v) {
		
			if (from_page.equals("TabConsume")) {
				startActivity(new Intent(ConsumeItem.this, MainTabActivity.class));
			} else {
				ConsumeItem.this.finish();
			}
		}
	};
	
}
