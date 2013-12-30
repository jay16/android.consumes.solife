package com.yyx.mconsumes;

import com.yyx.mconsumes.db.ConsumeDao;
import android.database.Cursor;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ConsumeItem extends BaseActivity {
	
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.consume_item);
		
		Cursor cursor;
		Float volue;
		String msg, created_at, updated_at;
		
		TextView textView_consume_volue = (TextView) findViewById(R.id.textView_consume_volue);
		TextView textView_consume_msg   = (TextView) findViewById(R.id.textView_consume_msg);
		TextView textView_consume_created_at = (TextView) findViewById(R.id.textView_consume_created_at);
		
		Intent intent = getIntent();
		Long row_id = intent.getLongExtra("row_id",(long)-1);
		
        ConsumeDao consumeDao = new ConsumeDao(ConsumeItem.this);

    	cursor = consumeDao.getRecordByRowId(row_id);
    	volue      = cursor.getFloat(cursor.getColumnIndex("volue"));
    	msg        = cursor.getString(cursor.getColumnIndex("msg"));
    	created_at  = cursor.getString(cursor.getColumnIndex("created_at"));

        textView_consume_volue.setText(volue.toString());
        textView_consume_msg.setText(msg);
        textView_consume_created_at.setText(created_at);
        
		Button button_back =(Button)findViewById(R.id.button_back); 
		button_back.setOnClickListener(button_back_listener); 
	}
	/* 创建consume submit监听对象  
     * */
	Button.OnClickListener button_back_listener = new Button.OnClickListener() {
		public void onClick(View v){
			Intent intent = new Intent(ConsumeItem.this,MainTabActivity.class);
			startActivity(intent);
		}
	};
}
