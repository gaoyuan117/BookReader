package com.justwayward.book.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;

import com.justwayward.book.R;
import com.justwayward.book.base.BaseActivity;
import com.justwayward.book.component.AppComponent;
import com.justwayward.book.ui.adapter.ViewPagerAdapter;
import com.justwayward.book.ui.fragment.CategoryFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class SubCategoryActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {

    @Bind(R.id.tab_fragment_city)
    TabLayout mTabLayout;
    @Bind(R.id.vp_fragment)
    ViewPager mViewPager;

    private ViewPagerAdapter mAdapter;
    private List<String> mTitles = new ArrayList<>();
    private List<Fragment> mList = new ArrayList<>();

    String type;
    private String id;

    @Override
    public int getLayoutId() {
        return R.layout.activity_sub_category;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        type = getIntent().getStringExtra("type");
        id = getIntent().getStringExtra("id");
    }

    @Override
    public void initToolBar() {
        mCommonToolbar.setNavigationIcon(R.drawable.ab_back);
        mCommonToolbar.setTitle(type);
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews() {
        setTabViewPager();
    }

    /**
     * 设置Tablayout
     */
    private void setTabViewPager() {
        mTitles.add("新书");
        mTitles.add("热门");
        mTitles.add("完结");

        //新书
        CategoryFragment newFragment = new CategoryFragment();
        Bundle newBundle = new Bundle();
        newBundle.putString("id", id);
        newBundle.putString("type", "is_new");
        newFragment.setArguments(newBundle);

        //热门
        CategoryFragment hotFragment = new CategoryFragment();
        Bundle hotBundle = new Bundle();
        hotBundle.putString("id", id);
        hotBundle.putString("type", "is_hot");
        hotFragment.setArguments(hotBundle);

        //完结
        CategoryFragment finishFragment = new CategoryFragment();
        Bundle finishBundle = new Bundle();
        finishBundle.putString("id", id);
        finishBundle.putString("type", "is_finish");
        finishFragment.setArguments(finishBundle);

        mList.add(newFragment);
        mList.add(hotFragment);
        mList.add(finishFragment);
        mAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), getActivity(), mList, mTitles);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabTextColors(ContextCompat.getColor(getActivity(),R.color.white), ContextCompat.getColor(getActivity(),R.color.tab_color));
        mTabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getActivity(),R.color.tab_color));
        mTabLayout.setOnTabSelectedListener(this);


    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        String text = tab.getText().toString();
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
}
