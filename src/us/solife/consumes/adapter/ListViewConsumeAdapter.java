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
 * ��ʾ���Ѽ�¼Adapter��
 * @author jay (http://solife.us/resume)
 * @version 1.0
 * @created 2014-02-25
 */
public class ListViewConsumeAdapter extends BaseAdapter{
	ArrayList<ConsumeInfo> consumeInfos;
	private Context        context;

	/**
	 * ʵ����Adapter
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
	 * ListView Item����
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
					
		//�Զ�����ͼ
		ViewHolder holder = null;
		
		if (convertView == null) {
			//��ȡlist_item�����ļ�����ͼ
			convertView   = View.inflate(context, R.layout.tab_list_item, null);

			//��ȡ�ؼ�����
			holder             = new ViewHolder();
			holder.item_value  = (TextView) convertView.findViewById(R.id.TextView_item_value);
			holder.item_date   = (TextView) convertView.findViewById(R.id.TextView_item_date);
			holder.item_week   = (TextView) convertView.findViewById(R.id.TextView_item_week);
			
			//���ÿؼ�����convertView
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		//�������Ѽ�¼����
		ConsumeInfo consumeInfo = consumeInfos.get(position);

		//����ֵ�������룬����һλС��
		BigDecimal volue = new BigDecimal(consumeInfo.get_value()).setScale(1, BigDecimal.ROUND_HALF_UP);
		holder.item_value.setText(volue + "Ԫ");
		holder.item_value.setTag(consumeInfo);//�������ز���(ʵ����)
		holder.item_date.setText(consumeInfo.get_created_at());
		
		//�������������ϸ��������2014-02-12����
		//���Խ�����������������
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
