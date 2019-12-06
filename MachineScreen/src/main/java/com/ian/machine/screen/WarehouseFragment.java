/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  WarehouseFragment
 * Created by  ianchang on 2018-08-16 14:43:24
 * Last modify date   2018-08-16 14:43:24
 */

package com.ian.machine.screen;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ian.machine.screen.base.BaseFragment;
import com.ian.machine.screen.bean.StuffInfo;
import com.ian.machine.screen.bean.StuffResultInfo;
import com.ian.machine.screen.cache.UserCenter;

import java.util.ArrayList;
import java.util.List;

import static com.ian.machine.screen.StuffAdapter.WAREHOUSE_CODE;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link WarehouseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WarehouseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WarehouseFragment extends BaseFragment implements View.OnClickListener, IOnItemClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button checkUpBtn;

    private RecyclerView mRecyclerView;
    private StuffAdapter mAdapter;

    public WarehouseFragment() {
        // Required empty public constructor
    }

    public static WarehouseFragment newInstance(String param1, String param2) {
        WarehouseFragment fragment = new WarehouseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_warehouse, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Find the +1 button
        checkUpBtn = (Button) view.findViewById(R.id.btn_check_up);
        checkUpBtn.setOnClickListener(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        DividerItemDecoration decoration = new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(decoration);

        mAdapter = new StuffAdapter(getData(), WAREHOUSE_CODE);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_check_up:
                Toast.makeText(getContext(), "异常处理成功", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void updateView(Object obj) {
        super.updateView(obj);

        if (mAdapter != null && obj != null) {
            StuffResultInfo info = (StuffResultInfo) obj;
            mAdapter.updateDate(info.shelf);
        }
    }


    private List<StuffInfo> getData() {
        ArrayList<StuffInfo> data = new ArrayList<>();

        StuffResultInfo resultInfo = UserCenter.shareInstance().getStuffResultInfo();
        if (resultInfo == null) return data;

        data.addAll(resultInfo.shelf);

        return data;
    }

    @Override
    public void onItemClick(View itemView, int position, Object object) {
        StuffInfo info = (StuffInfo) object;
        Toast.makeText(getContext(), String.format("%d#商品名称:%s 价格:%d", position, info.name, info.count), Toast.LENGTH_SHORT).show();
    }

}
