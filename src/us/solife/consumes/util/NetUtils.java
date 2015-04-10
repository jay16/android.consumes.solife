package us.solife.consumes.util;

import java.io.BufferedOutputStream;













import android.util.Base64;

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
import us.solife.consumes.db.TagTb;
import us.solife.consumes.db.UserTb;
import us.solife.consumes.entity.ConsumeInfo;
import us.solife.consumes.entity.CurrentUser;
import us.solife.consumes.entity.TagInfo;
import us.solife.consumes.entity.UpdateInfo;
import us.solife.consumes.entity.UserInfo;
import us.solife.consumes.parse.ConsumeListParse;
import us.solife.consumes.parse.TagListParse;
import us.solife.consumes.parse.UpdateInfoParse;
import us.solife.consumes.parse.UserListParse;
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
import android.net.Uri;
import android.os.Build;
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
	 * 判断是否有网络
	 * @param context
	 * @return
	 */
	public static boolean has_network(Context context) {
		ConnectivityManager con = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo workinfo = con.getActiveNetworkInfo();
		return (workinfo != null && workinfo.isAvailable() ? true : false); 
	}
	/**
	 * 判断是否有网络
	 * @param context
	 * @return
	 */
	public static String network_type(Context context) {
		ConnectivityManager con = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo workinfo = con.getActiveNetworkInfo();
		String net_str = "无";
		if (workinfo != null && workinfo.isAvailable()) {
			switch(workinfo.getType()) {
			  case ConnectivityManager.TYPE_MOBILE: net_str = "手机"; break;
			  case ConnectivityManager.TYPE_WIFI: net_str = "wifi"; break;
			  default: net_str = "无"; break;
			}
		}
		return net_str;
	}
	//创建consume
	public static String[] create_record(String token, ConsumeInfo consume_info) 
			throws HttpException, IOException, JSONException {
		// 从服务器接口中获取当前账号和密码的配对情况
		String[] ret_array = { "0", "return null","" };
        Log.w("PostCreate", "beforePost:"+consume_info.to_string());
		org.apache.commons.httpclient.NameValuePair[] params = new org.apache.commons.httpclient.NameValuePair[] {
		  new org.apache.commons.httpclient.NameValuePair("token", token),
		  new org.apache.commons.httpclient.NameValuePair("format", "json"),
		  new org.apache.commons.httpclient.NameValuePair("record[value]", consume_info.get_value()+""),
		  new org.apache.commons.httpclient.NameValuePair("record[ymdhms]", consume_info.get_ymdhms()),
		  new org.apache.commons.httpclient.NameValuePair("record[remark]", consume_info.get_remark()),
		  new org.apache.commons.httpclient.NameValuePair("record[klass]", consume_info.get_klass()+""),
		  new org.apache.commons.httpclient.NameValuePair("record[tags_list]", consume_info.get_tags_list()),
		  new org.apache.commons.httpclient.NameValuePair("record[created_at]", consume_info.get_created_at()),
		  new org.apache.commons.httpclient.NameValuePair("record[browser]", Build.BRAND+"_"+Build.MODEL)
		};

		HashMap<String, Object> hash_map = ApiClient._Post(URLs.URL_RECORD, params);
		int statusCode  = (Integer)hash_map.get("statusCode");
		String response = (String)hash_map.get("response");
			// 请求成功
		if (statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_CREATED) {
			JSONObject jsonObject = new JSONObject(response);
			// 获取返回值
			ret_array[0] = "1";
			ret_array[1] = jsonObject.toString();
			ret_array[2] = jsonObject.getInt("id")+"&"+jsonObject.getString("updated_at");
		}
		Log.w("CreateRecord", ret_array.toString());
		return ret_array;
	}

	
	//update a record
	public static String[] update_record(String token, ConsumeInfo consume_info) 
			throws HttpException, IOException, JSONException {
		String[] ret_array = { "0", "return null", "" };

        Log.w("PostUpdate", "beforePost:"+consume_info.to_string());
		org.apache.commons.httpclient.NameValuePair[] params = new org.apache.commons.httpclient.NameValuePair[] {
		  new org.apache.commons.httpclient.NameValuePair("token", token),
		  new org.apache.commons.httpclient.NameValuePair("format", "json"),
		  new org.apache.commons.httpclient.NameValuePair("record[value]", consume_info.get_value()+""),
		  new org.apache.commons.httpclient.NameValuePair("record[ymdhms]", consume_info.get_ymdhms()),
		  new org.apache.commons.httpclient.NameValuePair("record[klass]", consume_info.get_klass()+""),
		  new org.apache.commons.httpclient.NameValuePair("record[tags_list]", consume_info.get_tags_list()),
		  new org.apache.commons.httpclient.NameValuePair("record[remark]", consume_info.get_remark()),
		  new org.apache.commons.httpclient.NameValuePair("record[created_at]", consume_info.get_created_at()),
		  new org.apache.commons.httpclient.NameValuePair("record[browser]", Build.BRAND+"_"+Build.MODEL)
		};

		HashMap<String, Object> hash_map = ApiClient._Post(URLs.URL_RECORD+"/"+consume_info.get_consume_id()+".json", params);
		int statusCode  = (Integer)hash_map.get("statusCode");
		String response = (String)hash_map.get("response");
		if (statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_CREATED) {
			JSONObject jsonObject = new JSONObject(response);
			ret_array[0] = "1";
			ret_array[1] = jsonObject.toString();
			ret_array[2] = jsonObject.getString("updated_at");
		}
		Log.w("UpdateRecord", ret_array.toString());
		return ret_array;
	}
	

	//创建consume
	public static String[] delete_record(String token, ConsumeInfo consume_info) 
			throws HttpException, IOException, JSONException {
		String[] ret_array = { "0", "return null" };
       
		String url = URLs.URL_RECORD+"/"+consume_info.get_consume_id()+".json?token="+Uri.encode(token);
		HashMap<String, Object> hash_map = ApiClient._Delete(url);
		int statusCode  = (Integer)hash_map.get("statusCode");
		String response = (String)hash_map.get("response");
		
		// 未认证成功
		if (statusCode == HttpStatus.SC_UNAUTHORIZED) {;
			// 获取返回值
			ret_array[0] = "-1";
		} else {
			ret_array[0] = "1";
		}
		JSONObject jsonObject = new JSONObject(response);
		ret_array[1] = jsonObject.toString();

		return ret_array;
	}
	
	/**
	 * 独立处理未同步Record数据
	 * @param context
	 * @param login_email
	 * @throws JSONException 
	 * @throws IOException 
	 * @throws HttpException 
	 */
	public static void sync_upload_records(Context context,String token, Long current_user_id) 
			throws HttpException, IOException, JSONException {
		    ArrayList<ConsumeInfo> consume_infos;
		    ConsumeTb              consumeDao;
		    
		    consumeDao    = ConsumeTb.getConsumeTb(context);
		    consume_infos = consumeDao.get_unsync_records();
		    
			Integer un_sync_count = consume_infos.size();
			
			if(un_sync_count > 0){
				CurrentUser current_user = CurrentUser.getCurrentUser(context, current_user_id);
				for(int i = 0; i < consume_infos.size(); i++) {
					ConsumeInfo consume_info = consume_infos.get(i);
					String[] ret_array = {"0","","-1"};
					if(consume_info.get_state().equals("create")){
					    ret_array = NetUtils.create_record(token, consume_info);
						if (ret_array[0].equals("1")) {
							//同步成功则修改数据库内容
							String[] arr = ret_array[2].split("&");
							Integer consume_id = Integer.valueOf(arr[0]);
							String updated_at = arr[1];
							consume_info.set_consume_id(consume_id);
							consume_info.set_updated_at(updated_at);
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
					    ret_array = NetUtils.update_record(token, consume_info);
						if (ret_array[0].equals("1")) {
							consume_info.set_updated_at(ret_array[2]);
							consume_info.set_sync((long)1);
							consume_info.set_state("");
							Log.w("NetUtils",consume_info.to_string());
							current_user.update_record(consume_info);
                            Log.w("NetUtils","Action:"+consume_info.get_state()+"-YES");
						} else {
                            Log.w("NetUtils","Action:"+consume_info.get_state()+"-NO");
						}	
					} else if(consume_info.get_state().equals("delete")) {
					  ret_array = NetUtils.delete_record(token, consume_info);
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
	
			
	public static void chk_user_gravatar(Context context)
			throws JSONException {
		if(ToolUtils.has_sdcard()) {
			UserTb user_table = UserTb.get_user_tb(context);
			ArrayList<UserInfo> user_infos = user_table.get_user_list();
			if(user_infos.size()>0) 
			for(int i=0; i<user_infos.size(); i++ ) {
				UserInfo user_info = user_infos.get(i);
				if(user_info.get_user_id()>0) {
					String email = user_info.get_email();
					File gravatar_path = new File(Gravatar.gravatar_path(email));
					//String gravatar_url  = Gravatar.gravatar_url(email);
					//存储卡可用并且用用户头像不存在
					if(!gravatar_path.exists()) download_gravatar_with_email(email);
				}
			}
		} 
	}
	
	public static void sync_upload_background(final Context context,final String token, final Long current_user_id) {
		if(NetUtils.has_network(context))
			 new Thread() {
				 public void run() {
					try {
						Integer unsync_count = sync_upload_tags(context, token, current_user_id);
						//record与tag关联，如果有未更新的tag则跳过更新record
						if(unsync_count == 0) {
							sync_upload_records(context, token, current_user_id);
							getFriendRecords(context, token);
							get_user_friends_info(context, token, false);
							getSelfTags(context, token, "syncWithId", current_user_id);
							getSelfRecords(context, token, "syncWithId", current_user_id);
							//SharedPreferences sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
							//getUserInfo(sharedPreferences,token, context);
						} else {
							Log.w("SyncBackGround",unsync_count+"tag没有成功更新!");
						}
						chk_user_gravatar(context);
					} catch (HttpException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				 };
			 }.start();
	 else
		 Log.w("NetUtils","No NetWork");
				 
	}
	

	public static String [] getUserInfo(SharedPreferences preferences,String token, Context context) 
			throws JSONException {
    	String [] ret_array = {"1","成功"};
	    Editor Editor = preferences.edit();
	    String url = URLs.URL_USER+"?token="+ Uri.encode(token);
        HashMap<String, Object> hash_map = ApiClient._Get(context, url);
        Log.w("getUserInfo","URL:"+ url);
        
		int statusCode  = (Integer)hash_map.get("statusCode");
		String response = (String)hash_map.get("json_str");

        if(statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_CREATED) {
		     JSONObject jsonObject = new JSONObject(response) ;
		     
	         int    user_id       = jsonObject.getInt("id");
	         String user_name     = jsonObject.getString("name");
	         String user_email    = jsonObject.getString("email");
	         String user_province = "china";//jsonObject.getString("user_province");
	         String user_register = jsonObject.getString("created_at");
	         String user_updated = jsonObject.getString("updated_at");
	         String user_gravatar = "gravatar"; //jsonObject.getString("user_gravatar");
	         UserInfo user_info = new UserInfo();
	         user_info.set_user_id(user_id);
	         user_info.set_name(user_name);
	         user_info.set_email(user_email);
	         user_info.set_created_at(user_register);
	         user_info.set_updated_at(user_updated);
	         user_info.set_info("current_user");
	         
	         UserTb user_tb = UserTb.get_user_tb(context);       
	         user_tb.insert_or_update_user(user_info);
	         
	        Editor.putLong("current_user_id",      user_id);
			Editor.putString("current_user_name",  user_name);
			Editor.putString("current_user_email", user_email);
			Editor.putString("current_user_province", user_province);
			Editor.putString("current_user_register", user_register);
			Editor.putString("current_user_gravatar", user_gravatar);
			Editor.putString("current_user_updated", user_updated);
			Editor.putString("current_user_token", token);
			Editor.putBoolean("is_login", true);
			
			//download_image_with_url(user_email);
			Editor.commit();
        } else {
        	ret_array[0] = "-1";
        	ret_array[1] = "fail:"+response;
        }

	    return ret_array;
	}
	
	public static String [] get_user_friends_info(Context context, String token, Boolean isDetailCheck) 
			throws JSONException {
    	String [] ret_array = {"1","成功"};
	    ConsumeTb consume_tb = ConsumeTb.getConsumeTb(context);
	    String ids = consume_tb.get_friends_ids();
	    
	    String url = URLs.URL_USER_FRIENDS+"?token="+ Uri.encode(token);
	    if(!isDetailCheck) url = url + "&ids=" + ids;
	    
        HashMap<String, Object> hash_map = ApiClient._Get(context, url);
        Log.w("getUserFriend","URL:"+ url);
        
		int statusCode  = (Integer)hash_map.get("statusCode");
		String response = (String)hash_map.get("json_str");

    	Log.w("userFriends","statusCode:"+statusCode);
    	Log.w("userFriends","json_str:"+response);
	    if(statusCode==HttpStatus.SC_OK) { 
	        UserListParse userListParse = new UserListParse();
	    	HashMap<String, Object> parse_json = userListParse.parseJSON(response);
	    	if((Boolean)parse_json.get("result")) {
	    	    ArrayList<UserInfo> user_infos = new  ArrayList<UserInfo>();
	    		user_infos = (ArrayList<UserInfo>)parse_json.get("user_infos");
	    		UserTb user_tb = UserTb.get_user_tb(context);
	    		for(int i= 0; i< user_infos.size(); i++) {
	    			UserInfo user_info = user_infos.get(i);
	    			Log.w("UserFriendsInfo", user_info.to_string());
	    			user_info.set_info("friend");
	    			user_tb.insert_or_update_user(user_info);
	    		}
	        } else {
	        	ret_array[0] = "-1";
	        	ret_array[1] = "fail";
	        }
        } 
	    return ret_array;
	}
	
	public static void download_gravatar_with_email(String email) {
		if(email == null || email.length() == 0) return;

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
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
	}
	public static Bitmap get_loacal_bitmap(String url) {
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
	
	// friend's records
	public static void getFriendRecords(Context context, String token) 
			throws JSONException {
	    ArrayList<ConsumeInfo> consume_infos = new  ArrayList<ConsumeInfo>();
	    ConsumeTb consume_tb = ConsumeTb.getConsumeTb(context);
	    long max_consume_id = consume_tb.get_friends_max_consume_id();
	    String url = URLs.URL_RECORD_FRIENDS+"?token="+Uri.encode(token)+"&id="+max_consume_id;
	    Log.w("FriendsRecords", url);
        HashMap<String, Object> hash_map = ApiClient._Get(context,url);
		int statusCode  = (Integer)hash_map.get("statusCode");
		String response = (String)hash_map.get("json_str");

    	Log.w("friendRecords","statusCode:"+statusCode);
	    if(statusCode==HttpStatus.SC_OK || statusCode==HttpStatus.SC_CREATED) {
	    	ConsumeListParse consumeListParse = new ConsumeListParse();
	    	HashMap<String, Object> parseJson = consumeListParse.parseJSON(response);
	    	if((Boolean)parseJson.get("result")) consume_infos = (ArrayList<ConsumeInfo>)parseJson.get("consumeInfos");
	    	 
	    	Log.w("friendRecords","parseJson:"+(Boolean)parseJson.get("result"));
	    	Log.w("friendRecords","records:"+consume_infos.size());
	    	
	    	if(consume_infos.size()>0) {
	    		ConsumeTb consume_table = ConsumeTb.getConsumeTb(context);
	    		consume_table.insertAllRecord(consume_infos,false);
	    		
	    		UIHelper.push_notice(context,"朋友消费圈","近期有"+consume_infos.size()+"笔消费记录",(int)Math.random()%66);
	    	}
	    }
	}
	
	public static void getSelfRecords(Context context, String token, String type, Long user_id) 
			throws JSONException {
	    ArrayList<ConsumeInfo> consumeInfos = new  ArrayList<ConsumeInfo>();
	    String url = URLs.URL_RECORD+"?token="+Uri.encode(token);
    	CurrentUser currentUser = CurrentUser.getCurrentUser(context, user_id);
	    if(type == "getAll") {
	    	url += "&id=0";
	    } else if(type == "syncWithUpdatedAt") {
	    	url += "&updated_at="+Uri.encode(currentUser.getLatestRecordUpdatedAt());
	    } else if(type == "syncWithId") {
	    	url += "&id="+currentUser.getMaxRecordId();
	    } else {
	    	Log.e("getSelfRecord", "Type is null!");
	    	return;
	    }
        HashMap<String, Object> hash_map = ApiClient._Get(context,url);
		int statusCode  = (Integer)hash_map.get("statusCode");
		String response = (String)hash_map.get("json_str");

    	Log.w("currentUserRecords","statusCode:"+statusCode);
	    if(statusCode==HttpStatus.SC_OK) {
	    	ConsumeListParse consumeListParse = new ConsumeListParse();
	    	HashMap<String, Object> parseJson = consumeListParse.parseJSON(response);
	    	if((Boolean)parseJson.get("result")) consumeInfos = (ArrayList<ConsumeInfo>)parseJson.get("consumeInfos");

	    	Log.w("currentUserRecords","parseJson:"+(Boolean)parseJson.get("result"));
	    	Log.w("currentUserRecords","records:"+consumeInfos.size());
	    	if(consumeInfos.size()>0) {
	    		ConsumeTb consumeTable = ConsumeTb.getConsumeTb(context);
	    		if(type == "getAll") {
	    		  consumeTable.insertAllRecord(consumeInfos,true);
	    		} else if(type == "syncWithId") {
		    		  consumeTable.insertAllRecord(consumeInfos,false);
	    		} else {
	    		  currentUser.syncRecordWithServer(consumeInfos);
	    		}
	    	}
	    }

	}
	public static void getSelfTags(Context context, String token, String type, Long userId) 
			throws JSONException {
	    ArrayList<TagInfo> tagInfos = new  ArrayList<TagInfo>();
	    String url = URLs.URL_TAG+"?token="+Uri.encode(token);
    	CurrentUser currentUser = CurrentUser.getCurrentUser(context, userId);
	    if(type == "getAll") {
	    	url += "&id=0";
	    } else if(type == "syncWithUpdatedAt") {
	    	url += "&updated_at="+Uri.encode(currentUser.getLatestTagUpdatedAt());
	    } else if(type == "syncWithId") {
	    	url += "&id="+currentUser.getMaxTagId();
	    } else {
	    	Log.e("getSelfTag", "Type is null!");
	    	return;
	    }
	    
        HashMap<String, Object> hash_map = ApiClient._Get(context,url);
		int statusCode  = (Integer)hash_map.get("statusCode");
		String response = (String)hash_map.get("json_str");

    	Log.w("currentUserRecords","statusCode:"+statusCode);
	    if(statusCode==HttpStatus.SC_OK) {
	    	TagListParse consumeListParse = new TagListParse();
	    	HashMap<String, Object> parseJson = consumeListParse.parseJSON(response);
	    	if((Boolean)parseJson.get("result")) tagInfos = (ArrayList<TagInfo>)parseJson.get("tagInfos");

	    	Log.w("currentUserRecords","parseJson:"+(Boolean)parseJson.get("result"));
	    	Log.w("currentUserRecords","records:"+tagInfos.size());
	    	if(tagInfos.size()>0) {
	    		TagTb tagTable = TagTb.getTagTb(context);
	    		if(type == "getAll") {
	    			tagTable.insertAllTag(tagInfos,true);
	    		} else if (type == "syncWithId") {
	    			tagTable.insertAllTag(tagInfos,false);
	    		} else {
	    		  currentUser.syncTagWithServer(tagInfos);
	    		}
	    	}
	    }

	}
	
	
	public static void chk_version_update(Context context) 
			throws JSONException, NameNotFoundException {
		//检测当前环境是否有网络
		if (!NetUtils.has_network(context)) return;
		
		HashMap<String, Object> http_get = ApiClient._Get(context,URLs.URL_VERSION);
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
	
	
	//创建consume
		public static String[] create_tag(String token, TagInfo tag_info) 
				throws HttpException, IOException, JSONException {
			// 从服务器接口中获取当前账号和密码的配对情况
			String[] ret_array = { "0", "return null","" };
	        Log.w("PostCreateTag", "beforePost:"+tag_info.to_string());
			org.apache.commons.httpclient.NameValuePair[] params = new org.apache.commons.httpclient.NameValuePair[] {
			  new org.apache.commons.httpclient.NameValuePair("token", token),
			  new org.apache.commons.httpclient.NameValuePair("format", "json"),
			  new org.apache.commons.httpclient.NameValuePair("tag[label]", tag_info.get_label()),
			  new org.apache.commons.httpclient.NameValuePair("tag[klass]", tag_info.get_klass()+""),
			  new org.apache.commons.httpclient.NameValuePair("tag[browser]", Build.BRAND+"_"+Build.MODEL)
			};

			HashMap<String, Object> hash_map = ApiClient._Post(URLs.URL_TAG, params);
			int statusCode  = (Integer)hash_map.get("statusCode");
			String response = (String)hash_map.get("response");
				// 请求成功
			if (statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_CREATED) {
				JSONObject jsonObject = new JSONObject(response);
				// 获取返回值
				ret_array[0] = "1";
				ret_array[1] = jsonObject.toString();
				ret_array[2] = jsonObject.getInt("id")+"&"+jsonObject.getString("updated_at");
				
			}
			Log.w("CreateTag", ret_array.toString());
			return ret_array;
		}

		
		//update a record
		public static String[] update_tag(String token, TagInfo tag_info) 
				throws HttpException, IOException, JSONException {
			String[] ret_array = { "0", "return null", "" };

	        Log.w("PostUpdate", "beforePost:"+tag_info.to_string());
			org.apache.commons.httpclient.NameValuePair[] params = new org.apache.commons.httpclient.NameValuePair[] {
			  new org.apache.commons.httpclient.NameValuePair("token", token),
			  new org.apache.commons.httpclient.NameValuePair("tag[label]", tag_info.get_label()),
			  new org.apache.commons.httpclient.NameValuePair("tag[klass]", tag_info.get_klass()+""),
			  new org.apache.commons.httpclient.NameValuePair("tag[browser]", Build.BRAND+"_"+Build.MODEL)
			};

			HashMap<String, Object> hash_map = ApiClient._Post(URLs.URL_TAG+"/"+tag_info.get_tag_id()+".json", params);
			int statusCode  = (Integer)hash_map.get("statusCode");
			String response = (String)hash_map.get("response");
			if (statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_CREATED) {
				JSONObject jsonObject = new JSONObject(response);
				ret_array[0] = "1";
				ret_array[1] = jsonObject.toString();
				ret_array[2] = jsonObject.getString("updated_at");
			}
			Log.w("UpdateTag", ret_array.toString());
			return ret_array;
		}
		

		//创建consume
		public static String[] delete_tag(String token, TagInfo tag_info) 
				throws HttpException, IOException, JSONException {
			String[] ret_array = { "0", "return null" };
	       
			String url = URLs.URL_TAG+"/"+tag_info.get_tag_id()+".json?token="+Uri.encode(token);
			HashMap<String, Object> hash_map = ApiClient._Delete(url);
			int statusCode  = (Integer)hash_map.get("statusCode");
			String response = (String)hash_map.get("response");
			
			// 未认证成功
			if (statusCode == HttpStatus.SC_UNAUTHORIZED) {;
				// 获取返回值
				ret_array[0] = "-1";
			} else {
				ret_array[0] = "1";
			}
			JSONObject jsonObject = new JSONObject(response);
			ret_array[1] = jsonObject.toString();

			return ret_array;
		}
		/**
		 * 独立处理未同步Tag数据
		 * @param context
		 * @param login_email
		 * @throws JSONException 
		 * @throws IOException 
		 * @throws HttpException 
		 */
		public static Integer sync_upload_tags(Context context,String token, Long current_user_id) 
				throws HttpException, IOException, JSONException {
			    ArrayList<TagInfo> tag_infos;
			    TagTb              tag_tb;
			    
			    tag_tb    = TagTb.getTagTb(context);
			    tag_infos = tag_tb.get_unsync_tags();
			    
				Integer un_sync_count = tag_infos.size();
				
				if(un_sync_count > 0){
					CurrentUser current_user = CurrentUser.getCurrentUser(context, current_user_id);
					for(int i = 0; i < tag_infos.size(); i++) {
						TagInfo tag_info = tag_infos.get(i);
						String[] ret_array = {"0","","-1"};
						if(tag_info.get_state().equals("create")){
						    ret_array = NetUtils.create_tag(token, tag_info);
							if (ret_array[0].equals("1")) {
								//同步成功则修改数据库内容
								String[] arr = ret_array[2].split("&");
								Integer tag_id = Integer.valueOf(arr[0]);
								String updated_at = arr[1];
								tag_info.set_tag_id(tag_id);
								tag_info.set_updated_at(updated_at);
								tag_info.set_sync((long)1);
								tag_info.set_state("");
								Log.w("NetUtils",tag_info.to_string());
								current_user.update_tag(tag_info);
	                            Log.w("NetUtils","Action:"+tag_info.get_state()+"-YES");
							} else {
								//同步失败则不做任何动作
	                            Log.w("NetUtils","Action:"+tag_info.get_state()+"-NO");
							}	
						} else if(tag_info.get_state().equals("update")){
						    ret_array = NetUtils.update_tag(token, tag_info);
							if (ret_array[0].equals("1")) {
								tag_info.set_updated_at(ret_array[2]);
								tag_info.set_sync((long)1);
								tag_info.set_state("");
								Log.w("NetUtils",tag_info.to_string());
								current_user.update_tag(tag_info);
	                            Log.w("NetUtils","Action:"+tag_info.get_state()+"-YES");
							} else {
	                            Log.w("NetUtils","Action:"+tag_info.get_state()+"-NO");
							}	
						} else if(tag_info.get_state().equals("delete")) {
						  ret_array = NetUtils.delete_tag(token, tag_info);
							if (ret_array[0].equals("1")) {
								Log.w("NetUtils",tag_info.to_string());
								current_user.destroy_record(tag_info.get_id());
	                            Log.w("NetUtils","Action:"+tag_info.to_string()+"-YES");
							} else {
	                            Log.w("NetUtils","Action:"+tag_info.to_string()+"-NO");
							}	
						} else {
							Log.e("NetUtils","Action Not Found:["+tag_info.to_string()+"]");
						}
					}	
				}
				tag_infos = tag_tb.get_unsync_tags();
				return tag_infos.size();
	    }
}