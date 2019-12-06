package com.function.ianchang.screenlibrary.style.widget;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.function.ianchang.screenlibrary.R;
import com.function.ianchang.screenlibrary.bean.BannerInfo;
import com.function.ianchang.screenlibrary.style.BaseFragment;
import com.function.ianchang.screenlibrary.bean.ScreenGroupInfo;

import java.util.List;

/**
 * Created by ianchang on 2017/10/30.
 *
 * 单屏布局结构
 *
 */
public class VideoFragment extends BaseFragment implements View.OnClickListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnVideoSizeChangedListener, SurfaceHolder.Callback{

    private View containerView;
    private TextView videoText;

    private ScreenGroupInfo screenGroupInfo;

    private MediaPlayer mediaPlayer;
    private SurfaceView videoView;
    private List<BannerInfo> datas;
    private int index; // 播放文件
    private int currentPosition; //  播放位置
    private boolean isPause, isPlay;

    public static VideoFragment create(ScreenGroupInfo screenGroupInfo){
        VideoFragment fragment = new VideoFragment();
        fragment.screenGroupInfo = screenGroupInfo;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);

        containerView = inflater.inflate(R.layout.layout_style_02, container, false);
        return containerView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("index", index);
        outState.putBoolean("isPause", isPause);
        outState.putBoolean("isPlay", isPlay);
        outState.putInt("currentPosition", mediaPlayer.getCurrentPosition());

        log("保存视频位置和状态："+index+" isPause="+isPause+" isPlay="+isPlay+" currentPosition="+currentPosition);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null){
            index = savedInstanceState.getInt("index");
            currentPosition = savedInstanceState.getInt("currentPosition");
            isPause = savedInstanceState.getBoolean("isPause");
            isPlay = savedInstanceState.getBoolean("isPlay");

            log("获取的视频位置和状态："+index+" isPause="+isPause+" isPlay="+isPlay+" currentPosition="+currentPosition);

        }else {
            index = 0;
            currentPosition = 0;
        }

        videoText = (TextView) view.findViewById(R.id.video_text);

        // 数据绑定
        if (screenGroupInfo != null && screenGroupInfo.datas != null){
            datas = (List<BannerInfo>) screenGroupInfo.datas;
        }else {
            videoText.setText("未设置视频URL");
        }


        videoView = (SurfaceView)view.findViewById(R.id.video_view);
        SurfaceHolder holder = videoView.getHolder();
        holder.addCallback(this);

        videoView.setOnClickListener(this);

        // 播放
        mediaPlayer = new MediaPlayer();
        // 设置相关的监听器
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnVideoSizeChangedListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mediaPlayer != null && mediaPlayer.getDuration() > 1 && isPause){
            isPause = false;
            mediaPlayer.start();
            return;
        }

        if (mediaPlayer != null){
            BannerInfo info = datas.get(index);
            startMediaPlayer(info.file);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        pauseMediaPlayer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopMediaPlayer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopMediaPlayer();
    }

    @Override
    public void onClick(View v) {

//        if (mediaPlayer.isPlaying()){
//            return;
//        }
//
//        BannerInfo info = datas.get(index);
//        startMediaPlayer(info.file);
    }

    /**
     * 初始化播放器
     */
    public void startMediaPlayer(String url) {
        try {

            if (TextUtils.isEmpty(url)){
                log("视频文件无法播放:url=" + url + " index="+index);
                videoText.setText("未找到视频源，无法正常播放");
                return;
            }

            videoText.setText("");

            log("播放器:播放文件" + url + " 播放文件索引:"+index);

            mediaPlayer.setDataSource(getContext(), Uri.parse(url));
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
        log("播放器：暂停播放");

        if (mediaPlayer != null){
            if (mediaPlayer.isPlaying()) {
                isPause = true;
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
            if (isPlay){
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
                isPlay = false;
            }

        }
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        log("onVideoSizeChanged->width="+width+",height="+height);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        log("播放完成onCompletion->" + index);

        // 播放完成一个文件，置空一次
        currentPosition = 0;

        if (index >= datas.size() - 1){
            index = 0;
        }else{
            index++;
        }

        mp.reset();
        BannerInfo info = datas.get(index);
        startMediaPlayer(info.file);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

        log("加载完成多少onBufferingUpdate->"+percent);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        log("onPrepared->时长："+mp.getDuration());
        int videoW = mediaPlayer.getVideoWidth();
        int videoH = mediaPlayer.getVideoHeight();

        int widthPixels = videoView.getMeasuredWidth();
        int heightPixels = videoView.getMeasuredHeight();
        log("视频加载完成，准备开始播放");
        log("onPrepared->View:width="+widthPixels+",height="+heightPixels+",视频：width="+videoW+",height="+videoH);

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) videoView.getLayoutParams();

        params.width = widthPixels;
        params.height = (int)(((float)widthPixels) / videoW * videoH + 0.5f);

        params.gravity = Gravity.CENTER;
        videoView.setLayoutParams(params);

        log("上次视频开始播放位置:"+currentPosition);

        // 播放上次播放的地方
        mp.seekTo(currentPosition);
        isPlay = true;
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
