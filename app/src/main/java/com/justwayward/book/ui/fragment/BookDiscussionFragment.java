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

import android.os.Bundle;

import com.justwayward.book.R;
import com.justwayward.book.base.BaseRVFragment;
import com.justwayward.book.base.Constant;
import com.justwayward.book.bean.DiscussionList;
import com.justwayward.book.bean.support.SelectionEvent;
import com.justwayward.book.component.AppComponent;
import com.justwayward.book.component.DaggerCommunityComponent;
import com.justwayward.book.ui.activity.BookDiscussionDetailActivity;
import com.justwayward.book.ui.contract.BookDiscussionContract;
import com.justwayward.book.ui.easyadapter.BookDiscussionAdapter;
import com.justwayward.book.ui.presenter.BookDiscussionPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * 综合讨论区Fragment
 *
 * @author yuyh.
 * @date 16/9/2.
 */
public class BookDiscussionFragment extends BaseRVFragment<BookDiscussionPresenter, DiscussionList.PostsBean> implements BookDiscussionContract.View {

    private static final String BUNDLE_BLOCK = "block";

    public static BookDiscussionFragment newInstance(String block) {
        BookDiscussionFragment fragment = new BookDiscussionFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_BLOCK, block);
        fragment.setArguments(bundle);
        return fragment;
    }

    private String block = "ramble";
    private String sort = Constant.SortType.DEFAULT;
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
        block = getArguments().getString(BUNDLE_BLOCK);
        EventBus.getDefault().register(this);
    }

    @Override
    public void configViews() {
        initAdapter(BookDiscussionAdapter.class, true, true);
        onRefresh();
    }

    @Override
    public void showBookDisscussionList(List<DiscussionList.PostsBean> list, boolean isRefresh) {
        if (isRefresh) {
            mAdapter.clear();
            start = 0;
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
        distillate = event.distillate;
        onRefresh();
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        mPresenter.getBookDisscussionList(block, sort, distillate, 0, limit);
    }

    @Override
    public void onLoadMore() {
        mPresenter.getBookDisscussionList(block, sort, distillate, start, limit);
    }

    @Override
    public void onItemClick(int position) {
        DiscussionList.PostsBean data = mAdapter.getItem(position);
        BookDiscussionDetailActivity.startActivity(activity, data._id);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

}
