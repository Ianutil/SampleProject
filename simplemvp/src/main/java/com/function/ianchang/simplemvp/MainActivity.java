package com.function.ianchang.simplemvp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

//import com.function.ianchang.libffmpeg.FFmpegUtils;
import com.function.ianchang.simplemvp.base.BaseActivity;
import com.function.ianchang.simplemvp.base.BasePresenter;

import java.nio.ByteBuffer;

import static android.R.attr.height;
import static android.R.attr.width;

public class MainActivity extends BaseActivity {

//    private FFmpegUtils ffmpeg;

    private MediaProjectionManager projectionManager;

    @Override
    public BasePresenter initPresenter() {
        return new MainPresenter(this, this);
    }

    @Override
    public View initContentView() {
        View contenView = LayoutInflater.from(this).inflate(R.layout.activity_home, null, false);
        return contenView;
    }


    public void nextView(View view){

        showToast("点击下一个");
//        mPresenter.pushController(WebviewActivity.class);

        projectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        startActivityForResult(projectionManager.createScreenCaptureIntent(),123);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            //获取MediaProjection
            MediaProjection mediaProjection = projectionManager.getMediaProjection(requestCode,data);

            DisplayMetrics metrics = this.getResources().getDisplayMetrics();

            ImageReader imageReader = ImageReader.newInstance(metrics.widthPixels, metrics.heightPixels, PixelFormat.RGBA_8888, 1);
            if(imageReader!=null){
                Log.d("TAG", "imageReader Successful");
            }
            mediaProjection.createVirtualDisplay("ScreenShout",
                    metrics.widthPixels,metrics.heightPixels,metrics.densityDpi,
                    DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                    imageReader.getSurface(),null,null);

            SystemClock.sleep(1000);
            Image image = imageReader.acquireNextImage();


            int width = image.getWidth();
            int height = image.getHeight();
            final Image.Plane[] planes = image.getPlanes();
            final ByteBuffer buffer = planes[0].getBuffer();
            int pixelStride = planes[0].getPixelStride();
            int rowStride = planes[0].getRowStride();
            int rowPadding = rowStride - pixelStride * width;
            Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
            bitmap.copyPixelsFromBuffer(buffer);
            image.close();


            ImageView imageView = (ImageView)findViewById(R.id.image);
            imageView.setImageBitmap(bitmap);
            imageView.postInvalidate();

        }
    }

    public void testFFmpeg(View view){

        showToast("点击下一个");

//        ffmpeg = new FFmpegUtils();
//        String result = ffmpeg.testFFmpeg("TEST");
//
//        Log.d("TAG", "result:"+result);
    }
}
