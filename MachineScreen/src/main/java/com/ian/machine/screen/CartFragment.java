/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  CartFragment
 * Created by  ianchang on 2018-08-16 14:40:12
 * Last modify date   2018-08-16 14:40:12
 */

package com.ian.machine.screen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ian.machine.screen.base.BaseFragment;
import com.ian.machine.screen.bean.StuffInfo;
import com.ian.machine.screen.bean.StuffResultInfo;
import com.ian.machine.screen.cache.UserCenter;

import java.util.ArrayList;
import java.util.List;

import static com.ian.machine.screen.StuffAdapter.CART_CODE;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link CartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends BaseFragment implements View.OnClickListener, IOnItemClickListener{

    private Button payOutBtn;
    private String name;
    private String url;

    private RecyclerView mRecyclerView;
    private StuffAdapter mAdapter;

    private StuffResultInfo info;

    private ImageView mImageView;

    public CartFragment() {
        // Required empty public constructor
    }

    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putString("name", param1);
        args.putString("url", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString("name");
            url = getArguments().getString("url");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Find the +1 button
        payOutBtn = (Button) view.findViewById(R.id.btn_pay_out);
        payOutBtn.setOnClickListener(this);

        mImageView = (ImageView)view.findViewById(R.id.image);

        info = UserCenter.shareInstance().getStuffResultInfo();
        if (info != null){
            mImageView.setOnClickListener(this);
            Glide.with(getActivity()).load(info.state).into(mImageView);
        }

        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        DividerItemDecoration decoration = new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(decoration);

        mAdapter = new StuffAdapter(getData(), CART_CODE);
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
    public void updateView(Object obj) {
        super.updateView(obj);

        if (mAdapter != null && obj != null) {
            info = (StuffResultInfo) obj;
            mAdapter.updateDate(info.cart);

            mImageView.setImageBitmap(null);
            Glide.with(getActivity()).load(info.state).into(mImageView);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_pay_out:
                Toast.makeText(getContext(), "click me", Toast.LENGTH_SHORT).show();
                break;
            case R.id.image:
                Intent intent = new Intent(getContext(), LandscapeActivity.class);
                intent.putExtra(LandscapeActivity.IMAGE_URL, info.state);
                startActivity(intent);
                break;
        }
    }

    private List<StuffInfo> getData(){
        ArrayList<StuffInfo> data = new ArrayList<>();

        StuffResultInfo resultInfo = UserCenter.shareInstance().getStuffResultInfo();
        if (resultInfo == null) return data;

        data.addAll(resultInfo.cart);

        return data;
    }

    @Override
    public void onItemClick(View itemView, int position, Object object) {
        StuffInfo info = (StuffInfo)object;
        Toast.makeText(getContext(), String.format("点击%d项# 商品名称:%s 价格:%.2f", position, info.name, info.price), Toast.LENGTH_SHORT).show();
    }
}
