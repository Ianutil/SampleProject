/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  MainActivity
 * Created by  ianchang on 2018-08-01 18:21:45
 * Last modify date   2018-08-01 18:21:44
 */

package com.ian.printer;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ian.printer.bean.BillInfo;
import com.ian.printer.bean.GoodInfo;
import com.ian.printer.printer.OnDataReceiveListener;
import com.ian.printer.printer.PrinterImageUtils;
import com.ian.printer.printer.PrinterManager;
import com.ian.printer.printer.PrinterTextUtils;
import com.ian.printer.printer.SerialPort;

import java.io.FileDescriptor;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class LockActivity extends AppCompatActivity implements View.OnClickListener, OnDataReceiveListener {
    private static final String TAG = LockActivity.class.getSimpleName();

    private PrinterManager printer;
    /***
     * Fixme /dev/ttyS0  /dev/ttyS1  /dev/ttyS2  /dev/ttyS3   /dev/ttyS4
     * Fixme      4800        9600         19200    115200
     */
    private String[] devices = {
            "/dev/ttyS0",
            "/dev/ttyS1",
            "/dev/ttyS2",
            "/dev/ttyS3",
            "/dev/ttyS4"
    };

    private String[] baudRates = {
            "4800",
            "9600",
            "19200",
            "115200",
    };

    private Handler handler = new Handler();
    private String device, baudRate;

    private TextView printerStatusTV;

    private SerialPort serialPort;

    private PendingIntent mPermissionIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);

        findViewById(R.id.button_open).setOnClickListener(this);
        findViewById(R.id.button_close).setOnClickListener(this);

        printerStatusTV = (TextView) findViewById(R.id.printer_status);

        RadioGroup group = (RadioGroup) findViewById(R.id.tty_group);
        group.setOnCheckedChangeListener(ttyListener);

        group = (RadioGroup) findViewById(R.id.rb_group);
        group.setOnCheckedChangeListener(rbListener);

        // FIXME: 2018/9/6 创建打印机对象
        printer = new PrinterManager(this);
        printer.setOnDataReceiveListener(this);
        device = devices[3];
        baudRate = baudRates[1];
    }

    @Override
    protected void onStart() {
        super.onStart();
        register();
    }

    @Override
    protected void onStop() {
        printer.closePrinter();

        unregister();
        printer = null;
        super.onStop();
    }

    private RadioGroup.OnCheckedChangeListener ttyListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

            switch (checkedId) {
                case R.id.tty_s0:
                    device = devices[0];
                    break;
                case R.id.tty_s1:
                    device = devices[1];
                    break;
                case R.id.tty_s2:
                    device = devices[2];
                    break;
                case R.id.tty_s3:
                    device = devices[3];
                    break;
                case R.id.tty_s4:
                    device = devices[4];
                    break;
            }

            Log.i(TAG, "选择设备:" + device);

        }
    };

    private RadioGroup.OnCheckedChangeListener rbListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            switch (checkedId) {
                case R.id.rb_4800:
                    baudRate = baudRates[0];
                    break;
                case R.id.rb_9600:
                    baudRate = baudRates[1];
                    break;
                case R.id.rb_19200:
                    baudRate = baudRates[2];
                    break;
                case R.id.rb_115200:
                    baudRate = baudRates[3];
                    break;
            }

            Log.i(TAG, "选择频率:" + baudRate);
        }
    };

    @Override
    public void onClick(View v) {

        // 获取USB设备
        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        int count = manager.getDeviceList().size();
        if (count > 0) {
            Set<String> keys = manager.getDeviceList().keySet();
            UsbDevice device = null;
            for (String key : keys) {
                device = manager.getDeviceList().get(key);

                boolean flag = manager.hasPermission(device);
                Log.e("TAG", "vid=" + device.getVendorId() + "---pid=" + device.getProductId() + " count:"+device.getInterfaceCount()+ " name:"+device.getDeviceName() + " is:"+flag);

                if(!flag){
                    Log.e("TAG", "该设备没有权限"+device.getDeviceName());
                    manager.requestPermission(device, mPermissionIntent);

                    return;
                }
            }

        }

        String state = "";
        switch (v.getId()) {
            case R.id.button_open:
                serialPort = printer.openPrinter(device, Integer.valueOf(baudRate));
                state = "打开串口";
                break;
            case R.id.button_close:
                printer.closePrinter();
                state = "关闭串口";
                break;
        }

        if (serialPort == null) {
            printerStatusTV.setText(state + ":设备连接失败");
        } else {
            FileDescriptor fileDescriptor = serialPort.mFd;
            String result = fileDescriptor.toString();
            Log.e(TAG, "--------result:" + result);
            printerStatusTV.setText(state + " " + result);
        }

        printerStatusTV.postInvalidate();
    }

    @Override
    public void onDataReceive(final String data) {

        Log.d(TAG, "进入数据监听事件中---->onDataReceive:" + data);
        //
        //在线程中直接操作UI会报异常：ViewRootImpl$CalledFromWrongThreadException
        //解决方法：handler
        handler.post(new Runnable() {
            @Override
            public void run() {
                printerStatusTV.setText(data);
            }
        });
    }


    public void testPrintStatus(View view) {
        if (serialPort == null) {
            serialPort = printer.openPrinter(device, Integer.valueOf(baudRate));
        }

        byte[] data = {(byte) 0x55, (byte) 0x01, (byte) 0x10, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x67};
        Log.e("TAG", "状态命令:" + Arrays.toString(data));
        printer.sendSerialPort(data);
    }

    public void openLock(View view) {
        if (serialPort == null) {
            serialPort = printer.openPrinter(device, Integer.valueOf(baudRate));
        }

        String msg = "55 01 12 00 00 00 01 69";

        byte[] data = toByteArray(msg);
//        byte[] data = {(byte) 0x55, (byte) 0x01, (byte) 0x12, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x69};
//        byte[] data = {(byte) 0x55, (byte) 0x01, (byte) 0x12, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x69};
        Log.e("TAG", "开门命令:" + Arrays.toString(data));

        printer.sendSerialPort(data);

    }

    public void closeLock(View view) {
        if (serialPort == null) {
            serialPort = printer.openPrinter(device, Integer.valueOf(baudRate));
        }

        //获取UsbManager实例方法
//        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);

        byte[] data = {(byte) 0x55, (byte) 0x01, (byte) 0x11, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x68};
        Log.e("TAG", "关门命令:" + Arrays.toString(data));

        printer.sendSerialPort(data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "请求权限成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "请求权限失败", Toast.LENGTH_SHORT).show();
        }

    }


    private void register(){
        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent("com.ian.usb"), 0);
        IntentFilter filter = new IntentFilter("com.ian.usb");
        filter.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
        registerReceiver(mUsbReceiver, filter);
    }

    private void unregister(){

        unregisterReceiver(mUsbReceiver);
    }


    private BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.e("TAG", "----------->"+intent.getAction());

            Toast.makeText(context, "授权成功", Toast.LENGTH_SHORT).show();
