package us.solife.consumes.recevier;

import us.solife.consumes.util.NetUtils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class TimerService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        //intent.getAction().equals("short")
    	
    	if(NetUtils.has_network(context)) {

    		SharedPreferences sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
    	    Long current_user_id = sharedPreferences.getLong("current_user_id", -1);
    		NetUtils.sync_upload_background(context, intent.getAction(), current_user_id);
            //Toast.makeText(context, ToolUtils.getStandardDetailDate()+"\n成功同步数据！", Toast.LENGTH_LONG).show();
    	} else {
            //Toast.makeText(context, "网络有问题,同步数据失败!", Toast.LENGTH_LONG).show();
    	}

    }

}