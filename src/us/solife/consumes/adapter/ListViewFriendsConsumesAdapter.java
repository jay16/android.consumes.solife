package us.solife.consumes.adapter;

import java.util.ArrayList;
import java.math.BigDecimal;
import us.solife.consumes.entity.ConsumeInfo;
import us.solife.consumes.util.ToolUtils;
import com.yyx.mconsumes.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class ListViewFriendsConsumesAdapter extends BaseAdapter{
	ArrayList<ConsumeInfo> consumeInfos;
	private Context        context;

	public ListViewFriendsConsumesAdapter(ArrayList<ConsumeInfo> consumeInfos, Context context) {
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
		String msg,date;
		
		ViewHolder holder;
		if (convertView != null) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			holder        = new ViewHolder();
			convertView   = View.inflate(context, R.layout.tab_chart_item, null);
			
			//holder.head   = (TextView) convertView.findViewById(R.id.gravatar_image);
			holder.name   = (TextView) convertView.findViewById(R.id.user_name);
			holder.date   = (TextView) convertView.findViewById(R.id.created_at);
			holder.desc   = (TextView) convertView.findViewById(R.id.describtion);
			
			convertView.setTag(holder);
		}
		//消费值四舍五入，保留一位小数
		BigDecimal volue = new BigDecimal(consumeInfo.getVolue()).setScale(1, BigDecimal.ROUND_HALF_UP);
		msg = consumeInfo.getMsg().toString();
		if(msg.length()>17)
			msg = msg.substring(0,18);
		date = consumeInfo.getCreated_at().toString();
		if(date.length()>15)
			date = date.substring(0,16);
			
		holder.desc.setText("￥"+volue + " - " + msg.toString().replace("\n","-")+"...");
		holder.date.setText(date);
		//holder.name.setText(consumeInfo.getUserName());
		
		/*
		if(consumeInfo.getCreated_at().length()>=10){
			String week_name = ToolUtils.getWeekName(consumeInfo.getCreated_at());
			holder.item_week.setText(week_name);
		}
		*/
		return convertView;
	}

	class ViewHolder {
		private TextView head,name,date,desc;
	}

}
