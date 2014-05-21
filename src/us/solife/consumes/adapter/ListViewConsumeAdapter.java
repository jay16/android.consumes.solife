package us.solife.consumes.adapter;

import java.util.ArrayList;
import java.math.BigDecimal;

import us.solife.iconsumes.R;
import us.solife.consumes.entity.ConsumeInfo;
import us.solife.consumes.util.ToolUtils;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 显示消费记录Adapter类
 * @author jay (http://solife.us/resume)
 * @version 1.0
 * @created 2014-02-25
 */
public class ListViewConsumeAdapter extends BaseAdapter{
	ArrayList<ConsumeInfo> consumeInfos;
	private Context        context;

	/**
	 * 实例化Adapter
	 * @param context
	 * @param consumeInfos
	 */
	public ListViewConsumeAdapter(ArrayList<ConsumeInfo> consumeInfos, Context context) {
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
		// TODO Auto-generated method stub
					
		//自定义视图
		ViewHolder holder = null;
		
		if (convertView == null) {
			//获取list_item布局文件的视图
			convertView   = View.inflate(context, R.layout.tab_list_item, null);

			//获取控件对象集
			holder             = new ViewHolder();
			holder.item_value  = (TextView) convertView.findViewById(R.id.TextView_item_value);
			holder.item_date   = (TextView) convertView.findViewById(R.id.TextView_item_date);
			holder.item_week   = (TextView) convertView.findViewById(R.id.TextView_item_week);
			
			//设置控件集到convertView
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		//设置消费记录数据
		ConsumeInfo consumeInfo = consumeInfos.get(position);

		//消费值四舍五入，保留一位小数
		BigDecimal volue = new BigDecimal(consumeInfo.get_value()).setScale(1, BigDecimal.ROUND_HALF_UP);
		holder.item_value.setText(volue + "元");
		holder.item_value.setTag(consumeInfo);//设置隐藏参数(实体类)
		holder.item_date.setText(consumeInfo.get_created_at());
		
		//如果消费日期详细内容至少2014-02-12长度
		//可以解析消费日期所在周
		if(consumeInfo.get_created_at().length()>=10){
			String week_name = ToolUtils.getWeekName(consumeInfo.get_created_at());
			holder.item_week.setText(week_name);
		}

		return convertView;
	}

	class ViewHolder {
		private TextView item_date,item_value,item_week;
	}

}
