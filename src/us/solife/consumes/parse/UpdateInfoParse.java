package us.solife.consumes.parse;

import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

import us.solife.consumes.entity.UpdateInfo;
import android.util.Log;

public class UpdateInfoParse extends BaseParse<HashMap<String, Object>> {

	private static UpdateInfoParse instance;
	public static UpdateInfoParse getInstance(){
		if(null == instance){
			instance = new UpdateInfoParse();
		}
		return instance;
	}
	
	@Override
	public HashMap<String, Object> parseJSON(String jsonStr) throws JSONException {

		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		if (jsonStr == null || jsonStr.length() == 0) {
			hashMap.put("result", false);
		} else { 
			//UpdateInfo update_info = (UpdateInfo) JSON.parse(jsonStr);
			JSONObject jsonObject = new JSONObject(jsonStr);
			UpdateInfo update_info = new UpdateInfo();
			update_info.set_version(jsonObject.getString("version"));
			update_info.set_url(jsonObject.getString("url"));
			update_info.set_apk_name(jsonObject.getString("apk_name"));
			update_info.set_description(jsonObject.getString("describtion"));

			Log.w("UpdateInfoParse","UpdateInfo:"+update_info.to_string());
			hashMap.put("result", true);
			hashMap.put("update_info", update_info);
		}

		return hashMap;
	}

}