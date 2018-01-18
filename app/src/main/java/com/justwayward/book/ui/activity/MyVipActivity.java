package com.justwayward.book.ui.activity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.justwayward.book.AppConfig;
import com.justwayward.book.PayUtils;
import com.justwayward.book.R;
import com.justwayward.book.ReaderApplication;
import com.justwayward.book.base.BaseActivity;
import com.justwayward.book.bean.MonthPackageBean;
import com.justwayward.book.bean.UserBean;
import com.justwayward.book.component.AppComponent;
import com.justwayward.book.retrofit.BaseObjObserver;
import com.justwayward.book.retrofit.HttpResult;
import com.justwayward.book.retrofit.RetrofitClient;
import com.justwayward.book.retrofit.RxUtils;
import com.justwayward.book.ui.adapter.MyVipPayAdapter;
import com.justwayward.book.utils.LogUtils;
import com.justwayward.book.view.MyGridView;
import com.yuyh.easyadapter.glide.GlideCircleTransform;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class MyVipActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @Bind(R.id.img_avatar)
    ImageView imgAvatar;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.grid_view)
    MyGridView gridView;
    @Bind(R.id.cb_wx_pay)
    CheckBox cbWxPay;
    @Bind(R.id.cb_ali_pay)
    CheckBox cbAliPay;
    @Bind(R.id.tv_pay)
    TextView tvPay;

    private List<MonthPackageBean> mList = new ArrayList<>();
    private MyVipPayAdapter mAdapter;
    private String money;
    private String payType;
    private String rechargeId = "";//充值金额

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_vip;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        EventBus.getDefault().register(this);
        mAdapter = new MyVipPayAdapter(this, mList);
        gridView.setAdapter(mAdapter);

        gridView.setOnItemClickListener(this);
    }

    @Override
    public void initToolBar() {
        mCommonToolbar.setNavigationIcon(R.drawable.ab_back);
        mCommonToolbar.setTitle("我的包月会员");
    }

    @Override
    public void initDatas() {
        getMonthPackage();
    }

    @Override
    public void configViews() {

    }


    @OnClick({R.id.img_avatar, R.id.cb_wx_pay, R.id.cb_ali_pay, R.id.tv_pay, R.id.ll_ali, R.id.ll_wx})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_avatar:
                break;

            case R.id.ll_wx:
                cbAliPay.setChecked(false);
                cbWxPay.setChecked(true);
                payType = AppConfig.WX;
                break;
            case R.id.ll_ali:
                cbWxPay.setChecked(false);
                cbAliPay.setChecked(true);
                payType = AppConfig.ALI;
                break;
            case R.id.cb_wx_pay:
                cbAliPay.setChecked(false);
                cbWxPay.setChecked(true);
                payType = AppConfig.WX;
                break;
            case R.id.cb_ali_pay:
                cbWxPay.setChecked(false);
                cbAliPay.setChecked(true);
                payType = AppConfig.ALI;
                break;
            case R.id.tv_pay:
                if (TextUtils.isEmpty(money)) {
                    showToastMsg("请选择金额");
                    return;
                }
                if (TextUtils.isEmpty(payType)) {
                    showToastMsg("请选择支付方式");
                    return;
                }

                PayUtils.pay(this, "member", money, rechargeId, payType);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mAdapter.setAllFalse();
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.cb_wx_pay);
        checkBox.setChecked(true);
        money = mList.get(i).getMoney();
        rechargeId = mList.get(i).getId() + "";
        Log.e("gy", "会员金额：" + rechargeId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUser();
    }

    /**
     * 获取包月会员
     */
    private void getMonthPackage() {
        RetrofitClient.getInstance().createApi().getMonthPackage(ReaderApplication.token)
                .compose(RxUtils.<HttpResult<List<MonthPackageBean>>>io_main())
                .subscribe(new BaseObjObserver<List<MonthPackageBean>>(this, "获取中") {
                    @Override
                    protected void onHandleSuccess(List<MonthPackageBean> list) {

                        if (list.isEmpty()) {
                            return;
                        }
                        mList.clear();
                        mList.addAll(list);
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

    /**
     * 获取用户会员信息
     */
    private void getUser() {
        RetrofitClient.getInstance().createApi().getUser(ReaderApplication.token)
                .compose(RxUtils.<HttpResult<UserBean>>io_main())
                .subscribe(new BaseObjObserver<UserBean>(getActivity(), false) {
                    @Override
                    protected void onHandleSuccess(UserBean userBean) {

                        ReaderApplication.user = userBean;
                        long expire_time = userBean.getExpire_time();//到期时间
                        Date currentTime = new Date();
                        long diff = expire_time * 1000 - currentTime.getTime();
                        long days = diff / (1000 * 60 * 60 * 24);
                        LogUtils.e("剩余时间：" + days);

                        if (days <= 0) {
                            tvTime.setText("暂未开通会员");
                        } else {
                            tvTime.setText("包月会员用户(剩余" + days + "天)");
                        }

                        tvName.setText(userBean.getUser_nickname());
                        Glide.with(mContext).load(userBean.getAvatar())
                                .error(R.drawable.avatar_default)
                                .transform(new GlideCircleTransform(mContext)).into(imgAvatar);
                    }
                });
    }

    @Subscribe
    public void payCallBack(String type) {
        if (type.equals(AppConfig.ALI)) {//支付宝支付成功
            showToastMsg("支付宝支付成功");

        } else if (type.equals(AppConfig.WX)) {//微信支付成功

            showToastMsg("微信支付成功");
        }

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
