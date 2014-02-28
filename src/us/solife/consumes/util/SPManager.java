package us.solife.consumes.util;

import us.solife.consumes.entity.UserInfo;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SPManager extends Activity {
	Context context;
	static SharedPreferences sharedPreferences;
	
	public UserInfo get_current_user_info() {
		sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);
		UserInfo user_info = new UserInfo();
		user_info.set_user_id(sharedPreferences.getLong("current_user_id",-1));
		user_info.set_email(sharedPreferences.getString("current_user_email", ""));
		user_info.set_name(sharedPreferences.getString("current_user_name", ""));
		user_info.set_area(sharedPreferences.getString("current_user_province", ""));
		user_info.set_created_at(sharedPreferences.getString("current_user_register", ""));
		user_info.set_gravatar(sharedPreferences.getString("current_user_gravatar", ""));
		user_info.set_email(sharedPreferences.getString("current_user_email", ""));
		return user_info;
	}

}
