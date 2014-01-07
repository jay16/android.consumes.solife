package us.solife.consumes;

import java.util.ArrayList;

import us.solife.consumes.adapter.ConsumeListAdapter;
import us.solife.consumes.db.ConsumeDao;
import us.solife.consumes.entity.ConsumeInfo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.yyx.mconsumes.R;

public class MainRecordActivity extends BaseActivity {

	ListView listView;
	int precursor;
	int index;
	SharedPreferences      preferences;
	ArrayList<ConsumeInfo> consumeInfos;

	@Override
	public void init() {
		// TODO Auto-generated method stub
		//设置显示框架
		setContentView(R.layout.tab_list);
		//消费列表显示控件
		listView = (ListView) findViewById(R.id.consumeListView);
		index = 0;
		//getSharedPreferences: 轻量级的存储类，主要是保存一些常用的配置比如窗口状态，
		//一般在Activity中重载窗口状态onSaveInstanceState保存一般使用SharedPreferences完成
		//第一个参数是存储时的名称，第二个参数则是文件的打开方式
		preferences = getSharedPreferences("config", Context.MODE_PRIVATE);
		boolean isDownLoad = preferences.getBoolean("isDownLoad", false);

		if (isDownLoad) {
			//consumeInfos = new ConsumeDao(getApplicationContext())
			//		.getAllRecords(getApplicationContext());

			if (consumeInfos != null && consumeInfos.size() != 0) {
				listView.setAdapter(new ConsumeListAdapter(consumeInfos,
						MainRecordActivity.this));
			}
		} else {
			// 该处可以重新获取数据 我就不写了
			//Toast是一种简易的消息提示框。
			//和Dialog不一样的是，它永远不会获得焦点，无法被点击。
			//Toast类的思想就是尽可能不引人注意，同时还向用户显示信息，希望他们看到
			Toast.makeText(MainRecordActivity.this, "没获取到数据！", 0).show();

		}

	}

	@Override
	public void netError() {
		// TODO Auto-generated method stub
		super.netError();
	}

}
