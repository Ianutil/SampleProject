package com.function.ianchang.simplemvp.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class BasePresenter implements ContactPresenter.Presenter {
    private ContactPresenter.PresenterView mPresenterView;
    private Context mContext;

    public BasePresenter(ContactPresenter.PresenterView presenterView, Context context){
        mPresenterView = presenterView;
        mContext = context;
    }

    @Override
    public void onCreatePresenter() {

        mPresenterView.log("onCreatePresenter");
    }

    @Override
    public void onDestroyPresenter() {
        mPresenterView.log("onDestroyPresenter");

    }

    @Override
    public void pushController(Class<?> view) {
        Intent intent = new Intent(mContext, view);
        mContext.startActivity(intent);
    }

    @Override
    public void pushController(Class<?> view, Intent params) {
        Intent intent = new Intent(mContext, view);
        intent.putExtras(params);
        mContext.startActivity(intent);
    }

    @Override
    public void popController() {

        if (mContext instanceof Activity){
            ((Activity)mContext).finish();
        }
    }
}
