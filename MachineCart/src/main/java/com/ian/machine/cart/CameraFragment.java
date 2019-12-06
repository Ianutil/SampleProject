/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  CameraFragment
 * Created by  ianchang on 2018-08-21 11:00:15
 * Last modify date   2018-08-15 19:25:22
 */

package com.ian.machine.cart;

import android.content.pm.ActivityInfo;
import android.graphics.SurfaceTexture;
import android.hardware.usb.UsbDevice;
import android.os.Build;
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
import android.widget.TextView;
import android.widget.Toast;

import com.ian.machine.cart.dialog.CameraDeviceSelectDialog;
import com.ian.machine.cart.utils.ToolUtils;
import com.ian.machine.cart.widget.CameraTextureView;
import com.ian.machine.cart.widget.FaceRectangleView;
import com.serenegiant.common.BaseFragment;
import com.serenegiant.usb.USBMonitor;
import com.serenegiant.usb.UVCCamera;
import com.serenegiant.usbcameracommon.UVCCameraHandler;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.serenegiant.usb.USBMonitor.OnDeviceConnectListener} interface
 * to handle interaction events.
 * Use the {@link CameraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CameraFragment extends BaseFragment implements USBMonitor.OnDeviceConnectListener, CameraDeviceSelectDialog.OnItemClickListener,
        View.OnClickListener {
    private static final int UPDATE_CAMERA_STATE_CODE = 1001; // 更新摄像机状态码

    private static final String KEY_USB_CAMERA = "position";

    /**
     * preview resolution(width)
     * if your camera does not support specific resolution and mode,
     * {@link UVCCamera#setPreviewSize(int, int, int)} throw exception
     * <p>
     * 1280/640/1920
     */
    private static final int PREVIEW_WIDTH = 1920;
    /**
     * preview resolution(height)
     * if your camera does not support specific resolution and mode,
     * {@link UVCCamera#setPreviewSize(int, int, int)} throw exception
     * 720/480/1080/1080
     */
    private static final int PREVIEW_HEIGHT = 1080;
    /**
     * set true if you want to record movie using MediaSurfaceEncoder
     * (writing frame data into Surface camera from MediaCodec
     * by almost same way as USBCameratest2)
     * set false if you want to record movie using MediaVideoEncoder
     */
    private static final boolean USE_SURFACE_ENCODER = false;
    /**
     * preview mode
     * if your camera does not support specific resolution and mode,
     * {@link UVCCamera#setPreviewSize(int, int, int)} throw exception
     * 0:YUYV, other:MJPEG
     */
    private static final int PREVIEW_MODE = 1;

    /**
     * for accessing USB
     */
    private USBMonitor mUSBMonitor;
    /**
     * Handler to execute camera related methods sequentially on private thread
     */
    private UVCCameraHandler mCameraHandler;

    /**
     * for camera preview display
     */
    private CameraTextureView mUVCCameraView;

    private TextView mCameraStateTV; // 外接相机连接状态

    private FaceRectangleView mFaceRectangleView;
    private CameraDeviceSelectDialog mCameraDialog;
    private int mPosition;

    private ICameraTextureListener mCameraTextureListener;
    private Handler mUIHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_CAMERA_STATE_CODE: // 更新摄像头状态
                    mCameraStateTV.setVisibility(msg.arg1);
                    break;
            }

        }
    };

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

        mUVCCameraView = (CameraTextureView) view.findViewById(R.id.camera_view);
        mUVCCameraView.setAspectRatio(PREVIEW_WIDTH / (float) PREVIEW_HEIGHT);

        mUSBMonitor = new USBMonitor(view.getContext(), this);

//        DisplayMetrics metrics = getResources().getDisplayMetrics();
        mCameraHandler = UVCCameraHandler.createHandler(getActivity(), mUVCCameraView,
                USE_SURFACE_ENCODER ? 0 : 1, PREVIEW_WIDTH, PREVIEW_HEIGHT, PREVIEW_MODE);
