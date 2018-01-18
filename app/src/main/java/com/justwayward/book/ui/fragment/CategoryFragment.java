package com.justwayward.book.ui.fragment;

import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.justwayward.book.view.recyclerview.decoration.DividerDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by gaoyuan on 2017/11/20.
 */

public class CategoryFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {


    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;

    private List<CategoryListBean.DataBean> mList = new ArrayList<>();
    private CategoryAdapter mAdapter;
    private String id;
    private String type;
    private int page = 1;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_book_city_home;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

        mAdapter = new CategoryAdapter(R.layout.item_sub_category_list, mList);
        mAdapter.setEnableLoadMore(true);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnLoadMoreListener(this, recyclerview);
        mAdapter.disableLoadMoreIfNotFullPage();

        DividerDecoration itemDecoration = new DividerDecoration(ContextCompat.getColor(getActivity(), R.color.common_divider_narrow), 1, 0, 0);
        itemDecoration.setDrawLastItem(false);
        recyclerview.addItemDecoration(itemDecoration);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.setAdapter(mAdapter);


        refresh.setOnRefreshListener(this);

    }

    @Override
    public void attachView() {
        id = getArguments().getString("id");
        type = getArguments().getString("type");
    }

    @Override
    public void initDatas() {
        getCategoryList();
    }

    @Override
    public void configViews() {

    }


    private void getCategoryList() {
        Map<String, Object> map = new HashMap<>();

        if (type.equals("is_new")) {//最新
            map.put("is_new", 1);
        } else if (type.equals("is_hot")) {//热门
            map.put("is_hot", 1);
        } else if (type.equals("is_finish")) {//完结
            map.put("is_finish", 1);
        }
        map.put("category_id", id);

        RetrofitClient.getInstance().createApi().getNovelByCategory(map, page)
                .compose(RxUtils.<HttpResult<CategoryListBean>>io_main())
                .subscribe(new BaseObjObserver<CategoryListBean>(getActivity(), refresh, mAdapter) {
                    @Override
                    protected void onHandleSuccess(CategoryListBean categoryListBean) {
                        if (page == 1) {
                            mList.clear();
                        }
                        if (categoryListBean.getData().isEmpty()) {
                            mAdapter.loadMoreEnd();
                            mAdapter.notifyDataSetChanged();
                            return;
                        }
                        mList.addAll(categoryListBean.getData());
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        BookDetailActivity.startActivity(getActivity(), mList.get(position).getId() + "");
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        getCategoryList();
    }

    @Override
    public void onRefresh() {
        page = 1;
        getCategoryList();
    }
}
