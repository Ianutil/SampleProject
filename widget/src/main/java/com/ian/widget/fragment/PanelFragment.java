/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  PanelFragment
 * Created by  ianchang on 2018-06-26 13:16:02
 * Last modify date   2018-06-07 15:25:59
 */

package com.ian.widget.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ian.widget.view.MapView;
import com.ian.widget.view.PanelRoseView;

/**
 * Created by ianchang on 2018/6/4.
 */

public class PanelFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);

        PanelRoseView panelRoseView = new PanelRoseView(container.getContext());
        return panelRoseView;
    }
}
