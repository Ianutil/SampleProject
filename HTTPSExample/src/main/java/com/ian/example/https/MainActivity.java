package com.ian.example.https;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.ian.example.https.service.ServiceMedia;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void testService(View view){

        String appKey = "7de7a59708ca45ec9f0113d25380f683";
        String appSecret = "95b55edc-3a36-4d05-846a-c4ad0ca18e1e";
        ServiceMedia.shareInstance().
                getToken(appKey, appSecret).
                map(new Function<String, String>() {
                    @Override
                    public String apply(@NonNull String s) throws Exception {
                        Log.e("TAG", "---------->请求结果："+s);

                        return s;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull String s) {


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
}
