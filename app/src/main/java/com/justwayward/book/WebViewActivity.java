package com.justwayward.book;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.justwayward.book.utils.LogUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WebViewActivity extends AppCompatActivity {

    @Bind(R.id.webview)
    WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);

        String url = getIntent().getStringExtra("url");
        LogUtils.e("url：" + url);

        if (TextUtils.isEmpty(url)) {
            finish();
            return;
        }

        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl(url);
//        finish();
    }
}
