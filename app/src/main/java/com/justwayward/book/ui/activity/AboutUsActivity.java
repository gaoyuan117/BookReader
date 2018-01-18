package com.justwayward.book.ui.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.justwayward.book.R;
import com.justwayward.book.ReaderApplication;
import com.justwayward.book.base.BaseActivity;
import com.justwayward.book.bean.AboutBean;
import com.justwayward.book.component.AppComponent;
import com.justwayward.book.retrofit.BaseObjObserver;
import com.justwayward.book.retrofit.HttpResult;
import com.justwayward.book.retrofit.RetrofitClient;
import com.justwayward.book.retrofit.RxUtils;
import com.justwayward.book.utils.RxUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AboutUsActivity extends BaseActivity {

    @Bind(R.id.tv_vertion)
    TextView tvVertion;
    @Bind(R.id.tv_content)
    TextView tvContent;

    @Override
    public int getLayoutId() {
        return R.layout.activity_about_us;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void initToolBar() {
        mCommonToolbar.setNavigationIcon(R.drawable.ab_back);
        mCommonToolbar.setTitle("关于我们");
    }

    @Override
    public void initDatas() {
        tvVertion.setText(getAppVersionName(this));
        getAbout();
    }

    @Override
    public void configViews() {

    }

    public String getAppVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            int versioncode = pi.versionCode;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }

        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    private void getAbout(){
        RetrofitClient.getInstance().createApi().getAbout(ReaderApplication.token)
                .compose(RxUtils.<HttpResult<AboutBean>>io_main())
                .subscribe(new BaseObjObserver<AboutBean>(this) {
                    @Override
                    protected void onHandleSuccess(AboutBean aboutBean) {
                        tvContent.setText(aboutBean.getAbout());
                    }
                });
    }
}
