package com.justwayward.book.ui.activity;

import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.justwayward.book.R;
import com.justwayward.book.base.BaseActivity;
import com.justwayward.book.component.AppComponent;
import com.justwayward.book.ui.adapter.BuyHistoryAdapter;
import com.justwayward.book.view.recyclerview.decoration.DividerDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class BuyHistoryActivity extends BaseActivity {


    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;
    @Bind(R.id.tv_num)
    TextView tvNum;

    private List<String> mList = new ArrayList<>();
    private BuyHistoryAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_buy_history;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        tvNum.setVisibility(View.VISIBLE);

        mList.add("");
        mList.add("");
        mList.add("");

        mAdapter = new BuyHistoryAdapter(R.layout.item_buy_history,mList);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerDecoration itemDecoration = new DividerDecoration(ContextCompat.getColor(getActivity(), R.color.common_divider_narrow), 1, 0, 0);
        itemDecoration.setDrawLastItem(false);
        recyclerview.addItemDecoration(itemDecoration);
        recyclerview.setAdapter(mAdapter);
    }

    @Override
    public void initToolBar() {
        mCommonToolbar.setTitle("购买记录");
        mCommonToolbar.setNavigationIcon(R.drawable.ab_back);
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews() {

    }

}
