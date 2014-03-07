package us.solife.consumes;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import us.solife.consumes.api.ApiClient;
import us.solife.consumes.parse.BaseParse;
import us.solife.consumes.util.AppManager;
import us.solife.consumes.util.NetUtils;
import us.solife.consumes.util.ThreadPoolManager;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.widget.Toast;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;

public abstract class BaseActivity extends Activity {
	public static final String UTF_8 = "UTF-8";
	public static final String DESC  = "descend";
	public static final String ASC   = "ascend";
	Context context;
	SharedPreferences sharedPreferences;

	private final static int TIMEOUT_CONNECTION = 20000;
	private final static int TIMEOUT_SOCKET     = 20000;
	private final static int RETRY_TIME         = 3;
    
	  
	//内部类MyHandler
	class MyHandler extends Handler {
		DataCallback callback;

		public MyHandler(DataCallback callback) {
			this.callback = callback;
		}

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int resultcode = msg.what;

			switch (resultcode) {
			case 1000:
				callback.processData(msg.obj, true);
				break;
			case 2000:
				netError();
				break;
			case 3000:
				netError();
				break;

			default:
				break;
			}

		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//添加Activity到堆栈
		AppManager.getAppManager().addActivity(this);
		try {
			init();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//结束Activity&从堆栈中移除
		AppManager.getAppManager().finishActivity(this);
	}
	
	public void netError() {
	}

	public void getDataFromServer(Context context, BaseParse baseParse, String url, DataCallback callback) {
		MyHandler handler = new MyHandler(callback);

		Task task = new Task(context, baseParse , url, handler);
		this.context = context;
		ThreadPoolManager manager = ThreadPoolManager.getInstance();
		manager.addTask(task);
	}

	public abstract void init() throws ParseException;


	//接口，继承类可以自定义处理
	public abstract interface DataCallback<T> {
		public abstract void processData(T paramObject, boolean paramBoolean);
	}

	//封装获取HttpClient代码
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

	//内部类Task
	class Task implements Runnable {
		String    url;
		BaseParse baseParse;
		MyHandler handler;
		Context   context;

		public Task(Context context, BaseParse baseParse, String url, MyHandler handler) {
			this.handler   = handler;
			this.baseParse = baseParse;
			this.url       = url;
			this.context   = context;
		}

		@Override
		public void run() {
			sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
			String email = sharedPreferences.getString("current_user_email", "");
			Message message = new Message();
			try {
				//检测当前环境是否有网络
				if (NetUtils.hasNetWork(context)) {
					HashMap<String, Object> http_get = ApiClient._get(context,url+"?email="+email);
					if ((Integer)http_get.get("statusCode")==HttpStatus.SC_OK) {
						String responseBody = (String)http_get.get("json_str");
						Object object = baseParse.parseJSON(responseBody);
						
						message.what = 1000;
						message.obj = object;
						handler.sendMessage(message);

					} else {
						message.what = 2000;// 获取出错
						handler.sendMessage(message);
					}
				} else {
					message.what = 3000;// 网络错误
					handler.sendMessage(message);
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
