/**
 * Copyright 2016 JustWayward Team
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.justwayward.book.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.justwayward.book.R;
import com.justwayward.book.base.BaseRVActivity;
import com.justwayward.book.bean.BookSource;
import com.justwayward.book.bean.SourceBean;
import com.justwayward.book.component.AppComponent;
import com.justwayward.book.component.DaggerBookComponent;
import com.justwayward.book.retrofit.BaseObjObserver;
import com.justwayward.book.retrofit.HttpResult;
import com.justwayward.book.retrofit.RetrofitClient;
import com.justwayward.book.retrofit.RxUtils;
import com.justwayward.book.ui.contract.BookSourceContract;
import com.justwayward.book.ui.easyadapter.BookSourceAdapter;
import com.justwayward.book.ui.presenter.BookSourcePresenter;

import java.util.List;

import javax.inject.Inject;

/**
 * @author yuyh.
 * @date 2016/9/8.
 */
public class BookSourceActivity extends BaseRVActivity<SourceBean.DataBean> implements BookSourceContract.View {

    public static final String INTENT_BOOK_ID = "bookId";

    public static void start(Activity activity, String bookId,String chapter, int reqId) {
        activity.startActivityForResult(new Intent(activity, BookSourceActivity.class)
                .putExtra(INTENT_BOOK_ID, bookId).putExtra("chapter", chapter), reqId);
    }


    private String bookId, chapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_common_recyclerview;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerBookComponent.builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    public void initToolBar() {
        bookId = getIntent().getStringExtra(INTENT_BOOK_ID);
        chapter = getIntent().getStringExtra("chapter");
        mCommonToolbar.setTitle("选择来源");
        mCommonToolbar.setNavigationIcon(R.drawable.ab_back);
    }

    @Override
    public void initDatas() {
        initAdapter(BookSourceAdapter.class, false, false);
    }

    @Override
    public void configViews() {
        getSource();
    }

    @Override
    public void onItemClick(int position) {
        SourceBean.DataBean bean = mAdapter.getItem(position);
        Intent intent = new Intent();
        intent.putExtra("source", bean.getId()+"");
        setResult(RESULT_OK, intent);
        finish();
    }


    @Override
    public void showBookSource(SourceBean bean) {
        mAdapter.clear();
        mAdapter.addAll(bean.getData());
    }

    @Override
    public void showError() {
        loaddingError();
    }

    @Override
    public void complete() {
        mRecyclerView.setRefreshing(false);
    }

    private void getSource() {

        RetrofitClient.getInstance().createApi().getSource(bookId, chapter)
                .compose(RxUtils.<HttpResult<SourceBean>>io_main())
                .subscribe(new BaseObjObserver<SourceBean>(this) {
                    @Override
                    protected void onHandleSuccess(SourceBean sourceBean) {
                        showBookSource(sourceBean);
                    }
                });

    }


}
