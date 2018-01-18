package com.justwayward.book;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.justwayward.book.alipay.AliPay;
import com.justwayward.book.bean.PayBean;
import com.justwayward.book.retrofit.BaseObjObserver;
import com.justwayward.book.retrofit.HttpResult;
import com.justwayward.book.retrofit.RetrofitClient;
import com.justwayward.book.retrofit.RxUtils;
import com.justwayward.book.utils.ToastUtils;
import com.justwayward.book.wxpay.WxPay;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by gaoyuan on 2017/11/24.
 */

public class PayUtils {
    private static String des;

    /**
     * 支付
     *
     * @param item  支付项目 取值：recharge 充值 member 购买会员
     * @param money 支付金额
     * @param id    套餐id
     * @param type  支付方式 alipay 支付宝 wxpay 微信支付
     */
    public static void pay(final Activity activity, final String item, final String money, String id, final String type) {
        if (item.equals("recharge")) {
            des = "充值";
        } else if (item.equals("member")) {
            des = "会员";
        }
        RetrofitClient.getInstance().createApi().pay(ReaderApplication.token, item, money, id, type)
                .compose(RxUtils.<HttpResult<PayBean>>io_main())
                .subscribe(new BaseObjObserver<PayBean>(activity, "创建订单中") {
                    @Override
                    protected void onHandleSuccess(PayBean payBean) {
                        if (TextUtils.isEmpty(payBean.getPay_sn())) {
                            ToastUtils.showToast("创建订单失败");
                            return;
                        }

                        if (type.equals(AppConfig.ALI)) {
                            aliPay(activity, money, payBean.getPay_sn());
                        } else if (type.equals(AppConfig.WX)) {
                            wxPay(activity, money, payBean.getPay_sn());
                        }

                    }
                });
    }

    private static void wxPay(Activity activity, String money, String orderNo) {
        WxPay.getWxPay().pay(activity, orderNo, des, money, Constants.WxPay.NOTIFY_URL, new WxPay.WxCallBack() {
            @Override
            public void payResponse(int code) {
                EventBus.getDefault().postSticky(AppConfig.WX);
            }
        });
    }

    /**
     * 支付宝支付
     *
     * @param activity
     * @param money
     * @param orderNo
     */
    private static void aliPay(Activity activity, String money, String orderNo) {

        new AliPay(activity).payV2(money, des, orderNo, new AliPay.AlipayCallBack() {
            @Override
            public void onSuccess() {
                EventBus.getDefault().postSticky(AppConfig.ALI);
            }

            @Override
            public void onDeeling() {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }


}
