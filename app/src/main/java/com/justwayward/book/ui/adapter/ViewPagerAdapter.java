package com.justwayward.book.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by gaoyuan on 2017/11/17.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mList;
    private List<String> mTitles;

    public ViewPagerAdapter(FragmentManager fm, Context context, List<Fragment> mList, List<String> mTitles) {
        super(fm);
        this.mList = mList;
        this.mTitles = mTitles;
    }

    public ViewPagerAdapter(FragmentManager fm, Context context, List<Fragment> mList) {
        super(fm);
        this.mList = mList;
    }


    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(mTitles==null){
            return "";
        }
        return mTitles.get(position);
    }
}
