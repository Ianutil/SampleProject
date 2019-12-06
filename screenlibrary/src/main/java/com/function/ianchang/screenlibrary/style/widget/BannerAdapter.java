package com.function.ianchang.screenlibrary.style.widget;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.function.ianchang.screenlibrary.R;
import com.function.ianchang.screenlibrary.bean.BannerInfo;

import java.util.List;

/**
 * Created by martindu on 08/09/2017.
 */

public class BannerAdapter extends PagerAdapter{

    private ImageView mPlaceholder;
    private boolean mLooper;
    private List<BannerInfo> datas;
    private ViewPager viewPager;

    public BannerAdapter(List<BannerInfo> been, boolean looper, ImageView placeholder) {
        this.datas = been;
        this.mLooper = looper;
        this.mPlaceholder = placeholder;
    }

    @Override
    public int getCount() {
        if (datas == null || datas.isEmpty())
            return 1;
        else if (mLooper && datas.size() > 1)
            return Integer.MAX_VALUE;
        else
            return datas.size();
    }

    public int getItemPosition(){
        int position = viewPager.getCurrentItem();
        return position % datas.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (datas == null || datas.isEmpty()) {
            container.addView(mPlaceholder);
            return mPlaceholder;
        } else {

            int index = position % datas.size();

            BannerInfo info = datas.get(index);


            View contentView = LayoutInflater.from(container.getContext()).inflate(R.layout.item_banner, null, false);
            container.addView(contentView);

            contentView.setTag(position);

            ImageView placeView = contentView.findViewById(R.id.item_img);

            placeView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(container.getContext()).load(info.file).into(placeView);

            return contentView;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(((View) object));
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    public void setViewPager(ViewPager viewPager){
        this.viewPager = viewPager;
    }


}
