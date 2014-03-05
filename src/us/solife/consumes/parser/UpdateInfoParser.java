package us.solife.consumes.parser;

import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import us.solife.consumes.entity.UpdateInfo;


import android.util.Log;
import android.util.Xml;


public class UpdateInfoParser {

	private static UpdateInfoParser instance;
	public static UpdateInfoParser getInstance(){
		if(null == instance){
			instance = new UpdateInfoParser();
		}
		return instance;
	}
	public UpdateInfo getUpdateInfo(InputStream is) throws Exception{
		UpdateInfo info = new UpdateInfo();
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(is, "UTF-8");
		int type = parser.getEventType();
		while(type != XmlPullParser.END_DOCUMENT){
			switch(type){
			case XmlPullParser.START_TAG:
				if("version".equals(parser.getName())){
					info.set_version(parser.nextText());
				}
				if("url".equals(parser.getName())){
					info.set_url(parser.nextText());
				}
				if("descriptoin".equals(parser.getName())){
					info.set_description(parser.nextText());
				}
			}
			type = parser.next();
			Log.w("UpdateInfoParser",info.to_string());
		}
		is.close();
		is = null;
		return info;
	}
}
