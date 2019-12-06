/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  MainActivity
 * Created by  ianchang on 2018-02-24 10:35:46
 * Last modify date   2018-02-24 10:35:45
 */

package com.function.ianchang.simplegreendao;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.opengl.GLES20;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.function.ianchang.dao.ActivityInfoDao;
import com.function.ianchang.dao.DaoMaster;
import com.function.ianchang.dao.DaoSession;
import com.function.ianchang.dao.GreenDaoContext;
import com.function.ianchang.dao.MySQLiteOpenHelper;
import com.function.ianchang.dao.PageInfoDao;
import com.function.ianchang.dao.ScheduleInfoDao;
import com.function.ianchang.dao.UserInfoDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DaoMaster.OpenHelper helper;

    private SQLiteDatabase database;
    private DaoSession mDaoSession;
    private DaoMaster mDaoMaster;
    private TextView showResult;
    private StringBuilder builder;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        setDatabase();

        builder = new StringBuilder();
        showResult = (TextView) findViewById(R.id.text_view);
        image = (ImageView) findViewById(R.id.image);

        dialogScreencap(showResult);
    }

    @Override
    public File getDatabasePath(String name) {

        Log.d("TAG", "getDatabasePath:" + name);
        File root = getRootDir();
        File database = new File(root, name);

        if (!database.exists()) {
            try {
                database.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return database;
//        return super.getDatabasePath(name);
    }


    /*********
     * 重载这个方法，是用来打开SD卡上的数据库的，android 2.3及以下会调用这个方法。
     * @param name
     * @param mode
     * @param factory
     * @return
     */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
//        return super.openOrCreateDatabase(name, mode, factory);

        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), factory);
        Log.d("TAG", "openOrCreateDatabase:" + name);

        return database;

    }

    /**********
     * Android 4.0会调用此方法获取数据库。
     * @param name
     * @param mode
     * @param factory
     * @param errorHandler
     * @return
     */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
