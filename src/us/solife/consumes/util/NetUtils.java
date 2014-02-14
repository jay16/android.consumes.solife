package us.solife.consumes.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
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
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.widget.Toast;

public class NetUtils {
	static String url_base_solife    = "http://solife.us/api/";
	static String url_consume_list   = url_base_solife + "consumes/list";
	static String url_consume_create = url_base_solife + "consumes/create";
	static String url_user_info      = url_base_solife + "users/info";
	
	static String storge_base_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/solife/";
	static String storge_gravatar_path = storge_base_path + "gravatar/";
	static String storge_image_path    = storge_base_path + "image/";
	static String storge_apk_path      = storge_base_path + "apk/";
	
	/**
	 * 与服务器发送请求，得到数据
	 * 
	 * @param url
	 * @return
	 */
	public static Object post(URL url) {
		return new Object();
	}
	/**
	 * 判断是否有网络
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
	
	//创建consume
	public static String[] solife_consume_create(String login_email, String value, String created_at, String msg) {
		// Step One 从服务器接口中获取当前账号和密码的配对情况
		String[] ret_array = { "0", "no return" };
		Integer ret = 0;
		String ret_info = "no return";

		// 创建httpRequest对象
		HttpPost httpRequest = new HttpPost(url_consume_create);
		// HttpGet httpRequest =new HttpGet(httpUrl);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("email", login_email));
		params.add(new BasicNameValuePair("consume[volue]", value));
		params.add(new BasicNameValuePair("consume[created_at]", created_at));
		params.add(new BasicNameValuePair("consume[msg]", msg));

		try {
			// 设置字符集
			HttpEntity httpentity = new UrlEncodedFormEntity(params, "utf-8");
			// 请求httpRequest
			httpRequest.setEntity(httpentity);
			// 取得HttpClinet对象
			HttpClient httpclient = new DefaultHttpClient();
			// 请求HttpClient,取得HttpResponse
			HttpResponse httpResponse = httpclient.execute(httpRequest);
			// 请求成功
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// 取得返回的字符串
				String strResult = EntityUtils.toString(httpResponse.getEntity());
				JSONObject jsonObject = new JSONObject(strResult);
				// 获取返回值
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
			
			if(un_sync_count>0){
				for(int i = 0; i < consumeInfos.size(); i++) {
					ConsumeInfo info = consumeInfos.get(i);
					String sync_state = "成功";
					String[] ret_array = NetUtils.solife_consume_create(login_email, Double.toString(info.getVolue()), info.getCreated_at(), info.getMsg());
					if (ret_array[0].equals("1")) {
						consumeDao.updateUnSyncRecordByRowId(info.getId(),info.getUser_id(),info.getConsume_id());
						sync_state = "成功";
					} else {
						sync_state = "失败";
					}	
				}	
			}

    }
	
	public static void upload_unsync_consumes_background(final Context context,final String login_email) {
		 new Thread() {
			 public void run() {
				sync_unupload_consumes(context, login_email);
			 };
		 }.start();
	}
	
	public static String [] get_user_info(SharedPreferences preferences,String email) {
    	String [] ret_array = {"0","更新成功"};
        Integer ret     = 0;
        String ret_info = "no return";
        
	    Editor Editor = preferences.edit();
        //httpGet 连接对象
        HttpGet httpRequest =new HttpGet(url_user_info+"?email="+email);

        try {
            //取得HttpClinet对象
            HttpClient httpclient = new DefaultHttpClient();
            // 请求HttpClient,取得HttpResponse
            HttpResponse httpResponse = httpclient.execute(httpRequest);
            //请求成功
            if(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK)
            {
     //取得返回的字符串
     String strResult=EntityUtils.toString(httpResponse.getEntity());
     
     JSONObject jsonObject = new JSONObject(strResult) ;
     //获取返回值,并判断是否正确
     //actionResult=jsonObject.getBoolean("ActionResult");
     ret      = jsonObject.getInt("ret");
     ret_info = jsonObject.getString("ret_info");
     ret_array[0] = ret.toString();
     ret_array[1] = ret_info;
     if(ret.toString().equals("1")) {
         //Integer user_id      = jsonObject.getInt("user_id");
         String user_name     = jsonObject.getString("user_name");
         String user_email    = jsonObject.getString("user_email");
         String user_province = jsonObject.getString("user_province");
         String user_register = jsonObject.getString("user_register");
         String user_gravatar = jsonObject.getString("user_gravatar");
					//Editor.putInt("current_user_id", user_id);
					Editor.putString("current_user_name", user_name);
					Editor.putString("current_user_email", user_email);
					Editor.putString("current_user_province", user_province);
					Editor.putString("current_user_register", user_register);
					Editor.putString("current_user_gravatar", user_gravatar);
					ret_array[1] = user_gravatar;

					String picDirStr = Environment.getExternalStorageDirectory().getAbsolutePath() + "/solife/"; 
					download_image_with_url(user_gravatar,picDirStr,user_email.replace("@", "_")+".jpg");
					Editor.commit();
     }
            }
        }
        catch(Exception e) {
        	ret_array[1] = e.getMessage().toString();
			Editor.commit();
        }
        
	    return ret_array;
	}
	
	public static void download_image_with_url(String image_url,String path,String fileName) {    
    	File picDir = new File(path);
        if(!picDir.exists()){
            picDir.mkdir();
        }
        try {

		     String picName = path + fileName;
		     File myCaptureFile = new File(picName);
		     if(!myCaptureFile.exists()) {
		         Bitmap bitmap = getHttpBitmap(image_url);
			     BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
			     bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
			     bos.flush();
			     bos.close();
			     bitmap.recycle();//回收bitmap空间
		     }
        } catch (ClientProtocolException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
	}
	public static Bitmap getLoacalBitmap(String url) {
        try {
             FileInputStream fis = new FileInputStream(url);
             return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片        

          } catch (FileNotFoundException e) {
             e.printStackTrace();
             return null;
        }
   }
	public static Bitmap getHttpBitmap(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
             myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
             e.printStackTrace();
        }
        try {
             HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
             conn.setConnectTimeout(0);
             conn.setDoInput(true);
             conn.connect();
             InputStream is = conn.getInputStream();
             bitmap = BitmapFactory.decodeStream(is);
             is.close();
        } catch (IOException e) {
             e.printStackTrace();
        }
        return bitmap;
   }
}