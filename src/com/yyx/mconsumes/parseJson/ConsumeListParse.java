package com.yyx.mconsumes.parseJson;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.yyx.mconsumes.entity.ConsumeInfo;

public class ConsumeListParse extends BaseParse<HashMap<String, Object>> {

	@Override
	public HashMap<String, Object> parseJSON(String jsonStr)
	
	throws JSONException {
		// TODO Auto-generated method stub
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		if (jsonStr == null || jsonStr.length() == 0) {
			hashMap.put("result", false);

		} else {
			ArrayList<ConsumeInfo> consumeInfos = (ArrayList<ConsumeInfo>) JSON.parseArray(jsonStr, ConsumeInfo.class);
			hashMap.put("consumeInfos", consumeInfos);
			hashMap.put("result", true);

		}

		return hashMap;
	}

}
