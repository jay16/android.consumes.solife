package us.solife.consumes.adapter;

import java.util.ArrayList;

import us.solife.consumes.TabList;
import us.solife.consumes.entity.ConsumeInfo;

import com.yyx.mconsumes.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


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
			convertView   = View.inflate(context, R.layout.consume_list_item, null);
			
			holder.item_id   = (TextView) convertView.findViewById(R.id.item_id);
			holder.list_id   = (TextView) convertView.findViewById(R.id.list_id);
			holder.volue   = (TextView) convertView.findViewById(R.id.volue);
			holder.created_at   = (TextView) convertView.findViewById(R.id.created_at);
			//holder.msg     = (TextView) convertView.findViewById(R.id.msg);
			holder.sync    = (TextView) convertView.findViewById(R.id.textView_consume_sync);
			convertView.setTag(holder);
		}
		position = position+1;
		holder.item_id.setText(""+consumeInfo.getId());
		holder.item_id.setVisibility(View.INVISIBLE);
		holder.list_id.setText(""+position);
		holder.volue.setText(consumeInfo.getVolue() + "Ԫ");
		holder.created_at.setText(consumeInfo.getCreated_at().substring(0,10));
		holder.sync.setText(""+consumeInfo.getSync());

		return convertView;
	}

	class ViewHolder {
		private TextView volue, msg, created_at,sync,item_id,list_id;

	}

}
