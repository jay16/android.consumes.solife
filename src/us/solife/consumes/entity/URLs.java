package us.solife.consumes.entity;


import java.io.Serializable;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import us.solife.consumes.util.StringUtils;

/**
 * 消费记录明细
 * @author jay (http://solife.us/resume)
 * @version 1.0
 * @created 2014-02-25
 */
public class URLs implements Serializable {
	
	public final static String HOST = "solife.us";
	public final static String HTTP = "http://";
	public final static String HTTPS = "https://";
	
	private final static String URL_SPLITTER = "/";
	private final static String URL_UNDERLINE = "_";
	
	private final static String URL_API_HOST = HTTP + HOST + URL_SPLITTER;
	public final static String USR_VALIDATE  = URL_API_HOST + "api/users/validate";
	public final static String CONSUME_LIST   = URL_API_HOST + "api/consumes/list";
	public final static String CONSUME_CREATE = URL_API_HOST + "api/consumes/create";
	public final static String CONSUME_UPDATE = URL_API_HOST + "api/consumes/update";
	public final static String CONSUME_DELETE = URL_API_HOST + "api/consumes/delete";
	public final static String CONSUME_SHOW   = URL_API_HOST + "api/consumes/show";
	
	
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
