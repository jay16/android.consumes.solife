package us.solife.consumes.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import us.solife.consumes.TabList;
import us.solife.consumes.db.ConsumeDao;
import us.solife.consumes.entity.ConsumeInfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetUtils {
	static String url_base_solife    = "http://solife.us/api/";
	static String url_consume_list   = url_base_solife + "consumes/list";
	static String url_consume_create = url_base_solife + "consumes/create";
	static SharedPreferences   preferences;
	/**
	 * ��������������󣬵õ�����
	 * 
	 * @param url
	 * @return
	 */
	public static Object post(URL url) {
		return new Object();
	}
	/**
	 * �ж��Ƿ�������
	 * @param context
	 * @return
	 */
	public static boolean hasNetWork(Context context) {
		ConnectivityManager con = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo workinfo = con.getActiveNetworkInfo();
		if (workinfo == null || !workinfo.isAvailable()) {
			return false;
		}
		return true;
	}
	
	//����consume
	public static String[] solife_consume_create(String login_email, String value, String created_at, String msg) {
		// Step One �ӷ������ӿ��л�ȡ��ǰ�˺ź������������
		String[] ret_array = { "0", "no return" };
		Integer ret = 0;
		String ret_info = "no return";

		// ����httpRequest����
		HttpPost httpRequest = new HttpPost(url_consume_create);
		// HttpGet httpRequest =new HttpGet(httpUrl);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("email", login_email));
		params.add(new BasicNameValuePair("consume[volue]", value));
		params.add(new BasicNameValuePair("consume[created_at]", created_at));
		params.add(new BasicNameValuePair("consume[msg]", msg));

		try {
			// �����ַ���
			HttpEntity httpentity = new UrlEncodedFormEntity(params, "utf-8");
			// ����httpRequest
			httpRequest.setEntity(httpentity);
			// ȡ��HttpClinet����
			HttpClient httpclient = new DefaultHttpClient();
			// ����HttpClient,ȡ��HttpResponse
			HttpResponse httpResponse = httpclient.execute(httpRequest);
			// ����ɹ�
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// ȡ�÷��ص��ַ���
				String strResult = EntityUtils.toString(httpResponse.getEntity());
				JSONObject jsonObject = new JSONObject(strResult);
				// ��ȡ����ֵ
				ret = jsonObject.getInt("ret");
				ret_info = jsonObject.getString("ret_info");
				ret_array[0] = ret.toString();
				ret_array[1] = ret_info;
			}
		} catch (Exception e) {
			ret_array[1] = e.getMessage().toString();
			return ret_array;
		}
		return ret_array;
	}
	
	public static void sync_unupload_consumes(Context context,String login_email) {
		    ArrayList<ConsumeInfo> consumeInfos;
		    ConsumeDao             consumeDao;
		    
		    consumeDao = ConsumeDao.getConsumeDao(context);
		    consumeInfos = consumeDao.getUnSyncRecords();
		    
			Integer un_sync_count = consumeInfos.size();
			
			for(int i = 0; i < consumeInfos.size(); i++) {
				ConsumeInfo info = consumeInfos.get(i);
				String sync_state = "�ɹ�";
				String[] ret_array = NetUtils.solife_consume_create(login_email, Double.toString(info.getVolue()), info.getCreated_at(), info.getMsg());
				if (ret_array[0].equals("1")) {
					consumeDao.updateUnSyncRecordByRowId(info.getId(),info.getUser_id(),info.getConsume_id());
					sync_state = "�ɹ�";
				} else {
					sync_state = "ʧ��";
				}	
				//Toast.makeText(context, "["+sync_state+"]ͬ����"+i+"/"+un_sync_count+"������", 0).show();
			}
			//Toast.makeText(context, "����δͬ���������", 0).show();		

    }
	
	public static void upload_unsync_consumes_background(final Context context,final String login_email) {
		 new Thread() {
			 public void run() {
				sync_unupload_consumes(context, login_email);
			 };
		 }.start();
	}
}