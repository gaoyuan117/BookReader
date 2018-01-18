package com.justwayward.book.ui.activity.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.justwayward.book.R;
import com.justwayward.book.ReaderApplication;
import com.justwayward.book.base.BaseActivity;
import com.justwayward.book.bean.LoginBean;
import com.justwayward.book.component.AppComponent;
import com.justwayward.book.manager.SettingManager;
import com.justwayward.book.retrofit.BaseObjObserver;
import com.justwayward.book.retrofit.HttpResult;
import com.justwayward.book.retrofit.RetrofitClient;
import com.justwayward.book.retrofit.RxUtils;
import com.justwayward.book.ui.activity.ForgetPwdActivity;
import com.justwayward.book.ui.activity.Main2Activity;
import com.justwayward.book.utils.RxUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

public class LoginActivity extends BaseActivity implements PlatformActionListener {

    @Bind(R.id.img_user)
    ImageView imgUser;
    @Bind(R.id.edt_login_user)
    EditText etLoginUser;
    @Bind(R.id.edt_reset_pwd)
    EditText etPwd;
    @Bind(R.id.btn_login)
    Button btnLogin;
    @Bind(R.id.login_register)
    TextView tvloginRegister;
    @Bind(R.id.login_find_pwd)
    TextView tvloginFindPwd;
    @Bind(R.id.tv_login_agreement)
    TextView tvLoginAgreement;
    private String mobile;

    private String loginType;
    private PlatformDb db;
    private String type;
    private ProgressDialog dialog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void initToolBar() {
        mCommonToolbar.setTitle("登录");
        mCommonToolbar.setNavigationIcon(R.drawable.ab_back);
        underlineSpan();
    }

    @Override
    public void initDatas() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("登录中");
        type = getIntent().getStringExtra("type");
    }

    @Override
    public void configViews() {

    }

    @OnClick({R.id.btn_login, R.id.login_register, R.id.login_find_pwd, R.id.tv_login_agreement, R.id.img_login_qq, R.id.img_login_wx})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login://登录
                login();
                break;

            case R.id.login_register://注册
                Intent registerIntent = new Intent(this, RegisterActivity.class);
                startActivity(registerIntent);
                break;

            case R.id.login_find_pwd://忘记密码
                Intent forgetIntent = new Intent(this, ForgetPwdActivity.class);
                startActivity(forgetIntent);
                break;

            case R.id.tv_login_agreement://用户协议
                break;

            case R.id.img_login_qq://qq登录
                loginType = "qq";
                dialog.show();
                otherLogin(QQ.NAME);
                break;

            case R.id.img_login_wx://微信登录
                dialog.show();
                loginType = "wx";
                otherLogin(Wechat.NAME);
                break;
        }
    }

    /**
     * 登录
     */
    private void login() {
        mobile = etLoginUser.getText().toString();
        String pwd = etPwd.getText().toString();

        if (TextUtils.isEmpty(mobile)) {
            showToastMsg(getResources().getString(R.string.please_input_phone));
            return;
        }

        if (TextUtils.isEmpty(pwd)) {
            showToastMsg(getResources().getString(R.string.please_input_pwd));
            return;
        }

        if (pwd.length()<4){
            showToastMsg("密码长度不能少于4位");
            return;
        }

        RetrofitClient.getInstance().createApi().login(mobile, pwd)
                .compose(RxUtils.<HttpResult<LoginBean>>io_main())
                .subscribe(new BaseObjObserver<LoginBean>(this, "登录中") {
                    @Override
                    protected void onHandleSuccess(LoginBean bean) {
                        loginSuccess(bean);
                    }
                });
    }

    /**
     * 登录成功设置数据
     *
     * @param bean
     */
    private void loginSuccess(LoginBean bean) {
        ReaderApplication.token = bean.getToken();
        //保存用户信息
        ReaderApplication.uid = bean.getUid() + "";
        SettingManager.getInstance().saveUserInfo(bean.getUid() + "", bean.getToken(), bean.getNickname());

        showToastMsg("登录成功");
        MobclickAgent.onProfileSignIn(bean.getUid() + "");

        if (TextUtils.isEmpty(type)) {
            startActivity(new Intent(LoginActivity.this, Main2Activity.class));
        }
        finish();
    }

    /**
     * 添加下划线
     */
    private void underlineSpan() {
        UnderlineSpan underlineSpan = new UnderlineSpan();
        SpannableString spannableString = new SpannableString(getString(R.string.agreement));
        spannableString.setSpan(underlineSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        tvLoginAgreement.setText(spannableString);
    }

    private void otherLogin(String name) {

        Platform other = ShareSDK.getPlatform(name);
        other.showUser(null);//执行登录，登录后在回调里面获取用户资料
        other.SSOSetting(false);  //设置false表示使用SSO授权方式
        other.setPlatformActionListener(this);
        other.removeAccount(true);
    }


    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        db = platform.getDb();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                quickLogin(db.getUserId(), db.getUserIcon(), db.getUserName());
            }
        });
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                showToastMsg("授权失败");
            }
        });
    }

    @Override
    public void onCancel(Platform platform, int i) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                showToastMsg("授权取消");
            }
        });
    }


    private void quickLogin(String openId, String avatar, String name) {
        RetrofitClient.getInstance().createApi().quickLogin(loginType, openId, avatar, name)
                .compose(RxUtils.<HttpResult<LoginBean>>io_main())
                .subscribe(new BaseObjObserver<LoginBean>(this) {
                    @Override
                    protected void onHandleSuccess(LoginBean loginBean) {
                        loginSuccess(loginBean);
                    }
                });
    }
}