//        return super.openOrCreateDatabase(name, mode, factory, errorHandler);

        return openOrCreateDatabase(name, mode, factory);
    }

    private File getRootDir() {
        File dir = new File(Environment.getExternalStorageDirectory(), "BL-Electronicscreen/database");

        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    private void setDatabase() {
        // 通过DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为greenDAO 已经帮你做了。
        // 注意：默认的DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
//        helper = new DaoMaster.DevOpenHelper(this, "simple.db", null);
        helper = new MySQLiteOpenHelper(new GreenDaoContext(getApplicationContext()), "simple.db");
//        String file = getDatabasePath("simple.db").getAbsolutePath();
//        Log.d("TAG", "getDatabasePath"+file);

        database = helper.getWritableDatabase();

        // 注意：该数据库连接属于DaoMaster，所以多个 Session 指的是相同的数据库连接。
        mDaoMaster = new DaoMaster(database);
        mDaoSession = mDaoMaster.newSession();

        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
    }

    public void addData(View view) {
        setDatabase();


        long start = System.currentTimeMillis();

        try {

            UserInfo userInfo = new UserInfo();
            userInfo.setName("Util_c");
            userInfo.setAge("28");

            userInfo.setStartDate(new Date());

            userInfo.setStartDate(new Date());
            long id = mDaoSession.getUserInfoDao().insertOrReplace(userInfo);
            Log.d("TAG", "UserInfo.id=" + id);


            PageInfo pageInfo = new PageInfo();
            pageInfo.name = "2018-02-24 11";
            pageInfo.activityCode = "q34535453545345";
            id = mDaoSession.getPageInfoDao().insertOrReplace(pageInfo);
            Log.d("TAG", "PageInfo.id=" + id);

            ActivityInfo activityInfo = new ActivityInfo();
            activityInfo.setActivityCode("q34535453545345");
            activityInfo.setName("2018-02-24 11");
            mDaoSession.getActivityInfoDao().insertOrReplace(activityInfo);

            ScheduleInfo scheduleInfo = new ScheduleInfo();
//            scheduleInfo.startDate = "2018-02-24 11";
//            scheduleInfo.endDate = "2018-02-24 12";
            scheduleInfo.activityCode = "q34535453545345";
            id = mDaoSession.getScheduleInfoDao().insertOrReplace(scheduleInfo);
            Log.d("TAG", "ScheduleInfo.id=" + id);


            List<ScheduleInfo> scheduleInfos = new ArrayList<>();
            scheduleInfos.add(scheduleInfo);
            activityInfo.setScheduleInfos(scheduleInfos);
            List<PageInfo> pageInfos = new ArrayList<>();
            pageInfos.add(pageInfo);
            activityInfo.setPageInfos(pageInfos);
            mDaoSession.getActivityInfoDao().insertOrReplace(activityInfo);

            if (!mDaoSession.getActivityInfoDao().hasKey(activityInfo)) {
                mDaoSession.getActivityInfoDao().insertOrReplace(activityInfo);
                showToast("不包含当前活动");
            } else {
                showToast("包含当前活动");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        start = System.currentTimeMillis() - start;

        Log.d("TAG", "消耗时长" + start / (1000f) + "s");

    }

    public void getData(View view) {

        try {
            long start = System.currentTimeMillis();
            UserInfo userInfo;
            List<UserInfo> data = mDaoSession.getUserInfoDao().loadAll();
            userInfo = mDaoSession.getUserInfoDao().loadByRowId(3);


            userInfo = mDaoSession.getUserInfoDao().queryBuilder().where(UserInfoDao.Properties.StartDate.le(new Date())).unique();

            Log.d("TAG", "Username:" + userInfo.toString());

            Log.d("TAG", "Username:" + data.size() + " " + data.toString());


            Log.d("TAG", "Activity result:" + mDaoSession.getActivityInfoDao().loadAll().toString());

            Log.d("TAG", "Schedule result:" + mDaoSession.getScheduleInfoDao().loadAll().toString());

            Log.d("TAG", "PageInfo result:" + mDaoSession.getPageInfoDao().loadAll().toString());

            QueryBuilder<ScheduleInfo> result = mDaoMaster.newSession().getScheduleInfoDao().queryBuilder();

            List<ScheduleInfo> scheduleInfos = result.where(ScheduleInfoDao.Properties.StartDate.ge("2018-02-24 11")).where(ScheduleInfoDao.Properties.EndDate.le("2018-02-24 12")).list();
//        List<ActivityInfo> scheduleInfos = result.where(ActivityInfoDao.Properties.StartDate.between("2018-02-24 11", "2018-02-24 12")).list();

            Log.d("TAG", "result:" + scheduleInfos.toString());

            if (scheduleInfos != null && scheduleInfos.size() > 0) {
                String activityCode = scheduleInfos.get(0).activityCode;

                Log.d("TAG", "根据activityCode查询相对应的活动页");


                QueryBuilder<ActivityInfo> activityInfos = mDaoSession.getActivityInfoDao().queryBuilder();

                ActivityInfo activityInfo = activityInfos.where(ActivityInfoDao.Properties.ActivityCode.eq(activityCode)).unique();
                Log.d("TAG", "ActivityInfo:" + activityInfo.toString());

                List<PageInfo> pageInfos = mDaoSession.getPageInfoDao().queryBuilder().where(PageInfoDao.Properties.ActivityCode.eq(activityCode)).list();
                Log.d("TAG", "PageInfo:" + pageInfos.toString());

            }

            start = System.currentTimeMillis() - start;

            Log.d("TAG", "消耗时长" + start / (1000f) + "s");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    public void testDate(View view) {
//        SELECT T."ACTIVITY_CODE",T."START_DATE",T."END_DATE",T."data" FROM "BLDBACTIVITY_SCHEDULE_INFO" T  WHERE T."START_DATE">=?

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, 2);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 15);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date startDate = calendar.getTime();

        calendar.setTime(new Date());

        calendar.set(Calendar.MONTH, 2);
        calendar.set(Calendar.DAY_OF_MONTH, 15);
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date endDate = calendar.getTime();


        calendar.setTime(new Date());
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date currentDate = calendar.getTime();
        Log.d("TAG", "Start date:" + startDate.getTime());
        Log.d("TAG", "End date:" + endDate.getTime());
        Log.d("TAG", "Current date:" + currentDate.getTime());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH");
        Log.d("TAG", "start:" + format.format(startDate) + " end:" + format.format(endDate) + " current:" + format.format(currentDate));

        ScheduleInfo info = new ScheduleInfo();
//        info.startDate = "2018-03-02 10";
//        info.endDate = "2018-03-03 10";
        info.startDate = startDate;
        info.endDate = endDate;
        info.activityCode = "测试";

        mDaoSession.getScheduleInfoDao().insertOrReplaceInTx(info);

        Log.d("TAG", "Activity result:" + mDaoSession.getScheduleInfoDao().queryBuilder().list());
        Log.d("TAG", "**************************");

        info = mDaoSession.getScheduleInfoDao().queryBuilder().
                where(ScheduleInfoDao.Properties.StartDate.le(currentDate)).
                where(ScheduleInfoDao.Properties.EndDate.ge(currentDate)).unique();
// String currentDate = "2018-03-02 11";
//        info = mDaoSession.getScheduleInfoDao().queryBuilder().
//                where(ScheduleInfoDao.Properties.StartDate.ge(currentDate)).
//                where(ScheduleInfoDao.Properties.EndDate.le(currentDate)).unique();

        Log.d("TAG", "Activity result:" + info);

    }

    public void selectDate(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View container = LayoutInflater.from(this).inflate(R.layout.dialog_select_timer, null, false);
        builder.setView(container);
        WheelView wheelView = (WheelView) container.findViewById(R.id.hour_picker);
        List<String> datas = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            datas.add("" + i);
        }
        wheelView.setItems(datas);

        wheelView = (WheelView) container.findViewById(R.id.minute_picker);
        datas = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            datas.add("" + i);
        }
        wheelView.setItems(datas);
        wheelView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                Log.i("TAG", "item=" + item + " index=" + selectedIndex);

            }
        });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();


            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


    // 截屏
    public void screencap(View view) {
        try {

            File dir = new File(Environment.getExternalStorageDirectory(), "BL-Electronicscreen/screencap");

            if (!dir.exists()) {
                boolean flag = dir.mkdirs();
                Log.e("TAG", dir.getAbsolutePath() + "文件是否存在flag=" + flag);
                builder.append(dir.getAbsolutePath() + "文件是否存在flag=" + flag);
                builder.append("\n");
            }

            File file = new File(dir, "ABC.jpg");

            if (file.exists()) {
                file.delete();
                boolean flag = file.createNewFile();
                Log.e("TAG", file.getAbsolutePath() + "文件是否存在flag=" + flag);
                builder.append(file.getAbsolutePath() + "文件是否存在flag=" + flag);
                builder.append("\n");
            }

            Process sh = Runtime.getRuntime().exec("su");
            PrintWriter out = new PrintWriter(sh.getOutputStream());
            String cmd = "screencap -p " + file;
//            String cmd = "/system/bin/screencap -p " + file;
            out.println(cmd);
            out.flush();
//            out.println("exit");
//            out.flush();
            out.close();
            sh.waitFor();

            BufferedInputStream in = new BufferedInputStream(sh.getErrorStream());
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) != -1) {
                builder.append(new String(buf, 0, len));
            }
            int result = sh.exitValue();
            Log.e("TAG", "执行系统截屏:result=" + result);
            builder.append("执行系统截屏:result=" + result);
            builder.append("\n");
            Log.e("TAG", "文件路径:" + file.getAbsolutePath() + " 大小：" + file.length());
            builder.append("文件路径:" + file.getAbsolutePath() + " 大小：" + file.length());
            builder.append("\n");

            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            image.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        showResult.setText(builder.toString());
        showResult.postInvalidate();


    }

    // 截屏
    public void myScreencap(View view) {
        try {

//            File dir = new File(Environment.getExternalStorageDirectory(), "BL-Electronicscreen/screencap");
            File cacheDir = getCacheDir();
            Log.d("TAG", "file----------->" + cacheDir.getAbsolutePath());
            File dir = new File("mnt/sdcard/BL-Electronicscreen/screencap/screencap");

            if (!dir.exists()) {
                boolean flag = dir.mkdirs();
                Log.e("TAG", dir.getAbsolutePath() + "文件是否存在flag=" + flag);
                builder.append(dir.getAbsolutePath() + "文件是否存在flag=" + flag);
                builder.append("\n");
            }

            File file = new File(dir, "ABC.jpg");

            if (file.exists()) {
                file.delete();
                boolean flag = file.createNewFile();
                Log.e("TAG", file.getAbsolutePath() + "文件是否存在flag=" + flag);
                builder.append(file.getAbsolutePath() + "文件是否存在flag=" + flag);
                builder.append("\n");
            }

            Process sh = Runtime.getRuntime().exec("su");
            PrintWriter out = new PrintWriter(sh.getOutputStream());
            String cmd = "/data/data/com.function.ianchang.simplegreendao/screencap -p " + file;
            out.println(cmd);
            out.flush();
//            out.println("exit");
//            out.flush();
            out.close();
            sh.waitFor();

            BufferedInputStream in = new BufferedInputStream(sh.getErrorStream());
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) != -1) {
                builder.append(new String(buf, 0, len));
            }
            int result = sh.exitValue();
            Log.e("TAG", "执行系统截屏:result=" + result);
            builder.append("执行系统截屏:result=" + result);
            builder.append("\n");
            Log.e("TAG", "文件路径:" + file.getAbsolutePath() + " 大小：" + file.length());
            builder.append("文件路径:" + file.getAbsolutePath() + " 大小：" + file.length());
            builder.append("\n");
        } catch (Exception e) {
            e.printStackTrace();
        }

        showResult.setText(builder.toString());
        showResult.postInvalidate();


    }


    public void viewScreencap(View view) {

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

        File output = new File("/mnt/sdcard/BL-Electronicscreen", System.currentTimeMillis() + ".jpg");
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

    }


    public void adbWifiConnect(View view) {
        Runtime mRuntime = Runtime.getRuntime();
        try {
            mRuntime.exec("setprop service.adb.tcp.port 5555 ");
            mRuntime.exec("stop adbd ");
            mRuntime.exec("start adbd ");
            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }



    }
    public void adbWifiConnect1(View view) {

        try {

            Process sh = Runtime.getRuntime().exec("su");
            PrintWriter out = new PrintWriter(sh.getOutputStream());
            String cmd = "netstat";
            out.println(cmd);
            out.flush();

//            out.println("adb kill-server");
//            out.flush();
//
//            out.println("adb start-server");
//            out.flush();

            out.println("setprop services.adb.tcp.port 5555");
            out.flush();

            out.println("stop adb");
            out.flush();

            out.println("start adb");
            out.flush();

            out.close();
            sh.waitFor();

            BufferedInputStream in = new BufferedInputStream(sh.getErrorStream());
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) != -1) {
                builder.append(new String(buf, 0, len));
            }
            int result = sh.exitValue();
            Log.e("TAG", "执行结果:result=" + result);
            builder.append("执行结果:result=" + result);
            builder.append("\n");
        } catch (Exception e) {
            e.printStackTrace();
        }

        showResult.setText(builder.toString());
        showResult.postInvalidate();
    }


    // 对话框截图
    public void dialogScreencap(View view) {

//        LoadingDialog dialog = new LoadingDialog(this);
//
//        File output = new File("/mnt/sdcard/BL-Electronicscreen", System.currentTimeMillis() + ".jpg");
//
//        dialog.show();
//
//        ScreentShotUtil.getInstance().takeScreenshot(this, output.getAbsolutePath());


        try {

//            File dir = new File(Environment.getExternalStorageDirectory(), "BL-Electronicscreen/screencap");
            File cacheDir = getCacheDir();
            Log.d("TAG", "file----------->" + cacheDir.getAbsolutePath());
            File dir = new File("mnt/sdcard/BL-Electronicscreen/screencap");

            if (!dir.exists()) {
                boolean flag = dir.mkdirs();
                Log.e("TAG", dir.getAbsolutePath() + "文件是否存在flag=" + flag);
                builder.append(dir.getAbsolutePath() + "文件是否存在flag=" + flag);
                builder.append("\n");
            }

            File file = new File(dir, "ABC.jpg");

            if (file.exists()) {
                file.delete();
                boolean flag = file.createNewFile();
                Log.e("TAG", file.getAbsolutePath() + "文件是否存在flag=" + flag);
                builder.append(file.getAbsolutePath() + "文件是否存在flag=" + flag);
                builder.append("\n");
            }

            Process sh = Runtime.getRuntime().exec("su");
            PrintWriter out = new PrintWriter(sh.getOutputStream());
            File mp4 = new File(file.getParentFile(), "123.mp4");
            Log.d("TAG", "Video:" + mp4.getAbsolutePath());
//            String cmd = "screenrecord --help";
            String cmd = "screenrecord --time-limit 2 " + mp4;
            out.println(cmd);
            out.flush();
            out.close();
            sh.waitFor();

            BufferedInputStream in = new BufferedInputStream(sh.getErrorStream());
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) != -1) {
                builder.append(new String(buf, 0, len));
            }
            int result = sh.exitValue();
            Log.e("TAG", "执行系统截屏:result=" + builder.toString());
            builder.append("执行系统截屏:result=" + builder.toString());
            builder.append("\n");
            builder.append("执行系统截屏:result=" + result);
            builder.append("\n");
            Log.e("TAG", "文件路径:" + mp4.getAbsolutePath() + " 大小：" + mp4.length());
            builder.append("文件路径:" + mp4.getAbsolutePath() + " 大小：" + mp4.length());
            builder.append("\n");

            if (!mp4.exists()) {
                Toast.makeText(this, "录制失败", Toast.LENGTH_SHORT).show();
                Log.e("TAG", "录制失败");
            }

            Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(mp4.getAbsolutePath(), MediaStore.Video.Thumbnails.MINI_KIND);

            image.setImageBitmap(bitmap);
            image.postInvalidate();

