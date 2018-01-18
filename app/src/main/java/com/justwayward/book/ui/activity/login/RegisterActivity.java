package com.justwayward.book.ui.activity.login;

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

public class RegisterActivity extends BaseActivity implements CountDownUtils.CountdownListener {

    @Bind(R.id.edt_register_user)
    EditText edtRegisterUser;
    @Bind(R.id.edt_register_code)
    EditText edtRegisterCode;
    @Bind(R.id.tv_register_code)
    TextView tvRegisterCode;
    @Bind(R.id.edt_register_pwd)
    EditText edtRegisterPwd;
    @Bind(R.id.edt_reset_pwd)
    EditText edtResetPwd;
    @Bind(R.id.btn_register)
    Button btnRegister;

    private String mobile;
    private CountDownUtils countDown;

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void initToolBar() {
        mCommonToolbar.setTitle("注册");
        mCommonToolbar.setNavigationIcon(R.drawable.ab_back);
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews() {
        countDown = new CountDownUtils(tvRegisterCode, "%s秒", 60);
        countDown.setCountdownListener(this);
    }


    @OnClick({R.id.tv_register_code, R.id.btn_register})
    public void onViewClicked(View view) {
        mobile = edtRegisterUser.getText().toString();
        switch (view.getId()) {
            case R.id.tv_register_code:
                if (TextUtils.isEmpty(mobile)) {
                    ToastUtils.showToast(ReaderApplication.getsInstance().getResources().getString(R.string.please_input_phone));
                    return;
                }
                tvRegisterCode.setEnabled(false);
                countDown.start();
                CoomonApi.sendsms(this, "register", mobile);
                break;
            case R.id.btn_register:
                register();
                break;
        }
    }

    /**
     * 注册
     */
    private void register() {
        String pwd = edtRegisterPwd.getText().toString();
        String rePwd = edtResetPwd.getText().toString();
        String code = edtRegisterCode.getText().toString();

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

        RetrofitClient.getInstance().createApi().register(mobile, pwd, code)
                .compose(RxUtils.<HttpResult<CommonBean>>io_main())
                .subscribe(new BaseObjObserver<CommonBean>(this, "注册中") {
                    @Override
                    protected void onHandleSuccess(CommonBean commonBean) {
                        showToastMsg("注册成功");
                        finish();
                    }
                });
    }


    @Override
    public void onStartCount() {

    }

    @Override
    public void onFinishCount() {
        if(tvRegisterCode==null){
            return;
        }
        tvRegisterCode.setEnabled(true);
        tvRegisterCode.setText("重新获取");
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
