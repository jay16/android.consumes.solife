package us.solife.consumes.util;

import java.text.SimpleDateFormat;
import android.util.Base64;
import java.util.Calendar;
import java.util.Date;



import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ParseException;
import android.os.Environment;

public class ToolUtils {
	
	/**
	 * 根据日期判断星期
	 * @param context
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String get_week_name(String date_str) {
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd");
		String week = "未知";
		try {
		    Date date = (Date)sdf.parse(date_str);
		    Calendar calendar = Calendar.getInstance();
		    calendar.setTime(date);  
		    int week_index = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		    String[] weeks = {"日","一","二","三","四","五","六"};
		    week =  '周'+weeks[week_index];
		} catch (ParseException e) {
			e.printStackTrace();
		}catch(java.text.ParseException e){
			e.printStackTrace();
		}
		
		return week;
	}
	
	@SuppressLint("SimpleDateFormat")
	public static int get_week_number(String date_str) throws java.text.ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cl = Calendar.getInstance();
		Date date = (Date)sdf.parse(date_str);
		//df.parse(y_m_d)
		cl.setTime(date); 
		int week = cl.get(Calendar.WEEK_OF_YEAR); 
		return week;
	}
	
	@SuppressLint("SimpleDateFormat")
	public static String get_ymd_date() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String y_m_d = df.format(new Date());
		return y_m_d;
	}	
	
	@SuppressLint("SimpleDateFormat")
	public static String get_ymdhms_date() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String y_m_d = df.format(new Date());
		return y_m_d;
	}
	
	@SuppressLint("SimpleDateFormat")
	public static String get_ymdhmsw_date() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String y_m_d = df.format(new Date());
		String w_name = get_week_name(y_m_d.substring(0,10));
		return y_m_d + " " + w_name;
	}
	@SuppressLint("SimpleDateFormat")
	public static String get_ymdw_date() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String y_m_d = df.format(new Date());
		String w_name = get_week_name(y_m_d.substring(0,10));
		return y_m_d + " " + w_name;
	}
	
	@SuppressLint("SimpleDateFormat")
	public static String add_dates(String date_str, Integer num) 
			throws java.text.ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = dateFormat.parse(date_str.substring(0, 19));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String ret_str = dateFormat.format(new Date(date.getTime() + num * 24 * 60 * 60 * 1000));
		ret_str += " " + get_week_name(ret_str.substring(0,10));
		return ret_str;
	}
	
	public static boolean has_sdcard() {
	     String status = Environment.getExternalStorageState();
	     if (status.equals(Environment.MEDIA_MOUNTED)) {
	         return true;
	     } else {
	         return false;
	     }
	 }
	public static String generate_user_token(String email, String password) {
		String _n1 = email.length() + "";
		String _n2 = _n1.length() + "";
		String token = _n2 + _n1 + email + password;
		
		return Base64.encodeToString(token.getBytes(),Base64.DEFAULT);
	}
}
