/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  CameraTextureView
 * Created by  ianchang on 2018-08-21 10:41:36
 * Last modify date   2018-08-16 10:31:12
 */

package com.ian.machine.cart.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Trace;
import android.util.AttributeSet;
import android.util.Log;

import com.ian.machine.cart.utils.ToolUtils;
import com.serenegiant.widget.UVCCameraTextureView;
import com.tzutalin.dlib.FaceDet;
import com.tzutalin.dlib.VisionDetRet;

import junit.framework.Assert;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ianchang on 2018/8/2.
 */

public class CameraTextureView extends UVCCameraTextureView {

    private static final int INPUT_SIZE = 224;

    private Handler mDetectHandler;
    private HandlerThread mDetectThead;
    private Bitmap mCroppedBitmap;

    private Paint mFacePaint;
    private FaceDet mFaceDet;
    private boolean isDetect;
    private boolean isDetecting;
    private FaceRectangleView mFaceRectangleView;
    private volatile float mBitmapScale;

    public CameraTextureView(Context context) {
        this(context, (AttributeSet) null, 0);
    }

    public CameraTextureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraTextureView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // 绘制人脸画笔
        mFacePaint = new Paint();
        mFacePaint.setColor(Color.RED);
        mFacePaint.setStrokeWidth(2);
        mFacePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (hasSurface()){
            // 检测人脸线程
            mDetectThead = new HandlerThread("DetectThread");
            mDetectThead.start();

            mDetectHandler = new Handler(mDetectThead.getLooper());
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        onSurfaceTextureDestroyed(null);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        super.onSurfaceTextureAvailable(surface, width, height);

        if (mDetectThead == null){
            // 检测人脸线程
            mDetectThead = new HandlerThread("DetectThread");
            mDetectThead.start();
        }

        if (mDetectHandler == null){
            mDetectHandler = new Handler(mDetectThead.getLooper());
        }

        if (mFaceDet == null){
            mFaceDet = new FaceDet();
        }

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

        if (mDetectHandler != null){
            mDetectHandler.removeCallbacks(detectThread);

            mDetectThead = null;
        }

        if (mDetectThead != null){
            mDetectThead.quitSafely();
            mDetectHandler = null;

        }

        if (mFaceDet != null){
            mFaceDet.release();
            mFaceDet = null;
        }

        return super.onSurfaceTextureDestroyed(surface);
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        super.onSurfaceTextureUpdated(surface);

//        Log.i("TAG", "onSurfaceTextureUpdated:" + Thread.currentThread().getName());


        if (isDetect && hasSurface()) {

            // 正在检测中，就跳过
            if (isDetecting) return;

            Bitmap src = getBitmap();
            if (src != null) {
//                mCroppedBitmap = Bitmap.createBitmap(INPUT_SIZE, INPUT_SIZE, Bitmap.Config.ARGB_8888);
//                drawResizedBitmap(getBitmap(), mCroppedBitmap);
                mCroppedBitmap = src;
                mBitmapScale = 1.0f;
                detector(mCroppedBitmap);
            }
        }

    }

    public boolean isDetect() {
        return isDetect;
    }

    public void isDetect(boolean flag) {
        this.isDetect = flag;
    }

    /********
     * 对原图进行了缩放
     * @param src
     * @param dst
     */
    private void drawResizedBitmap(final Bitmap src, final Bitmap dst) {

        Assert.assertEquals(dst.getWidth(), dst.getHeight());
        final float minDim = Math.min(src.getWidth(), src.getHeight());

        final Matrix matrix = new Matrix();

        // We only want the center square out of the original rectangle.
        final float translateX = -Math.max(0, (src.getWidth() - minDim) / 2);
        final float translateY = -Math.max(0, (src.getHeight() - minDim) / 2);
        matrix.preTranslate(translateX, translateY);

        final float scaleFactor = dst.getHeight() / minDim;
        matrix.postScale(scaleFactor, scaleFactor);

        // Rotate around the center if necessary.
//        if (mScreenRotation != 0) {
//            matrix.postTranslate(-dst.getWidth() / 2.0f, -dst.getHeight() / 2.0f);
//            matrix.postRotate(mScreenRotation);
//            matrix.postTranslate(dst.getWidth() / 2.0f, dst.getHeight() / 2.0f);
//        }

        final Canvas canvas = new Canvas(dst);
        canvas.drawBitmap(src, matrix, null);

        mBitmapScale = scaleFactor;
    }

    public void detectBitmap(String file) {
        Bitmap src = BitmapFactory.decodeFile(file, null);
        if (src == null) return;
        mCroppedBitmap = Bitmap.createBitmap(INPUT_SIZE, INPUT_SIZE, Bitmap.Config.ARGB_8888);
        drawResizedBitmap(src, mCroppedBitmap);
        detector(mCroppedBitmap);
    }


    private Runnable detectThread = new Runnable() {
        @Override
        public void run() {

            if (!new File(ToolUtils.getFaceShapeModelPath()).exists()) {
//                mWindow.setMoreInformation("Copying landmark model to " + ToolUtils.getFaceShapeModelPath());
//                ToolUtils.copyFileFromRawToOthers(getContext(), R.raw.shape_predictor_68_face_landmarks, ToolUtils.getFaceShapeModelPath());
            }

            long startTime = System.currentTimeMillis();
            List<VisionDetRet> results;

            synchronized (FaceDet.class) {
                results = mFaceDet.detect(mCroppedBitmap);
            }

            long endTime = System.currentTimeMillis();
//            mWindow.setFPS((endTime - startTime) / 1000f);
//            mWindow.setMoreInformation("未知");

            Log.e("TAG", "Time cost: " + String.valueOf((endTime - startTime) / 1000f) + " sec");

            ArrayList<VisionDetRet> data = new ArrayList<>();
//            mFPSTV.setText("Time cost: " + String.valueOf((endTime - startTime) / 1000f) + " sec");
            // Draw on bitmap
            if (results != null) {

                Log.e("RESULT", "Result:" + results.size());

                int index = 0;
                VisionDetRet info;
                for (final VisionDetRet ret : results) {

                    Rect bounds = new Rect();
                    bounds.left = (int) (ret.getLeft() * mBitmapScale);
                    bounds.top = (int) (ret.getTop() * mBitmapScale);
                    bounds.right = (int) (ret.getRight() * mBitmapScale);
                    bounds.bottom = (int) (ret.getBottom() * mBitmapScale);

                    index++;

                    // 还原图片大小的位置
                    info = new VisionDetRet(ret.getLabel(), ret.getConfidence(), bounds.left, bounds.top, bounds.right, bounds.bottom);
                    data.add(info);

                    ToolUtils.saveFaceOfPerson(mCroppedBitmap, bounds, ToolUtils.getRootDir() + "/face_" + index + ".png", 30);

                    // Draw landmark
                    ArrayList<Point> landmarks = ret.getFaceLandmarks();

                    for (Point point : landmarks) {
                        info.getFaceLandmarks().add(new Point((int) (point.x * mBitmapScale), (int) (point.y * mBitmapScale)));
                    }
                }
            }

            isDetecting = false;

            if (mFaceRectangleView != null){
                mFaceRectangleView.updateRectangle(data);
            }

            mCroppedBitmap.recycle();
        }
    };

    private void detector(Bitmap bitmap) {

        Trace.beginSection("DetectFace");
        isDetecting = true;
        mDetectHandler.postDelayed(detectThread, 1000 / 24);

        Trace.endSection();
    }

    public void setFaceRectangleView(FaceRectangleView mFaceRectangleView) {
        this.mFaceRectangleView = mFaceRectangleView;
    }
}
