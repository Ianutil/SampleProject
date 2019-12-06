package com.function.ianchang.apppush;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.igexin.sdk.PushManager;

public class TestPushDataActivity extends Activity {
    private WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_push_data);

        mWebView = (WebView)findViewById(R.id.webview);
        mWebView = (WebView) findViewById(R.id.webview);

        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });

        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        //启用支持javascript
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);

        mWebView.loadUrl("http://www.baidu.com");
        if (savedInstanceState != null) {
            String url = savedInstanceState.getString("url");
            if (!TextUtils.isEmpty(url)) {
                mWebView.loadUrl(url);
            }

        }
    }

    @Override
    public void setIntent(Intent newIntent) {
        super.setIntent(newIntent);
        Log.d("TAG", "setIntent");

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Log.d("TAG", "onNewIntent");

    }
}
