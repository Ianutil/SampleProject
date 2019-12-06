package com.ian.detector;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ianchang on 2018/8/31.
 */

public class DetectCameraManager implements Camera.PreviewCallback, Camera.AutoFocusMoveCallback {

    private Camera mCamera;
    private Camera.Size mPreviewSize;
    private int mCameraRotation;
    private Activity mContext;

    private OnCameraFrameListener onCameraFrameListener;

    public DetectCameraManager(Activity context) {
        this.mContext = context;
    }

    /****
     * 打开相机
     * @param holder
     */
    public void openCamera(SurfaceHolder holder, int cameraId) {
        try {
            closeCamera();

            mCamera = Camera.open(cameraId);
//            mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);

            // 设置用于显示拍照影像的SurfaceHolder对象
            mCamera.setPreviewDisplay(holder);
            // 设置相机方向
            mCameraRotation = getPreviewDegree();
            mCamera.setDisplayOrientation(mCameraRotation);

            mCamera.setAutoFocusMoveCallback(this);
            mCamera.setPreviewCallback(this);
        } catch (IOException e) {
            e.printStackTrace();
            closeCamera();
        } catch (Exception e) {
            e.printStackTrace();
            closeCamera();
        }
    }

    /******
     * 关闭相机
     */
    public void closeCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }


    /**
     * 设置参数
     */
    public void setCameraParameters(int screenWidth, int screenHeight) {
        try {
            if (mCamera == null) return;

            // 设置相机相关参数
            Camera.Parameters parameters = mCamera.getParameters();

            // 设置预览大小
            Camera.Size previewSize = choosePreviewSize(screenWidth, screenHeight);
            Log.e("TAG", "设置相机预览大小:"+previewSize.width + " "+previewSize.height);
            parameters.setPreviewSize(previewSize.width, previewSize.height);
            mPreviewSize = previewSize;

            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            parameters.setPictureFormat(PixelFormat.JPEG);//设置图片格式
            //不能与setPreviewSize一起使用，否则setParamters会报错
//            parameters.setPreviewFrameRate(5);//设置每秒显示4帧
            parameters.setJpegQuality(80);// 设置照片质量

            // 图片大小
            Camera.Size pictureSize = choosePictureSize(screenWidth, screenHeight);
            parameters.setPictureSize(pictureSize.width, pictureSize.height);// 设置保存的图片尺寸
            mCamera.setParameters(parameters);

            mCamera.startPreview();

        } catch (Exception e) {
            e.printStackTrace();

            closeCamera();
        }
    }

    /*******
     * 选择相机支持的预览格式
     * @param width
     * @param height
     * @return
     */
    private Camera.Size choosePreviewSize(int width, int height) {

        List<Camera.Size> data = new ArrayList<>();
        List<Camera.Size> sizes = mCamera.getParameters().getSupportedPreviewSizes();

        StringBuilder builder = new StringBuilder("支持的相机预览格式:[");
        for (Camera.Size size : sizes) {

            builder.append(size.width + "x" + size.height);
            builder.append(",");

//            if (size.width >= width && size.height >= height) {
            if (size.width >= width) {
                data.add(size);
            }
        }

        builder.append("]");

        Log.e("TAG", builder.toString());

        // 默认选择第一个
        Camera.Size size = sizes.get(0);

        // 选择最大的预览大小
        if (data.size() > 2) {
            size = Collections.max(data, new Comparator<Camera.Size>() {
                @Override
                public int compare(Camera.Size o1, Camera.Size o2) {
                    return Long.signum(o1.width * o1.height - o2.width * o2.height);
                }
            });
        }

        return size;
    }

    /**********
     * 相机支持的图片大小
     * @param width
     * @param height
     * @return
     */
    private Camera.Size choosePictureSize(int width, int height) {
        List<Camera.Size> sizes = mCamera.getParameters().getSupportedPictureSizes();

        List<Camera.Size> data = new ArrayList<>();

        StringBuilder builder = new StringBuilder("支持的图片预览格式:[");
        for (Camera.Size size : sizes) {

            builder.append(size.width + "x" + size.height);
            builder.append(",");

            if (size.width >= width && size.height >= height) {
                data.add(size);
            }
        }

        builder.append("]");

        Log.e("TAG", builder.toString());

        Camera.Size size = sizes.get(0);

        if (data.size() > 2) {
            size = Collections.max(data, new Comparator<Camera.Size>() {
                @Override
                public int compare(Camera.Size o1, Camera.Size o2) {
                    return Long.signum(o1.width * o1.height - o2.width * o2.height);
                }
            });
        }

        return size;
    }

    // 提供一个静态方法，用于根据手机方向获得相机预览画面旋转的角度
    private int getPreviewDegree() {
        // 获得手机的方向
        int rotation = mContext.getWindowManager().getDefaultDisplay().getRotation();
        int degree = 0;
        // 根据手机的方向计算相机预览画面应该选择的角度
        switch (rotation) {
            case Surface.ROTATION_0:
                degree = 90;
                break;
            case Surface.ROTATION_90:
                degree = 0;
                break;
            case Surface.ROTATION_180:
                degree = 270;
                break;
            case Surface.ROTATION_270:
                degree = 180;
                break;
        }

        Log.e("TAG", "degree:" + degree);
        return degree;
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {

        if (onCameraFrameListener != null) {
            onCameraFrameListener.onPreviewFrame(data);
        }
    }

    @Override
    public void onAutoFocusMoving(boolean start, Camera camera) {

    }

    public void setOnCameraFrameListener(OnCameraFrameListener onCameraFrameListener) {
        this.onCameraFrameListener = onCameraFrameListener;
    }

    public int getPreviewWidth() {
        if (mPreviewSize != null) {
            return mPreviewSize.width;
        }

        return 0;
    }

    public int getPreviewHeight() {
        if (mPreviewSize != null) {
            return mPreviewSize.height;
        }

        return 0;
    }


    public int getCameraRotation() {
        return mCameraRotation;
    }

    public interface OnCameraFrameListener {
        void onPreviewFrame(byte[] data);
    }
}
