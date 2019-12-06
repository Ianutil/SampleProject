/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  SerialPos
 * Created by  ianchang on 2018-08-03 09:59:48
 * Last modify date   2017-12-13 11:10:08
 */

package com.ian.printer.printer;

import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerialPort {

    private static final String TAG = SerialPort.class.getSimpleName();

    public FileDescriptor mFd;
    private FileInputStream mFileInputStream;
    private FileOutputStream mFileOutputStream;

    static {//加载jni下的C文件库
        Log.d(TAG, "本地库加载中");
        System.loadLibrary("serial_port_ian");
    }


    /************
     * 串口通信
     * @param device
     * @param baudRate
     * @param flags
     * @throws SecurityException
     * @throws IOException
     */
    public SerialPort(File device, int baudRate, int flags) throws SecurityException, IOException {

        //检查访问权限，如果没有读写权限，进行文件操作，修改文件访问权限
        if (!device.canRead() || !device.canWrite()) {

            try {
                //通过挂载到linux的方式，修改文件的操作权限
                Process su = Runtime.getRuntime().exec("/system/bin/su");
                String cmd = "chmod 777 " + device.getAbsolutePath() + "\n" + "exit\n";
                su.getOutputStream().write(cmd.getBytes());

                if ((su.waitFor() != 0) || !device.canRead() || !device.canWrite()) {
                    Log.e("SerialPort", "SerialPort: 没有权限");
                    throw new SecurityException();
                }
            } catch (Exception e) {
                e.printStackTrace();
//                throw new SecurityException();
            }
        }

        mFd = open(device.getAbsolutePath(), baudRate, flags);

        if (mFd == null) {
            Log.e(TAG, "native open returns null");
            throw new IOException();
        }

        mFileInputStream = new FileInputStream(mFd);
        mFileOutputStream = new FileOutputStream(mFd);
    }

    // Getters and setters
    public InputStream getInputStream() {
        return mFileInputStream;
    }

    public OutputStream getOutputStream() {
        return mFileOutputStream;
    }


    // JNI(调用java本地接口，实现串口的打开和关闭)
    private native static FileDescriptor open(String path, int baudrate, int flags);

    public native void close();


}
