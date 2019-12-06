/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  TestGLActivity
 * Created by  ianchang on 2018-08-14 10:48:10
 * Last modify date   2018-08-14 10:48:10
 */

package com.function.ianchang.simplegreendao;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TestGLActivity extends AppCompatActivity {

    private TestGLView glView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_gl);

        glView = (TestGLView)findViewById(R.id.gl_view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (glView != null){
            glView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        glView.onPause();

    }


}
