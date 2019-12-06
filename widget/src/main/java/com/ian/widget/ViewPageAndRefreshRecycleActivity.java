/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  ViewPageAndRefreshRecycleActivity
 * Created by  ianchang on 2018-06-04 16:34:22
 * Last modify date   2018-06-04 15:47:19
 */

package com.ian.widget;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.ian.widget.fragment.MapViewFragment;
import com.ian.widget.fragment.MenuBothViewFragment;
import com.ian.widget.fragment.MenuLeftViewFragment;
import com.ian.widget.fragment.MenuMoreViewFragment;
import com.ian.widget.fragment.MenuViewFragment;
import com.ian.widget.fragment.PanelFragment;
import com.ian.widget.fragment.RecycleViewFragment;
import com.ian.widget.fragment.RefreshViewFragment;
import com.ian.widget.fragment.TabView01Fragment;
import com.ian.widget.fragment.TabViewFragment;

import java.util.ArrayList;
import java.util.List;

public class ViewPageAndRefreshRecycleActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    private ViewPager viewPager;
    private List<Fragment> data;

    private TextView mNumberText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpage_refresh);

        viewPager = (ViewPager) findViewById(R.id.viewpager);


        mNumberText = (TextView)findViewById(R.id.number_text);

        data = new ArrayList<>();

        data.add(new PanelFragment());
        data.add(new MapViewFragment());
        data.add(new RecycleViewFragment());
        data.add(new RefreshViewFragment());
        data.add(new MenuViewFragment());
        data.add(new TabViewFragment());
        data.add(new MenuLeftViewFragment());
        data.add(new TabView01Fragment());
        data.add(new MenuBothViewFragment());
        data.add(new MenuMoreViewFragment());

        viewPager.addOnPageChangeListener(this);
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        onPageSelected(0);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mNumberText.setText("第"+position+"页");
        mNumberText.postInvalidate();

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

       public MyPagerAdapter(FragmentManager fm) {
           super(fm);
       }

       @Override
       public Fragment getItem(int position) {
           return data.get(position);
       }

       @Override
       public int getCount() {
           return data.size();
       }
   }
}
