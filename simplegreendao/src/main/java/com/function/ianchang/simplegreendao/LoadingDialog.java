/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  LoadingDialog
 * Created by  ianchang on 2018-06-13 14:13:14
 * Last modify date   2018-05-31 13:15:27
 */

package com.function.ianchang.simplegreendao;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ianchang on 2018/5/31.
 *
 *
 * loading加载框显示
 *
 */

public class LoadingDialog extends Dialog {

    public LoadingDialog(@NonNull Context context) {
        this(context, R.style.Theme_AppCompat_Dialog_Alert);



        init();
    }

    public LoadingDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);

        // 加载布局
        setContentView(R.layout.dialog_loading);

        getWindow().getDecorView();
        init();
    }

    protected LoadingDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }


    private void init(){

        // 设置Dialog参数
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        // 回调
        cancel();
        // 关闭Loading
        dismiss();
    }


    public void screencap(String path){

        View cv = getWindow().getDecorView();

        cv.setDrawingCacheEnabled(true);
        cv.buildDrawingCache();
        Bitmap bitmap = cv.getDrawingCache();
        if (bitmap == null) {
            return;
        }

        bitmap.setHasAlpha(false);
        bitmap.prepareToDraw();

        if (bitmap == null || bitmap.getHeight() == 0 || bitmap.getHeight() == 0) return;

        Log.i("TAG", "onScreenFinished:width=" + bitmap.getWidth() + " height=" + bitmap.getHeight() + " thread:" + Thread.currentThread().getName());

        File output = new File(path);
        if (output.exists()) {
            output.getParentFile().mkdirs();
            try {
                output.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileOutputStream fos;
        try {

            fos = new FileOutputStream(output, false);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        Log.i("TAG", "screencap:大小=" + output.length() +" 路径："+output.getAbsolutePath());

    }
}
