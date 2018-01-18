package com.justwayward.book.ui.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.justwayward.book.R;
import com.justwayward.book.base.BaseActivity;
import com.justwayward.book.component.AppComponent;

import butterknife.Bind;
import butterknife.OnClick;

public class InputMoneyActivity extends BaseActivity {

    @Bind(R.id.et_money)
    EditText etMoney;
    @Bind(R.id.tv_recharge)
    TextView tvRecharge;


    @Override
    public int getLayoutId() {
        return R.layout.activity_input_money;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void initToolBar() {
        mCommonToolbar.setTitle("充值");
        mCommonToolbar.setNavigationIcon(R.drawable.ab_back);
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews() {
        etMoney.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable edt) {
                String temp = edt.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2) {
                    edt.delete(posDot + 3, posDot + 4);
                }
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
        });

    }

    @OnClick(R.id.tv_recharge)
    public void onViewClicked() {
        String money = etMoney.getText().toString().trim();

        if (TextUtils.isEmpty(money)) {
            showToastMsg("请输入充值金额");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("money", money);
        setResult(RESULT_OK, intent);
        finish();
    }
}
