package org.estgroup.phphub.common.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.estgroup.phphub.ui.view.MainActivity;
import org.estgroup.phphub.ui.view.ReplyActivity;
import org.estgroup.phphub.ui.view.topic.TopicDetailsActivity;
import org.json.JSONObject;

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
            boolean openedOtherActivity = handleOpenNotification(context, bundle);

            if (!openedOtherActivity) {
                Intent i = new Intent();
                i.setClass(context, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
            }
        }
    }

    /**
     * 处理用户打开通知操作
     *
     * @param context Context
     * @param bundle  Bundle
     * @return boolean
     */
    private boolean handleOpenNotification(Context context, Bundle bundle) {
        Log.d(TAG, "用户点击打开了通知");
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);

        Intent intent = null;
        try {
            if (extras == null) return false;

            JSONObject extraJson = new JSONObject(extras);

            if (extraJson.has("replies_url")) {
                String replies_url = extraJson.getString("replies_url");
                intent = ReplyActivity.getCallingIntent(context, replies_url);
            } else if (extraJson.has("topic_id")) {
                int topic_id = extraJson.getInt("topic_id");
                intent = TopicDetailsActivity.getCallingIntent(context, topic_id);
            } else {
                return false;
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
