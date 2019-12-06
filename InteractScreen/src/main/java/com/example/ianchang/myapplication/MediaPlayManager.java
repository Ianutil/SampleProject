package com.example.ianchang.myapplication;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by ianchang on 2017/10/19.
 *
 * @see #setContext(Context)  第一步
 * @see #setStartView(ImageView, ImageView) 第二步
 * @see #setStartView(ImageView, ImageView) 第三步
 *
 */
public class MediaPlayManager implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnVideoSizeChangedListener,  SurfaceHolder.Callback{

    private MediaPlayer mediaPlayer;

    private SurfaceView videoView;
    private ImageView placeView, startView;
    private int currentPosition;
    private boolean isPause, isPlay;
    private Context context;
    private String url;

    private static MediaPlayManager instance;
    public static MediaPlayManager shareInstance(){

        synchronized (MediaPlayManager.class){
            if (instance == null){
                instance = new MediaPlayManager();
            }
        }

        return instance;
    }

    private MediaPlayManager(){
        mediaPlayer = new MediaPlayer();
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        LogUtil.e("onVideoSizeChanged->width="+width+",height="+height);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        LogUtil.e("onCompletion->");

        mp.reset();
        mp.start();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

        LogUtil.e("onBufferingUpdate->"+percent);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        LogUtil.e("onPrepared->时长："+mp.getDuration());
        int videoW = mediaPlayer.getVideoWidth();
        int videoH = mediaPlayer.getVideoHeight();

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) videoView.getLayoutParams();
        DisplayMetrics dm = context.getResources().getDisplayMetrics();

        params.width = dm.widthPixels;
        params.height = (int)(((float)dm.widthPixels) / videoW * videoH + 0.5f);

        params.gravity = Gravity.CENTER;


        LogUtil.e("onPrepared-->：video="+videoW + ","+videoH+",params="+params.width+","+params.height+",pixels="+dm.widthPixels+","+dm.heightPixels);

        mp.seekTo(currentPosition);

        startView.setImageResource(R.drawable.mediacontroller_pause);

        videoView.setLayoutParams(params);

        startView.setImageResource(R.drawable.mediacontroller_pause);
        mp.start();

        isPlay = true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mediaPlayer != null){
            mediaPlayer.setDisplay(holder);
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    /**
     * 初始化播放器
     */
    public void startMediaPlayer(String url) {
        try {
            this.url = url;
            placeView.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);

            mediaPlayer.reset();
            mediaPlayer.setDataSource(context, Uri.parse(url));
            LogUtil.e("播放器:播放文件" + url);
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

    /******
     * 暂停播放
     */
    public void pauseMediaPlayer(){
        LogUtil.e("播放器：暂停播放");
        if (mediaPlayer != null && isPlay){
            if (mediaPlayer.isPlaying()) {
                startView.setImageResource(R.drawable.mediacontroller_play);
                mediaPlayer.pause();
                isPlay = false;
            }
        }
    }

    /******
     * 停止播放
     */
    public void stopMediaPlayer(){
        LogUtil.e("播放器：停止播放");
        if (mediaPlayer != null && isPlay){
            if (startView != null){
                startView.setImageResource(R.drawable.mediacontroller_play);
            }

            isPlay = false;
            mediaPlayer.reset();
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    /******
     * 设置上下文
     * @param context
     */
    public void setContext(Context context){
        this.context = context;

    }

    /*******
     * 设置Video
     *
     * @param surfaceView
     */
    public void setSurfaceView(SurfaceView surfaceView){
        this.videoView = surfaceView;
        this.videoView.getHolder().addCallback(this);

        videoView.setOnClickListener(new View.OnClickListener() {
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
        });
    }

    /******
     * 设置播放控制器
     *
     * @param _startView
     * @param _placeView
     */
    public void setStartView(ImageView _startView, ImageView _placeView){
        this.startView = _startView;
        this.placeView = _placeView;

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


                if (!TextUtils.isEmpty(url)){
                    startMediaPlayer(url);
                }
            }
        });
    }


}
