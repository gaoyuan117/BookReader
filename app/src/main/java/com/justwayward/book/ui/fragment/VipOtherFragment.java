package com.justwayward.book.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.justwayward.book.R;
import com.justwayward.book.base.BaseFragment;
import com.justwayward.book.bean.CategoryListBean;
import com.justwayward.book.component.AppComponent;
import com.justwayward.book.retrofit.BaseObjObserver;
import com.justwayward.book.retrofit.HttpResult;
import com.justwayward.book.retrofit.RetrofitClient;
import com.justwayward.book.retrofit.RxUtils;
import com.justwayward.book.ui.activity.BookDetailActivity;
import com.justwayward.book.ui.adapter.CategoryAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by gaoyuan on 2017/11/27.
 */

public class VipOtherFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.OnItemClickListener {

    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;

    private List<CategoryListBean.DataBean> mList = new ArrayList<>();
    private CategoryAdapter mAdapter;
    private String id;
    private String type;//会员分类，畅读书城，会员，其他

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_book_city_home;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        id = getArguments().getString("id");
        type = getArguments().getString("type");
        mAdapter = new CategoryAdapter(R.layout.item_sub_category_list, mList);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.setAdapter(mAdapter);
        refresh.setOnRefreshListener(this);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    public void attachView() {

    }

    @Override
    public void initDatas() {
        getList();
    }

    @Override
    public void configViews() {

    }

    private void getList() {
        if (TextUtils.isEmpty(id)) return;

        Map<String, Object> map = new HashMap<>();
        map.put("type", type);
        map.put("category_id", id);

        RetrofitClient.getInstance().createApi().getNovelByCategory(map, 1)
                .compose(RxUtils.<HttpResult<CategoryListBean>>io_main())
                .subscribe(new BaseObjObserver<CategoryListBean>(getActivity(), refresh) {
                    @Override
                    protected void onHandleSuccess(CategoryListBean categoryListBean) {

                        if (categoryListBean.getData().isEmpty()) {
                            return;
                        }
                        mList.clear();
                        mList.addAll(categoryListBean.getData());
                        mAdapter.notifyDataSetChanged();

                    }
                });

    }

    @Override
    public void onRefresh() {
        getList();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        BookDetailActivity.startActivity(getActivity(), mList.get(position).getId() + "");
    }
}

