/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  MainActivity
 * Created by  ianchang on 2018-08-23 15:34:42
 * Last modify date   2018-08-23 15:34:41
 */

package com.ian.machine.member;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Camera;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.ian.machine.member.bean.StuffResultInfo;
import com.ian.machine.member.cache.UserCenter;
import com.ian.machine.member.service.ServiceMedia;
import com.ian.machine.member.utils.FTPManager;
import com.ian.machine.member.utils.ToolUtils;
import com.ian.machine.member.widget.BubbleLayout;

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
import java.util.Arrays;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private BubbleLayout mBubbleLayout;
    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e("TAG", "MainActivity");

        initView();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        Log.e("TAG", "onConfigurationChanged");

    }

    public void initView(){
//        mBubbleLayout = (BubbleLayout)findViewById(R.id.layout_bubble);

//        ImageView imageView = (ImageView)findViewById(R.id.background_img);
//        String url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1533027181762&di=6aba6bd63d62a8055b271ed79bef3b8d&imgtype=0&src=http%3A%2F%2Fpic.bizhi360.com%2Fbpic%2F41%2F5641.jpg";
//        Glide.with(this).load(url).into(imageView);

//        try {
//            mSocket = new Socket("192.168.25.160", 50008);
//
//            Log.e("TAG", "Socket:是否连接成功..."+mSocket.isConnected());
//
//            mSocket.g
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        mSocket = IO.socket(URI.create("ftp://192.168.25.160:50008"));
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (mBubbleLayout != null){
            mBubbleLayout.startBubble();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mBubbleLayout != null){
            mBubbleLayout.stopBubble();
        }

        try {
            if (socket != null){
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @android.support.annotation.NonNull String[] permissions, @android.support.annotation.NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.e("TAG","onRequestPermissionsResult:"+requestCode+" "+Arrays.toString(permissions)+" "+ Arrays.toString(grantResults));

        if (requestCode == 1000){

        }

    }

    public void ftpUpload(View view){


        int result = ActivityCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE");
        if (result != PackageManager.PERMISSION_GRANTED) {
            // 没有写的权限，去申请写的权限，会弹出对话框
            ActivityCompat.requestPermissions(this, new String[]{
                            "android.permission.WRITE_EXTERNAL_STORAGE",
                            "android.permission.READ_EXTERNAL_STORAGE"
                    },
                    1000);

            return;
        }

        new Thread(){

            @Override
            public void run() {
                super.run();

                Log.e("TAG","Thead_name:"+Thread.currentThread().getName());

                try {

                    FTPClient client = getFTPClient();

                    /**
                     * 设置文件传输模式
                     * 避免一些可能会出现的问题，在这里必须要设定文件的传输格式。
                     * 在这里我们使用BINARY_FILE_TYPE来传输文本、图像和压缩文件。
                     */
                    client.setFileType(FTP.BINARY_FILE_TYPE);
                    client.enterLocalPassiveMode();

                    File file = new File(ToolUtils.getRootDir(), "1535357161908.jpg");
//                    File file = new File(ToolUtils.getRootDir(), "face_1.png");

                    Log.e("TAG","上传文件:"+file.getAbsolutePath() + "  "+file.exists());

                    client.setCharset(Charset.forName("UTF-8"));

                    String msg = "member"+"_"+file.getName();
                    client.sendCommand(new String(msg.getBytes(), "UTF-8"));


                    FTPFile[] ftpFile = client.listFiles();
                    for (FTPFile ft : ftpFile){
                        Log.e("TAG","目录:"+ft.getName()+" "+ft.getGroup());

                    }

//                    client.list(file.getAbsolutePath());


                    // 上传单个文件
                    FileInputStream fos = new FileInputStream(file);
                    String remote = "ftp_face/"+file.getName();


                    boolean flag = client.changeWorkingDirectory("./img_face");

                    Log.e("TAG","创建文件是否成功:"+flag);
                    flag = client.storeFile(remote, fos);

                    fos.close();

                    Log.e("TAG","是否上传成功:"+flag);
                    client.disconnect();
                    client = null;

                    if (socket == null){
                        socket = new Socket("192.168.25.160", 50008);
                        socket.setKeepAlive(true);
                        socket.setSoTimeout(10 * 1000);
                        Log.e("TAG", "Socket是否连接成功:" + socket.isConnected());
                    }

                    BufferedInputStream input = new BufferedInputStream(socket.getInputStream());
                    PrintWriter output = new PrintWriter(socket.getOutputStream());

                    String name = file.getName();
                    name = "1_"+name.substring(0, name.indexOf("."));
                    Log.e("TAG", "发送的数据:" + name);

                    output.print(new String(name.getBytes(), "UTF-8"));
                    output.flush();

                    byte[] buf = new byte[4];
                    input.read(buf, 0, buf.length);

                    String data = new String(buf, "UTF-8");

//                    output.close();
//                    input.close();

//                    socket.close();
                    Log.e("TAG","接收一条消息:"+data);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();

    }


    public FTPClient getFTPClient(){

        FTPClient client = new FTPClient();
        try {
            client.connect("192.168.25.160", 2121);
            boolean result = client.login("haojutao", "123456");

            Log.e("TAG", "是否已经连接到FTP服务器："+client.isConnected()+" result:"+result);
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }

        return client;
    }

}
