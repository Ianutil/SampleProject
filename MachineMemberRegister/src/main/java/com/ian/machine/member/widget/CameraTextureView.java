/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  CameraTextureView
 * Created by  ianchang on 2018-08-23 16:16:43
 * Last modify date   2018-08-23 16:16:42
 */

package com.ian.machine.member.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;

import com.ian.machine.member.detector.DetectThread;
import com.serenegiant.widget.UVCCameraTextureView;

/**
 * Created by ianchang on 2018/8/2.
 */

public class CameraTextureView extends UVCCameraTextureView {


    private DetectThread mDetectThread;
    private boolean isDetect;

    private FaceRectangleView mFaceRectangleView;
    private DetectThread.OnDetectResultListener mOnDetectResultListener;

    public CameraTextureView(Context context) {
        this(context, (AttributeSet) null, 0);
    }

    public CameraTextureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraTextureView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        super.onSurfaceTextureAvailable(surface, width, height);

        if (mDetectThread == null){
            // 检测人脸线程
            mDetectThread = new DetectThread(getContext());
            mDetectThread.setOnDetectResultListener(mOnDetectResultListener);
        }

        mDetectThread.startDetect();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        super.onSurfaceTextureSizeChanged(surface, width, height);

        if (mFaceRectangleView != null) {
            mFaceRectangleView.setViewSize(width, height);
        }

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {

        if (mDetectThread != null){
            mDetectThread.stopDetect();
            mDetectThread = null;
        }
        return super.onSurfaceTextureDestroyed(surface);
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        super.onSurfaceTextureUpdated(surface);

        if (isDetect && hasSurface()) {
            mDetectThread.detectFace(getBitmap());
        }

    }

    public boolean isDetect() {
        return isDetect;
    }

    public void isDetect(boolean flag) {
        this.isDetect = flag;
    }

    public void detectBitmap(String file) {
        Bitmap src = BitmapFactory.decodeFile(file, null);
        if (src == null) return;

        mDetectThread.detectFace(src);
    }

    public void setFaceRectangleView(FaceRectangleView mFaceRectangleView) {
        this.mFaceRectangleView = mFaceRectangleView;
    }

    public void setOnDetectResultListener(DetectThread.OnDetectResultListener listener){
        this.mOnDetectResultListener = listener;
    }

}
