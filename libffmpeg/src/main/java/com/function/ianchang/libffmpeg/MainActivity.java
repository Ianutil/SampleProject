/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  MainActivity
 * Created by  ianchang on 2018-01-25 16:06:57
 * Last modify date   2018-01-25 16:06:50
 */

package com.function.ianchang.libffmpeg;

import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback, MediaPlayer.OnCompletionListener{

    private FFmpegUtils ffmpeg;

    private SurfaceView surfaceView;
    private VideoView videoView;
    private String resVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoView = (VideoView) findViewById(R.id.videoView);
        videoView.getHolder().addCallback(this);
        videoView.setOnCompletionListener(this);
//        surfaceView = (SurfaceView)findViewById(R.id.surfaceView);
//        surfaceView.getHolder().addCallback(this);


        ffmpeg = new FFmpegUtils();

        putVideo();
    }

    public void testFFmpeg(View view){

        Toast.makeText(this, "点击下一个", Toast.LENGTH_SHORT).show();

        String result = ffmpeg.testFFmpeg("TEST");

        Log.d("TAG", "result:"+result);


    }


    public void playVideo(View view){

        File resFile = new File(resVideo);

        Log.d("TAG", "playVideo->resFile = "+resFile.getPath());
        Log.d("TAG", "playVideo->resFile = "+resFile.exists());


        ffmpeg.paly(resVideo, videoView.getHolder().getSurface());
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {


    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//        videoView.setVideoPath(resVideo);
//        videoView.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }


    private void putVideo(){
        try {
            String fileName = "test.mp4";
//            String fileName = "111.avi";
            File outFile = new File(getCacheDir(), fileName);

            BufferedInputStream bis = new BufferedInputStream(getAssets().open(fileName));
            FileOutputStream fos = new FileOutputStream(outFile);

            byte[] buf = new byte[1024];
            int len;
            while ((len = bis.read(buf)) != -1){
                fos.write(buf, 0, len);
            }

            fos.flush();
            fos.close();
            bis.close();

            Log.d("TAG", "outFile = "+outFile.exists());

            resVideo = outFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.seekTo(0);
    }
}
