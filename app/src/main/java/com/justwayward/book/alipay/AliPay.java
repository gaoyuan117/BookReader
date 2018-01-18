package com.justwayward.book.alipay;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.justwayward.book.Constants;

import java.util.Map;

/**
 * 直接new出对象使用即可，不需要单例，单例系统不回收。
 */
public class AliPay {

    private PayTask mPayTask;

    public interface AlipayCallBack {
        void onSuccess();

        void onDeeling();

        void onCancel();

        void onFailure(String msg);
    }

    /**
     * 创建支付对象
     *
     * @param activity activity
     */
    public AliPay(Activity activity) {
        this.mPayTask = new PayTask(activity);
    }

    /**
     * 调起支付
     *
     * @param total_amount   订单总价
     * @param content        标题
     * @param order_trade_no 订单no
     * @param callBack       支付宝支付结果回调
     */
    public void payV2(String total_amount, final String content, final String order_trade_no, final AlipayCallBack callBack) {
        final Handler handler = new Handler();

        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(Constants.AliPay.APPID, total_amount, content, order_trade_no, isRsa2());
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
        String sign = OrderInfoUtil2_0.getSign(params, Constants.AliPay.RSA_PRIVATE, isRsa2());

        final String orderInfo = orderParam + "&" + sign;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (mPayTask == null) {
                    return;
                }
                Map<String, String> payResult = mPayTask.payV2(orderInfo, true);
                final AliPayResult aliPayResult = new AliPayResult(payResult);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callBack == null) {
                            return;
                        }
                        String resultStatus = aliPayResult.getResultStatus();
                        Log.e("payResult", aliPayResult.toString());
                        if ("9000".equals(resultStatus)) {
                            callBack.onSuccess();
                        } else if ("8000".equals(resultStatus)) {
                            callBack.onDeeling();
                        } else if ("4000".equals(resultStatus)) {
                            callBack.onFailure("订单支付失败");
                        } else if ("6001".equals(resultStatus)) {
                            callBack.onCancel();
                        } else if ("6002".equals(resultStatus)) {
                            callBack.onFailure("网络未连接");
                        } else if ("6004".equals(resultStatus)) {
                            callBack.onDeeling();
                        }
                    }
                });
            }
        };
        new Thread(runnable).start();
    }

    private boolean isRsa2() {
        return Constants.AliPay.RSA_PRIVATE.length() > 1024;
    }
}
