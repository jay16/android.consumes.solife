package us.solife.consumes.recevier;

import us.solife.consumes.Main;
import us.solife.iconsumes.R;
import us.solife.consumes.util.NetUtils;
import us.solife.consumes.util.ToolUtils;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class TimerService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        //intent.getAction().equals("short")
    	
    	if(NetUtils.hasNetWork(context)) {
    		NetUtils.upload_unsync_consumes_background(context, intent.getAction());
            //Toast.makeText(context, ToolUtils.getStandardDetailDate()+"\n成功同步数据！", Toast.LENGTH_LONG).show();
    	} else {
            //Toast.makeText(context, "网络有问题,同步数据失败!", Toast.LENGTH_LONG).show();
    	}

    }

}