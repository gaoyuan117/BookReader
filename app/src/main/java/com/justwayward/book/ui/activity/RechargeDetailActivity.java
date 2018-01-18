package com.justwayward.book.ui.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.justwayward.book.R;
import com.justwayward.book.ReaderApplication;
import com.justwayward.book.base.BaseActivity;
import com.justwayward.book.bean.RechargeRecordBean;
import com.justwayward.book.component.AppComponent;
import com.justwayward.book.retrofit.BaseObjObserver;
import com.justwayward.book.retrofit.HttpResult;
import com.justwayward.book.retrofit.RetrofitClient;
import com.justwayward.book.retrofit.RxUtils;
import com.justwayward.book.ui.adapter.RechargeDetailAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class RechargeDetailActivity extends BaseActivity {

    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;

    private List<RechargeRecordBean> mList = new ArrayList<>();
    private RechargeDetailAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_buy_history;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

        mAdapter = new RechargeDetailAdapter(R.layout.activity_recharge_detail, mList);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(mAdapter);
    }

    @Override
    public void initToolBar() {
        mCommonToolbar.setNavigationIcon(R.drawable.ab_back);
        mCommonToolbar.setTitle("充值明细");
    }

    @Override
    public void initDatas() {
        getRechargeDetail();
    }

    @Override
    public void configViews() {

    }


    private void getRechargeDetail() {
        RetrofitClient.getInstance().createApi().getPay(ReaderApplication.token)
                .compose(RxUtils.<HttpResult<List<RechargeRecordBean>>>io_main())
                .subscribe(new BaseObjObserver<List<RechargeRecordBean>>(this) {
                    @Override
                    protected void onHandleSuccess(List<RechargeRecordBean> list) {
                        if (list != null && list.isEmpty()) {
                            return;
                        }
                        mList.clear();
                        mList.addAll(list);
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }


}
