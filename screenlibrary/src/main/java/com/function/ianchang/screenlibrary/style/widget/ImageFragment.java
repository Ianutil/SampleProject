package com.function.ianchang.screenlibrary.style.widget;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.function.ianchang.screenlibrary.DepthPageTransformer;
import com.function.ianchang.screenlibrary.FixedSpeedScroller;
import com.function.ianchang.screenlibrary.R;
import com.function.ianchang.screenlibrary.bean.BannerInfo;
import com.function.ianchang.screenlibrary.style.BaseFragment;
import com.function.ianchang.screenlibrary.bean.ScreenGroupInfo;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ianchang on 2017/10/30.
 * <p>
 * 单屏布局结构
 */
public class ImageFragment extends BaseFragment {

    private View containerView;

    private ScreenGroupInfo screenGroupInfo;
    private List<BannerInfo> mDatas;
    private ViewPager mViewPager;
    private Timer mTimer;
    private BannerAdapter adapter;
    private boolean isPause; // 是否暂停

    public static ImageFragment create(ScreenGroupInfo screenGroupInfo) {
        ImageFragment fragment = new ImageFragment();
        fragment.screenGroupInfo = screenGroupInfo;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);

        containerView = inflater.inflate(R.layout.layout_style_01, container, false);

        return containerView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int currentItemPosition;
        if (savedInstanceState != null){
            currentItemPosition = savedInstanceState.getInt("currentItemPosition");
            log("拿到上次图片播放位置："+currentItemPosition);
        }else {
            currentItemPosition = 0;
        }

        mViewPager = (ViewPager) containerView.findViewById(R.id.viewpager);

        mViewPager.setPageTransformer(true, new DepthPageTransformer());
        setSlowingScroller();

        // 设置默认图片
        ImageView defaultView = new ImageView(getContext());
        defaultView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        int orientation = screenGroupInfo == null ? ScreenGroupInfo.LANDSCAPE : screenGroupInfo.orientation;
        if (ScreenGroupInfo.LANDSCAPE == orientation) {
            Glide.with(this).load(R.mipmap.bailian_default_landscape).into(defaultView);
        } else {
            Glide.with(this).load(R.mipmap.bailian_default_portrait).into(defaultView);
        }

        // 获取数据
        if (screenGroupInfo != null && screenGroupInfo.datas != null) {
            mDatas = (List<BannerInfo>) screenGroupInfo.datas;
        }

        adapter = new BannerAdapter(mDatas, true, defaultView);
        adapter.setViewPager(mViewPager);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(currentItemPosition);

        // 定时器
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                // 没有显示在屏幕上面时，不再更新UI
                if (!isPause){
                    mHandler.sendEmptyMessage(1);
                }
            }
        },  1 * 60 * 1000, 1 * 60 * 1000);  // 1 * 60 * 1000, 1000
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        int currentItemPosition = adapter.getItemPosition();
        outState.putInt("currentItemPosition", currentItemPosition);

        log("保存图片播放位置："+currentItemPosition);
    }

    @Override
    public void onResume() {
        super.onResume();
        isPause = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        isPause = true;
    }

    @Override
    public void onDestroyView() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        super.onDestroyView();
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 1){
                if (mViewPager != null && mDatas != null && !mDatas.isEmpty()) {
                    log("******************开始切换图片******************");
                    int index = mViewPager.getCurrentItem();
                    index++;

                    mViewPager.setCurrentItem(index);

                    index = adapter.getItemPosition();
                    log("切换下一张图片："+index+" url="+mDatas.get(index).file);
                }
            }

        }
    };

    private void setSlowingScroller() {
        try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(mViewPager.getContext(), sInterpolator);
            mScroller.set(mViewPager, scroller);
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
    }

    private static final Interpolator sInterpolator = new Interpolator() {
        @Override
        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t * t * t + 1.0f;
        }
    };

}
