package us.solife.consumes.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import us.solife.consumes.entity.ConsumeInfo;
import us.solife.consumes.entity.TagInfo;
import us.solife.consumes.util.StringUtils;
import us.solife.consumes.util.UIHelper;
import us.solife.iconsumes.R;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

public class ListViewTagSelectAdapter extends BaseAdapter{
	ArrayList<TagInfo> tag_infos;
    Context            context;
    TextView           editText_tags;
    ArrayList<String>  tags_checked = new ArrayList<String>();

	public ListViewTagSelectAdapter(ArrayList<TagInfo> tag_infos, Context context, TextView editText_tags) {
		this.tag_infos = tag_infos;
		this.context   = context;
		this.editText_tags = editText_tags;
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
		final TagInfo tag_info = tag_infos.get(position);

		//自定义视图
		ViewHolder holder = null;
		
		if (convertView != null) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			convertView   = View.inflate(context, R.layout.tag_list_view, null);
			holder        = new ViewHolder();
			holder.label   = (CheckBox) convertView.findViewById(R.id.checkBox_label);
			holder.state   = (TextView) convertView.findViewById(R.id.textView_state);
			
			convertView.setTag(holder);
		}			

		holder.label.setText(tag_info.get_label());
		holder.label.setTag(tag_info);
		holder.state.setText(tag_info.get_sync()==(long)0 ? "*_*" : "^_^");
   
		holder.label.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
	        @Override
	        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            	String label = tag_info.get_label();
	            if(isChecked){
	            	Log.w("TagClick", label + "- checked");
	            	tags_checked.add(label);
	            } else {
	            	Log.w("TagClick", label + "- unchecked");
	            	tags_checked.remove(tags_checked.indexOf(label));
	            }
	            Log.w("TagsChecked", tags_checked.toString());
	            String checked_tags = "";
	            for(int i=0; i < tags_checked.size(); i++) {
	            	if(i == 0) {
	            		checked_tags += tags_checked.get(i);
	            	} else {
	            		checked_tags += "," + tags_checked.get(i);
	            	}
	            }
	            editText_tags.setText(checked_tags);
	        }
	    });

		
		return convertView;
	}

	class ViewHolder {
		private TextView state;
		private CheckBox label;
	}	
	
	private View.OnClickListener figClickListener = new View.OnClickListener(){

		@Override
		public void onClick(View v) {
			TagInfo tag_info = (TagInfo)v.getTag();
			Log.w("TagClick",tag_info.to_string());
		}
	};
}
