package com.justwayward.book.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.justwayward.book.AppConfig;
import com.justwayward.book.PayUtils;
import com.justwayward.book.R;
import com.justwayward.book.ReaderApplication;
import com.justwayward.book.base.BaseActivity;
import com.justwayward.book.bean.CoinRateBean;
import com.justwayward.book.bean.RechargeListBean;
import com.justwayward.book.bean.RechargeMoneyBean;
import com.justwayward.book.component.AppComponent;
import com.justwayward.book.retrofit.BaseObjObserver;
import com.justwayward.book.retrofit.HttpResult;
import com.justwayward.book.retrofit.RetrofitClient;
import com.justwayward.book.retrofit.RxUtils;
import com.justwayward.book.ui.adapter.MyAssetsAdapter;
import com.justwayward.book.utils.LogUtils;
import com.justwayward.book.utils.RxUtil;
import com.justwayward.book.view.MyGridView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * 我的资产
 */
public class MyAssetsActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.tv_mingxi)
    TextView tvMingxi;
    @Bind(R.id.tv_jindou)
    TextView tvJindou;
    @Bind(R.id.grid_view)
    MyGridView gridView;
    @Bind(R.id.cb_wx_pay)
    CheckBox cbWxPay;
    @Bind(R.id.cb_ali_pay)
    CheckBox cbAliPay;
    @Bind(R.id.tv_recharge)
    TextView tvRecharge;


    private List<RechargeListBean> mList = new ArrayList<>();
    private MyAssetsAdapter mAdapter;
    public static int rate;//充值比例
    public static int give;//充值赠送比例
    private String money;//充值金额
    private String rechargeId = "";//充值金额
    private String type;//充值方式

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_assets;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        EventBus.getDefault().register(this);
        String coin = getIntent().getStringExtra("coin");
        if (!TextUtils.isEmpty(coin)) {
            tvJindou.setText(coin + "金豆");
        }
        mAdapter = new MyAssetsAdapter(this, mList);
        gridView.setAdapter(mAdapter);

        gridView.setOnItemClickListener(this);

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void initDatas() {
        getCoinRate();
        getRechargePackage();
    }

    @Override
    public void configViews() {

    }

    @OnClick({R.id.img_back, R.id.tv_mingxi, R.id.cb_wx_pay, R.id.cb_ali_pay, R.id.tv_recharge, R.id.ll_ali, R.id.ll_wx})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_mingxi://明细
                startActivity(new Intent(this, RechargeDetailActivity.class));
                break;
            case R.id.ll_wx:
                cbAliPay.setChecked(false);
                cbWxPay.setChecked(true);
                type = AppConfig.WX;
                break;
            case R.id.ll_ali:
                cbWxPay.setChecked(false);
                cbAliPay.setChecked(true);
                type = AppConfig.ALI;
                break;
            case R.id.cb_wx_pay:
                cbAliPay.setChecked(false);
                cbWxPay.setChecked(true);
                type = AppConfig.WX;
                break;
            case R.id.cb_ali_pay:
                cbWxPay.setChecked(false);
                cbAliPay.setChecked(true);
                type = AppConfig.ALI;
                break;
            case R.id.tv_recharge:
                if (TextUtils.isEmpty(money)&&TextUtils.isEmpty(mAdapter.inputMoney)) {
                    showToastMsg("请选择充值金额");
                    return;
                }
                if(!TextUtils.isEmpty(mAdapter.inputMoney)){
                    money = mAdapter.inputMoney;
                }
                if (TextUtils.isEmpty(type)) {
                    showToastMsg("请选择支付方式");
                    return;
                }

                LogUtils.e("充值金额："+money);

                PayUtils.pay(this, "recharge", money, rechargeId, type);

                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (mList.get(i).getAdd_time() == -1) {
            money = "";
            mAdapter.setAllFalse(i);
            mAdapter.setSelect(true);
            return;
        }
        mAdapter.setSelect(false);
        mAdapter.setAllFalse(i);
        money = mList.get(i).getMoney();
    }

    /**
     * 获取充值比例
     */
    private void getCoinRate() {
        RetrofitClient.getInstance().createApi().getCoinRate(ReaderApplication.token)
                .compose(RxUtils.<HttpResult<CoinRateBean>>io_main())
                .subscribe(new BaseObjObserver<CoinRateBean>(this) {
                    @Override
                    protected void onHandleSuccess(CoinRateBean coinRateBean) {
                        rate = Integer.valueOf(coinRateBean.getCoin_rate());
                        give = Integer.valueOf(coinRateBean.getCoin_giving());
                        Log.e("gy", "充值比例：" + rate);
                    }
                });
    }

    /**
     * 获取充值优惠
     */
    private void getRechargePackage() {
        RetrofitClient.getInstance().createApi().getRechargeMoney(ReaderApplication.token)
                .compose(RxUtils.<RechargeMoneyBean>io_main())
                .subscribe(new Consumer<RechargeMoneyBean>() {
                    @Override
                    public void accept(RechargeMoneyBean bean) throws Exception {
                        mList.clear();
                        if (bean.getCode() == 200) {
                            for (int i = 0; i < bean.getData().size(); i++) {
                                String money = bean.getData().get(i);
                                Integer d = Integer.valueOf(money);
                                RechargeListBean listBean = new RechargeListBean();
                                listBean.setAdd_time(10);
                                listBean.setMoney(money);
                                listBean.setCoin(d * rate);
                                listBean.setAddcoin(d * give);
                                mList.add(listBean);
                            }
                        } else {
                            showToastMsg(bean.getMessage());
                        }
                        RechargeListBean inputBean = new RechargeListBean();
                        inputBean.setAdd_time(-1);
                        mList.add(inputBean);
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Subscribe
    public void payCallBack(String type) {
        if (type.equals(AppConfig.ALI)) {//支付宝支付成功
            showToastMsg("支付宝支付成功");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}