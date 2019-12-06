package com.function.ianchang.apppush;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.function.ianchang.apppush.gt.PushService;
import com.function.ianchang.apppush.gt.ReceiveService;
import com.igexin.sdk.PushManager;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.common.Constants;

import cn.jpush.android.api.JPushInterface;

public class TestPushActivity extends Activity implements XGIOperateCallback{

    private MessageInfo mMessageInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_push);

        // ***************个推***************
        // PushService 为第三方自定义推送服务
        PushManager.getInstance().initialize(this.getApplicationContext(), PushService.class);
        // MyService为第三方自定义接收推送服务
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), ReceiveService.class);


        // ***************极光***************
        JPushInterface.setDebugMode(true);
        // 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
        JPushInterface.init(this);


        long xg_access_id = 0;
        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);

            xg_access_id = appInfo.metaData.getInt("XG_ACCESS_ID");

            String jpush_appkey = appInfo.metaData.getString("JPUSH_APPKEY");
            String jpush_pkgname = appInfo.metaData.getString("JPUSH_PKGNAME");

            Log.d("TAG", "jpush_appkey=" + jpush_appkey + "," + jpush_pkgname + "----->xg_access_id="+xg_access_id);
            Log.d("TAG", "----->xg_access_id="+xg_access_id);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        }


        // ***************腾讯信鸽***************
        //代码内动态注册access ID
        XGPushConfig.setAccessId(this, xg_access_id);
        //开启信鸽的日志输出，线上版本不建议调用
        XGPushConfig.enableDebug(this, true);


        mMessageInfo = new MessageInfo();
    }

    public void startService(View view) {


        String clientId = PushManager.getInstance().getClientid(this);
        String version = PushManager.getInstance().getVersion(this);

        // 个推
        mMessageInfo.clickId = clientId;
        Toast.makeText(this, "开启服务完成", Toast.LENGTH_SHORT).show();
        Log.d("TAG", "开启服务完成clientId=" + clientId + " version ---->" + version);
        startService(new Intent(this, PushService.class));


        JPushInterface.init(this);
        JPushInterface.resumePush(getApplicationContext());

        String rid = JPushInterface.getRegistrationID(getApplicationContext());
        Log.d("TAG", "开启极光服务完成---->" + rid);

        // 极光
        mMessageInfo.clickId = rid;

        // 腾讯信鸽 注册服务
        /*
        注册信鸽服务的接口
        如果仅仅需要发推送消息调用这段代码即可
        */
        XGPushManager.registerPush(getApplicationContext(), this);

    }

    @Override
    public void setIntent(Intent newIntent) {
        super.setIntent(newIntent);
        Log.d("TAG", "setIntent");

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("TAG", "onNewIntent");
    }

    @Override
    protected void onResume() {
        super.onResume();

        XGPushClickedResult click = XGPushManager.onActivityStarted(this);
        Log.d("TPush", "onResumeXGPushClickedResult:" + click);
        if (click != null) { // 判断是否来自信鸽的打开方式
            Toast.makeText(this, "通知被点击:" + click.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        XGPushManager.onActivityStoped(this);
    }

    @Override
    public void onSuccess(Object data, int flag) {
        Log.e(Constants.LogTag, "+++ register push sucess. token:" + data+"flag" +flag);

    }
    @Override
    public void onFail(Object data, int errCode, String msg) {
        Log.e(Constants.LogTag, "+++ register push fail. token:" + data + ", errCode:" + errCode + ",msg:" + msg);

    }
}
