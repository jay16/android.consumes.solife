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
		//������ʾ���
		setContentView(R.layout.tab_list);
		//�����б���ʾ�ؼ�
		listView = (ListView) findViewById(R.id.consumeListView);
		index = 0;
		//getSharedPreferences: �������Ĵ洢�࣬��Ҫ�Ǳ���һЩ���õ����ñ��細��״̬��
		//һ����Activity�����ش���״̬onSaveInstanceState����һ��ʹ��SharedPreferences���
		//��һ�������Ǵ洢ʱ�����ƣ��ڶ������������ļ��Ĵ򿪷�ʽ
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
			// �ô��������»�ȡ���� �ҾͲ�д��
			//Toast��һ�ּ��׵���Ϣ��ʾ��
			//��Dialog��һ�����ǣ�����Զ�����ý��㣬�޷��������
			//Toast���˼����Ǿ����ܲ�����ע�⣬ͬʱ�����û���ʾ��Ϣ��ϣ�����ǿ���
			Toast.makeText(MainRecordActivity.this, "û��ȡ�����ݣ�", 0).show();

		}

	}

	@Override
	public void netError() {
		// TODO Auto-generated method stub
		super.netError();
	}

}
