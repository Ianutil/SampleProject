package com.example.ianchang.myapplication.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.function.ianchang.simpleaidl.BLPackageInfo;
import com.function.ianchang.simpleaidl.IBLPackageListener;

/**
 * Created by ianchang on 2017/12/25.
 */

public class MyService extends Service{

    @Override
    public void onCreate() {
        super.onCreate();

        log("BackgroundService->onCreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        log("BackgroundService->onCreate:"+intent.getAction());
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
         super.onStartCommand(intent, flags, startId);
        log("BackgroundService->onStartCommand:"+flags+" "+startId);

        return START_STICKY;
    }

    private IBLPackageListener.Stub binder = new IBLPackageListener.Stub() {
        @Override
        public void setBLPackageInfo(BLPackageInfo info) throws RemoteException {

            log("IBLPackageListener->onPackageInfo:");

            info.name = "util_c";
        }

        @Override
        public BLPackageInfo getBLPackageInfo() throws RemoteException {
            log("IBLPackageListener->getBLPackageInfo:");

            BLPackageInfo info = new BLPackageInfo();
            info.name = "util_c";
            return info;
        }
    };


    private void log(String msg){
        Log.d("BackgroundService", msg);
    }
}
