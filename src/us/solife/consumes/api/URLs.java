package us.solife.consumes.api;


import java.io.Serializable;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import android.os.Environment;
import us.solife.consumes.util.StringUtils;

/**
 * 消费记录明细
 * @author jay (http://solife.us/resume)
 * @version 1.0
 * @created 2014-02-25
 */
public class URLs implements Serializable {
	
	public final static String GRAVATAR_BASE_URL = "http://gravatar.com/avatar/";
	
	public final static String HOST = "consume.solife.us";
	public final static String HTTP = "http://";
	public final static String HTTPS = "https://";
	
	private final static String URL_SPLITTER = "/";
	private final static String URL_UNDERLINE = "_";
	
	private final static String URL_API_HOST = HTTP + HOST;// + URL_SPLITTER;

	public final static String API_VERSION = "v1";
	
	//users
	public final static String URL_USER  = String.format("%s/api/%s/%s", URL_API_HOST,API_VERSION,"users.json");
	public final static String URL_USER_FRIENDS  = String.format("%s/api/%s/%s",URL_API_HOST,API_VERSION,"users/friends.json");
	//consumes
	
	public final static String URL_RECORD   = String.format("%s/api/%s/%s",URL_API_HOST,API_VERSION,"records");
	public final static String URL_RECORD_FRIENDS   = String.format("%s/api/%s/%s",URL_API_HOST,API_VERSION,"records/friends.json");
	public final static String URL_TAG   = String.format("%s/api/%s/%s",URL_API_HOST,API_VERSION,"tags");
	//android app
	public final static String URL_VERSION   = URL_API_HOST + "api/version.json?os=android";
	

	public final static String STORAGE_BASE     = Environment.getExternalStorageDirectory().getAbsolutePath() 
			+ "/solife.consume/";
	public final static String STORAGE_GRAVATAR = STORAGE_BASE + "gravatar";
	public final static String STORAGE_APK      = STORAGE_BASE + "apk";
	public final static String STORAGE_IMAGES   = STORAGE_BASE + "images";
	
	public final static int URL_OBJ_TYPE_OTHER = 0x000;
	public final static int URL_OBJ_TYPE_NEWS = 0x001;
	public final static int URL_OBJ_TYPE_SOFTWARE = 0x002;
	public final static int URL_OBJ_TYPE_QUESTION = 0x003;
	public final static int URL_OBJ_TYPE_ZONE = 0x004;
	public final static int URL_OBJ_TYPE_BLOG = 0x005;
	public final static int URL_OBJ_TYPE_TWEET = 0x006;
	public final static int URL_OBJ_TYPE_QUESTION_TAG = 0x007;
	
	
	/**
	 * 对URL进行格式处理
	 * @param path
	 * @return
	 */
	private final static String formatURL(String path) {
		if(path.startsWith("http://") || path.startsWith("https://"))
			return path;
		return "http://" + URLEncoder.encode(path);
	}	
}
