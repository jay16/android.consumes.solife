package us.solife.consumes.adapter;

import java.util.ArrayList;
import java.math.BigDecimal;

import us.solife.consumes.ConsumeItem;
import us.solife.consumes.R;
import us.solife.consumes.TabList;
import us.solife.consumes.adapter.ListViewConsumeAdapter.ViewHolder;
import us.solife.consumes.entity.ConsumeInfo;


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

import us.solife.consumes.util.UIHelper;
import android.net.ParseException;
import android.widget.RelativeLayout;

/**
 * 显示消费记录明细Adapter类
 * @author jay (http://solife.us/resume)
 * @version 1.0
 * @created 2014-02-25
 */
public class ListViewConsumeItemAdapter extends BaseAdapter{
	ArrayList<ConsumeInfo> consumeInfos;
	private Context        context;

	/**
	 * 实例化Adapter
	 * @param context
	 * @param consumeInfos
	 */
	public ListViewConsumeItemAdapter(ArrayList<ConsumeInfo> consumeInfos, Context context) {
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

	/**
	 * ListView Item设置
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//自定义视图
		ViewHolder holder = null;
		if (convertView == null) {
			holder        = new ViewHolder();
			convertView   = View.inflate(context, R.layout.consume_item_listview, null);
			
			holder.created_at = (TextView) convertView.findViewById(R.id.consume_item_created_at);
			holder.msg  = (TextView) convertView.findViewById(R.id.consume_item_msg);
			holder.sync = (TextView) convertView.findViewById(R.id.consume_item_sync);
     
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		//取得消费记录数据
        ConsumeInfo consumeInfo = consumeInfos.get(position);
        
		holder.created_at.setText(consumeInfo.get_created_at().substring(10, 16));
		holder.msg.setText(consumeInfo.get_msg());
		holder.msg.setTag(consumeInfo);//设置隐藏参数(实体类)
		holder.msg.setOnLongClickListener(msgLongClickListener);
		
		//同步状态
		if(String.valueOf(consumeInfo.get_sync()).toString().equals("1")) {
		    holder.sync.setText("^_^");
		} else {
			holder.sync.setText("!_!");
		}
		
		return convertView;
	}

	class ViewHolder {
		private TextView  msg, created_at, sync;
	}
	
	private View.OnClickListener msgClickListener = new View.OnClickListener(){
		public void onClick(View v) {
		}
	};
	
	private View.OnLongClickListener msgLongClickListener = new View.OnLongClickListener(){

		@Override
		public boolean onLongClick(View v) {
			ConsumeInfo comment_info = (ConsumeInfo)v.getTag();
			UIHelper.showCommentInfoOptionDialog(v.getContext(), comment_info);
			return false;
		}
	};

}
