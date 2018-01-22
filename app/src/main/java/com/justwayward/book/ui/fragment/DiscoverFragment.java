package com.justwayward.book.ui.fragment;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.justwayward.book.R;
import com.justwayward.book.ReaderApplication;
import com.justwayward.book.WebViewActivity;
import com.justwayward.book.api.CoomonApi;
import com.justwayward.book.base.BaseFragment;
import com.justwayward.book.bean.BannerBean;
import com.justwayward.book.bean.DiscoverBean;
import com.justwayward.book.component.AppComponent;
import com.justwayward.book.retrofit.BaseObjObserver;
import com.justwayward.book.retrofit.HttpResult;
import com.justwayward.book.retrofit.RetrofitClient;
import com.justwayward.book.retrofit.RxUtils;
import com.justwayward.book.ui.DiscoverOtherActivity;
import com.justwayward.book.ui.activity.MyYuanActivity;
import com.justwayward.book.ui.activity.SearchActivity;
import com.justwayward.book.ui.activity.TopActivity;
import com.justwayward.book.ui.adapter.DiscoverAdapter;
import com.justwayward.book.utils.MyImageLoader;
import com.justwayward.book.view.recyclerview.decoration.DividerDecoration;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by gaoyuan on 2017/11/20.
 */

public class DiscoverFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, OnBannerClickListener {

    @Bind(R.id.img_recommend_search)
    ImageView imgRecommendSearch;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refreshLayout;

    private View headView;
    private Banner mBanner;
    private List<String> bannerList = new ArrayList<>();
    private List<DiscoverBean> mList = new ArrayList<>();
    private DiscoverAdapter mAdapter;
    private List<BannerBean> bannerBeens;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_discover;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        headView = LayoutInflater.from(activity).inflate(R.layout.layout_banner, null);
        mBanner = (Banner) headView.findViewById(R.id.banner);
        mBanner.setOnBannerClickListener(this);
    }

    @Override
    public void attachView() {

        mAdapter = new DiscoverAdapter(R.layout.item_discover, mList);
        mAdapter.addHeaderView(headView);

        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerDecoration itemDecoration = new DividerDecoration(ContextCompat.getColor(getActivity(), R.color.common_divider_narrow), 1, 0, 0);
        itemDecoration.setDrawLastItem(false);
        recyclerview.addItemDecoration(itemDecoration);
        recyclerview.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(this);
        refreshLayout.setOnRefreshListener(this);
        imgRecommendSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });
    }

    @Override
    public void initDatas() {
        addItem();
        getAdList();
        getZoneList();
    }

    @Override
    public void configViews() {

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (position == 0) {
            startActivity(new Intent(getActivity(), TopActivity.class));
        } else if (position == 1) {
            startActivity(new Intent(getActivity(), MyYuanActivity.class));
        } else {
            CoomonApi.toBrowser(getActivity(), mList.get(position).getZone_link());
        }
    }


    private void addItem() {
        DiscoverBean bean = new DiscoverBean();
        bean.setZone_name("排行榜");
        DiscoverBean bean1 = new DiscoverBean();
        bean1.setZone_name("我的源");

        mList.add(bean);
        mList.add(bean1);
    }

    /**
     * 获取轮播图
     */
    private void getAdList() {
        RetrofitClient.getInstance().createApi().getAdList("3")
                .compose(RxUtils.<HttpResult<List<BannerBean>>>io_main())
                .subscribe(new BaseObjObserver<List<BannerBean>>(getActivity()) {
                    @Override
                    protected void onHandleSuccess(List<BannerBean> bannerBeen) {
                        if (bannerBeen == null || bannerBeen.size() == 0) {
                            return;
                        }
                        bannerBeens = bannerBeen;
                        bannerList.clear();
                        for (int i = 0; i < bannerBeen.size(); i++) {
                            bannerList.add(bannerBeen.get(i).getImage_url());
                        }
                        setBanner(bannerList);
                    }
                });
    }

    /**
     * 设置轮播图
     *
     * @param list
     */
    private void setBanner(List<String> list) {
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        mBanner.setImageLoader(new MyImageLoader());
        mBanner.setImages(list);
        mBanner.setBannerAnimation(Transformer.Default);
        mBanner.isAutoPlay(true);
        mBanner.setViewPagerIsScroll(true);
        mBanner.setDelayTime(3000);
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        mBanner.start();
    }

    private void getZoneList() {
        RetrofitClient.getInstance().createApi().getZoneList(ReaderApplication.token)
                .compose(RxUtils.<HttpResult<List<DiscoverBean>>>io_main())
                .subscribe(new BaseObjObserver<List<DiscoverBean>>(getActivity(), refreshLayout) {
                    @Override
                    protected void onHandleSuccess(List<DiscoverBean> list) {
                        if (list.isEmpty()) {
                            return;
                        }
                        mList.addAll(list);
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onRefresh() {
        mList.clear();
        addItem();
        getAdList();
        getZoneList();
    }

    @Override
    public void OnBannerClick(int position) {
        CoomonApi.toBrowser(getActivity(), bannerBeens.get(position - 1).getUrl());
    }
}
