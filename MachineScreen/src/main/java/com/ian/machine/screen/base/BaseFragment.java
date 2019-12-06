/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  BaseFragment
 * Created by  ianchang on 2018-08-17 15:31:50
 * Last modify date   2018-08-17 15:31:49
 */

package com.ian.machine.screen.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by ianchang on 2018/8/17.
 */

public class BaseFragment extends Fragment {


    public void updateView(Object obj){

    }

    /********
     * 显示fragment
     * @param fragment
     * @param resId
     */
    public void showFragme(BaseFragment fragment, int resId){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(resId, fragment, fragment.getTag());
        ft.commit();
    }
}
