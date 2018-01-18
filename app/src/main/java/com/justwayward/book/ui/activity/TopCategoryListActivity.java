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

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.justwayward.book.R;
import com.justwayward.book.base.BaseActivity;
import com.justwayward.book.bean.CategoryBean;
import com.justwayward.book.component.AppComponent;
import com.justwayward.book.component.DaggerFindComponent;
import com.justwayward.book.ui.adapter.TopCategoryListAdapter;
import com.justwayward.book.ui.contract.TopCategoryListContract;
import com.justwayward.book.ui.presenter.TopCategoryListPresenter;
import com.justwayward.book.view.SupportGridItemDecoration;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

/**
 * Created by lfh on 2016/8/30.
 */
public class TopCategoryListActivity extends BaseActivity implements TopCategoryListContract.View {


    @Bind(R.id.ll_category)
    LinearLayout llCategory;

    @Inject
    TopCategoryListPresenter mPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_top_category_list;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerFindComponent.builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    public void initToolBar() {
        mCommonToolbar.setTitle(getString(R.string.category));
        mCommonToolbar.setNavigationIcon(R.drawable.ab_back);
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews() {
//        showDialog();
        mPresenter.attachView(this);
        mPresenter.getCategoryList();
    }


    @Override
    public void showCategoryList(List<CategoryBean> data) {

        llCategory.removeAllViews();
        for (int i = 0; i < data.size(); i++) {
            addView(data.get(i));
        }


    }

    @Override
    public Context getContext() {
        return this;
    }

    /**
     * 添加分布局
     */
    private void addView(CategoryBean bean) {
        View layout = View.inflate(this, R.layout.layout_category, null);
        TextView tvType = (TextView) layout.findViewById(R.id.tv_type);
        tvType.setText(bean.getCategory());
        RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.rvMaleCategory);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.addItemDecoration(new SupportGridItemDecoration(this));
        TopCategoryListAdapter adapter = new TopCategoryListAdapter(mContext, bean.getSub_category());
        recyclerView.setAdapter(adapter);

        llCategory.addView(layout);

    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {
        dismissDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}
