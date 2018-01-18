package com.justwayward.book.ui.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.justwayward.book.R;
import com.justwayward.book.base.BaseActivity;
import com.justwayward.book.bean.BookCityCategoryBean;
import com.justwayward.book.component.AppComponent;
import com.justwayward.book.retrofit.BaseObjObserver;
import com.justwayward.book.retrofit.HttpResult;
import com.justwayward.book.retrofit.RetrofitClient;
import com.justwayward.book.retrofit.RxUtils;
import com.justwayward.book.ui.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class TopActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {

    @Bind(R.id.tab_fragment_city)
    TabLayout mTabLayout;
    @Bind(R.id.vp_fragment)
    ViewPager mViewPager;

    private ViewPagerAdapter mAdapter;
    private List<String> mTitles = new ArrayList<>();
    private List<Fragment> mList = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_top;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void initToolBar() {
        mCommonToolbar.setTitle("排行榜");
        mCommonToolbar.setNavigationIcon(R.drawable.ab_back);
    }

    @Override
    public void initDatas() {
        getCategoryList();
    }

    @Override
    public void configViews() {
        setTabViewPager();
    }

    /**
     * 设置Tablayout
     */
    private void setTabViewPager() {

        mAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), getActivity(), mList, mTitles);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabTextColors(ContextCompat.getColor(getActivity(),R.color.black), ContextCompat.getColor(getActivity(),R.color.black));
        mTabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getActivity(),R.color.global));
        mTabLayout.setOnTabSelectedListener(this);
        mViewPager.setOffscreenPageLimit(10);

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


    /**
     * 获取导航栏
     */
    private void getCategoryList() {
        RetrofitClient.getInstance().createApi().getCategoryList("0")
                .compose(RxUtils.<HttpResult<List<BookCityCategoryBean>>>io_main())
                .subscribe(new BaseObjObserver<List<BookCityCategoryBean>>(getActivity()) {
                    @Override
                    protected void onHandleSuccess(List<BookCityCategoryBean> list) {
                        if(list.isEmpty()){
                            return;
                        }
                        for (int i = 0; i < list.size(); i++) {

                            Top2Fragment fragment = new Top2Fragment();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("id",list.get(i).getId()+"");
                            fragment.setArguments(bundle);

                            mTitles.add(list.get(i).getCategory());
                            mList.add(fragment);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                });

    }


}
