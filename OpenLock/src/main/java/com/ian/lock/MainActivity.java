package com.ian.lock;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SerialDevice.OnDeviceStateListener{

    private TextView mTextStatus;

    private StringBuilder mStrBuilder;
    private SerialDevice mDevice;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 保持常亮的屏幕的状态
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);

        mTextStatus = (TextView)findViewById(R.id.printer_status);

        mStrBuilder = new StringBuilder();
        
        mDevice = new SerialDevice(this, "Lock");
        mDevice.baudRate = 19200;
//        mDevice.baudRate = 9600;
        mDevice.setOnDeviceStatusListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mDevice.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mDevice.onStop();
    }

    private void showMessage(final String msg){

        if (TextUtils.isEmpty(msg)) return;

        mTextStatus.post(new Runnable() {
            @Override
            public void run() {
                mTextStatus.setText(msg);

            }
        });
    }
    
    public void lockStatus(View view) {
        int result = mDevice.checkStatusOfLock();

        mStrBuilder.append("锁状态:");
        mStrBuilder.append(result);
        mStrBuilder.append("\n");

        showMessage(mStrBuilder.toString());
    }

    public void openLock(View view) {
        int result = mDevice.openLock();

        mStrBuilder.append("开锁命令:");
        mStrBuilder.append(result);
        mStrBuilder.append("\n");

        showMessage(mStrBuilder.toString());
    }

    public void closeLock(View view) {
        int result = mDevice.closeLock();
        mStrBuilder.append("关锁命令:");
        mStrBuilder.append(result);
        mStrBuilder.append("\n");

        showMessage(mStrBuilder.toString());
    }

    @Override
    public void onDeviceState(String name, String msg) {
        Log.d("TAG", "name:"+name+" msg:"+msg);
        showMessage(name+ ":"+System.currentTimeMillis()+" data:"+msg);
    }
}
