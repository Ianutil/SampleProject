/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  LandscapFragment
 * Created by  ianchang on 2018-08-17 16:04:00
 * Last modify date   2018-08-17 16:04:00
 */

package com.ian.machine.screen;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.ian.machine.screen.base.BaseFragment;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LandscapeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LandscapeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LandscapeFragment extends BaseFragment implements View.OnClickListener{
    private static final String IMAGE_URL = "IMAGE_URL";
    private String url;

    private PhotoView mPhotoView;

    public LandscapeFragment() {
    }

    public static LandscapeFragment newInstance(String url) {
        LandscapeFragment fragment = new LandscapeFragment();
        Bundle args = new Bundle();
        args.putString(IMAGE_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            url = getArguments().getString(IMAGE_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_landscape, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.come_back).setOnClickListener(this);

        mPhotoView = (PhotoView)view.findViewById(R.id.phote_view);

        if (!TextUtils.isEmpty(url)){
            Glide.with(getActivity()).load(url).into(mPhotoView);
        }
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
        getChildFragmentManager().popBackStack();
    }
}