//        mCameraHandler = UVCCameraHandler.createHandler(getActivity(), mUVCCameraView,
//                USE_SURFACE_ENCODER ? 0 : 1, PREVIEW_WIDTH, PREVIEW_HEIGHT, PREVIEW_MODE);

        // 选择相机
        view.findViewById(R.id.btn_camera).setOnClickListener(this);
        view.findViewById(R.id.btn_detect).setOnClickListener(this);

        mFaceRectangleView = (FaceRectangleView) view.findViewById(R.id.face_view);
        mUVCCameraView.setFaceRectangleView(mFaceRectangleView);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_USB_CAMERA, mPosition);
    }

    @Override
    public void onStart() {
        super.onStart();
        log("onStart:");

        mUSBMonitor.register();
        if (mUVCCameraView != null)
            mUVCCameraView.onResume();
    }


    @Override
    public void onStop() {
        log("onStop:");
        mCameraHandler.close();
        if (mUVCCameraView != null)
            mUVCCameraView.onPause();


        if (mCameraDialog != null) {
            mCameraDialog.dismiss();
            mCameraDialog = null;
        }

        super.onStop();
    }

    @Override
    public void onDestroy() {
        log("onDestroy:");
        if (mCameraHandler != null) {
            mCameraHandler.release();
            mCameraHandler = null;
        }
        if (mUSBMonitor != null) {
            mUSBMonitor.destroy();
            mUSBMonitor = null;
        }
        mUVCCameraView = null;

        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_camera: // 打开选择相机
                // 切换相机时，先关闭目前所打开的相机
                mCameraHandler.close();
                showDeviceDialog();
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
        }
    }

    // 打开相机，绘制图像
    private void showUsbDevices() {

        int deviceCount = mUSBMonitor.getDeviceCount();
        log("showUsbDevices#count:count=" + deviceCount + " position=" + mPosition);

        // 显示选择USB相机设备对话框
//        if (!mCameraHandler.isOpened()) {
//            CameraDialog.showDialog(getActivity());
//        }

        // 判断相机是否已经打开
        if (mCameraHandler.isOpened()) {

            showDeviceDialog();
            return;
        }

        // Fixme 当大于或等于USB挂载的个数时，停止
        // 判断是否有可以打开的外接摄像机
        if (deviceCount > 0 && mPosition < deviceCount) {

            UsbDevice device = mUSBMonitor.getDeviceList().get(mPosition);

            // 请求是否拥有权限打开相机
            boolean state = mUSBMonitor.requestPermission(device);
            log("showUsbDevices#state:" + state);

            // 更新相机的状态
            Message msg = mUIHandler.obtainMessage();
            msg.what = UPDATE_CAMERA_STATE_CODE;
            if (state == false) {
                msg.arg1 = View.INVISIBLE;
            } else {
                msg.arg1 = View.VISIBLE;
            }
            mUIHandler.sendMessage(msg);

            // 自动轮询相机
            mPosition++;
        }

    }

    //    _____________________________ 外接摄像头监听  _____________________________________
    @Override
    public void onAttach(final UsbDevice device) {
        log("onAttach:");

        boolean isOpened = false;
        String cameraId = null;

        // 优先开启板载的相机
        if (!isOpened && TextUtils.isEmpty(cameraId)) {
            log("onAttach:Start camera of USB");
            showUsbDevices();
        }

    }

    @Override
    public void onConnect(final UsbDevice device, final USBMonitor.UsbControlBlock ctrlBlock, final boolean createNew) {
        log("onConnect:" + createNew + " --deviceName:" + ctrlBlock.getDevice().getDeviceName());
        mCameraHandler.open(ctrlBlock);

        // 绑定UI
        SurfaceTexture texture = mUVCCameraView.getSurfaceTexture();

        mCameraHandler.startPreview(new Surface(texture));

        // 开始识别
        mUVCCameraView.isDetect(true);

        int width = mUVCCameraView.getMeasuredWidth();
        int height = mUVCCameraView.getMeasuredHeight();

        log("------------------->>>>>#width=" + width + " height=" + height);

        if (mCameraTextureListener != null){
            mCameraTextureListener.onTextureSize(width, height);
        }

    }

    @Override
    public void onDisconnect(final UsbDevice device, final USBMonitor.UsbControlBlock ctrlBlock) {
        log("onDisconnect:");

        if (mCameraHandler != null) {
            queueEvent(new Runnable() {
                @Override
                public void run() {
                    mCameraHandler.close();
                }
            }, 0);
        }

    }

    @Override
    public void onDettach(final UsbDevice device) {
        log("onAttach:");

    }


    @Override
    public void onCancel(final UsbDevice device) {
        log("onCancel:");

    }

    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    public void log(String msg) {
        Log.e("TAG", msg);
    }


    public void setICameraTextureListener(ICameraTextureListener listener){
        mCameraTextureListener = listener;
    }

    // 显示选择设备对话框
    private void showDeviceDialog() {
        if (mCameraDialog == null) {
            mCameraDialog = new CameraDeviceSelectDialog(getActivity());
            mCameraDialog.createWindow();
            mCameraDialog.setItemClickListener(this);
        }

        if (mUSBMonitor.getDeviceList().isEmpty()) {
            showToast("未找到USB外接设备");
            return;
        }

        mCameraDialog.setData(mUSBMonitor.getDeviceList());

        mCameraDialog.showDialog(mCameraStateTV, mPosition);
    }

    @Override
    public void onItemClick(UsbDevice info, int position) {

        if (info == null) return;

        mCameraDialog.dismiss();

        mPosition = position;

        log("position:" + position);
        if (mCameraHandler.isOpened()) {
            mCameraHandler.close();
        }

        // 打开选择的相机
        mUSBMonitor.requestPermission(info);

    }

    public interface ICameraTextureListener{
        void onTextureSize(int width, int height);
    }
}
