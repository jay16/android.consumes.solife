package us.solife.consumes.util;

import java.text.SimpleDateFormat;
import android.util.Base64;
import java.util.Calendar;
import java.util.Date;


import android.content.Context;
import android.net.ParseException;
import android.os.Environment;

public class ToolUtils {
	
	/**
	 * 根据日期判断星期
	 * @param context
	 * @return
	 */
	public static String getWeekName(String date_str) {
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
	
	public static int getWeekNumber(String date_str) throws java.text.ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cl = Calendar.getInstance();
		Date date = (Date)sdf.parse(date_str);
		//df.parse(y_m_d)
		cl.setTime(date); 
		int week = cl.get(Calendar.WEEK_OF_YEAR); 
		return week;
	}
	
	public static String getStandardDate() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String y_m_d = df.format(new Date());
		return y_m_d;
	}	
	
	public static String getStandardDetailDate() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String y_m_d = df.format(new Date());
		return y_m_d;
	}
	
	public static boolean hasSdcard() {
	     String status = Environment.getExternalStorageState();
	     if (status.equals(Environment.MEDIA_MOUNTED)) {
	         return true;
	     } else {
	         return false;
	     }
	 }
	public static String generateUserToken(String email, String password) {
		String _n1 = email.length() + "";
		String _n2 = _n1.length() + "";
		String token = _n2 + _n1 + email + password;
		
		return Base64.encodeToString(token.getBytes(),Base64.DEFAULT);
	}
}
