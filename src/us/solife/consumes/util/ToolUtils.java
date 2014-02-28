package us.solife.consumes.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


import android.content.Context;
import android.net.ParseException;

public class ToolUtils {
	
	/**
	 * ���������ж�����
	 * @param context
	 * @return
	 */
	public static String getWeekName(String date_str) {
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd");
		String week = "δ֪";
		try {
		    Date date = (Date)sdf.parse(date_str);
		    Calendar calendar = Calendar.getInstance();
		    calendar.setTime(date);  
		    int week_index = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		    String[] weeks = {"��","һ","��","��","��","��","��"};
		    week =  '��'+weeks[week_index];
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
}
