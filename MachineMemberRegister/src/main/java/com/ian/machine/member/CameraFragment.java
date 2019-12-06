/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  CameraFragment
 * Created by  ianchang on 2018-08-23 16:29:38
 * Last modify date   2018-08-22 16:00:56
 */

package com.ian.machine.member;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ian.machine.member.base.BaseFragment;
import com.ian.machine.member.detector.DetectThread;
import com.ian.machine.member.detector.UVCCameraManager;
import com.ian.machine.member.dialog.CameraDeviceSelectDialog;
import com.ian.machine.member.service.ServiceMedia;
import com.ian.machine.member.utils.FTPManager;
import com.ian.machine.member.utils.ToolUtils;
import com.ian.machine.member.widget.CameraTextureView;
import com.ian.machine.member.widget.FaceRectangleView;
import com.serenegiant.usb.USBMonitor;
import com.serenegiant.usbcameracommon.UVCCameraHandler;

import java.io.File;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.serenegiant.usb.USBMonitor.OnDeviceConnectListener} interface
 * to handle interaction events.
 * Use the {@link CameraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CameraFragment extends BaseFragment implements UVCCameraManager.OnUVCCameraListener, View.OnClickListener, DetectThread.OnDetectResultListener{


    private final static int MAX_COUNT = 5; // 最大处理事件组
    private final static long MAX_INTERVAL = 10 * 1000; //

    private int count;
    private long lastTime;

    private static final String KEY_USB_CAMERA = "position";

    /**
     * for camera preview display
     */
    private CameraTextureView mUVCCameraView;

    private TextView mCameraStateTV; // 外接相机连接状态

    private FaceRectangleView mFaceRectangleView;

    private int mPosition;

    private Button mRegisterBtn;
    private String mCode;

    private UVCCameraManager mUVCCameraManager;

    public static CameraFragment newInstance() {
        CameraFragment fragment = new CameraFragment();
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mPosition = savedInstanceState.getInt(KEY_USB_CAMERA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // FIXME 外接相机状态
        mCameraStateTV = (TextView) view.findViewById(R.id.tv_camera_state);
        mCameraStateTV.setText("无法连接到外接相机...");

        // 选择相机
        view.findViewById(R.id.btn_camera).setOnClickListener(this);
        view.findViewById(R.id.btn_detect).setOnClickListener(this);


        mRegisterBtn = (Button)view.findViewById(R.id.btn_register);
        mRegisterBtn.setOnClickListener(this);

        mUVCCameraView = (CameraTextureView) view.findViewById(R.id.camera_view);

        mUVCCameraManager = new UVCCameraManager(mUVCCameraView);
        mUVCCameraManager.setOnUVCCameraListener(this);

        mFaceRectangleView = (FaceRectangleView) view.findViewById(R.id.face_view);
        mUVCCameraView.setFaceRectangleView(mFaceRectangleView);
        mUVCCameraView.setOnDetectResultListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_USB_CAMERA, mPosition);
    }

    @Override
    public void onStart() {
        super.onStart();
        mUVCCameraManager.onStart();
    }


    @Override
    public void onStop() {
        super.onStop();
        mUVCCameraManager.onStop();
    }

    @Override
    public void onDestroy() {
        mUVCCameraManager.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_camera: // 打开选择相机
                mUVCCameraManager.showDeviceDialog(mCameraStateTV);
                break;
            case R.id.btn_detect:
//                String file = ToolUtils.getRootDir() + "/test.jpg";
                String file = ToolUtils.getRootDir() + "/timg.jpeg";
                mUVCCameraView.detectBitmap(file);

                //如果是竖排,则改为横排
                if (getActivity().getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else if (getActivity().getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                    //如果是横排,则改为竖排
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }

                break;
            case R.id.btn_register:
                mCode = "1001";
                registerMember();
                break;
        }
    }

    @Override
    public void onConnect(UVCCameraHandler cameraHandler, boolean isOpen) {

    }

    @Override
    public void onDisconnect(final UVCCameraHandler cameraHandler) {

        if (cameraHandler != null) {
            queueEvent(new Runnable() {
                @Override
                public void run() {
                    cameraHandler.close();
                }
            }, 0);
        }
    }

    @Override
    public void onCameraState(final String text) {

        mCameraStateTV.post(new Runnable() {
            @Override
            public void run() {
                mCameraStateTV.setText(text);

            }
        });

    }


    public void searchMember(String data) {

        mCode = data;
        ServiceMedia.shareInstance().
                searchMember(mCode).
                map(new Function<String, String>() {
                    @Override
                    public String apply(@NonNull String s) throws Exception {

                        Log.e("TAG", "---------->请求结果："+s);
                        return s;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull String s) {

                        if (!TextUtils.isEmpty(s)){
                            mCameraStateTV.setText(s);
                            mCameraStateTV.postInvalidate();
                            mCameraStateTV.setVisibility(View.VISIBLE);
                        }


                        if (TextUtils.isEmpty(mCode) || !"10001".contentEquals(mCode)){
                            mRegisterBtn.setVisibility(View.VISIBLE);
                        }else {
                            mRegisterBtn.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onDetectResult(Bitmap src, List<Rect> data) {
        mFaceRectangleView.updateRectangle(data);

        showDetectResult(src, data);
    }

    /*******
     * 显示检测结果
     * @param src
     * @param data
     */
    private void showDetectResult(Bitmap src, List<Rect> data) {

        Log.e("TAG", "检测结果:" + data.size());

        Rect maxRect = null;
        int index = 0;

        // 绘制检测到的人脸
        for (Rect rect : data) {
            index++;

            // 判断与相机最近的人脸
            if (maxRect == null) {
                maxRect = rect;
                // 找到最近的人脸
            } else if (maxRect.width() < rect.width() && maxRect.height() < rect.height()) {
                maxRect = rect;
            }

            // 保存人脸
            ToolUtils.saveFaceOfPerson(src, rect, ToolUtils.getRootDir() + "/face_" + index + ".png", 30);
        }

        // 一次只上传五张
        if (maxRect != null && count < MAX_COUNT) {

            // 保存距离最近的一张人脸图片
            String path = ToolUtils.getRootDir() + "/" + System.currentTimeMillis() + ".jpg";
            boolean isSuccess = ToolUtils.saveFaceOfPerson(src, maxRect, path, 30);

//            if (isSuccess) {
//                updateFile(path);
//            }

        }

        // 1分钟没有检测到时，可以进入下次重次上传流程
        long interval = System.currentTimeMillis() - lastTime;
        if (interval >= MAX_INTERVAL) {
            count = 0;
        }
    }



    /******
     * 上传检测到的用户人人脸
     * @param path
     * @return
     */
    private boolean updateFile(String path) {
        File srcFile = new File(path);
        // 上传到服务器文件名称
        String remote = "ftp_face/" + srcFile.getName();
        String data = FTPManager.shareInstance().uploadFile(srcFile.getAbsolutePath(), remote);
        srcFile.delete();

        boolean flag = false;
        if (!TextUtils.isEmpty(data)) {
            count++;
            lastTime = System.currentTimeMillis();

            Log.e("TAG", count + "#上传成功:" + srcFile.getAbsolutePath());

            flag = true;
        }

        if (TextUtils.isEmpty(data) || data.contentEquals("0000")) return flag;
        searchMember(data);

        return flag;
    }


    public void registerMember(){
        ServiceMedia.shareInstance().
                searchMember(mCode).
                map(new Function<String, String>() {
                    @Override
                    public String apply(@NonNull String s) throws Exception {
                        Log.e("TAG", "---------->请求结果："+s);

                        return s;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull String s) {

                        if (!TextUtils.isEmpty(s)){
                            mCameraStateTV.setText(s);
                            mCameraStateTV.postInvalidate();
                            mCameraStateTV.setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
