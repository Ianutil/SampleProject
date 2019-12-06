package com.example.ianchang.myapplication.vitamio;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ianchang.myapplication.R;

import java.io.File;
import java.io.IOException;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnInfoListener;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class VideoBufferActivity extends Activity implements OnInfoListener, OnBufferingUpdateListener {

//  private String path = "http://mp4.vjshi.com/2017-10-15/a6555e4aae18e24da344bdd7b33ddde0.mp4";
  private String path = "http://live.3gv.ifeng.com/zixun.m3u8";
  private Uri uri;
  private VideoView mVideoView;
  private ProgressBar pb;
  private TextView downloadRateView, loadRateView;

  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    if (!LibsChecker.checkVitamioLibs(this))
      return;
    setContentView(R.layout.videobuffer);

    Log.d("TAG", "****************** SSSSS ***************************");

    mVideoView = (VideoView) findViewById(R.id.buffer);
    pb = (ProgressBar) findViewById(R.id.probar);

    downloadRateView = (TextView) findViewById(R.id.download_rate);
    loadRateView = (TextView) findViewById(R.id.load_rate);
    if (path == "") {
      // Tell the user to provide a media file URL/path.
      Toast.makeText(
          VideoBufferActivity.this,
          "Please edit VideoBuffer Activity, and set path"
              + " variable to your media file URL/path", Toast.LENGTH_LONG).show();
      return;
    } else {
      /*
       * Alternatively,for streaming media you can use
       * mVideoView.setVideoURI(Uri.parse(URLstring));
       */
      File cacheFile = getVideoCacheFile();
      if (cacheFile.length() > 1024){
        mVideoView.setBufferSize(0);
      }else {
        mVideoView.setBufferSize(512 * 1024);
      }

      uri = Uri.parse(path);
      uri = Uri.parse("cache:"+cacheFile.getAbsolutePath()+":" + uri);
      mVideoView.setVideoURI(uri);
      mVideoView.setMediaController(new MediaController(this));
      mVideoView.requestFocus();
      mVideoView.setOnInfoListener(this);
      mVideoView.setOnBufferingUpdateListener(this);
      mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
          // optional need Vitamio 4.0
          mediaPlayer.setPlaybackSpeed(1.0f);
        }
      });
    }

  }

  @Override
  public boolean onInfo(MediaPlayer mp, int what, int extra) {
    switch (what) {
    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
      if (mVideoView.isPlaying()) {
        mVideoView.pause();
        pb.setVisibility(View.VISIBLE);
        downloadRateView.setText("");
        loadRateView.setText("");
        downloadRateView.setVisibility(View.VISIBLE);
        loadRateView.setVisibility(View.VISIBLE);

      }
      break;
    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
      mVideoView.start();
      pb.setVisibility(View.GONE);
      downloadRateView.setVisibility(View.GONE);
      loadRateView.setVisibility(View.GONE);
      break;
    case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
      downloadRateView.setText("" + extra + "kb/s" + "  ");
      break;
    }
    return true;
  }

  @Override
  public void onBufferingUpdate(MediaPlayer mp, int percent) {
    loadRateView.setText(percent + "%");
  }


  private File getRootCacheDir(){
    File root;
    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
      root = Environment.getRootDirectory();
    }else {
      root = this.getCacheDir();
    }
    return root;
  }


  private File getVideoCacheFile(){
    File cacheFile = new File(getRootCacheDir(), "download.mp4");
    if (cacheFile.exists()){
      cacheFile.delete();
      try {
        cacheFile.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return cacheFile;
  }
}
