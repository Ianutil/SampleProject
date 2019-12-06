/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  CartFragment
 * Created by  ianchang on 2018-08-22 10:05:38
 * Last modify date   2018-08-22 10:05:38
 */

package com.ian.machine.cart;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.ian.machine.cart.bean.StuffResultInfo;
import com.ian.machine.cart.cache.UserCenter;
import com.ian.machine.cart.service.ServiceMedia;
import com.serenegiant.common.BaseFragment;
import com.zhy.autolayout.utils.AutoUtils;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.ian.machine.cart.StuffAdapter.CART_CODE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment implements IOnItemClickListener, CameraFragment.ICameraTextureListener{

    private RecyclerView mRecyclerView;
    private StuffAdapter mAdapter;

    private StuffResultInfo info;
    private Handler mHandler = new Handler();

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("TAG", "onViewCreated");

        AutoUtils.autoSize(view);

        FragmentManager fm = getFragmentManager();
        CameraFragment cameraFragment = (CameraFragment)fm.findFragmentByTag("CameraFragment");
        if (cameraFragment != null){
            cameraFragment.setICameraTextureListener(this);
        }

        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        DividerItemDecoration decoration = new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(decoration);

//        getData("1234");

        info = UserCenter.shareInstance().getStuffResultInfo();
        mAdapter = new StuffAdapter(null, CART_CODE);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

        if (info != null){
            mAdapter.updateDate(info.cart);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.e("TAG", "onResume");
        mHandler.postDelayed(requestData, 600);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("TAG", "onPause");

        mHandler.removeCallbacks(requestData);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private void getData(String devices){

        Log.e("TAG", "getData");
        ServiceMedia.shareInstance()
                .getStuffData(devices)
                .map(new Function<String, StuffResultInfo>() {

                    @Override
                    public StuffResultInfo apply(@NonNull String s) throws Exception {

                        Log.e("TAG", "Response:"+s + "  --->"+Thread.currentThread().getName());

                        StuffResultInfo info = new Gson().fromJson(s, StuffResultInfo.class);
                        return info;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<StuffResultInfo>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull StuffResultInfo data) {
                Log.e("TAG", "Response:"+data + "  --->"+Thread.currentThread().getName());

                UserCenter.shareInstance().saveStuff(data);
                info = data;

                if (mAdapter != null){
                    mAdapter.updateDate(info.cart);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

                mHandler.postDelayed(requestData, 1000 * 30);
            }
        });
    }

    @Override
    public void onItemClick(View itemView, int position, Object data) {

    }

    @Override
    public void onTextureSize(int width, int height) {
        FragmentManager fm = getChildFragmentManager();
        CameraFragment cameraFragment = (CameraFragment)fm.findFragmentByTag("CameraFragment");

        ViewGroup.LayoutParams params = cameraFragment.getView().getLayoutParams();
        if (params == null){
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }

        params.height = height;
        params.width = width;
//        params.height = getResources().getDisplayMetrics().heightPixels/2;
//        view.requestLayout();

    }

    private Runnable requestData = new Runnable() {
        @Override
        public void run() {
            getData("1234");
        }
    };
}
