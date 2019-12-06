package com.ian.machine.member;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.camera2.CameraManager;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.PermissionChecker;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ian.machine.member.detector.DetectCameraManager;
import com.ian.machine.member.detector.DetectThread;
import com.ian.machine.member.utils.FTPManager;
import com.ian.machine.member.utils.ToolUtils;
import com.ian.machine.member.widget.FocusView;
import com.serenegiant.utils.PermissionCheck;

import java.io.File;
import java.util.List;

/**
 * Created by ianchang on 2018/8/24.
 */

public class Camera01Fragment extends Fragment implements SurfaceHolder.Callback,
        DetectCameraManager.OnCameraFrameListener, DetectThread.OnDetectResultListener,
        View.OnClickListener , FTPManager.OnReceiverUploadDataListener{

    private final static int MAX_COUNT = 5; // 最大处理事件组
    private final static long MAX_INTERVAL = 10 * 1000; //

    private int count;
    private Paint mFacePaint;
    private long lastTime;

    private SurfaceView mSurfaceView;

    private DetectThread mDetectThread;
    private DetectCameraManager mCameraManager;

    private FocusView mDetectView;
    private ShowResultFragment resultFragment;

    private int mCameraId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.fragment_camera_01, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSurfaceView = (SurfaceView) view.findViewById(R.id.camera_view);

        mDetectView = (FocusView) view.findViewById(R.id.detect_view);
        view.findViewById(R.id.switch_camera).setOnClickListener(this);

        mSurfaceView.getHolder().addCallback(this);
        mSurfaceView.setKeepScreenOn(true);
        mSurfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        mDetectThread = new DetectThread(view.getContext());
        mDetectThread.setOnDetectResultListener(this);

        mCameraManager = new DetectCameraManager(getActivity());
        mCameraManager.setOnCameraFrameListener(this);


        resultFragment = new ShowResultFragment();

        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if (fm.findFragmentByTag(resultFragment.getTag()) == null) {
            ft.replace(R.id.layout_result, resultFragment, resultFragment.getClass().getSimpleName());
        }

        ft.commit();
        ft.show(resultFragment);
    }

    @Override
    public void onStart() {
        super.onStart();
        FTPManager.shareInstance().setOnReceiveDataListener(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        FTPManager.shareInstance().removeOnReceiveDataListener();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {


        int count = Camera.getNumberOfCameras();
        Log.e("TAG", "count:" + count);
        if (count == 0){

            FragmentManager fm = getChildFragmentManager();
//            FragmentManager fm = getActivity().getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            CameraFragment cameraFragment = new CameraFragment();
            ft.replace(R.id.layout_camera, cameraFragment, cameraFragment.getClass().getSimpleName());
            ft.commit();
            ft.show(cameraFragment);

        }


        mDetectThread.startDetect();

        if (PermissionCheck.hasCamera(getContext())){
            mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
            mCameraManager.openCamera(holder, mCameraId);
        }else {
            requestPermissions(new String[]{"android.permission.CAMERA"}, 1000);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1000 && grantResults != null && grantResults.length > 0){
            Log.e("TAG", "授权成功...");
            mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
            mCameraManager.openCamera(mSurfaceView.getHolder(), mCameraId);
        }


    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        Log.e("TAG", "surfaceChanged:width=" + width + " height=" + height);

        mCameraManager.setCameraParameters(width, height);

        int previewWidth = mCameraManager.getPreviewWidth();
        int previewHeight = mCameraManager.getPreviewHeight();

        // 根据相机支持的预览大小，来设置SurfaceView的显示大小
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)mSurfaceView.getLayoutParams();

        float scale = 1.0f * width / previewWidth;
        params.width = (int)(previewWidth * scale);
        params.height = (int)(scale * previewHeight);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        mSurfaceView.setLayoutParams(params);

        Log.e("TAG", "当前预览大小:width=" + params.width + " height=" + params.height + " "+scale);

        FrameLayout.LayoutParams detectParams = (FrameLayout.LayoutParams)mDetectView.getLayoutParams();
        detectParams.width = params.width;
        detectParams.height = params.height;
        detectParams.gravity = Gravity.CENTER_HORIZONTAL;
        mDetectView.setLayoutParams(detectParams);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        mDetectThread.stopDetect();

        mCameraManager.closeCamera();
    }

    @Override
    public void onPreviewFrame(byte[] data) {
        mDetectThread.detectFace(data, mCameraManager.getPreviewWidth(), mCameraManager.getPreviewHeight(), mCameraManager.getCameraRotation());
    }

    @Override
    public void onDetectResult(Bitmap src, List<Rect> data) {

        if (src == null || data == null) return;

        showDetectResult(src, data);
//        mDetectView.updatePosition(data);

//        mDetectView.updatePosition(src, data);
//        mDetectView.updatePosition(src);
    }

    /*******
     * 显示检测结果
     * @param src
     * @param data
     */
    private void showDetectResult(Bitmap src, List<Rect> data) {

        Log.e("TAG", "检测结果:" + data.size());

        if (mFacePaint == null) {
            mFacePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mFacePaint.setColor(Color.RED);
            mFacePaint.setStyle(Paint.Style.STROKE);
            mFacePaint.setStrokeWidth(3);
        }


        Rect maxRect = null;
        int index = 0;

        // 绘制检测到的人脸
        Bitmap bitmap = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight());
        Canvas canvas = new Canvas(bitmap);

        for (Rect rect : data) {
            canvas.drawRect(rect, mFacePaint);

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

            if (isSuccess) {
                updateFile(path);
            }

        }


        // 1分钟没有检测到时，可以进入下次重次上传流程
        long interval = System.currentTimeMillis() - lastTime;
        if (interval >= MAX_INTERVAL) {
            count = 0;
        }

        mDetectView.updatePosition(bitmap);

        if ((data == null || data.isEmpty())){
            resultFragment.hideView();
        }


        bitmap.recycle();
        bitmap = null;
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
        resultFragment.searchMember(data);

        return flag;
    }


    @Override
    public void onClick(View v) {

        int count = Camera.getNumberOfCameras();
        Log.e("TAG", "count:" + count);

        int camera;
        if (mCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
            camera = Camera.CameraInfo.CAMERA_FACING_FRONT;
        } else {
            camera = Camera.CameraInfo.CAMERA_FACING_BACK;
        }

        if (mCameraId == camera) return;

        try {
            mCameraManager.closeCamera();
            mCameraManager.openCamera(mSurfaceView.getHolder(), camera);
            mCameraManager.setCameraParameters(mSurfaceView.getWidth(), mSurfaceView.getHeight());
        } catch (Exception e) {
            mCameraManager.closeCamera();
            e.printStackTrace();
        }

    }

    @Override
    public void onUploadReceiver(String data) {

        Log.e("TAG", "接收消息#onUploadReceiver:" + data);
        if (TextUtils.isEmpty(data) || data.contentEquals("0000"))
            return;

        resultFragment.searchMember(data);

    }
}