//            image.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    Log.e("TAG", "显示结果" );
//
//                    File mp4 = new File("mnt/sdcard/BL-Electronicscreen/screencap", "123.mp4");
//
//                    Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(mp4.getAbsolutePath(), MediaStore.Video.Thumbnails.MINI_KIND);
//
//                    image.setImageBitmap(bitmap);
//                    image.postInvalidate();
//                }
//            }, 1000 * 10);

        } catch (Exception e) {
            e.printStackTrace();
        }

        showResult.setText(builder.toString());
        showResult.postInvalidate();

    }

    public void breathAnimate(View view) {
        startActivity(new Intent(this, BreathActivity.class));
    }


    public void cropScreen(View view) {

        startActivity(new Intent(this, TestGLActivity.class));

//        DisplayMetrics metrics = getResources().getDisplayMetrics();
//
//        sendImage(metrics.widthPixels, metrics.heightPixels);
    }


    /**********
     * 保存成图片
     * @param width
     * @param height
     */
    public void sendImage (int width, int height){
        ByteBuffer rgbaBuf = ByteBuffer.allocateDirect(width * height * 4);
        rgbaBuf.position(0);
        long start = System.nanoTime();
        GLES20.glReadPixels(0, 0, width, height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, rgbaBuf);
        long end = System.nanoTime();
        Log.d("TryOpenGL", "glReadPixels: " + (end - start));
        saveRgb2Bitmap(rgbaBuf, getRootDir().getAbsolutePath() + "/gl_dump_" + width + "_" + height + ".png", width, height);
    }

    public void saveRgb2Bitmap(Buffer buf, String filename, int width, int height) {
        Log.d("TryOpenGL", "Creating " + filename);
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(filename));
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bmp.copyPixelsFromBuffer(buf);
            image.setImageBitmap(bmp);
            image.postInvalidate();
            bmp.compress(Bitmap.CompressFormat.PNG, 90, bos);
            bmp.recycle();
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
    }
}
