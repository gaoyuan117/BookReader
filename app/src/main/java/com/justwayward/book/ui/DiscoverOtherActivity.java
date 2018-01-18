package com.justwayward.book.ui;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;

import com.justwayward.book.R;
import com.justwayward.book.base.BaseActivity;
import com.justwayward.book.component.AppComponent;
import com.justwayward.book.ui.adapter.ViewPagerAdapter;
import com.justwayward.book.ui.fragment.DiscoverOtherFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class DiscoverOtherActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {

    @Bind(R.id.tab_fragment_city)
    TabLayout mTabLayout;
    @Bind(R.id.vp_fragment)
    ViewPager mViewPager;

    private ViewPagerAdapter mAdapter;
    private List<String> mTitles = new ArrayList<>();
    private List<Fragment> mList = new ArrayList<>();

    String type;

    @Override
    public int getLayoutId() {
        return R.layout.activity_sub_category;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        type = getIntent().getStringExtra("type");
    }

    @Override
    public void initToolBar() {
        mCommonToolbar.setTitle(type);
        mCommonToolbar.setNavigationIcon(R.drawable.ab_back);
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
        mTitles.add("最新电影");
        mTitles.add("国内电影");
        mTitles.add("国外电影");
        mTitles.add("经典影片");
        mTitles.add("其他");
        mList.add(new DiscoverOtherFragment());
        mList.add(new DiscoverOtherFragment());
        mList.add(new DiscoverOtherFragment());
        mList.add(new DiscoverOtherFragment());
        mList.add(new DiscoverOtherFragment());
        mAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), getActivity(), mList, mTitles);
        mViewPager.setAdapter(mAdapter);
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
