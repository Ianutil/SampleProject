package com.ian.detector;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.ian.detector.utils.ToolUtils;
import com.tzutalin.dlib.FaceDet;
import com.tzutalin.dlib.VisionDetRet;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ianchang on 2018/8/31.
 */

public class DetectThread implements Runnable {

    private static final String TAG = DetectThread.class.getSimpleName();

    private final static int DETECT_SIZE = 320;

    private FaceDet mFaceDet;
    private Handler mDetectHandler;
    private HandlerThread mHandlerThread;
    private boolean isDetecting;

    private Bitmap mDetectBitmap;

    private OnDetectResultListener onDetectResultListener;

    private List<Rect> data = new ArrayList<>();
    private Context mContext;
    private float mScaleSize;
    private int mWidth, mHeight;

    public DetectThread(Context context) {
        this.mContext = context;
        mScaleSize = 1.0f;
    }

    public void startDetect() {

        try {
            mFaceDet = new FaceDet();

            mHandlerThread = new HandlerThread("Detect-Thread");
            mHandlerThread.start();

            mDetectHandler = new Handler(mHandlerThread.getLooper());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void stopDetect() {

        try {
            if (mHandlerThread != null) {
                mHandlerThread.quitSafely();
                mHandlerThread = null;
            }

            if (mDetectHandler != null) {
                mDetectHandler.removeCallbacks(this);
                mDetectHandler = null;
            }

            if (mFaceDet != null) {
                mFaceDet.release();
                mFaceDet = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /***********
     * 检测相机的数据帧
     * @param data
     * @param width
     * @param height
     * @param rotation
     */
    public void detectFace(byte[] data, int width, int height, int rotation) {
        try {

            if (data == null || mDetectHandler == null) return;

            if (isDetecting) return;

            isDetecting = true;

            YuvImage image = new YuvImage(data, ImageFormat.NV21, width, height, null);
            File file = new File(ToolUtils.getRootDir(), "/frame.png");
            FileOutputStream bos = new FileOutputStream(file, false);
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            image.compressToJpeg(new Rect(0, 0, width, height), 100, bos);
//            Bitmap bitmap = BitmapFactory.decodeByteArray(bos.toByteArray(), 0, bos.size());
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), null);
            bos.close();

            mWidth = width;
            mHeight = height;

            // 进行缩放原图
//            mDetectBitmap = Bitmap.createBitmap(width/2, height/2, Bitmap.Config.ARGB_8888);
            mDetectBitmap = Bitmap.createBitmap(DETECT_SIZE, DETECT_SIZE, Bitmap.Config.ARGB_8888);
            resizeBitmap(bitmap, mDetectBitmap, rotation);

            mDetectHandler.postDelayed(this, 1000 / 24);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /********
     * 检测图片
     * @param src
     */
    public void detectFace(Bitmap src) {
        try {

            if (src == null) return;

            // 进行缩放原图
//            mDetectBitmap = Bitmap.createBitmap(DETECT_SIZE, DETECT_SIZE, Bitmap.Config.ARGB_8888);
//            resizeBitmap(src, mDetectBitmap, 0);
            mDetectBitmap = resizeBitmap(src);
            mDetectHandler.postDelayed(this, 1000 / 24);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {

        if (mFaceDet == null) return;

        if (!new File(ToolUtils.getFaceShapeModelPath()).exists()) {
            Log.e(TAG, "请复制数据模型到：" + ToolUtils.getFaceShapeModelPath());
//                ToolUtils.copyFileFromRawToOthers(getApplicationContext(), R.raw.shape_predictor_68_face_landmarks, ToolUtils.getFaceShapeModelPath());
        }

        if (mDetectBitmap == null) return;

        ToolUtils.saveBitmap(mDetectBitmap);

        long startTime = System.currentTimeMillis();
        List<VisionDetRet> results;

        synchronized (Runnable.class) {
            results = mFaceDet.detect(mDetectBitmap);
        }

        long endTime = System.currentTimeMillis();
        Log.e(TAG, "Time cost: " + String.valueOf((endTime - startTime) / 1000f) + " sec ");


        data.clear();

        if (results != null && !results.isEmpty()) {

//            float resizeRatio = 1.0f;
            float resizeRatio = mScaleSize;
//            float resizeRatio = mWidth * 1.0f / DETECT_SIZE;

            Rect bounds, dstRect;
            for (final VisionDetRet ret : results) {

                bounds = new Rect();
                bounds.left = (int) (ret.getLeft() * resizeRatio);
                bounds.top = (int) (ret.getTop() * resizeRatio);
                bounds.right = (int) (ret.getRight() * resizeRatio);
                bounds.bottom = (int) (ret.getBottom() * resizeRatio);

                Log.e(TAG, "脸框位置: " + bounds.toString());

//                dstRect = new Rect();
//                dstRect.left = bounds.top;
//                dstRect.top = bounds.right;
//                dstRect.right = bounds.bottom;
//                dstRect.bottom = bounds.left;

                data.add(bounds);
            }

        }

        if (onDetectResultListener != null) {
            onDetectResultListener.onDetectResult(mDetectBitmap, data);
        }

        mDetectBitmap.recycle();
        mDetectBitmap = null;
        isDetecting = false;
    }

    /*******
     * 缩放图片
     * @param src
     * @param dst
     * @return
     */
    private void resizeBitmap(Bitmap src, Bitmap dst, int rotation) {
        float minDim = Math.min(src.getWidth(), src.getHeight());

        final Matrix matrix = new Matrix();

        // We only want the center square out of the original rectangle.
//        final float translateX = -Math.max(0, (src.getWidth() - minDim) / 2);
//        final float translateY = -Math.max(0, (src.getHeight() - minDim) / 2);
//        matrix.preTranslate(translateX, translateY);

        final float scaleFactor = dst.getHeight() / minDim;
        matrix.postScale(scaleFactor, scaleFactor);

        // Rotate around the center if necessary.
        if (rotation != 0) {
            matrix.postTranslate(-dst.getWidth() / 2.0f, -dst.getHeight() / 2.0f);
            matrix.postRotate(rotation);
            matrix.postTranslate(dst.getWidth() / 2.0f, dst.getHeight() / 2.0f);
        }

        mScaleSize = scaleFactor;
        final Canvas canvas = new Canvas(dst);
        canvas.drawBitmap(src, matrix, null);
    }

    private Bitmap resizeBitmap(Bitmap src){
        float min = Math.min(src.getWidth(), src.getHeight());

        float scale = min / 10;
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        Bitmap bitmap = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        return bitmap;
    }

    public void setOnDetectResultListener(OnDetectResultListener listener) {
        onDetectResultListener = listener;
    }

    public interface OnDetectResultListener {

        void onDetectResult(Bitmap src, List<Rect> data);
    }
}