//            context.startActivity(new Intent(context, LockActivity.class));
        }
    };

    /**
     * 将String转化为byte[]数组
     * @param arg
     *            需要转换的String对象
     * @return 转换后的byte[]数组
     */
    private byte[] toByteArray(String arg) {
        if (arg != null) {
			/* 1.先去除String中的' '，然后将String转换为char数组 */
            char[] NewArray = new char[1000];
            char[] array = arg.toCharArray();
            int length = 0;
            for (int i = 0; i < array.length; i++) {
                if (array[i] != ' ') {
                    NewArray[length] = array[i];
                    length++;
                }
            }
			/* 将char数组中的值转成一个实际的十进制数组 */
            int EvenLength = (length % 2 == 0) ? length : length + 1;
            if (EvenLength != 0) {
                int[] data = new int[EvenLength];
                data[EvenLength - 1] = 0;
                for (int i = 0; i < length; i++) {
                    if (NewArray[i] >= '0' && NewArray[i] <= '9') {
                        data[i] = NewArray[i] - '0';
                    } else if (NewArray[i] >= 'a' && NewArray[i] <= 'f') {
                        data[i] = NewArray[i] - 'a' + 10;
                    } else if (NewArray[i] >= 'A' && NewArray[i] <= 'F') {
                        data[i] = NewArray[i] - 'A' + 10;
                    }
                }
				/* 将 每个char的值每两个组成一个16进制数据 */
                byte[] byteArray = new byte[EvenLength / 2];
                for (int i = 0; i < EvenLength / 2; i++) {
                    byteArray[i] = (byte) (data[i * 2] * 16 + data[i * 2 + 1]);
                }
                return byteArray;
            }
        }
        return new byte[] {};
    }

}
