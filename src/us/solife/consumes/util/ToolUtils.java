package us.solife.consumes.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
}
