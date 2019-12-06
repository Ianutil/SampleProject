package com.ian.detector;

import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.usb.UsbDevice;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.Toast;

import com.ian.detector.dialog.CameraDeviceSelectDialog;
import com.ian.detector.widget.CameraTextureView;
import com.serenegiant.usb.USBMonitor;
import com.serenegiant.usbcameracommon.UVCCameraHandler;

/**
 * Created by ianchang on 2018/9/10.
 */

public class UVCCameraManager implements USBMonitor.OnDeviceConnectListener, CameraDeviceSelectDialog.OnItemClickListener{

    private static final int UPDATE_CAMERA_STATE_CODE = 1001; // 更新摄像机状态码

    private static final int PREVIEW_WIDTH = 1920;
    private static final int PREVIEW_HEIGHT = 1080;

    private Context mContext;

    private USBMonitor mUSBMonitor;
    private UVCCameraHandler mCameraHandler;
    private CameraTextureView mCameraTextureView;

    private int mPosition;

    private CameraDeviceSelectDialog mCameraDialog;

    private OnUVCCameraListener mOnUVCCameraListener;

    public UVCCameraManager(CameraTextureView cameraTextureView){

        mContext = cameraTextureView.getContext();
        mCameraTextureView = cameraTextureView;
        mUSBMonitor = new USBMonitor(mContext, this);

        mCameraTextureView.setAspectRatio(PREVIEW_WIDTH / (float) PREVIEW_HEIGHT);

        mCameraHandler = UVCCameraHandler.createHandler((Activity) mContext, mCameraTextureView,
                1, PREVIEW_WIDTH, PREVIEW_HEIGHT, 1);
    }

    public void onStart() {
        log("onStart:");

        mUSBMonitor.register();
        if (mCameraTextureView != null) {
            mCameraTextureView.onResume();
        }
    }


    public void onStop() {
        log("onStop:");
        mCameraHandler.close();

        if (mCameraTextureView != null)
            mCameraTextureView.onPause();


        if (mCameraDialog != null) {
            mCameraDialog.dismiss();
            mCameraDialog = null;
        }

    }

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

    }

    //    _____________________________ 外接摄像头监听  _____________________________________
    @Override
    public void onAttach(final UsbDevice device) {
        log("onAttach:");

        log("onAttach:Start camera of USB");
        showUsbDevices();
    }

    @Override
    public void onConnect(final UsbDevice device, final USBMonitor.UsbControlBlock ctrlBlock, final boolean createNew) {
        log("onConnect:" + createNew + " --deviceName:" + ctrlBlock.getDevice().getDeviceName());
        mCameraHandler.open(ctrlBlock);

        // 绑定UI
        SurfaceTexture texture = mCameraTextureView.getSurfaceTexture();

        mCameraHandler.startPreview(new Surface(texture));

        // 开始识别
        mCameraTextureView.isDetect(true);

        int width = mCameraTextureView.getMeasuredWidth();
        int height = mCameraTextureView.getMeasuredHeight();

        log("------------------->>>>>#width=" + width + " height=" + height);

        if (mOnUVCCameraListener != null){
            mOnUVCCameraListener.onConnect(mCameraHandler, mCameraHandler.isOpened());
        }
    }

    @Override
    public void onDisconnect(final UsbDevice device, final USBMonitor.UsbControlBlock ctrlBlock) {
        log("onDisconnect:");
        if (mOnUVCCameraListener != null){
            mOnUVCCameraListener.onDisconnect(mCameraHandler);
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

    // 打开相机，绘制图像
    private void showUsbDevices() {

        int deviceCount = mUSBMonitor.getDeviceCount();
        log("showUsbDevices#count:count=" + deviceCount + " position=" + mPosition);

        // 显示选择USB相机设备对话框
//        if (!mCameraHandler.isOpened()) {
//            MessageDialogFragment.showDialog(getActivity());
//        }

        // 判断相机是否已经打开
        if (mCameraHandler.isOpened()) {

            showDeviceDialog(mCameraTextureView);
            return;
        }

        // Fixme 当大于或等于USB挂载的个数时，停止
        // 判断是否有可以打开的外接摄像机
        if (deviceCount > 0 && mPosition < deviceCount) {

            UsbDevice device = mUSBMonitor.getDeviceList().get(mPosition);

            // 请求是否拥有权限打开相机
            boolean state = mUSBMonitor.requestPermission(device);
            log("showUsbDevices#state:" + state);

            if (mOnUVCCameraListener != null){
                mOnUVCCameraListener.onCameraState("showUsbDevices#state:" + state);
            }


            // 自动轮询相机
            mPosition++;
        }

    }


    public void log(String msg) {
        Log.e("TAG", msg);
    }

    // 显示选择设备对话框
    public void showDeviceDialog(View view) {

        if (mCameraDialog == null) {
            mCameraDialog = new CameraDeviceSelectDialog(mContext);
            mCameraDialog.createWindow();
            mCameraDialog.setItemClickListener(this);
        }

        if (mUSBMonitor.getDeviceList().isEmpty()) {
            showToast("未找到USB外接设备");

            if (mOnUVCCameraListener != null){
                mOnUVCCameraListener.onCameraState("未找到USB外接设备");
            }

            return;
        }

        mCameraDialog.setData(mUSBMonitor.getDeviceList());

        mCameraDialog.showDialog(view, mPosition);
    }

    @Override
    public void onItemClick(UsbDevice info, int position) {

        if (info == null) return;

        mCameraDialog.dismiss();
        mPosition = position;

        log("position:" + position);

        // 切换相机时，先关闭目前所打开的相机
        if (mCameraHandler.isOpened()) {
            mCameraHandler.close();
        }

        // 打开选择的相机
        mUSBMonitor.requestPermission(info);

    }


    private void showToast(String msg){
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }


    public interface OnUVCCameraListener{
        void onConnect(UVCCameraHandler cameraHandler, boolean isOpen);

        void onDisconnect(UVCCameraHandler cameraHandler);

        void onCameraState(String text);
    }


    public void setOnUVCCameraListener(OnUVCCameraListener listener){
        mOnUVCCameraListener = listener;
    }

}
