package com.justwayward.book.ui.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.justwayward.book.R;
import com.justwayward.book.base.BaseFragment;
import com.justwayward.book.component.AppComponent;
import com.justwayward.book.ui.adapter.DiscoverOtherAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by gaoyuan on 2017/11/21.
 */

public class DiscoverOtherFragment extends BaseFragment{

    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;

    private List<String> mList = new ArrayList<>();
    private DiscoverOtherAdapter mAdapter;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_book_city_home;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

        mList.add("");
        mList.add("");
        mList.add("");
        mAdapter = new DiscoverOtherAdapter(R.layout.item_discover_other,mList);
        recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerview.setAdapter(mAdapter);
    }

    @Override
    public void attachView() {

    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews() {

    }
}
