package com.function.ianchang.simplemvp;

import android.content.Context;

import com.function.ianchang.simplemvp.base.BasePresenter;
import com.function.ianchang.simplemvp.base.ContactPresenter;

public class WebviewPresenter extends BasePresenter {

    private ContactPresenter.PresenterView mPresenterView;
    public WebviewPresenter(ContactPresenter.PresenterView presenterView, Context context){
        super(presenterView, context);
        mPresenterView = presenterView;
    }

    @Override
    public void onCreatePresenter() {

        mPresenterView.log("onCreatePresenter");
    }

    @Override
    public void onDestroyPresenter() {
        mPresenterView.log("onDestroyPresenter");

    }


}
