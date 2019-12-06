package com.example.ianchang.myapplication;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ianchang on 2017/10/18.
 */

public class VideoActivity extends AppCompatActivity implements View.OnClickListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnVideoSizeChangedListener, SurfaceHolder.Callback{

    private MediaPlayer mediaPlayer;
    private SurfaceView videoView;
    private List<String> datas;
    private int index;
    private ImageView placeView, startView;
    private int currentPosition;
    private boolean isPause;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏

        setContentView(R.layout.activity_video);


        datas = new ArrayList<>();
        datas.add("http://mp4.vjshi.com/2017-10-17/941d4e3f4aa6b00e7d2713cf08e37f99.mp4");
        datas.add("http://mp4.vjshi.com/2017-10-15/a6555e4aae18e24da344bdd7b33ddde0.mp4");
        datas.add("http://mp4.vjshi.com/2017-10-16/0270343e6bcb48050a749cf10ae31407.mp4");
        datas.add("http://mp4.vjshi.com/2017-10-17/7ebcf5e19d0d1d5fcc5b94645f1f46fd.mp4");
        datas.add("http://mp4.vjshi.com/2017-10-17/a724d90ab06b5223dc6d3e9243f9ec7d.mp4");
        datas.add("http://mp4.vjshi.com/2017-09-08/025c4cf374d3f67840edd180d2817068.mp4");

        if (savedInstanceState != null){
            index = savedInstanceState.getInt("index");
            currentPosition = savedInstanceState.getInt("currentPosition");
            isPause = savedInstanceState.getBoolean("isPause");
        }else {
            index = getIntent().getIntExtra("index", 0);
            currentPosition = 0;
        }

        Log.e("TAG", "----------------->>>>>>>>index="+index);

        placeView = (ImageView)findViewById(R.id.item_img);
        Glide.with(this).load(datas.get(index)).into(placeView);

        startView = (ImageView)findViewById(R.id.item_btn);
        startView.setImageResource(R.drawable.mediacontroller_play);
//        String icon = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508320493800&di=3ade518a37370135723597fc8ec26b18&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F01aa2f5825918ba84a0d304fb7e7f0.jpg";
//        Glide.with(this).load(icon).into(startView);

        startView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mediaPlayer.isPlaying()){
                    startView.setImageResource(R.drawable.mediacontroller_play);
                    mediaPlayer.pause();

                    isPause = true;
                    return;
                }

                startView.setImageResource(R.drawable.mediacontroller_pause);
                v.setVisibility(View.GONE);

                if (isPause){
                    isPause = false;
                    mediaPlayer.start();
                    return;
                }


                startMediaPlayer(datas.get(index));
            }
        });

        videoView = (SurfaceView)findViewById(R.id.item_video);
        SurfaceHolder holder = videoView.getHolder();
        holder.addCallback(this);

        videoView.setOnClickListener(this);

        mediaPlayer = new MediaPlayer();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("index", index);
        outState.putBoolean("isPause", isPause);
        outState.putInt("currentPosition", mediaPlayer.getCurrentPosition());

    }

    /**
     * 初始化播放器
     */
    public void startMediaPlayer(String url) {
        try {
            placeView.setVisibility(View.GONE);
            mediaPlayer.setDataSource(this, Uri.parse(url));
            log("播放器:播放文件" + url);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnPreparedListener(this);
            // 设置相关的监听器
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnVideoSizeChangedListener(this);
            mediaPlayer.setLooping(true);
            // 播放准备，使用异步方式，配合OnPreparedListener
            mediaPlayer.prepareAsync();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();

        pauseMediaPlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMediaPlayer();
    }

    /******
     * 暂停播放
     */
    public void pauseMediaPlayer(){
        log("播放器：暂停播放");

        if (mediaPlayer != null){
            if (mediaPlayer.isPlaying()) {
                startView.setImageResource(R.drawable.mediacontroller_play);
                mediaPlayer.pause();
            }
        }

    }

    /******
     * 停止播放
     */
    public void stopMediaPlayer(){
        log("播放器：停止播放");

        if (mediaPlayer != null) {
            startView.setImageResource(R.drawable.mediacontroller_play);
            mediaPlayer.reset();
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    private void log(String msg){
        Log.e("TAG", msg);
    }

    @Override
    public void onClick(View v) {

        if (startView.getVisibility() == View.VISIBLE){
            startView.setVisibility(View.GONE);
        }else{
            startView.setVisibility(View.VISIBLE);
        }

        if (!mediaPlayer.isPlaying()){
            startView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        log("onVideoSizeChanged->width="+width+",height="+height);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        log("onCompletion->");

        mp.reset();
        mp.start();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

        log("onBufferingUpdate->"+percent);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        log("onPrepared->时长："+mp.getDuration());
        int videoW = mediaPlayer.getVideoWidth();
        int videoH = mediaPlayer.getVideoHeight();

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) videoView.getLayoutParams();
        DisplayMetrics dm = videoView.getContext().getResources().getDisplayMetrics();

        params.width = dm.widthPixels;
        params.height = (int)(((float)dm.widthPixels) / videoW * videoH + 0.5f);

        params.gravity = Gravity.CENTER;


        log("onPrepared--：video="+videoW + ","+videoH+",params="+params.width+","+params.height+",pixels="+dm.widthPixels+","+dm.heightPixels);

        mp.seekTo(currentPosition);

        startView.setImageResource(R.drawable.mediacontroller_pause);

        videoView.setLayoutParams(params);

        startView.setImageResource(R.drawable.mediacontroller_pause);
        mp.start();
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mediaPlayer.setDisplay(holder);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
