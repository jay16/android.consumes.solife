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
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListViewTagSelectAdapter extends BaseAdapter{
	ArrayList<TagInfo> tagInfos;
    Context            context;
    TextView           editTextTags;
    LinearLayout       linearLayoutTags;
    ConsumeInfo        recordInfo;
    ArrayList<String>  tagsChecked = new ArrayList<String>();

	public ListViewTagSelectAdapter(ConsumeInfo recordInfo, ArrayList<TagInfo> tagInfos, Context context, TextView editTextTags, LinearLayout linearLayoutTags) {
		this.tagInfos     = tagInfos;
		this.context      = context;
		this.editTextTags = editTextTags;
		this.linearLayoutTags = linearLayoutTags;
		this.recordInfo   = recordInfo;
	}
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return tagInfos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return tagInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final TagInfo tagInfo = tagInfos.get(position);

		//自定义视图
		ViewHolder holder = null;
		
		if (convertView != null) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			convertView   = View.inflate(context, R.layout.tag_list_view, null);
			holder        = new ViewHolder();
			holder.checkBox   = (CheckBox) convertView.findViewById(R.id.checkBox_label);
			holder.state   = (TextView) convertView.findViewById(R.id.textView_state);
			
			convertView.setTag(holder);
		}			

		holder.checkBox.setText(tagInfo.get_label());

		holder.checkBox.setTag(tagInfo);
		holder.state.setText(tagInfo.get_sync()==(long)0 ? "*_*" : "^_^");
   
		holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
	        @Override
	        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            	String label = tagInfo.get_label();
	            if(isChecked){
	            	Log.w("TagClick", label + "- checked");
	            	tagsChecked.add(label);
	            } else {
	            	Log.w("TagClick", label + "- unchecked");
	            	tagsChecked.remove(tagsChecked.indexOf(label));
	            }
	            Log.w("TagsChecked", tagsChecked.toString());
	            String checked_tags = "";
	            for(int i=0; i < tagsChecked.size(); i++) {
	            	if(i == 0) {
	            		checked_tags += tagsChecked.get(i);
	            	} else {
	            		checked_tags += "," + tagsChecked.get(i);
	            	}
	            }
	            linearLayoutTags.setVisibility(View.VISIBLE);
	            editTextTags.setText(checked_tags);
	        }
	    });
		if(recordInfo.get_tags_list() != null &&
		   recordInfo.get_tags_list().length() >= 0) {
			String[] tmpArr =recordInfo.get_tags_list().split(",");
			
			for (int i = 0 ; i <tmpArr.length ; i++ ) { 
			  if(tmpArr[i].equals(tagInfo.get_label()))
		        holder.checkBox.setChecked(true);
			}
		}
		return convertView;
	}

	class ViewHolder {
		private TextView state;
		private CheckBox checkBox;
	}	
	
	private View.OnClickListener figClickListener = new View.OnClickListener(){

		@Override
		public void onClick(View v) {
			TagInfo tag_info = (TagInfo)v.getTag();
			Log.w("TagClick",tag_info.to_string());
		}
	};
}
