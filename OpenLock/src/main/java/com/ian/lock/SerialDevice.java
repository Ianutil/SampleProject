package com.ian.lock;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.wch.ch34xuartdriver.CH34xUARTDriver;

/**
 * Created by ianchang on 2018/9/13.
 */

public class SerialDevice implements Runnable {

    private static final String USB_ACTION = "com.ian.usb";

    private CH34xUARTDriver mDriver;

    private Context mContext;
    private PendingIntent mPermissionIntent;

    private UsbManager mUsbManager;

    private ExecutorService mExecutorService;
    private String mDeviceName;

    private OnDeviceStateListener mOnDeviceStatusListener;

    // 配置串口波特率，函数说明可参照编程手册
    public int baudRate = 9600;

    public SerialDevice(Context context, String name) {
        this.mContext = context;

        mDeviceName = name;

        mUsbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        mDriver = new CH34xUARTDriver(mUsbManager, context, USB_ACTION);

        mExecutorService = Executors.newSingleThreadExecutor();
    }

    public void onStart() {
        Log.e("TAG", "onStart");

        register();

        boolean isSupport = isSupportUSBDevice(mDriver);

        if (!isSupport) {
            return;
        }

        try {
            boolean isPermission = checkPermission(mUsbManager);
            if (!isPermission) {
                sendStatus("打开设备失败!");
                Toast.makeText(mContext, "打开设备失败!", Toast.LENGTH_SHORT).show();
                return;
            }
            openDevice();
            mExecutorService.execute(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void onStop() {

        try {

            Log.e("TAG", "onStop");
            unregister();

            if (mDriver != null) {
                mDriver.CloseDevice();
                mDriver = null;
            }

            if (mExecutorService != null && !mExecutorService.isShutdown()) {
                mExecutorService.shutdown();
            }

            mExecutorService = null;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void openDevice() {
        try {

            if (mDriver == null) {
                mDriver = new CH34xUARTDriver(mUsbManager, mContext, USB_ACTION);
            }

            // ResumeUsbList方法用于枚举CH34X设备以及打开相关设备
            mDriver.ResumeUsbList();

            byte dataBit = 8;
            byte stopBit = 1;
            byte parity = 0;
            byte flowControl = 0;
            Log.e("TAG", "rate:" + baudRate);
            mDriver.SetConfig(baudRate, dataBit, stopBit, parity, flowControl);
            // 对串口设备进行初始化操作
            mDriver.UartInit();

            sendStatus("打开设备成功");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int openLock() {

        if (mDriver == null) {
            openDevice();
        }

        String msg = "55 01 12 00 00 00 01 69";
        sendStatus(msg);

        byte[] data = ToolUtil.toByteArray(msg.toString());
        // 写数据，第一个参数为需要发送的字节数组，第二个参数为需要发送的字节长度，返回实际发送的字节长度
        int result = mDriver.WriteData(data, data.length);

        return result;
    }

    public int closeLock() {

        if (mDriver == null) {
            openDevice();
        }

        String msg = "55 01 11 00 00 00 01 68";
        sendStatus(msg);

        byte[] data = ToolUtil.toByteArray(msg.toString());
        //写数据，第一个参数为需要发送的字节数组，第二个参数为需要发送的字节长度，返回实际发送的字节长度
        int result = mDriver.WriteData(data, data.length);

        return result;

    }

    public int checkStatusOfLock() {

        if (mDriver == null) {
            openDevice();
        }

        String msg = "55 01 10 00 00 00 01 67";

        sendStatus(msg);

        byte[] data = ToolUtil.toByteArray(msg.toString());
        // 写数据，第一个参数为需要发送的字节数组，第二个参数为需要发送的字节长度，返回实际发送的字节长度
        int result = mDriver.WriteData(data, data.length);

        return result;
    }


    @Override
    public void run() {
        byte[] buffer = new byte[64];

        while (true) {

            if (mDriver == null) return;

            try {
                int length = mDriver.ReadData(buffer, buffer.length);

                if (length > 0) {
                    String data = ToolUtil.toHexString(buffer, length);

                    Log.e("TAG", "--------->" + data + " len=" + length + " code:" + new String(buffer, 0, length, "UTF-8"));

                    sendStatus(new String(buffer, 0, length));
                }

//                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    private boolean checkPermission(UsbManager manager) {

        int count = manager.getDeviceList().size();

        if (count > 0) {
            Set<String> keys = manager.getDeviceList().keySet();
            UsbDevice device;
            for (String key : keys) {

                device = manager.getDeviceList().get(key);

                boolean flag = manager.hasPermission(device);
                Log.e("TAG", "vid=" + device.getVendorId() + "---pid=" + device.getProductId() + " count:" + device.getInterfaceCount() + " name:" + device.getDeviceName() + " is:" + flag);

                if (!flag) {
                    Log.e("TAG", "该设备没有权限" + device.getDeviceName());

                    sendStatus("该设备没有权限" + device.getDeviceName());

                    manager.requestPermission(device, mPermissionIntent);

                    return false;
                }
            }

            return true;
        }
        return false;
    }

    private void register() {

        try {
            mPermissionIntent = PendingIntent.getBroadcast(mContext, 0, new Intent("com.ian.usb"), 0);
            IntentFilter filter = new IntentFilter("com.ian.usb");
            filter.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
            mContext.registerReceiver(mUsbReceiver, filter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unregister() {

        try {
            mContext.unregisterReceiver(mUsbReceiver);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.e("TAG", "----------->" + intent.getAction());

            onStart();

        }
    };

    /********
     * 是否支持USB设备
     * @param drive
     * @return
     */
    private boolean isSupportUSBDevice(CH34xUARTDriver drive) {

        if (drive == null) return false;

        if (drive.UsbFeatureSupported()) {
            return true;
        }

        sendStatus("您的手机不支持USB HOST，请更换其他手机再试");

        Dialog dialog = new AlertDialog.Builder(mContext)
                .setTitle("提示")
                .setMessage("您的手机不支持USB HOST，请更换其他手机再试！")
                .setPositiveButton("确认",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0,
                                                int arg1) {
                                System.exit(0);
                            }
                        }).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        return false;
    }


    public void setOnDeviceStatusListener(OnDeviceStateListener listener) {
        mOnDeviceStatusListener = listener;
    }


    private void sendStatus(String msg) {
        if (mOnDeviceStatusListener != null) {
            mOnDeviceStatusListener.onDeviceState(mDeviceName, msg);
        }
    }

    public interface OnDeviceStateListener {
        void onDeviceState(String name, String msg);

    }

}
