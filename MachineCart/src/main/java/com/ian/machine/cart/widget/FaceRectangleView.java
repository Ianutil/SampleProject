package com.ian.machine.cart.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ian.machine.cart.R;
import com.plattysoft.leonids.ParticleSystem;
import com.tzutalin.dlib.VisionDetRet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ianchang on 2018/8/15.
 */

public class FaceRectangleView extends View {
    private static final String TAG = FaceRectangleView.class.getSimpleName();

    private Paint mFacePaint;

    private List<VisionDetRet> data;

    public FaceRectangleView(Context context) {
        this(context, null);
    }

    public FaceRectangleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FaceRectangleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        data = new ArrayList<>();

        mFacePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFacePaint.setColor(Color.RED);
        float stroke = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
        mFacePaint.setStrokeWidth(stroke);
        mFacePaint.setStyle(Paint.Style.STROKE);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        canvas.drawColor(0x3f8E0000);
        canvas.drawColor(0x3FFF88FF);


//        canvas.drawRect(20, 20, getMeasuredWidth() - 20, getMeasuredHeight() - 20, mFacePaint);
        Rect rect = new Rect();
        ArrayList<Point> landmarks;
        for (VisionDetRet ret : data) {
            rect.left = ret.getLeft();
            rect.right = ret.getRight();
            rect.bottom = ret.getBottom();
            rect.top = ret.getTop();
            canvas.drawRect(rect, mFacePaint);

            // Draw landmark
            landmarks = ret.getFaceLandmarks();
            Log.e(TAG, "landmark=" + landmarks.size());
            for (Point point : landmarks) {
                canvas.drawCircle(point.x, point.y, 2, mFacePaint);
            }
        }


    }

    public void setViewSize(int width, int height) {
        Log.e(TAG, "setViewSize:" + width + " height=" + height + " width:" + getMeasuredWidth() + " height:" + getMeasuredHeight());

        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = width;
        params.height = height;
        this.setLayoutParams(params);

    }


    public void updateRectangle(List<VisionDetRet> data) {
        this.data.clear();
        this.data.addAll(data);
        postInvalidate();

//        handler.sendEmptyMessageDelayed(0, 600);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

//            if (data.size() > 2) {
            ParticleSystem particleSystem = new ParticleSystem((Activity) getContext(), 100, R.drawable.star, 800);
            particleSystem.setSpeedRange(0.1f, 0.25f);
            particleSystem.oneShot(FaceRectangleView.this, 500);
//            }
            Toast.makeText(getContext(), "恭喜您，中了一张优惠券!!!", Toast.LENGTH_SHORT).show();
        }
    };
}
