package com.justwayward.book.alipay;



import com.justwayward.book.Constants;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;


/**
 * 支付PayV2构造支付参数
 * <p/>
 * 1.支付调用流程流程 。 构造mapParams -> orderParams -getSign ->OrderInfo
 */
public class OrderInfoUtil2_0 {


    /**
     * 构造授权参数列表
     *
     * @param pid       商户签约拿到的pid，如：2088102123816631
     * @param app_id    商户签约拿到的app_id，如：2013081700024223
     * @param target_id 商户唯一标识， 如：kkkkk091125
     * @return
     */
    public static Map<String, String> buildAuthInfoMap(String pid, String app_id, String target_id) {
        Map<String, String> keyValues = new HashMap<String, String>();

        keyValues.put("app_id", app_id);
        keyValues.put("pid", pid);
        // 服务接口名称， 固定值
        keyValues.put("apiname", "com.alipay.account.auth");
        // 商户类型标识， 固定值
        keyValues.put("app_name", "mc");
        // 业务类型， 固定值
        keyValues.put("biz_type", "openservice");
        // 产品码， 固定值
        keyValues.put("product_id", "APP_FAST_LOGIN");
        // 授权范围， 固定值
        keyValues.put("scope", "kuaijie");
        // 商户唯一标识，如：kkkkk091125
        keyValues.put("target_id", target_id);
        // 授权类型， 固定值
        keyValues.put("auth_type", "AUTHACCOUNT");
        // 签名类型
        keyValues.put("sign_type", "RSA");
        return keyValues;
    }

    /**
     * 构造支付订单Map参数 -> String参数
     *
     * @param app_id 商户签约拿到的app_id，如：2013081700024223
     * @return 构造支付的map参数 ->String params
     */
    public static Map<String, String> buildOrderParamMap(String app_id, String total_amount, String content, String out_trade_no, boolean rsa2) {
        Map<String, String> keyValues = new HashMap<>();
        //支付包分发非开发者的app_id，必须

        keyValues.put("app_id", app_id);
        //业务参数集合
        keyValues.put("biz_content", getBizContent(total_amount, content, out_trade_no));
        //请求使用的编码风格
        keyValues.put("charset", "utf-8");
        //接口名称
        keyValues.put("method", "alipay.trade.app.pay");
        //签名算法
        keyValues.put("sign_type", rsa2 ? "RSA2" : "RSA");

        //请求发送时间
        keyValues.put("timestamp", "2016-07-29 16:55:53");
        //调用接口版本，固定为1.0
        keyValues.put("version", "1.0");
        //支付宝异步回调地址
        keyValues.put("notify_url", Constants.AliPay.NOTIFY_URL);

        return keyValues;
    }

    /**
     * 构造业务参数
     *
     * @param total_amount 订单总价
     * @param content      订单标题
     * @param out_trade_no 订单编号
     * @return
     */
    private static String getBizContent(String total_amount, String content, String out_trade_no) {
        StringBuilder builder = new StringBuilder();

        builder.append("{\"timeout_express\":\"30m\",\"product_code\":\"QUICK_MSECURITY_PAY\",");

        builder.append("\"total_amount\":").append("\"").append(total_amount).append("\"").append(",");
        builder.append("\"subject\":").append("\"").append(content).append("\"").append(",");
        builder.append("\"body\":").append("\"").append(content).append("\"").append(",");
        builder.append("\"out_trade_no\":").append("\"").append(out_trade_no).append("\"").append("}");

        return builder.toString();
    }

    /**
     * 构造支付订单参数信息
     *
     * @param map 支付订单参数
     * @return
     */
    public static String buildOrderParam(Map<String, String> map) {
        List<String> keys = new ArrayList<String>(map.keySet());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            sb.append(buildKeyValue(key, value, true));
            sb.append("&");
        }

        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        sb.append(buildKeyValue(tailKey, tailValue, true));

        return sb.toString();
    }

    /**
     * 拼接键值对
     *
     * @param key
     * @param value
     * @param isEncode
     * @return
     */
    private static String buildKeyValue(String key, String value, boolean isEncode) {
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append("=");
        if (isEncode) {
            try {
                sb.append(URLEncoder.encode(value, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                sb.append(value);
            }
        } else {
            sb.append(value);
        }
        return sb.toString();
    }

    /**
     * 对支付参数信息进行签名
     *
     * @param map 待签名授权信息
     * @return sign
     */
    public static String getSign(Map<String, String> map, String rsaKey, boolean rsa2) {
        List<String> keys = new ArrayList<String>(map.keySet());
        // key排序
        Collections.sort(keys);

        StringBuilder authInfo = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            authInfo.append(buildKeyValue(key, value, false));
            authInfo.append("&");
        }

        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        authInfo.append(buildKeyValue(tailKey, tailValue, false));

        String oriSign = SignUtils.sign(authInfo.toString(), rsaKey,rsa2);
        String encodedSign = "";

        try {
            encodedSign = URLEncoder.encode(oriSign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "sign=" + encodedSign;
    }

    /**
     * 要求外部订单号必须唯一。
     *
     * @return getOutTradeNo
     */
    private static String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);
        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }
}
