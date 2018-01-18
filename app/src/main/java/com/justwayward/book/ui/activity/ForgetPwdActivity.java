package com.justwayward.book.ui.activity;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.justwayward.book.R;
import com.justwayward.book.ReaderApplication;
import com.justwayward.book.api.CoomonApi;
import com.justwayward.book.base.BaseActivity;
import com.justwayward.book.bean.CommonBean;
import com.justwayward.book.component.AppComponent;
import com.justwayward.book.retrofit.BaseObjObserver;
import com.justwayward.book.retrofit.HttpResult;
import com.justwayward.book.retrofit.RetrofitClient;
import com.justwayward.book.retrofit.RxUtils;
import com.justwayward.book.utils.CountDownUtils;
import com.justwayward.book.utils.ToastUtils;

import butterknife.Bind;
import butterknife.OnClick;

public class ForgetPwdActivity extends BaseActivity implements CountDownUtils.CountdownListener {

    @Bind(R.id.common_toolbar)
    Toolbar commonToolbar;
    @Bind(R.id.edt_forget_user)
    EditText edtForgetUser;
    @Bind(R.id.edt_forget_code)
    EditText edtForgetCode;
    @Bind(R.id.tv_forget_code)
    TextView tvForgetCode;
    @Bind(R.id.edt_forget_pwd)
    EditText edtForgetPwd;
    @Bind(R.id.edt_forget_re_pwd)
    EditText edtForgetRePwd;
    @Bind(R.id.btn_forget)
    Button btnForget;

    private String mobile;
    private CountDownUtils countDown;

    @Override
    public int getLayoutId() {
        return R.layout.activity_forget_pwd;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void initToolBar() {
        mCommonToolbar.setNavigationIcon(R.drawable.ab_back);
        mCommonToolbar.setTitle("找回密码");
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews() {
        countDown = new CountDownUtils(tvForgetCode, "%s秒", 60);
        countDown.setCountdownListener(this);
    }

    @OnClick({R.id.tv_forget_code, R.id.btn_forget})
    public void onViewClicked(View view) {
        mobile = edtForgetUser.getText().toString();
        switch (view.getId()) {
            case R.id.tv_forget_code:
                if (TextUtils.isEmpty(mobile)) {
                    ToastUtils.showToast(ReaderApplication.getsInstance().getResources().getString(R.string.please_input_phone));
                    return;
                }
                tvForgetCode.setEnabled(false);
                countDown.start();
                CoomonApi.sendsms(this, "forgotten", mobile);
                break;
            case R.id.btn_forget:
                forgetpwd();
                break;
        }
    }


    /**
     * 找回密码
     */
    private void forgetpwd() {
        String pwd = edtForgetPwd.getText().toString();
        String rePwd = edtForgetRePwd.getText().toString();
        String code = edtForgetCode.getText().toString();

        if (TextUtils.isEmpty(mobile)) {
            showToastMsg(getResources().getString(R.string.please_input_phone));
            return;
        }

        if (TextUtils.isEmpty(pwd)) {
            showToastMsg(getResources().getString(R.string.please_input_pwd));
            return;
        }

        if (TextUtils.isEmpty(rePwd)) {
            showToastMsg(getResources().getString(R.string.please_input_re_pwd));
            return;
        }

        if (TextUtils.isEmpty(code)) {
            showToastMsg(getResources().getString(R.string.please_input_code));
            return;
        }

        if (!pwd.equals(rePwd)) {
            showToastMsg(getResources().getString(R.string.pwn_no_repwd));
            return;
        }

        RetrofitClient.getInstance().createApi().forgetpwd(mobile, pwd, code)
                .compose(RxUtils.<HttpResult<CommonBean>>io_main())
                .subscribe(new BaseObjObserver<CommonBean>(this, "重置中") {
                    @Override
                    protected void onHandleSuccess(CommonBean commonBean) {
                        showToastMsg("密码重置成功");
                        finish();
                    }
                });
    }

    @Override
    public void onStartCount() {

    }

    @Override
    public void onFinishCount() {
        if (tvForgetCode == null) {
            return;
        }
        tvForgetCode.setEnabled(true);
        tvForgetCode.setText("重新获取");
    }

    @Override
    public void onUpdateCount(int currentRemainingSeconds) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDown.isRunning()) {
            countDown.stop();
        }
    }
}
