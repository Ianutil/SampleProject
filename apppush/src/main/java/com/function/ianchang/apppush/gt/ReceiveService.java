package com.function.ianchang.apppush.gt;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.function.ianchang.apppush.MessageInfo;
import com.function.ianchang.apppush.TestPushDataActivity;
import com.google.gson.Gson;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.GTServiceManager;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;

import java.io.UnsupportedEncodingException;


/**
 *
 * 个推消息推送
 *
 * 继承 GTIntentService 接收来自个推的消息, 所有消息在线程中回调, 如果注册了该服务, 则务必要在 AndroidManifest中声明, 否则无法接受消息<br>
 * onReceiveMessageData 处理透传消息<br>
 * onReceiveClientId 接收 cid <br>
 * onReceiveOnlineState cid 离线上线通知 <br>
 * onReceiveCommandResult 各种事件处理回执 <br>
 */
public class ReceiveService extends GTIntentService {

    @Override
    public void onReceiveServicePid(Context context, int i) {

    }

    @Override
    public void onReceiveClientId(Context context, String clientId) {
        Log.e(TAG, "onReceiveClientId -> " + "clientId = " + clientId);

    }

    @Override
    public void onReceiveMessageData(final Context context, GTTransmitMessage gtTransmitMessage) {
        Log.e(TAG, "onReceiveMessageData -> " + "clientId = " + "-------" + gtTransmitMessage.getMessageId());
        Log.e(TAG, "onReceiveMessageData -> " + "clientId = " + gtTransmitMessage.getTaskId() + "-------" + gtTransmitMessage.getPayloadId());

        if (gtTransmitMessage.getPayload() != null) {

            try {
                final String datas = new String(gtTransmitMessage.getPayload(), "UTF-8");

                Log.d("TAG", "接入到的数据值：" + datas);

                // 根据 makeType 跳转到相对应的首页界面
                final MessageInfo messageInfo = new Gson().fromJson(datas, MessageInfo.class);

                Handler handler = new Handler(context.getMainLooper());
                if (!TextUtils.isEmpty(messageInfo.url)) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, datas, Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(context, TestPushDataActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("url", messageInfo.url);
                            context.startActivity(intent);
                        }
                    });

                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean b) {

    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage gtCmdMessage) {
        Log.e(TAG, "onReceiveCommandResult -> " + "clientId = " + gtCmdMessage.toString() + "-------" + gtCmdMessage.getAction());
    }
}
