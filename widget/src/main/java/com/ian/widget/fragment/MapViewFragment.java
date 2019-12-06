/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  MapViewFragment
 * Created by  ianchang on 2018-06-07 15:24:33
 * Last modify date   2018-06-05 10:36:54
 */

package com.ian.widget.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ian.widget.MenuItemLayout;
import com.ian.widget.MenuRecyclerView;
import com.ian.widget.R;
import com.ian.widget.view.MapView;
import com.ian.widget.view.RefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ianchang on 2018/6/4.
 */

public class MapViewFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);

        MapView mapView = new MapView(container.getContext());
        return mapView;
    }
}
