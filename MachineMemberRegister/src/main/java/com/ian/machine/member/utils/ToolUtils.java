/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  ToolUtils
 * Created by  ianchang on 2018-08-23 16:16:43
 * Last modify date   2018-08-23 16:16:12
 */

package com.ian.machine.member.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.opengl.GLES20;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RawRes;
import android.util.Log;

import com.ian.machine.member.MachineMemberApplication;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
 * Created by ianchang on 2018/8/9.
 */

public class ToolUtils {

    public static String getRootDir() {
        File root;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            root = new File(Environment.getExternalStorageDirectory(), "detector");
        } else {
            root = MachineMemberApplication.shareInstance().getFilesDir();
        }

        if (!root.exists())
            root.mkdirs();

        return root.getAbsolutePath();
    }

    public static String getFaceShapeModelPath() {
        String targetPath = getRootDir() + File.separator + "shape_predictor_68_face_landmarks.dat";
        return targetPath;
    }

    @NonNull
    public static final void copyFileFromRawToOthers(@NonNull final Context context, @RawRes int id, @NonNull final String targetPath) {
        InputStream in = context.getResources().openRawResource(id);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(targetPath, false);
            byte[] buff = new byte[1024];
            int read = 0;
            while ((read = in.read(buff)) > 0) {
                out.write(buff, 0, read);
            }
            out.flush();
            Log.e("TAG", "下载完成..." + targetPath);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**********
     * 保存成图片
     * @param width
     * @param height
     */
    public static Bitmap sendImage(int width, int height) {
        ByteBuffer rgbaBuf = ByteBuffer.allocateDirect(width * height * 4);
        rgbaBuf.position(0);
        long start = System.nanoTime();
        GLES20.glReadPixels(0, 0, width, height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, rgbaBuf);
        long end = System.nanoTime();
        Log.e("ToolUtils", "glReadPixels: " + (end - start));
        return saveRgb2Bitmap(rgbaBuf, ToolUtils.getRootDir() + "/gl_dump_" + width + "_" + height + ".png", width, height);
    }

    /*******
     * 保存图片
     * @param buf
     * @param filename
     * @param width
     * @param height
     * @return
     */
    public static Bitmap saveRgb2Bitmap(Buffer buf, String filename, int width, int height) {
        Log.d("ToolUtils", "Creating " + filename);
        BufferedOutputStream bos = null;
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        try {
            bos = new BufferedOutputStream(new FileOutputStream(filename));
            bmp.copyPixelsFromBuffer(buf);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, bos);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return bmp;
    }

    /********
     * 保存图片
     * @param bitmap
     * @param filename
     * @return
     */
    public static boolean saveBitmapToFile(Bitmap bitmap, String filename) {
        Log.d("ToolUtils", "Creating " + filename);
        if (bitmap == null) return false;
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(filename));
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, bos);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }

    /*********
     *
     * @param bitmap
     * @param rect
     * @param path
     * @param margin 120
     */
    public static boolean saveFaceOfPerson(Bitmap bitmap, Rect rect, String path, int margin) {

        // 截脸
        int x = rect.left - margin / 2;
        int y = rect.top - margin / 2;
        int width = rect.width() + margin;
        int height = rect.height() + margin;
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (width > bitmap.getWidth()) width = bitmap.getWidth();
        if (height > bitmap.getHeight()) height = bitmap.getHeight();

        if (y + height > bitmap.getHeight()){
            y = bitmap.getHeight() - height;
        }

        if (x + width > bitmap.getWidth()){
            x = bitmap.getWidth() - width;
        }

        Bitmap face = Bitmap.createBitmap(bitmap, x, y, width, height);
        return saveBitmapToFile(face, path);
    }


    /**
     *
     * Saves a Bitmap object to disk for analysis.
     *
     * @param bitmap The bitmap to save.
     */
    public static void saveBitmap(Bitmap bitmap) {
        String root = ToolUtils.getRootDir();
        Log.i("TAG", String.format("Saving %dx%d bitmap to %s.", bitmap.getWidth(), bitmap.getHeight(), root));
        final File myDir = new File(root);

        if (!myDir.mkdirs()) {
            Log.i("TAG", "Make dir failed");
        }

        String name = "preview.png";
        File file = new File(myDir, name);
        if (file.exists()) {
            file.delete();
        }

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 99, out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
