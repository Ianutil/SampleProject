package com.function.ianchang.simplemvp.base;

import android.content.Intent;

public class ContactPresenter {

    public interface PresenterView extends IBasePresenterView<Presenter> {

        void showProgress();
        void dismissProgress();

        void showToast(String msg);

        void log(String msg);
    }

   public interface Presenter extends IBasePresenter {

       void pushController(Class<?> view);
       void pushController(Class<?> view, Intent intent);
       void popController();
   }
}
