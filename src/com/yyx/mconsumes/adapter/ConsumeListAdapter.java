package com.yyx.mconsumes.adapter;

import java.util.ArrayList;

import com.yyx.mconsumes.R;
import com.yyx.mconsumes.entity.ConsumeInfo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ConsumeListAdapter extends BaseAdapter {
	ArrayList<ConsumeInfo> consumeInfos;
	private Context context;

	public ConsumeListAdapter(ArrayList<ConsumeInfo> consumeInfos, Context context) {
		this.consumeInfos = consumeInfos;
		this.context = context;
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
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.consume_list_item, null);
			holder.amount = (TextView) convertView.findViewById(R.id.amount);
			holder.date = (TextView) convertView.findViewById(R.id.date);
			holder.remark = (TextView) convertView.findViewById(R.id.remark);
			convertView.setTag(holder);
		}
		holder.amount.setText("消费金额：" + consumeInfo.getVolue() + "元");
		holder.date.setText("日期：" + consumeInfo.getCreated_at());
		holder.remark.setText("备注：" + consumeInfo.getMsg());

		return convertView;
	}

	class ViewHolder {
		private TextView date, amount, remark;

	}

}
