package us.solife.consumes.adapter;

import java.util.ArrayList;
import java.io.File;
import java.math.BigDecimal;

import us.solife.iconsumes.R;
import us.solife.consumes.TabAbout;
import us.solife.consumes.TabUser;
import us.solife.consumes.api.Gravatar;
import us.solife.consumes.db.UserTb;
import us.solife.consumes.entity.ConsumeInfo;
import us.solife.consumes.entity.UserInfo;
import us.solife.consumes.util.NetUtils;
import us.solife.consumes.util.ToolUtils;
import us.solife.consumes.util.UIHelper;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class ListViewFriendsConsumesAdapter extends BaseAdapter{
	ArrayList<ConsumeInfo> consume_infos;
	ArrayList<UserInfo> user_infos;
	private Context        context;

	public ListViewFriendsConsumesAdapter(ArrayList<ConsumeInfo> consume_infos,ArrayList<UserInfo> user_infos, Context context) {
		this.consume_infos = consume_infos;
		this.user_infos    = user_infos;
		this.context       = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return consume_infos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return consume_infos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ConsumeInfo consume_info = consume_infos.get(position);
		String remark,date;
		
		ViewHolder holder;
		if (convertView != null) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			holder        = new ViewHolder();
			convertView   = View.inflate(context, R.layout.tab_chart_item, null);

			holder.name   = (TextView) convertView.findViewById(R.id.user_name);
			holder.value  = (TextView) convertView.findViewById(R.id.value);
			holder.date   = (TextView) convertView.findViewById(R.id.created_at);
			holder.desc   = (TextView) convertView.findViewById(R.id.describtion);
			holder.sync_state   = (TextView) convertView.findViewById(R.id.sync_state);
			holder.gravatar   = (ImageView) convertView.findViewById(R.id.gravatar_image);
			
			convertView.setTag(holder);
		}			
		UserInfo user_info = new UserInfo();
		for(int i=0;i< user_infos.size();i++) {
			if(consume_info.get_user_id()==user_infos.get(i).get_user_id())
		      user_info = user_infos.get(i);
		}
		
		if(user_info.get_id()>0) {
			holder.name.setText(user_info.get_name());
			String picDirStr = Gravatar.gravatar_path(user_info.get_email());
			File picDir = new File(picDirStr);
	        if(picDir.exists()){
	    		Bitmap bitmap = NetUtils.get_loacal_bitmap(picDirStr); 
	    		holder.gravatar.setImageBitmap(bitmap); //设置Bitmap
	        }
		} else {
			holder.name.setText(consume_info.get_user_id()+"-"+consume_info.get_consume_id()+"-"+consume_info.get_sync()+"-"+consume_info.get_state());
		}

		//消费值四舍五入，保留一位小数
		BigDecimal volue = new BigDecimal(consume_info.get_value()).setScale(1, BigDecimal.ROUND_HALF_UP);	
		remark = consume_info.get_remark().toString();	
		//if(remark.length()>17)
		//	remark = remark.substring(0,18);
		date = consume_info.get_created_at().toString();
		if(date.length()>15) date = date.substring(0,16);
		String klass;
		switch(consume_info.get_klass()+"") {
			case "1": klass = "衣"; break;
			case "2": klass = "食"; break;
			case "3": klass = "住"; break;
			case "4": klass = "行"; break;
			case "5": klass = ""; break; 
			default: klass = ""; break;
		}
		holder.value.setText("￥"+volue);
		holder.date.setText(date);
		holder.desc.setTag(consume_info);//设置隐藏参数(实体类)
		//holder.desc.setText("￥"+volue + " - " + msg.toString().replace("\n","-")+"...");
		holder.desc.setText(remark.toString());
		holder.desc.setOnClickListener(figClickListener);
		
		String sync_sate = "^_^";
	    if(consume_info.get_sync() == (long)0)  sync_sate = "*_*";
	    holder.sync_state.setText(sync_sate+"  ");
		/*
		if(consumeInfo.getCreated_at().length()>=10){
			String week_name = ToolUtils.getWeekName(consumeInfo.getCreated_at());
			holder.item_week.setText(week_name);
		}
		*/
		return convertView;
	}

	class ViewHolder {
		private TextView head,name,date,desc, value, sync_state;
		private ImageView gravatar;
	}	
	
	private View.OnClickListener figClickListener = new View.OnClickListener(){

		@Override
		public void onClick(View v) {
			ConsumeInfo consume_info = (ConsumeInfo)v.getTag();
			Log.w("ListViewFriendsConsumeAAdapter",consume_info.to_string());
			UIHelper.FriendsConsumeItemDialog(v.getContext(), consume_info);
		}
	};

}
