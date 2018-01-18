package com.justwayward.book.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.justwayward.book.R;
import com.justwayward.book.base.BaseFragment;
import com.justwayward.book.bean.BookCityBean;
import com.justwayward.book.component.AppComponent;
import com.justwayward.book.retrofit.BaseObjObserver;
import com.justwayward.book.retrofit.HttpResult;
import com.justwayward.book.retrofit.RetrofitClient;
import com.justwayward.book.retrofit.RxUtils;
import com.justwayward.book.ui.adapter.BookOtherAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by gaoyuan on 2017/11/19.
 */

public class BookOtherFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;

    private List<BookCityBean> mList = new ArrayList<>();
    private BookOtherAdapter mAdapter;
    private String id;
    private String type;//会员分类，畅读书城，会员，其他

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_book_city_home;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        Bundle bundle = getArguments();
        if(bundle==null){
            return;
        }
        id = getArguments().getString("id");
        type = getArguments().getString("type");
        mAdapter = new BookOtherAdapter(R.layout.item_book_city_home, mList);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.setAdapter(mAdapter);
        refresh.setOnRefreshListener(this);
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

    private void getList(){
        if(TextUtils.isEmpty(id)) return;

        RetrofitClient.getInstance().createApi().getNovelByCategoryIndex(id)
                .compose(RxUtils.<HttpResult<List<BookCityBean>>>io_main())
                .subscribe(new BaseObjObserver<List<BookCityBean>>(getActivity(),refresh) {
                    @Override
                    protected void onHandleSuccess(List<BookCityBean> bookCityBeen) {
                        if(bookCityBeen==null||bookCityBeen.size()==0){
                            return;
                        }

                        mList.clear();

                        for (int i = 0; i < bookCityBeen.size(); i++) {
                            if(bookCityBeen.get(i).getList().size()>0){
                                mList.add(bookCityBeen.get(i));
                            }
                        }

                        mAdapter.notifyDataSetChanged();

                    }
                });
    }

    @Override
    public void onRefresh() {
        getList();
    }
}
