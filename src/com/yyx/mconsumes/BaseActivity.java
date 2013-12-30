package com.yyx.mconsumes;

import java.io.IOException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.yyx.mconsumes.parseJson.BaseParse;
import com.yyx.mconsumes.util.AppManager;
import com.yyx.mconsumes.util.NetUtils;
import com.yyx.mconsumes.util.ThreadPoolManager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public abstract class BaseActivity extends Activity {
	public static final String UTF_8 = "UTF-8";
	public static final String DESC  = "descend";
	public static final String ASC   = "ascend";
	Context context;

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
		AppManager.getAppManager().addActivity(this);
		init();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		AppManager.getAppManager().finishActivity(this);
	}

	public void netError() {
	}

	public void getdataFromServer(Context context, BaseParse baseParse, String url, DataCallback callback) {
		MyHandler handler = new MyHandler(callback);

		Task task = new Task(url, baseParse, handler, context);
		this.context = context;
		ThreadPoolManager manager = ThreadPoolManager.getInstance();
		manager.addTask(task);

	}

	public abstract void init();

	public abstract interface DataCallback<T> {
		public abstract void processData(T paramObject, boolean paramBoolean);
	}

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

		public Task(String url, BaseParse baseParse, MyHandler handler,Context context) {
			this.handler   = handler;
			this.baseParse = baseParse;
			this.url       = url;
			this.context   = context;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			HttpClient client = getHttpClient();
			GetMethod httpGet = new GetMethod(url);
			httpGet.getParams().setSoTimeout(TIMEOUT_SOCKET);
			Message message = new Message();
			try {
				//检测当前环境是否有网络
				if (NetUtils.hasNetWork(context)) {
					int statusCode = client.executeMethod(httpGet);
					if (statusCode == HttpStatus.SC_OK) {
						String responseBody = httpGet.getResponseBodyAsString();
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
