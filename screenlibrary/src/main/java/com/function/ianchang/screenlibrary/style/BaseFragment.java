package com.function.ianchang.screenlibrary.style;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.function.ianchang.screenlibrary.bean.ScreenGroupInfo;
import com.function.ianchang.screenlibrary.bean.ScreenStyleInfo;

/**
 * Created by ianchang on 2017/10/30.
 *
 * 单屏布局结构
 *
 */
public class BaseFragment extends Fragment {

    protected String TAG = "TAG";

    public BaseFragment(){
        TAG = this.getClass().getSimpleName();
    }

    protected void setLayoutParams(ScreenGroupInfo info, View layout) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layout.getLayoutParams();

        if (info == null || info.weight == 0){
            params.weight = 0;
        }else {
            params.weight = info.weight;
        }

        layout.setLayoutParams(params);
    }

    public void updateContentView(ScreenGroupInfo info, int resId){
        if (info == null || info.weight == 0) return;

        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(resId, StyleScreenManager.createScreen(info));
        ft.commit();
    }


    /********
     * 设置屏幕方向
     * @param layout
     * @param orientation
     */
    public void setScreenOrientation(LinearLayout layout, int orientation){

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        // 横屏
        if (orientation == ScreenStyleInfo.LANDSCAPE){
            layout.setOrientation(LinearLayout.HORIZONTAL);
            width = 0;
        }else if (orientation == ScreenStyleInfo.PORTRAIT){
            layout.setOrientation(LinearLayout.VERTICAL);
            height = 0;
        }

        View child;
        LinearLayout.LayoutParams params;
        for (int i = 0; i < layout.getChildCount(); i++){
            child = layout.getChildAt(i);
            params = new LinearLayout.LayoutParams(width, height, 1);
            child.setLayoutParams(params);
        }

    }

    public void log(String msg){
        Log.i(TAG, msg);
    }
}
