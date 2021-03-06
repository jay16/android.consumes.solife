package us.solife.consumes.parse;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import us.solife.consumes.entity.ConsumeInfo;
import android.util.Log;

import com.alibaba.fastjson.JSON;

public class ConsumeListParse extends BaseParse<HashMap<String, Object>> {

	@Override
	public HashMap<String, Object> parseJSON(String jsonStr) throws JSONException {
		// TODO Auto-generated method stub
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		if (jsonStr == null || jsonStr.length() == 0) {
			hashMap.put("result", false);
		} else {
			ArrayList<ConsumeInfo> consumeInfos = (ArrayList<ConsumeInfo>) JSON.parseArray(jsonStr, ConsumeInfo.class);
			Log.w("ConsumeListParse","ConsumeSize:"+consumeInfos.size());

			//for(int i=0; i<consume_infos.size(); i++ ) {
			//	ConsumeInfo consume_info = consume_infos.get(i);
			//	Log.w("ConsumeListParse","ConsumeInfo:"+consume_info.get_msg());
			//}
			hashMap.put("consumeInfos", consumeInfos);
			hashMap.put("result", true);
		}

		return hashMap;
	}

}
