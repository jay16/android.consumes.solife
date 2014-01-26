package us.solife.consumes.adapter;

import java.util.ArrayList;
import java.math.BigDecimal;

import us.solife.consumes.TabList;
import us.solife.consumes.entity.ConsumeInfo;

import com.yyx.mconsumes.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import java.util.Calendar;
import java.text.SimpleDateFormat;
//import java.sql.Date;
import java.util.Date;
import android.net.ParseException;
import android.widget.RelativeLayout;

public class ConsumeItemListAdapter extends BaseAdapter{
	ArrayList<ConsumeInfo> consumeInfos;
	private Context        context;

	public ConsumeItemListAdapter(ArrayList<ConsumeInfo> consumeInfos, Context context) {
		this.consumeInfos = consumeInfos;
		this.context      = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return consumeInfos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return consumeInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ConsumeInfo consumeInfo = consumeInfos.get(position);
					
		ViewHolder holder;
		if (convertView != null) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			holder        = new ViewHolder();
			convertView   = View.inflate(context, R.layout.consume_item_listview, null);
			
			//holder.volue   = (TextView) convertView.findViewById(R.id.consume_item_created_value);
			holder.created_at   = (TextView) convertView.findViewById(R.id.consume_item_created_at);
			holder.msg   = (TextView) convertView.findViewById(R.id.consume_item_msg);
			holder.sync   = (TextView) convertView.findViewById(R.id.consume_item_sync);
     
			convertView.setTag(holder);
		}

		holder.created_at.setText(consumeInfo.getCreated_at());
		holder.msg.setText(consumeInfo.getMsg());
		if(String.valueOf(consumeInfo.getSync()).toString().equals("1")) {
		    holder.sync.setText("^_^");
		} else {
			holder.sync.setText("!_!");
		}
		/*	
		holder.msg.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
			}
		});
		*/
		return convertView;
	}

	class ViewHolder {
		private TextView  msg, created_at, sync;
	}

}
