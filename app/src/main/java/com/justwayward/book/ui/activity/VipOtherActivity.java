package com.justwayward.book.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.justwayward.book.R;
import com.justwayward.book.base.BaseActivity;
import com.justwayward.book.bean.BookCityCategoryBean;
import com.justwayward.book.component.AppComponent;
import com.justwayward.book.retrofit.BaseObjObserver;
import com.justwayward.book.retrofit.HttpResult;
import com.justwayward.book.retrofit.RetrofitClient;
import com.justwayward.book.retrofit.RxUtils;
import com.justwayward.book.ui.adapter.ViewPagerAdapter;
import com.justwayward.book.ui.fragment.VipOtherFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class VipOtherActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tab_fragment_city)
    TabLayout mTabLayout;
    @Bind(R.id.vp_fragment)
    ViewPager mViewPager;

    private String title,type;
    private ViewPagerAdapter mAdapter;
    private List<String> mTitles = new ArrayList<>();
    private List<Fragment> mList = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_vip_other;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        title = getIntent().getStringExtra("title");
        type = getIntent().getStringExtra("type");
        Log.e("gy","title："+title);
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }
    }

    @Override
    public void initToolBar() {


    }

    @Override
    public void initDatas() {
        getCategoryList();
    }

    @Override
    public void configViews() {
        setTabViewPager();
    }


    @OnClick({R.id.img_back, R.id.img_recommend_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_recommend_search:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
        }
    }

    /**
     * 设置Tablayout
     */
    private void setTabViewPager() {

        mAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), getActivity(), mList, mTitles);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabTextColors(ContextCompat.getColor(this,R.color.white), ContextCompat.getColor(this,R.color.tab_color));
        mTabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this,R.color.tab_color));
        mTabLayout.setOnTabSelectedListener(this);

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            if (mTabLayout.getTabAt(i) == tab) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    /**
     * 获取导航栏
     */
    private void getCategoryList() {
        RetrofitClient.getInstance().createApi().getCategoryList("0")
                .compose(RxUtils.<HttpResult<List<BookCityCategoryBean>>>io_main())
                .subscribe(new BaseObjObserver<List<BookCityCategoryBean>>(getActivity(),"加载中") {
                    @Override
                    protected void onHandleSuccess(List<BookCityCategoryBean> list) {

                        for (int i = 0; i < list.size(); i++) {
                            mTitles.add(list.get(i).getCategory());
                            VipOtherFragment fragment = new VipOtherFragment();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("id", list.get(i).getId() + "");
                            bundle.putSerializable("type",type);
                            fragment.setArguments(bundle);
                            mList.add(fragment);
                        }

                        mAdapter.notifyDataSetChanged();
                    }
                });

    }
}
