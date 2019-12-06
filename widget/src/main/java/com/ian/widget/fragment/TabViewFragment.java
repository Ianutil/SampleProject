/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  TabViewFragment
 * Created by  ianchang on 2018-06-05 09:35:49
 * Last modify date   2018-06-04 18:01:05
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

public class TabViewFragment extends Fragment {

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

        titleData.add("上下拉刷新");
        titleData.add("右滑菜单");
        titleData.add("左滑菜单");
        titleData.add("左右滑动菜单");
        titleData.add("更多滑动菜单");


        contentData.add(new RefreshViewFragment());
        contentData.add(new MenuViewFragment());
        contentData.add(new MenuLeftViewFragment());
        contentData.add(new MenuBothViewFragment());
        contentData.add(new MenuMoreViewFragment());


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
