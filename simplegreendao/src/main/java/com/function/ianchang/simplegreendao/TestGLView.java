/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  TestGLView
 * Created by  ianchang on 2018-08-14 10:49:06
 * Last modify date   2018-08-14 10:49:05
 */

package com.function.ianchang.simplegreendao;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLUtils;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;

import java.io.File;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by ianchang on 2018/8/14.
 */

public class TestGLView extends GLSurfaceView implements Renderer {

    //声明一个三角形对象
    private Triangle triangle = null;
    //声明一个正方形对象
    private Square square = null;

    public TestGLView(Context context) {
        this(context,null);
    }

    public TestGLView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 创建一个OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        //创建渲染器实例
        Renderer renderer = this;

        // 设置渲染器
        setRenderer(renderer);

        //设置渲染模式
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //设置背景的颜色
        GLES20.glClearColor(0.5f, 0f, 0f, 1.0f);
//        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        triangle = new Triangle();
        square = new Square();

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // 清除屏幕和深度缓存
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

//        square.draw();

//        triangle.draw();

        // FIXME: 2018/8/14 直接绘制图片 第一种方式
        int[] texNames = new int[1];
        GLES20.glGenTextures(1, texNames, 0);
        int mTexName = texNames[0];
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexName);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.timg);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        bitmap.recycle();
        bitmap = null;
    }

    public static String getRootDir() {
        File root;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            root = new File(Environment.getExternalStorageDirectory(), "detector");
        } else {
            root = Environment.getExternalStorageDirectory();
        }

        if (!root.exists())
            root.mkdirs();

        return root.getAbsolutePath();
    }

}
