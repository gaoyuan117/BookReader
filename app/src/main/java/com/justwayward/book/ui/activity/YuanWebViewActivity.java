package com.justwayward.book.ui.activity;

import android.os.Bundle;
import android.webkit.WebView;

import com.justwayward.book.R;
import com.justwayward.book.base.BaseActivity;
import com.justwayward.book.component.AppComponent;

import butterknife.Bind;
import butterknife.ButterKnife;

public class YuanWebViewActivity extends BaseActivity {


    @Bind(R.id.webview)
    WebView webview;

    @Override
    public int getLayoutId() {
        return R.layout.activity_web_view2;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void initToolBar() {
        mCommonToolbar.setNavigationIcon(R.drawable.ab_back);
        mCommonToolbar.setTitle("");
    }

    @Override
    public void initDatas() {
        String url = getIntent().getStringExtra("url");
        String title = getIntent().getStringExtra("title");
        mCommonToolbar.setTitle(title);
        url = url.replace("https", "http");
        webview.loadUrl(url);
    }

    @Override
    public void configViews() {

    }

}
