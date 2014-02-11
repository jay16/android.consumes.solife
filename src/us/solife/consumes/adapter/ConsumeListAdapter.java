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


public class ConsumeListAdapter extends BaseAdapter{
	ArrayList<ConsumeInfo> consumeInfos;
	private Context        context;

	public ConsumeListAdapter(ArrayList<ConsumeInfo> consumeInfos, Context context) {
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
			convertView   = View.inflate(context, R.layout.tab_list_item, null);
			
			holder.item_value   = (TextView) convertView.findViewById(R.id.TextView_item_value);
			holder.item_date   = (TextView) convertView.findViewById(R.id.TextView_item_date);
			holder.item_week   = (TextView) convertView.findViewById(R.id.TextView_item_week);
			
			convertView.setTag(holder);
		}

		//消费值四舍五入，保留一位小数
		BigDecimal volue = new BigDecimal(consumeInfo.getVolue()).setScale(1, BigDecimal.ROUND_HALF_UP);
		holder.item_value.setText(volue + "元");
		holder.item_date.setText(consumeInfo.getCreated_at());
		
		String week_name = ToolUtils.getWeekName(consumeInfo.getCreated_at());
		holder.item_week.setText(week_name);

		return convertView;
	}

	class ViewHolder {
		private TextView item_date,item_value,item_week;
	}

}
