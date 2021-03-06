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
package com.justwayward.book.ui.fragment;

import com.justwayward.book.R;
import com.justwayward.book.base.BaseRVFragment;
import com.justwayward.book.base.Constant;
import com.justwayward.book.bean.BookReviewList;
import com.justwayward.book.bean.support.SelectionEvent;
import com.justwayward.book.component.AppComponent;
import com.justwayward.book.component.DaggerCommunityComponent;
import com.justwayward.book.ui.activity.BookReviewDetailActivity;
import com.justwayward.book.ui.contract.BookReviewContract;
import com.justwayward.book.ui.easyadapter.BookReviewAdapter;
import com.justwayward.book.ui.presenter.BookReviewPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * 书评区Fragment
 *
 * @author lfh.
 * @date 16/9/3.
 */
public class BookReviewFragment extends BaseRVFragment<BookReviewPresenter, BookReviewList.ReviewsBean> implements BookReviewContract.View {

    private String sort = Constant.SortType.DEFAULT;
    private String type = Constant.BookType.ALL;
    private String distillate = Constant.Distillate.ALL;

    @Override
    public int getLayoutResId() {
        return R.layout.common_easy_recyclerview;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerCommunityComponent.builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    public void initDatas() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void configViews() {
        initAdapter(BookReviewAdapter.class, true, true);
        onRefresh();
    }

    @Override
    public void showBookReviewList(List<BookReviewList.ReviewsBean> list, boolean isRefresh) {
        if (isRefresh) {
            mAdapter.clear();
        }
        mAdapter.addAll(list);
        start = start + list.size();
    }

    @Override
    public void showError() {
        loaddingError();
    }

    @Override
    public void complete() {
        mRecyclerView.setRefreshing(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void initCategoryList(SelectionEvent event) {
        mRecyclerView.setRefreshing(true);
        sort = event.sort;
        type = event.type;
        distillate = event.distillate;
        start = 0;
        mPresenter.getBookReviewList(sort, type, distillate, start, limit);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        mPresenter.getBookReviewList(sort, type, distillate, start, limit);
    }

    @Override
    public void onLoadMore() {
        mPresenter.getBookReviewList(sort, type, distillate, start, limit);
    }

    @Override
    public void onItemClick(int position) {
        BookReviewList.ReviewsBean data = mAdapter.getItem(position);
        BookReviewDetailActivity.startActivity(activity, data._id);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
