package com.justwayward.book.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.justwayward.book.Constants;
import com.justwayward.book.R;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
  private final String TAG = this.getClass().getName();

  private IWXAPI api;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.pay_result);

    api = WXAPIFactory.createWXAPI(this, Constants.WxPay.APP_ID);
    api.handleIntent(getIntent(), this);
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    setIntent(intent);
    api.handleIntent(intent, this);
  }

  @Override
  public void onReq(BaseReq req) {
  }

  @Override
  public void onResp(BaseResp resp) {
    Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
    finish();
  }
}