package us.solife.consumes.parse;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;

import us.solife.consumes.entity.UserInfo;
import android.util.Log;
import com.alibaba.fastjson.JSON;

public class UserListParse extends BaseParse<HashMap<String, Object>>{
	
	@Override
	public HashMap<String, Object> parseJSON(String jsonStr) throws JSONException {
		// TODO Auto-generated method stub
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		if (jsonStr == null || jsonStr.length() == 0) {
			hashMap.put("result", false);
		} else {
			ArrayList<UserInfo> user_infos = (ArrayList<UserInfo>) JSON.parseArray(jsonStr, UserInfo.class);
			Log.w("UserListParse","User Size:"+user_infos.size());

			//for(int i=0; i<consume_infos.size(); i++ ) {
			//	ConsumeInfo consume_info = consume_infos.get(i);
			//	Log.w("ConsumeListParse","ConsumeInfo:"+consume_info.get_msg());
			//}
			hashMap.put("user_infos", user_infos);
			hashMap.put("result", true);
		}

		return hashMap;
	}
}
