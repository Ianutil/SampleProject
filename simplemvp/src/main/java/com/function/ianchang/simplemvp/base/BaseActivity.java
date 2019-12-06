package com.function.ianchang.simplemvp.base;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.function.ianchang.simplemvp.R;

public abstract class BaseActivity extends AppCompatActivity implements ContactPresenter.PresenterView {

    private static String TAG;

    protected ContactPresenter.Presenter mPresenter;
    protected AlertDialog progressDialog;

    protected FrameLayout contentLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TAG = this.getClass().getSimpleName();

        setContentView(R.layout.activity_main);

        contentLayout = (FrameLayout) findViewById(R.id.layout_content);

        // 加载内容
        contentLayout.addView(initContentView());

        // 绑定关系
        setPresenter(initPresenter());

        mPresenter.onCreatePresenter();

    }

    public abstract BasePresenter initPresenter();

    public abstract View initContentView();

    @Override
    public void setPresenter(ContactPresenter.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroyPresenter();
        super.onDestroy();
    }


    @Override
    public void showProgress() {
        if (progressDialog == null){
            progressDialog = new AlertDialog.Builder(this, android.R.style.Holo_ButtonBar_AlertDialog)
                    .setTitle("提示")
                    .setMessage("正在努力加载中...")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
        }

        progressDialog.show();
    }

    @Override
    public void dismissProgress() {
        if (progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    public void showToast(String msg) {

        if (!TextUtils.isEmpty(msg)){
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void log(String msg) {

        Log.d(TAG, msg);
    }
}
