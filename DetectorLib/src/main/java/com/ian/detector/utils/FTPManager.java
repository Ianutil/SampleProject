package com.ian.detector.utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * Created by ianchang on 2018/8/24.
 *
 * @see #connectFTP()
 * @see #connectService()
 * @see #startService()
 * @see #stopService()
 */

public class FTPManager {

    private static final String TAG = FTPManager.class.getSimpleName();

    private static final int KEY_CONNECT = 10000;
    private static final int KEY_SEND_MESSAGE = 10001;
    private static final int KEY_RECEIVE_MESSAGE = 10002;
    private static final int KEY_UPLOAD_FILE = 10003;

    private static FTPManager instance;

    private FTPClient mClient;
    private Socket mSocket;
    private PrintWriter mOutput;
    private BufferedInputStream mInput;

    private HandlerThread mUploadThread;
    private Handler mHandler;

    private OnReceiverUploadDataListener mOnReceiveDataListener;

    private FTPManager() {
    }

    public static FTPManager shareInstance() {

        if (instance == null) {
            synchronized (FTPManager.class) {
                if (instance == null) {

                    instance = new FTPManager();

                }
            }
        }

        return instance;
    }


    public void startService(){

        mUploadThread = new HandlerThread("FTPUpload-Thread");

        mUploadThread.start();

        mHandler = new Handler(mUploadThread.getLooper());

        mHandler.post(new Runnable() {
            @Override
            public void run() {

                try {
                    connectFTP();

                    connectService();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });


        // 开始接收消息
        mHandler.postDelayed(mUploadRunnable, 3000);
    }


    public void stopService(){
        try {

            if (mUploadThread != null){
                mUploadThread.quitSafely();
                mUploadThread = null;
            }

            if (mHandler != null){
                mHandler.removeCallbacks(mUploadRunnable);
                mHandler = null;
            }

            if (mClient != null) {
                if (mClient.isConnected())
                    mClient.disconnect();
                mClient = null;
            }

            if (mSocket != null){
                if (!mSocket.isInputShutdown()){
                    mSocket.shutdownInput();
                }

                if (!mSocket.isOutputShutdown()){
                    mSocket.shutdownOutput();
                }

                mSocket.close();
            }

            if (mOutput != null){
                mOutput.close();
            }

            if (mInput != null){
                mInput.close();
            }

            mSocket = null;
            mOutput = null;
            mInput = null;
            instance = null;
            mClient = null;
            Log.e(TAG, "关闭FTP连接...");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String uploadFile(String srcFile, String remoteFile) {

        String data= null;

        try {
            Log.e(TAG, "上传资源文件："+srcFile);


            if (TextUtils.isEmpty(srcFile) || TextUtils.isEmpty(remoteFile)) {
                Log.e(TAG, "上传资源路径为空");
                return data;
            }

            connectFTP();

            // 设置文件名
            File src = new File(srcFile);
            if (!src.exists()){
                Log.e(TAG, "上传资源文件不存");
                return data;
            }

            // 上传单个文件
            FileInputStream fos = new FileInputStream(src);
            boolean flag = mClient.storeFile(remoteFile, fos);
            fos.close();

            Log.e(TAG, "是否上传成功:" + flag);

            src.delete();

            if (flag == true) {

                String name = src.getName();
                String msg = "1" + "_" + name.substring(0, name.indexOf("."));

                connectService();

                // 发送消息
                sendMessageToService(msg);
//                data = receiveMessage();
                // 1000 注册

                Log.e(TAG, "----------->接收到的消息:"+data);

                if (TextUtils.isEmpty(data)){
                    data = "0000";
                }

//                mHandler.post(mUploadRunnable);
            }

        } catch (IOException e) {
            e.printStackTrace();
            stopService();
        }catch (Exception e){
            e.printStackTrace();
            stopService();
        }

        return data;
    }

    public FTPClient connectFTP() throws IOException {

        if (mClient != null && mClient.isConnected()) {
            return mClient;
        }

        mClient = new FTPClient();
        mClient.connect("192.168.25.160", 2121);
        boolean result = mClient.login("haojutao", "123456");

        Log.e(TAG, "是否已经连接到FTP服务器：" + mClient.isConnected() + " result:" + result);

        /**
         * 设置文件传输模式
         * 避免一些可能会出现的问题，在这里必须要设定文件的传输格式。
         * 在这里我们使用BINARY_FILE_TYPE来传输文本、图像和压缩文件。
         */
        mClient.setFileType(FTP.BINARY_FILE_TYPE);
        mClient.enterLocalPassiveMode();

        mClient.setCharset(Charset.forName("UTF-8"));

        FTPFile[] ftpFile = mClient.listFiles();
        for (FTPFile ft : ftpFile) {
            Log.e("TAG", "FTP服务器文件路径:" + ft.getName() + " " + ft.getGroup());
        }

        boolean flag = mClient.changeWorkingDirectory("img_face");
        Log.e("TAG", "创建文件是否成功:" + flag);

        return mClient;
    }

    public void connectService() throws IOException{
        if (mSocket != null && mSocket.isConnected()) {
            return;
        }

        mSocket = new Socket("192.168.25.160", 50008);
        mSocket.setSoTimeout(10 * 1000);
        mSocket.setKeepAlive(true);

        Log.e("TAG", "Socket是否连接成功:" + mSocket.isConnected());

        mInput = new BufferedInputStream(mSocket.getInputStream());
        mOutput = new PrintWriter(mSocket.getOutputStream());
    }

    public void sendMessageToService(String msg) {

        try {
            mOutput.print(new String(msg.getBytes(), "UTF-8"));
            mOutput.flush();
//            mOutput.close();
            Log.e(TAG, "发送请求:"+msg);

        } catch (IOException e) {
            e.printStackTrace();

            try {
                if (mSocket == null || !mSocket.isConnected()){
                    connectService();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }

    }

    // 接收消息监听
    private String receiveMessage() {

        String data = null;

        if (mInput == null) return data;

        try {

            byte[] buf = new byte[4];
            int length = mInput.read(buf, 0, buf.length);

            if (length > 0){
                data = new String(buf, "UTF-8");

                Log.e(TAG, "接收消息#receiveMessage:" + data);

                if (!TextUtils.isEmpty(data) && mOnReceiveDataListener != null){
                    mOnReceiveDataListener.onUploadReceiver(data);
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    private Runnable mUploadRunnable = new Runnable() {

        @Override
        public void run() {
            Log.e(TAG, "----------->进入接收消息线程:"+Thread.currentThread().getName());

            String data = receiveMessage();

            Log.e(TAG, "----------->接收到的消息:"+data );

            // 监听接收到的消息
//            mHandler.postDelayed(this, 1000);
            mHandler.post(this);
        }

    };

    public void uploadFile(final String path){

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                File srcFile = new File(path);

                // 上传到服务器文件名称
                String remote = "ftp_face/"+srcFile.getName();

                uploadFile(remote, srcFile.getAbsolutePath());
            }
        }, 600);
    }


    public void setOnReceiveDataListener(OnReceiverUploadDataListener listener){
        mOnReceiveDataListener = listener;
    }

    public void removeOnReceiveDataListener(){
        mOnReceiveDataListener = null;
    }
    public interface OnReceiverUploadDataListener{
        void onUploadReceiver(String data);
    }
}
