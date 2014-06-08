package us.solife.consumes.adapter;

import java.util.ArrayList;

import us.solife.consumes.entity.TagInfo;
import us.solife.iconsumes.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewTagSelectAdapter extends BaseAdapter{
	ArrayList<TagInfo> tag_infos;
	private Context    context;

	public ListViewTagSelectAdapter(ArrayList<TagInfo> tag_infos, Context context) {
		this.tag_infos = tag_infos;
		this.context   = context;
	}
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return tag_infos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return tag_infos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TagInfo tag_info = tag_infos.get(position);

		//自定义视图
		ViewHolder holder = null;
		
		if (convertView != null) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			convertView   = View.inflate(context, R.layout.tag_list_view, null);
			holder        = new ViewHolder();
			holder.label   = (TextView) convertView.findViewById(R.id.checkBox_label);
			
			convertView.setTag(holder);
		}			

		holder.label.setText(tag_info.get_label());
		holder.label.setTag(tag_info);

		return convertView;
	}

	class ViewHolder {
		private TextView label;
	}	
}
