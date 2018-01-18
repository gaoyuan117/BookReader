package com.justwayward.book.wxpay;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import com.justwayward.book.Constants;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class WxPay {

    private static WxPay sWxPay;

    public static WxPay getWxPay() {
        if (sWxPay == null) {
            sWxPay = new WxPay();
        }
        return sWxPay;
    }

    private WxPay() {
        sWxPay = this;
    }

    private WxCallBack mWxCallBack;

    public interface WxCallBack {
        //code = 0 为成功，参考微信App支付文档
        void payResponse(int code);
    }

    /**
     * 生成签名
     */
    public static String genPackageSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.WxPay.WX_API_KEUSTORE);

        String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        return packageSign;
    }

    public static String genProductArgs(String orderId, String goods_name, String fee, String back_url) {
        try {
            String nonceStr = genNonceStr();
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
            packageParams.add(new BasicNameValuePair("appid", Constants.WxPay.APP_ID));
            packageParams.add(new BasicNameValuePair("body", goods_name));//商品名中文冲突
            packageParams.add(new BasicNameValuePair("mch_id", Constants.WxPay.WX_SHOP_NUM));
            packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
            packageParams.add(new BasicNameValuePair("notify_url", back_url));
            packageParams.add(new BasicNameValuePair("out_trade_no", orderId));
            packageParams.add(new BasicNameValuePair("spbill_create_ip", "127.0.0.1"));
            packageParams.add(new BasicNameValuePair("total_fee", String.valueOf((int) (Double.valueOf(fee) * 100))));
            packageParams.add(new BasicNameValuePair("trade_type", "APP"));

            String sign = genPackageSign(packageParams);
            packageParams.add(new BasicNameValuePair("sign", sign));
            String xmlstring = toXml(packageParams);
            return new String(xmlstring.toString().getBytes(), "ISO8859-1");
        } catch (Exception e) {
            return null;
        }
    }

    public static String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    public static String toXml(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (int i = 0; i < params.size(); i++) {
            sb.append("<" + params.get(i).getName() + ">");
            sb.append(params.get(i).getValue());
            sb.append("</" + params.get(i).getName() + ">");
        }
        sb.append("</xml>");
        return sb.toString();
    }

    private static String genOutTradNo() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    public Map<String, String> decodeXml(String content) {
        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String nodeName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if ("xml".equals(nodeName) == false) {
                            //实例化student对象
                            xml.put(nodeName, parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }
            return xml;
        } catch (Exception e) {
            Log.e("orion", e.toString());
        }
        return null;

    }

    private class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String, String>> {
        private String order_id;
        private Context context;
        private String goods_name;
        private String back_url;
        private String fee;
        private ProgressDialog mProgressDialog;

        GetPrepayIdTask(Context context, String order_id, String goods_name, String fee, String back_url) {
            this.context = context;
            this.order_id = order_id;
            this.goods_name = goods_name;
            this.back_url = back_url;
            this.fee = fee;
            mProgressDialog = ProgressDialog.show(context, "", "加载中", true, true);
        }

        @Override
        protected void onPreExecute() {
            if (mProgressDialog != null) {
                mProgressDialog.show();
            }
        }

        @Override
        protected void onPostExecute(Map<String, String> result) {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

            genPayReq(context, result.get("prepay_id"));
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Map<String, String> doInBackground(Void... params) {

            String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
            String entity = genProductArgs(order_id, goods_name, fee, back_url);
            byte[] buf = Util.httpPost(url, entity);
            String content = new String(buf);
            Log.e("pay", "下单返回值：" + content);
            Map<String, String> xml = decodeXml(content);
            return xml;
        }
    }


    private void genPayReq(Context context, String prepay_id) {
        PayReq req = new PayReq();
        req.appId = Constants.WxPay.APP_ID;
        req.partnerId = Constants.WxPay.WX_SHOP_NUM;
        req.prepayId = prepay_id;
        req.packageValue = "Sign=WXPay";
//        req.packageValue = "prepay_id=" + prepay_id;
        req.nonceStr = genNonceStr();
        req.timeStamp = String.valueOf(genTimeStamp());

        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

        req.sign = genAppSign(signParams);
        IWXAPI msgApi = WXAPIFactory.createWXAPI(context, Constants.WxPay.APP_ID);
        msgApi.registerApp(Constants.WxPay.APP_ID);
        msgApi.sendReq(req);

    }

    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.WxPay.WX_API_KEUSTORE);

        String appSign = MD5.getMessageDigest(sb.toString().getBytes());
        return appSign;
    }

    /**
     * 支付 //商品名中文冲突 详情genProductArgs()方法
     *
     * @param context
     * @param goods_name
     * @param fee
     * @param back_url
     */
    public void pay(Context context, String order_id, String goods_name, String fee, String back_url, WxCallBack wxcallabck) {
        this.mWxCallBack = wxcallabck;
        new GetPrepayIdTask(context, order_id, goods_name, fee, back_url).execute();
    }

    /**
     * 在 WxEntryActivity中返回该方法，拿到支付结果。
     *
     * @param code
     */
    public void ç(int code) {
        if (mWxCallBack != null) {
            mWxCallBack.payResponse(code);
        }
    }
}
