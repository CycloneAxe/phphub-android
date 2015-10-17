package org.estgroup.phphub.common.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.estgroup.phphub.ui.view.MainActivity;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Xuan on 10/17/15.
 */
public class JPushReReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";


    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Intent i = new Intent();
            i.setClass(context, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(i);
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
        }
    }
}
