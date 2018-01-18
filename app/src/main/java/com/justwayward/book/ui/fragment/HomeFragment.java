package com.justwayward.book.ui.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.justwayward.book.R;
import com.justwayward.book.ReaderApplication;
import com.justwayward.book.WebViewActivity;
import com.justwayward.book.api.CoomonApi;
import com.justwayward.book.base.BaseFragment;
import com.justwayward.book.bean.BannerBean;
import com.justwayward.book.bean.BookCityBean;
import com.justwayward.book.bean.BookCityCategoryBean;
import com.justwayward.book.bean.FreeBean;
import com.justwayward.book.component.AppComponent;
import com.justwayward.book.retrofit.BaseObjObserver;
import com.justwayward.book.retrofit.HttpResult;
import com.justwayward.book.retrofit.RetrofitClient;
import com.justwayward.book.retrofit.RxUtils;
import com.justwayward.book.ui.activity.BookDetailActivity;
import com.justwayward.book.ui.activity.SubCategoryActivity;
import com.justwayward.book.ui.activity.TopActivity;
import com.justwayward.book.ui.activity.TopCategoryListActivity;
import com.justwayward.book.ui.adapter.HomeAdapter;
import com.justwayward.book.utils.MyImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 书城首页
 * Created by gaoyuan on 2017/11/17.
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, OnBannerClickListener {

    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;

    private View headView;
    private List<BookCityBean> mList = new ArrayList<>();
    private List<String> bannerList = new ArrayList<>();
    private HomeAdapter mAdapter;
    private ViewHolder viewHolder;

    private String freeId;
    private Banner mBanner;
    private List<BookCityCategoryBean> categoryList;
    private List<TextView> tvList = new ArrayList<>();
    private List<BannerBean> bannerBeens;


    @Override
    public int getLayoutResId() {
        return R.layout.fragment_book_city_home;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

        headView = View.inflate(getActivity(), R.layout.head_book_city_home, null);
        mBanner = (Banner) headView.findViewById(R.id.banner);
        mBanner.setOnBannerClickListener(this);
        viewHolder = new ViewHolder(headView);
        tvList.add(viewHolder.tvDushi);
        tvList.add(viewHolder.tvXianyan);
        tvList.add(viewHolder.tvQita);
        mAdapter = new HomeAdapter(mList);
        mAdapter.addHeaderView(headView);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.setAdapter(mAdapter);

        refresh.setOnRefreshListener(this);

    }

    @Override
    public void attachView() {

    }

    @Override
    public void initDatas() {
        getTodyFree();
        getHomeList();
        getAdList();
        getCategory();
    }

    @Override
    public void configViews() {
        viewHolder.tvPaihang.setOnClickListener(this);
        viewHolder.tvFenlei.setOnClickListener(this);
        viewHolder.tvDushi.setOnClickListener(this);
        viewHolder.tvXianyan.setOnClickListener(this);
        viewHolder.tvQita.setOnClickListener(this);
        viewHolder.tvTj.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_home_paihang://排行
                Intent intent = new Intent(getActivity(), TopActivity.class);
                getActivity().startActivity(intent);
                break;

            case R.id.img_home_fenlei://分类
                getActivity().startActivity(new Intent(getActivity(), TopCategoryListActivity.class));
                break;

            case R.id.img_home_dushi://都市
                getActivity().startActivity(new Intent(getActivity(),
                        SubCategoryActivity.class).putExtra("type", "都市").putExtra("id", categoryList.get(0).getId()));
                break;

            case R.id.img_home_xianyan://现言
                getActivity().startActivity(new Intent(getActivity(),
                        SubCategoryActivity.class).putExtra("type", "现言").putExtra("id", categoryList.get(1).getId()));
                break;

            case R.id.img_home_qita://其他
                getActivity().startActivity(new Intent(getActivity(),
                        SubCategoryActivity.class).putExtra("type", "其他").putExtra("id", categoryList.get(2).getId()));
                break;

            case R.id.tv_tj://今日免费
                BookDetailActivity.startActivity(getActivity(), freeId);
                break;
        }
    }

    /**
     * 获取今日推荐
     */
    private void getTodyFree() {
        RetrofitClient.getInstance().createApi().getTodyFree(ReaderApplication.token)
                .compose(RxUtils.<HttpResult<FreeBean>>io_main())
                .subscribe(new BaseObjObserver<FreeBean>(getActivity(), false) {

                    @Override
                    protected void onHandleSuccess(FreeBean freeBean) {
                        freeId = freeBean.getId() + "";
                        viewHolder.tvTj.setText("今日免费：" + freeBean.getTitle());
                    }
                });
    }

    /**
     * 获取小说列表
     */
    private void getHomeList() {
        RetrofitClient.getInstance().createApi().getIndexRecommend(ReaderApplication.token)
                .compose(RxUtils.<HttpResult<List<BookCityBean>>>io_main())
                .subscribe(new BaseObjObserver<List<BookCityBean>>(getActivity(), refresh) {
                    @Override
                    protected void onHandleSuccess(List<BookCityBean> bookCityBeen) {
                        if (bookCityBeen == null || bookCityBeen.size() == 0) {
                            return;
                        }
                        mList.clear();

                        for (int i = 0; i < bookCityBeen.size(); i++) {
                            if (bookCityBeen.get(i).getList().size() > 0) {
                                mList.add(bookCityBeen.get(i));
                            }
                        }

                        mAdapter.notifyDataSetChanged();

                    }
                });
    }

    /**
     * 获取首页分类.都市 现言 其他
     */
    private void getCategory() {
        RetrofitClient.getInstance().createApi().getCategory(1)
                .compose(RxUtils.<HttpResult<List<BookCityCategoryBean>>>io_main())
                .subscribe(new BaseObjObserver<List<BookCityCategoryBean>>(getActivity()) {
                    @Override
                    protected void onHandleSuccess(List<BookCityCategoryBean> list) {
                        if (list.isEmpty()) {
                            return;
                        }
                        for (int i = 0; i < list.size(); i++) {
                            if (i > 3) return;
                            tvList.get(i).setVisibility(View.VISIBLE);
                        }
                        categoryList = list;
                    }
                });
    }

    @Override
    public void onRefresh() {
        getTodyFree();
        getHomeList();
        getAdList();
        getCategory();
    }

    /**
     * 获取轮播图
     */
    private void getAdList() {
        RetrofitClient.getInstance().createApi().getAdList("1")
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

    @Override
    public void OnBannerClick(int position) {
        CoomonApi.toBrowser(getActivity(),bannerBeens.get(position - 1).getUrl());
//        startActivity(new Intent(getActivity(), WebViewActivity.class).putExtra("url", bannerBeens.get(position - 1).getUrl()));
    }

    static class ViewHolder {
        @Bind(R.id.banner)
        Banner banner;
        @Bind(R.id.tv_home_paihang)
        TextView tvPaihang;
        @Bind(R.id.img_home_fenlei)
        TextView tvFenlei;
        @Bind(R.id.img_home_dushi)
        TextView tvDushi;
        @Bind(R.id.img_home_xianyan)
        TextView tvXianyan;
        @Bind(R.id.img_home_qita)
        TextView tvQita;
        @Bind(R.id.tv_tj)
        TextView tvTj;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
