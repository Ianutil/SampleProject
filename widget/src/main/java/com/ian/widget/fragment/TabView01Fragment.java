/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  TabView01Fragment
 * Created by  ianchang on 2018-06-05 11:02:21
 * Last modify date   2018-06-05 10:57:08
 */

package com.ian.widget.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ian.widget.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ianchang on 2018/6/4.
 */

public class TabView01Fragment extends Fragment {

    private List<String> titleData;
    private List<Fragment> contentData;
    private MyAdapter adapter;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);

        return LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_tabview, null, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        titleData = new ArrayList<>();
        contentData = new ArrayList<>();

        titleData.add("内嵌TAB1");
        titleData.add("内嵌TAB2");
        titleData.add("内嵌TAB3");
        titleData.add("内嵌TAB4");


        contentData.add(new TabViewFragment());
        contentData.add(new TabViewFragment());
        contentData.add(new TabViewFragment());
        contentData.add(new TabViewFragment());


        mTabLayout = (TabLayout) view.findViewById(R.id.tab_layout);

        for (String str : titleData) {
            TabLayout.Tab tab = mTabLayout.newTab().setText(str);
            mTabLayout.addTab(tab, false);
        }




        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);

        adapter = new MyAdapter(getChildFragmentManager());
        mViewPager.setAdapter(adapter);

        mTabLayout.setupWithViewPager(mViewPager);
    }

    private class MyAdapter extends FragmentStatePagerAdapter {


        public MyAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleData.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return contentData.get(position);
        }

        @Override
        public int getCount() {
            return contentData.size();
        }

    }
}
