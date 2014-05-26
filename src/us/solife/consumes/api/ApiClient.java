package us.solife.consumes.api;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import android.content.Context;
import android.util.Log;

/**
 * API客户端接口：用于访问网络数据
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class ApiClient {

	public static final String UTF_8 = "UTF-8";
	public static final String DESC = "descend";
	public static final String ASC = "ascend";
	
	private final static int TIMEOUT_CONNECTION = 20000;
	private final static int TIMEOUT_SOCKET = 20000;
	private final static int RETRY_TIME = 3;

	
	private static HttpClient getHttpClient() {        
        HttpClient httpClient = new HttpClient();
		// 设置 HttpClient 接收 Cookie,用与浏览器一样的策略
		httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        // 设置 默认的超时重试处理策略
		httpClient.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		// 设置 连接超时时间
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(TIMEOUT_CONNECTION);
		// 设置 读数据超时时间 
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(TIMEOUT_SOCKET);
		// 设置 字符集
		httpClient.getParams().setContentCharset(UTF_8);
		return httpClient;
	}	
	
	private static String _MakeURL(String p_url, Map<String, Object> params) {
		StringBuilder url = new StringBuilder(p_url);
		if(url.indexOf("?")<0)
			url.append('?');

		for(String name : params.keySet()){
			url.append('&');
			url.append(name);
			url.append('=');
			url.append(String.valueOf(params.get(name)));
			//不做URLEncoder处理
			//url.append(URLEncoder.encode(String.valueOf(params.get(name)), UTF_8));
		}

		return url.toString().replace("?&", "?");
	}
	
	/**
	 * get请求URL
	 */
	public static HashMap<String, Object> _Get(Context context, String url){	
		HttpClient client = getHttpClient();
		GetMethod httpGet = new GetMethod(url);
		httpGet.getParams().setSoTimeout(TIMEOUT_SOCKET);
		HashMap<String, Object> hash_map = new HashMap<String, Object>();
		try {
			int statusCode = client.executeMethod(httpGet);
			hash_map.put("statusCode", statusCode);
			if (statusCode == HttpStatus.SC_OK) 
				hash_map.put("json_str", httpGet.getResponseBodyAsString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return hash_map ;
	}
	
	public static HashMap<String, Object> _post(String url, org.apache.commons.httpclient.NameValuePair[] params)
			throws HttpException, IOException{	
		HttpClient http_client = getHttpClient();
		PostMethod http_post = new PostMethod(url);
		// 请求httpRequest
		http_post.setRequestBody(params);				        
        int statusCode  = http_client.executeMethod(http_post);
        String response = "";
		try {
			response = http_post.getResponseBodyAsString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        HashMap<String, Object> hash_map = new HashMap<String, Object>();
        
        hash_map.put("statusCode", statusCode);
        hash_map.put("response", response);
        Log.w("_POST", hash_map.toString());
        return hash_map;
	}
	public static HashMap<String, Object> _delete(String url)
			throws HttpException, IOException{	
		HttpClient http_client = getHttpClient();
		DeleteMethod http_delete = new DeleteMethod(url);
		// 请求httpRequest     
        int statusCode  = http_client.executeMethod(http_delete);
        String response = "";
		try {
			response = http_delete.getResponseBodyAsString();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        HashMap<String, Object> hash_map = new HashMap<String, Object>();
        
        hash_map.put("statusCode", statusCode);
        hash_map.put("response", response);
        Log.w("_DELETE", hash_map.toString());
        return hash_map;
	}
	

	
}
