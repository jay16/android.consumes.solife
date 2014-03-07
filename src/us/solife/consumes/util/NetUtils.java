package us.solife.consumes.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.httpclient.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import us.solife.consumes.TabAbout;
import us.solife.consumes.api.ApiClient;
import us.solife.consumes.api.Gravatar;
import us.solife.consumes.api.URLs;
import us.solife.consumes.db.ConsumeTb;
import us.solife.consumes.db.UserTb;
import us.solife.consumes.entity.ConsumeInfo;
import us.solife.consumes.entity.CurrentUser;
import us.solife.consumes.entity.UpdateInfo;
import us.solife.consumes.entity.UserInfo;
import us.solife.consumes.parse.ConsumeListParse;
import us.solife.consumes.parse.UpdateInfoParse;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
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
	public static String[] solife_consume_create(String login_email, ConsumeInfo consume_info) 
			throws HttpException, IOException, JSONException {
		// 从服务器接口中获取当前账号和密码的配对情况
		String[] ret_array = { "0", "return null","-1" };

		org.apache.commons.httpclient.NameValuePair[] params = new org.apache.commons.httpclient.NameValuePair[] {
		  new org.apache.commons.httpclient.NameValuePair("email", login_email),
		  new org.apache.commons.httpclient.NameValuePair("consume[volue]", consume_info.get_volue()+""),
		  new org.apache.commons.httpclient.NameValuePair("consume[created_at]", consume_info.get_created_at()),
		  new org.apache.commons.httpclient.NameValuePair("consume[msg]", consume_info.get_msg())
		};

		HashMap<String, Object> hash_map = ApiClient._post(URLs.CONSUME_CREATE, params);
		int statusCode  = (Integer)hash_map.get("statusCode");
		String response = (String)hash_map.get("response");
			// 请求成功
		if (statusCode == HttpStatus.SC_OK) {
			JSONObject jsonObject = new JSONObject(response);
			// 获取返回值
			ret_array[0] = jsonObject.getInt("ret")+"";
			ret_array[1] = jsonObject.getString("ret_info");
			ret_array[2] = jsonObject.getInt("consume_id")+"";
		}
		return ret_array;
	}

	
	//创建consume
	public static String[] solife_consume_update(String login_email, ConsumeInfo consume_info) 
			throws HttpException, IOException, JSONException {
		// 从服务器接口中获取当前账号和密码的配对情况
		String[] ret_array = { "0", "return null" };

		org.apache.commons.httpclient.NameValuePair[] params = new org.apache.commons.httpclient.NameValuePair[] {
		  new org.apache.commons.httpclient.NameValuePair("email", login_email),
		  new org.apache.commons.httpclient.NameValuePair("id",consume_info.get_consume_id()+""),
		  new org.apache.commons.httpclient.NameValuePair("consume[volue]", consume_info.get_volue()+""),
		  new org.apache.commons.httpclient.NameValuePair("consume[created_at]", consume_info.get_created_at()),
		  new org.apache.commons.httpclient.NameValuePair("consume[msg]", consume_info.get_msg())
		};

		HashMap<String, Object> hash_map = ApiClient._post(URLs.CONSUME_UPDATE, params);
		int statusCode  = (Integer)hash_map.get("statusCode");
		String response = (String)hash_map.get("response");
		if (statusCode == HttpStatus.SC_OK) {
			JSONObject jsonObject = new JSONObject(response);
			ret_array[0] = jsonObject.getInt("ret")+"";
			ret_array[1] = jsonObject.getString("ret_info");
		}
		return ret_array;
	}
	

	//创建consume
	public static String[] solife_consume_delete(String login_email, ConsumeInfo consume_info) 
			throws HttpException, IOException, JSONException {
		String[] ret_array = { "0", "return null" };

		org.apache.commons.httpclient.NameValuePair[] params = new org.apache.commons.httpclient.NameValuePair[] {
		  new org.apache.commons.httpclient.NameValuePair("email", login_email),
		  new org.apache.commons.httpclient.NameValuePair("id",consume_info.get_consume_id()+"")
		};

		HashMap<String, Object> hash_map = ApiClient._post(URLs.CONSUME_DELETE, params);
		int statusCode  = (Integer)hash_map.get("statusCode");
		String response = (String)hash_map.get("response");
		
		// 请求成功
		if (statusCode == HttpStatus.SC_OK) {;
			JSONObject jsonObject = new JSONObject(response);
			// 获取返回值
			ret_array[0] = jsonObject.getInt("ret")+"";
			ret_array[1] = jsonObject.getString("ret_info");
		}

		return ret_array;
	}
	
	/**
	 * 独立处理未同步数据
	 * @param context
	 * @param login_email
	 * @throws JSONException 
	 * @throws IOException 
	 * @throws HttpException 
	 */
	public static void upload_unsync_consumes(Context context,String login_email) 
			throws HttpException, IOException, JSONException {
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
	
	public static UserInfo get_user_info_with_user_id(Context context,Integer user_id) 
			throws JSONException {
        UserInfo user_info = new UserInfo();
        String [] ret_array = {"0","更新成功"};
        Integer ret     = 0; 
        String ret_info = "no return";
        HashMap<String, Object> hash_map = ApiClient._get(context,URLs.USR_INFO+"?id="+user_id);
		int statusCode  = (Integer)hash_map.get("statusCode");
		String response = (String)hash_map.get("json_str");
		
	    if(statusCode==HttpStatus.SC_OK)
	    {
		     JSONObject jsonObject = new JSONObject(response);
		     ret      = jsonObject.getInt("ret");
		     ret_info = jsonObject.getString("ret_info");
		     ret_array[0] = ret.toString();
		     ret_array[1] = ret_info;
		     if(ret.toString().equals("1")) {
		    	 user_info.set_user_id(jsonObject.getInt("id"));
		         user_info.set_name(jsonObject.getString("name"));
		         user_info.set_email(jsonObject.getString("email"));
		         user_info.set_created_at(jsonObject.getString("created_at"));
		         user_info.set_updated_at(jsonObject.getString("updated_at"));
		     }
	    }
	    return user_info;
	}
	
	public static void sync_user_list(Context context) 
			throws JSONException {
		UserTb user_table = UserTb.getUserTb(context);
		ArrayList<Integer> user_ids = user_table.get_unsync_user_list();
		if(user_ids.size() == 0) return;
		
		for(int i=0; i<user_ids.size(); i++ ) {
			UserInfo user_info = get_user_info_with_user_id(context,user_ids.get(i));
			if(user_info.get_user_id()>0) {
			    user_table.insert_record(user_info);
				Log.w("NetUtils","Insert User:"+user_info.to_string());
			}
		}
	}
	
	public static void chk_user_gravatar(Context context)
			throws JSONException {
		UserTb user_table = UserTb.getUserTb(context);
		ArrayList<UserInfo> user_infos = user_table.get_user_list();
		if(user_infos.size()>0) 
		for(int i=0; i<user_infos.size(); i++ ) {
			UserInfo user_info = user_infos.get(i);//get_user_info_with_user_id(context,user_infos.get(i).get_user_id());
			if(user_info.get_user_id()>0) {
				String email = user_info.get_email();
				File gravatar_path = new File(Gravatar.gravatar_path(email));
				String gravatar_url  = Gravatar.gravatar_url(email);
				//存储卡可用并且用用户头像不存在
				if(ToolUtils.hasSdcard() && !gravatar_path.exists()) {
					download_image_with_url(email);
				}
			}
		}
		
	}
	
	public static void upload_unsync_consumes_background(final Context context,final String login_email) {
		 new Thread() {
			 public void run() {
				try {
					upload_unsync_consumes(context, login_email);
					sync_user_list(context);
					chk_user_gravatar(context);
					get_new_friends_consumes(context, login_email);
				} catch (HttpException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
					
					download_image_with_url(user_email);
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
	
	public static void download_image_with_url(String email) {    
		
		if(email.length() == 0) return;

		String gravatar_url = Gravatar.gravatar_url(email);
        try {
		     String gravatar_path = Gravatar.gravatar_path(email);
		     File myCaptureFile = new File(gravatar_path);
		     if(!myCaptureFile.exists()) {
		         Bitmap bitmap = getHttpBitmap(gravatar_url);
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
	
	public static void get_new_friends_consumes(Context context, String email) 
			throws JSONException {
	    ArrayList<ConsumeInfo> consume_infos = new  ArrayList<ConsumeInfo>();

		UserTb user_table = UserTb.getUserTb(context);
		UserInfo user_info = user_table.get_record_with_email(email);

		ConsumeTb consume_table = ConsumeTb.getConsumeTb(context);
		Integer consume_id = consume_table.get_friends_max_consume_id(user_info.get_user_id());
        HashMap<String, Object> hash_map = ApiClient._get(context,URLs.CONSUME_FRIEND_NEW+"?consume_id="+consume_id+"&email="+email);
		int statusCode  = (Integer)hash_map.get("statusCode");
		String response = (String)hash_map.get("json_str");

    	Log.w("get_new_friends_consumes","statusCode:"+statusCode);
	    if(statusCode==HttpStatus.SC_OK) {
	    	ConsumeListParse consumeListParse = new ConsumeListParse();
	    	HashMap<String, Object> parse_json = consumeListParse.parseJSON(response);
	    	if((Boolean)parse_json.get("result")) {
	    		consume_infos = (ArrayList<ConsumeInfo>)parse_json.get("consume_infos");
	    	}
	    	Log.w("get_new_friends_consumes","parseJson:"+(Boolean)parse_json.get("result"));
	    	Log.w("get_new_friends_consumes","consume_infos:"+consume_infos.size());
	    	if(consume_infos.size()>0) {
	    		consume_table.insert_all_record(consume_infos,false);
	    		for(int i=0;i<consume_infos.size();i++) {
	    			ConsumeInfo c_info = consume_infos.get(i);
	    			UserInfo u_info = user_table.get_record_with_user_id(c_info.get_user_id());
	    			String title   = u_info.get_name()+"[￥"+c_info.get_volue()+"]";
	    			String content = c_info.get_msg();
	    			UIHelper.push_notice(context,title,content,10+i);
	    		}
	    	}
	    }

	}
	
	public static void get_all_consumes(Context context, String email) 
			throws JSONException {
	    ArrayList<ConsumeInfo> consume_infos = new  ArrayList<ConsumeInfo>();
        HashMap<String, Object> hash_map = ApiClient._get(context,URLs.CONSUME_LIST+"?email="+email);
		int statusCode  = (Integer)hash_map.get("statusCode");
		String response = (String)hash_map.get("json_str");

    	Log.w("get_new_friends_consumes","statusCode:"+statusCode);
	    if(statusCode==HttpStatus.SC_OK) {
	    	ConsumeListParse consumeListParse = new ConsumeListParse();
	    	HashMap<String, Object> parse_json = consumeListParse.parseJSON(response);
	    	if((Boolean)parse_json.get("result")) {
	    		consume_infos = (ArrayList<ConsumeInfo>)parse_json.get("consume_infos");
	    	}
	    	Log.w("get_new_friends_consumes","parseJson:"+(Boolean)parse_json.get("result"));
	    	Log.w("get_new_friends_consumes","consume_infos:"+consume_infos.size());
	    	if(consume_infos.size()>0) {
	    		ConsumeTb consume_table = ConsumeTb.getConsumeTb(context);
	    		consume_table.insert_all_record(consume_infos,true);
	    	}
	    }

	}
	
	
	public static void chk_version_update(Context context) 
			throws JSONException, NameNotFoundException {
		//检测当前环境是否有网络
		if (!NetUtils.hasNetWork(context)) return;
		
		HashMap<String, Object> http_get = ApiClient._get(context,URLs.VERSION_UPDATE);
		UpdateInfo update_info = null;
		if ((Integer)http_get.get("statusCode")==HttpStatus.SC_OK) {
			String responseBody = (String)http_get.get("json_str");
			HashMap<String, Object> hash_map = UpdateInfoParse.getInstance().parseJSON(responseBody);
			if((Boolean)hash_map.get("result")) {
				update_info = (UpdateInfo) hash_map.get("update_info");
				Log.w("CheckVersionTash",update_info.to_string());
			}
			
			final PackageManager pm = context.getPackageManager();
			final PackageInfo packInfo = pm.getPackageInfo("us.solife.consumes", PackageManager.GET_ACTIVITIES);
			final String version = packInfo.versionName;
			Log.w("UIHelper","Version :"+version);
			
			/*版本相同无需下载*/
			if (!version.equals(update_info.get_version())) {
				UIHelper.push_notice(context, "版本更新通知", update_info.get_description(), 0);
			}
		}
	}
}