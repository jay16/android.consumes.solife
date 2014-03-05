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
import us.solife.consumes.api.URLs;
import us.solife.consumes.db.ConsumeTb;
import us.solife.consumes.entity.ConsumeInfo;
import us.solife.consumes.entity.CurrentUser;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

/**
 * 网络相关操作
 * @author jay (http://solife.us/resume)
 * @version 1.0
 * @created 2014-02-25
 */
public class NetUtils {
	
	/**
	 * 与服务器发送请求，得到数据
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
	public static String[] solife_consume_create(String login_email, ConsumeInfo consume_info) {
		// 从服务器接口中获取当前账号和密码的配对情况
		String[] ret_array = { "0", "no return","-1" };

		// 创建httpRequest对象
		HttpPost httpRequest = new HttpPost(URLs.CONSUME_CREATE);
		// HttpGet httpRequest =new HttpGet(httpUrl);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("email", login_email));
		params.add(new BasicNameValuePair("consume[volue]", consume_info.get_volue()+""));
		params.add(new BasicNameValuePair("consume[created_at]", consume_info.get_created_at()));
		params.add(new BasicNameValuePair("consume[msg]", consume_info.get_msg()));

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
				ret_array[0] = jsonObject.getInt("ret")+"";
				ret_array[1] = jsonObject.getString("ret_info");
				ret_array[2] = jsonObject.getInt("consume_id")+"";
			}
		} catch (Exception e) {
			ret_array[1] = e.getMessage().toString();
			return ret_array;
		}
		return ret_array;
	}

	
	//创建consume
	public static String[] solife_consume_update(String login_email, ConsumeInfo consume_info) {
		// 从服务器接口中获取当前账号和密码的配对情况
		String[] ret_array = { "0", "no return" };

		// 创建httpRequest对象
		HttpPost httpRequest = new HttpPost(URLs.CONSUME_UPDATE);
		// HttpGet httpRequest =new HttpGet(httpUrl);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("email", login_email));
		params.add(new BasicNameValuePair("id",consume_info.get_consume_id()+""));
		params.add(new BasicNameValuePair("consume[volue]", consume_info.get_volue()+""));
		params.add(new BasicNameValuePair("consume[created_at]", consume_info.get_created_at()));
		params.add(new BasicNameValuePair("consume[msg]", consume_info.get_msg()));

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
				ret_array[0] = jsonObject.getInt("ret")+"";
				ret_array[1] = jsonObject.getString("ret_info");
			}
		} catch (Exception e) {
			ret_array[1] = e.getMessage().toString();
			return ret_array;
		}
		return ret_array;
	}
	

	//创建consume
	public static String[] solife_consume_delete(String login_email, ConsumeInfo consume_info) {
		// 从服务器接口中获取当前账号和密码的配对情况
		String[] ret_array = { "0", "no return" };

		// 创建httpRequest对象
		HttpPost httpRequest = new HttpPost(URLs.CONSUME_DELETE);
		// HttpGet httpRequest =new HttpGet(httpUrl);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("email", login_email));
		params.add(new BasicNameValuePair("id",consume_info.get_consume_id()+""));

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
				ret_array[0] = jsonObject.getInt("ret")+"";
				ret_array[1] = jsonObject.getString("ret_info");
			}
		} catch (Exception e) {
			ret_array[1] = e.getMessage().toString();
			return ret_array;
		}
		return ret_array;
	}
	
	/**
	 * 独立处理未同步数据
	 * @param context
	 * @param login_email
	 */
	public static void sync_unupload_consumes(Context context,String login_email) {
		    ArrayList<ConsumeInfo> consume_infos;
		    ConsumeTb             consumeDao;
		    
		    consumeDao = ConsumeTb.getConsumeTb(context);
		    consume_infos = consumeDao.get_unsync_records();
		    
			Integer un_sync_count = consume_infos.size();
			
			if(un_sync_count>0){
				for(int i = 0; i < consume_infos.size(); i++) {
					ConsumeInfo consume_info = consume_infos.get(i);
					CurrentUser current_user = CurrentUser.getCurrentUser(context, consume_info.get_user_id());
					String[] ret_array = {"0","","-1"};
					if(consume_info.get_state().equals("create")){
					  ret_array = NetUtils.solife_consume_create(login_email, consume_info);
						if (ret_array[0].equals("1")) {
							//同步成功则修改数据库内容
							consume_info.set_consume_id(Integer.valueOf(ret_array[2]));
							consume_info.set_sync((long)1);
							consume_info.set_state("");
							Log.w("NetUtils",consume_info.to_string());
							current_user.update_record(consume_info);
                            Log.w("NetUtils","Action:"+consume_info.get_state()+"-YES");
						} else {
							//同步失败则不做任何动作
                            Log.w("NetUtils","Action:"+consume_info.get_state()+"-NO");
						}	
					} else if(consume_info.get_state().equals("update")){
					  ret_array = NetUtils.solife_consume_update(login_email, consume_info);
						if (ret_array[0].equals("1")) {
							consume_info.set_sync((long)1);
							consume_info.set_state("");
							Log.w("NetUtils",consume_info.to_string());
							current_user.update_record(consume_info);
                            Log.w("NetUtils","Action:"+consume_info.get_state()+"-YES");
						} else {
                            Log.w("NetUtils","Action:"+consume_info.get_state()+"-NO");
						}	
					} else if(consume_info.get_state().equals("delete")) {
					  ret_array = NetUtils.solife_consume_delete(login_email, consume_info);
						if (ret_array[0].equals("1")) {
							Log.w("NetUtils",consume_info.to_string());
							current_user.destroy_record(consume_info.get_id());
                            Log.w("NetUtils","Action:"+consume_info.get_state()+"-YES");
						} else {
                            Log.w("NetUtils","Action:"+consume_info.get_state()+"-NO");
						}	
					} else {
						Log.e("NetUtils","Action Not Found:["+consume_info.get_state()+"]");
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
        HttpGet httpRequest =new HttpGet(URLs.USR_VALIDATE+"?email="+email);

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
			         int user_id = jsonObject.getInt("user_id");
			         
					//Editor.putInt("current_user_id", user_id);
					Editor.putString("current_user_name", user_name);
					Editor.putString("current_user_email", user_email);
					Editor.putString("current_user_province", user_province);
					Editor.putString("current_user_register", user_register);
					Editor.putString("current_user_gravatar", user_gravatar);
					Editor.putLong("current_user_id", user_id);
					Editor.putBoolean("is_login", true);
					ret_array[1] = user_gravatar;
					
					download_image_with_url(user_gravatar,URLs.STORAGE_GRAVATAR,user_email.replace("@", "_")+".jpg");
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