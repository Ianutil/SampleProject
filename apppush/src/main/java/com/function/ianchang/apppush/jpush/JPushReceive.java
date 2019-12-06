package com.function.ianchang.apppush.jpush;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.function.ianchang.apppush.MessageInfo;
import com.function.ianchang.apppush.TestPushActivity;
import com.google.gson.Gson;

import cn.jpush.android.api.JPushInterface;


/**********
 * 极光推送消息接收
 */
public class JPushReceive extends BroadcastReceiver {
    private NotificationManager nm;
    private static final String TAG = JPushReceive.class.getSimpleName();
    
    @Override
    public void onReceive(Context context, Intent intent) {
        if (null == nm) {
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        Bundle bundle;
        bundle = intent.getExtras();
        Log.d(TAG, "onReceive - " + intent.getAction() + ", extras: " + bundle.toString());

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            Log.d(TAG, "JPush用户注册成功");

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "接受到推送下来的自定义消息");

            // Push Talk messages are push down by custom message format
            processCustomMessage(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "接受到推送下来的通知");

            receivingNotification(context,bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "用户点击打开了通知");

            openNotification(context,bundle);

        } else {
            Log.d(TAG, "Unhandled intent - " + intent.getAction());
        }
    }

    private void processCustomMessage(Context context, Bundle bundle) {
        String title = bundle.getString(JPushInterface.EXTRA_TITLE);
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//        if (TextUtils.isEmpty(title) ||) {
            Log.w(TAG, "Unexpected: empty title (friend). Give up"+message+":   "+title);
//            return;
//        }

        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        MessageInfo messageInfo = new Gson().fromJson(extras, MessageInfo.class);
        Log.w(TAG, "--------------------------->"+messageInfo.makeType + ", "+messageInfo.url);

    }

    private void receivingNotification(Context context, Bundle bundle){
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        Log.d(TAG, " title : " + title);
        String message = bundle.getString(JPushInterface.EXTRA_ALERT);
        Log.d(TAG, "message : " + message);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Log.d(TAG, "extras : " + extras);
    }

    private void openNotification(Context context, Bundle bundle){
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        MessageInfo messageInfo = new Gson().fromJson(extras, MessageInfo.class);
        Log.w(TAG, "--------------------------->"+messageInfo.makeType + ", "+messageInfo.url);


        Intent mIntent = new Intent(context, TestPushActivity.class);
        mIntent.putExtra("url", messageInfo.url);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(mIntent);
    }
}
