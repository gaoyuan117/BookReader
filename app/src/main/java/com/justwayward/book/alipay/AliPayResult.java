package com.justwayward.book.alipay;

import android.text.TextUtils;

import java.util.Map;

/**
 * 封装支付宝支付结果返回对象
 * <p/>
 * resultStatus:返回状态码。
 * 9000	订单支付成功
 * 8000	正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
 * 4000	订单支付失败
 * 6001	用户中途取消
 * 6002	网络连接出错
 * 6004	支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
 * 其它	其它支付错误
 */
public class AliPayResult {
    /**
     * 状态代码
     */
    private String resultStatus;
    /**
     * 本次操作返回的结果数据
     */
    private String result;
    /**
     * 提示信息，支付宝保留参数，一般为空
     */
    private String memo;


    public AliPayResult(String rawResult) {

        if (TextUtils.isEmpty(rawResult)) {
            return;
        }
        String[] resultParams = rawResult.split(";");
        for (String resultParam : resultParams) {
            if (resultParam.startsWith("resultStatus")) {
                resultStatus = gatValue(resultParam, "resultStatus");
            }
            if (resultParam.startsWith("result")) {
                result = gatValue(resultParam, "result");
            }
            if (resultParam.startsWith("memo")) {
                memo = gatValue(resultParam, "memo");
            }
        }

    }

    public AliPayResult(Map<String, String> rawResult) {
        if (rawResult == null) {
            return;
        }
        for (String key : rawResult.keySet()) {
            if (TextUtils.equals(key, "resultStatus")) {
                resultStatus = rawResult.get(key);
            } else if (TextUtils.equals(key, "result")) {
                result = rawResult.get(key);
            } else if (TextUtils.equals(key, "memo")) {
                memo = rawResult.get(key);
            }
        }
    }

    @Override
    public String toString() {
        return "resultStatus={" + resultStatus + "};memo={" + memo
                + "};result={" + result + "}";
    }

    private String gatValue(String content, String key) {
        String prefix = key + "={";
        return content.substring(content.indexOf(prefix) + prefix.length(),
                content.lastIndexOf("}"));
    }

    /**
     * @return the resultStatus
     */
    public String getResultStatus() {
        return resultStatus;
    }

    /**
     * @return the memo
     */
    public String getMemo() {
        return memo;
    }

    /**
     * @return the result
     */
    public String getResult() {
        return result;
    }
}
