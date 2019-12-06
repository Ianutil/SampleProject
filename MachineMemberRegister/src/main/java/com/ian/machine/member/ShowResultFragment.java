package com.ian.machine.member;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.ian.machine.member.R;
import com.ian.machine.member.bean.ResultInfo;
import com.ian.machine.member.service.ServiceMedia;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ianchang on 2018/8/31.
 */

public class ShowResultFragment extends Fragment implements View.OnClickListener{

    private String mCode;

    private Button mRegisterBtn;

    private View mLayoutPerson;

    private TextView mResultTextView;

    private Handler mHandler = new Handler(Looper.getMainLooper());
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.fragment_show_result, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRegisterBtn = (Button)view.findViewById(R.id.btn_register);
        mRegisterBtn.setOnClickListener(this);
        mRegisterBtn.setVisibility(View.GONE);

        mLayoutPerson = view.findViewById(R.id.layout_person);
        mLayoutPerson.setVisibility(View.GONE);


        mResultTextView = (TextView)view.findViewById(R.id.text_status);
        mResultTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_register:
                registerMember();
                break;
        }
    }

    public void searchMember(String data) {

        mCode = data;
        ServiceMedia.shareInstance().
                searchMember(data).
                map(new Function<String, ResultInfo>() {
                    @Override
                    public ResultInfo apply(@NonNull String s) throws Exception {

                        Log.e("TAG", "---------->请求结果：" + s);

                        ResultInfo info = new Gson().fromJson(s, ResultInfo.class);

                        return info;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultInfo>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ResultInfo s) {

                        mResultTextView.setVisibility(View.VISIBLE);

                        mResultTextView.setText(s.msg);
                        mRegisterBtn.setVisibility(View.VISIBLE);

                        showPersonInfo(s);

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void registerMember() {

        ServiceMedia.shareInstance().
                registerMember(mCode).
                map(new Function<String, ResultInfo>() {
                    @Override
                    public ResultInfo apply(@NonNull String s) throws Exception {
                        Log.e("TAG", "---------->请求结果：" + s);
                        ResultInfo info = new Gson().fromJson(s, ResultInfo.class);

                        return info;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultInfo>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ResultInfo s) {
                        mResultTextView.setText(s.msg);
                        mRegisterBtn.setVisibility(View.VISIBLE);

                        showPersonInfo(s);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();

                        mRegisterBtn.setVisibility(View.VISIBLE);
                        mResultTextView.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void hideView(){

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mRegisterBtn.setVisibility(View.GONE);
                mLayoutPerson.setVisibility(View.GONE);
                mResultTextView.setVisibility(View.GONE);
            }
        });

    }

    private void showPersonInfo(ResultInfo s) {

        if (TextUtils.isEmpty(s.user_id)){
            mLayoutPerson.setVisibility(View.GONE);
            return;
        }

        mRegisterBtn.setVisibility(View.GONE);
        mLayoutPerson.setVisibility(View.VISIBLE);

        ImageView imageView = (ImageView) mLayoutPerson.findViewById(R.id.image_head);
        imageView.setImageURI(null);

        if (!TextUtils.isEmpty(s.avator)) {
            Glide.with(mLayoutPerson.getContext()).load(s.avator).into(imageView);
        }


        if (!TextUtils.isEmpty(s.user_id)) {
            TextView textView = (TextView) mLayoutPerson.findViewById(R.id.text_name);
            textView.setText(s.user_id);
        }

        if (!TextUtils.isEmpty(s.msg)) {
            TextView textView = (TextView) mLayoutPerson.findViewById(R.id.text_msg);
            textView.setText(s.msg);
        }

    }
}
