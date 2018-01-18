package com.justwayward.book.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mobad.feeds.BaiduNative;
import com.baidu.mobad.feeds.NativeErrorCode;
import com.baidu.mobad.feeds.NativeResponse;
import com.baidu.mobad.feeds.RequestParameters;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.justwayward.book.R;
import com.justwayward.book.ReaderApplication;
import com.justwayward.book.WebViewActivity;
import com.justwayward.book.api.CoomonApi;
import com.justwayward.book.base.BaseFragment;
import com.justwayward.book.bean.BannerBean;
import com.justwayward.book.bean.BookBean;
import com.justwayward.book.bean.BookShelfBean;
import com.justwayward.book.bean.BookShelfCache;
import com.justwayward.book.bean.BookShelfCacheDao;
import com.justwayward.book.bean.ChapterListBean;
import com.justwayward.book.bean.CommonBean;
import com.justwayward.book.bean.Recommend;
import com.justwayward.book.bean.support.DownloadMessage;
import com.justwayward.book.bean.support.DownloadProgress;
import com.justwayward.book.bean.support.DownloadQueue;
import com.justwayward.book.bean.support.RefreshCollectionListEvent;
import com.justwayward.book.component.AppComponent;
import com.justwayward.book.manager.CollectionsManager;
import com.justwayward.book.retrofit.BaseObjObserver;
import com.justwayward.book.retrofit.HttpResult;
import com.justwayward.book.retrofit.RetrofitClient;
import com.justwayward.book.retrofit.RxUtils;
import com.justwayward.book.service.DownloadBookService;
import com.justwayward.book.ui.activity.BookDetailActivity;
import com.justwayward.book.ui.activity.ReadActivity;
import com.justwayward.book.ui.activity.SearchActivity;
import com.justwayward.book.ui.adapter.BookShelfAdapter;
import com.justwayward.book.utils.AppUtils;
import com.justwayward.book.utils.LogUtils;
import com.justwayward.book.utils.MyImageLoader;
import com.justwayward.book.utils.SharedPreferencesUtil;
import com.justwayward.book.utils.ToastUtils;
import com.justwayward.book.view.DrawableCenterButton;
import com.justwayward.book.view.recyclerview.decoration.DividerDecoration;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.OnClick;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by gaoyuan on 2017/11/15.
 */

public class BookShelfFragment extends BaseFragment implements BaseQuickAdapter.OnItemLongClickListener, BaseQuickAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, OnBannerClickListener {

    @Bind(R.id.img_recommend_search)
    ImageView imgRecommendSearch;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.tvSelectAll)
    TextView tvSelectAll;
    @Bind(R.id.tvDelete)
    TextView tvDelete;
    @Bind(R.id.llBatchManagement)
    LinearLayout llBatchManagement;
    @Bind(R.id.banner)
    Banner mBanner;
    @Bind(R.id.tvTip)
    TextView tvTip;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refreshLayout;

    private View footVeiw;
    private BookShelfAdapter mAdapter;

    private List<String> bannerList = new ArrayList<>();
    private List<BookShelfBean.DataBean> mList = new ArrayList<>();
    private TextView tvFootAdd;
    public List<Boolean> list = new ArrayList<>();
    private boolean isSelectAll = false;
    private View emptyView;
    private BookShelfBean shelfBean;
    private List<BannerBean> bannerBeens;
    private BookShelfCacheDao shlfCacheDao;
    private RequestParameters requestParameters;
    private List<NativeResponse> nrAdList = new ArrayList<>();

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_book_shelf;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        footVeiw = LayoutInflater.from(activity).inflate(R.layout.foot_view_shelf, null);
        footVeiw.findViewById(R.id.tv_add_book).setOnClickListener(new View.OnClickListener() {//添加小说
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky("1");
            }
        });
        getAdList();
    }

    @Override
    public void attachView() {
        shlfCacheDao = ReaderApplication.getDaoInstant().getBookShelfCacheDao();
        emptyView = View.inflate(getActivity(), R.layout.bookshelf_empty_view, null);

        try {
            //获取本地缓存数据
            List<BookShelfCache> list = shlfCacheDao.queryBuilder().where(BookShelfCacheDao.Properties.Uid.eq(ReaderApplication.uid)).build().list();
            LogUtils.e("书架列表数据：" + list.size());
            if (list != null && list.size() > 0) {
                String js = list.get(0).getJs();
                shelfBean = new Gson().fromJson(js, BookShelfBean.class);
                mList.clear();
                mList.addAll(shelfBean.getData());
            }
        } catch (Exception e) {

        }
        mAdapter = new BookShelfAdapter(R.layout.item_recommend_list, mList, BookShelfFragment.this);
        mAdapter.addFooterView(footVeiw);
        mAdapter.setEmptyView(emptyView);

        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerDecoration itemDecoration = new DividerDecoration(ContextCompat.getColor(getActivity(), R.color.common_divider_narrow), 1, 0, 0);
        itemDecoration.setDrawLastItem(false);
        recyclerview.addItemDecoration(itemDecoration);
        recyclerview.setAdapter(mAdapter);
        refreshLayout.setOnRefreshListener(this);

        emptyView.findViewById(R.id.btnToAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky("1");
            }
        });

    }


    @Override
    public void initDatas() {
    }

    @Override
    public void configViews() {
        mAdapter.setOnItemLongClickListener(this);
        mAdapter.setOnItemClickListener(this);
        mBanner.setOnBannerClickListener(this);
    }


    @OnClick({R.id.img_recommend_search, R.id.tvSelectAll, R.id.tvCancel, R.id.tvDelete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_recommend_search://搜索
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            case R.id.tvSelectAll://全选
                selectAll();
                break;
            case R.id.tvCancel://取消
                goneBatchManagementAndRefreshUI();
                break;
            case R.id.tvDelete://删除

                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i)) {
                        Log.e("gy", "选中状态：" + mList.size() + "\n" + list.get(i));
                        sb.append(mList.get(i).getNovel_id() + ",");
                    }
                }

                if (TextUtils.isEmpty(sb.toString())) {
                    ToastUtils.showToast("请选择要删除的书");
                    return;
                }

                showDeleteCacheDialog(sb.toString());

                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        getList();
        //这样监听返回键有个缺点就是没有拦截Activity的返回监听，如果有更优方案可以改掉
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    if (isVisible(llBatchManagement)) {
                        goneBatchManagementAndRefreshUI();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
        //批量管理时，屏蔽长按事件
        if (isVisible(llBatchManagement)) return false;
        showLongClickDialog(position);
        return true;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (isVisible(llBatchManagement)) {
            goneBatchManagementAndRefreshUI();
            return;
        }

        if (refreshLayout.isRefreshing()) {
            return;
        }

        BookShelfBean.DataBean bean = mList.get(position);

        if (bean.getType() != null && bean.getType().equals("ad")) {
            NativeResponse nrAd = nrAdList.get(bean.getAd_position());
            nrAd.handleClick(view);
            return;
        }

        if (shelfBean.isMember_switch()) {//后台开启了会员模式
            if (shelfBean.isUser_member()) {//用户是会员
                toReadActivity(bean);
            } else {//不是会员
                CoomonApi.showBuyVipDialog(getActivity());
            }
        } else {//没有开启会员开关
            toReadActivity(bean);
        }
    }

    private void toReadActivity(BookShelfBean.DataBean bean) {
        ReadActivity.startActivity(getActivity(), bean.getTitle(), bean.getNovel_id() + "", true, bean.getPic(), bean.getAuthor(), bean.getDesc());

    }

    /**
     * 显示长按对话框
     *
     * @param position
     */
    private void showLongClickDialog(final int position) {
        final BookShelfBean.DataBean bean = mList.get(position);
        String[] items = getResources().getStringArray(R.array.recommend_item_long_click_choice);
        DialogInterface.OnClickListener listener;

        listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0://置顶、取消置顶
                        if (AppUtils.isTrue(bean.getIs_top())) {
                            cancelTop(bean.getId() + "");
                        } else {
                            setTop(bean.getId() + "");
                        }
                        break;
                    case 1://书籍详情
                        BookDetailActivity.startActivity(activity, mAdapter.getItem(position).getNovel_id() + "");
                        break;
                    case 2://缓存全本
                        getChapterList(mAdapter.getItem(position).getNovel_id() + "");
                        break;
                    case 3://删除
                        showDeleteCacheDialog(bean.getNovel_id() + "");
                        break;
                    case 4:
                        //批量管理
                        showBatchManagementLayout();
                        break;
                    default:
                        break;
                }
                dialog.dismiss();
            }
        };

        if (AppUtils.isTrue(mList.get(position).getIs_top()))
            items[0] = getString(R.string.cancle_top);
        new AlertDialog.Builder(activity)
                .setTitle(mList.get(position).getTitle())
                .setItems(items, listener)
                .setNegativeButton(null, null)
                .create().show();
    }

    /**
     * 获取轮播图
     */
    private void getAdList() {
        RetrofitClient.getInstance().createApi().getAdList("2")
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

    /**
     * 获取书架
     */
    private void getList() {
        RetrofitClient.getInstance().createApi().getList(ReaderApplication.token)
                .compose(RxUtils.<HttpResult<BookShelfBean>>io_main())
                .subscribe(new BaseObjObserver<BookShelfBean>(getActivity(), refreshLayout, false) {
                    @Override
                    protected void onHandleSuccess(BookShelfBean bookShelfBean) {
                        shelfBean = bookShelfBean;
                        mList.clear();
                        list.clear();
                        if (bookShelfBean.getData() == null || bookShelfBean.getData().size() == 0) {
                            mAdapter.notifyDataSetChanged();
                            return;
                        }

                        mList.addAll(bookShelfBean.getData());

                        for (int i = 0; i < mList.size(); i++) {
                            list.add(false);
                        }

                        mAdapter.notifyDataSetChanged();

                        fetchAd();
                        if (isVisible(llBatchManagement)) {
                            //批量管理完成后，隐藏批量管理布局并刷新页面
                            goneBatchManagementAndRefreshUI();
                        }

                        ReaderApplication.isvip = bookShelfBean.isUser_member();

                        saveShelf(new Gson().toJson(bookShelfBean));
                    }
                });
    }

    /**
     * 保存书架到本地
     */
    private void saveShelf(String json) {
        if (TextUtils.isEmpty(ReaderApplication.uid)) return;

        BookShelfCache cache = new BookShelfCache();
        cache.setUid(ReaderApplication.uid);
        cache.setJs(json);
        cache.setId(1l);
        shlfCacheDao.insertOrReplace(cache);
    }

    /**
     * 设置置顶
     *
     * @param id
     */
    private void setTop(String id) {
        RetrofitClient.getInstance().createApi().setTop(ReaderApplication.token, id)
                .compose(RxUtils.<HttpResult<CommonBean>>io_main())
                .subscribe(new BaseObjObserver<CommonBean>(getActivity(), "设置中") {
                    @Override
                    protected void onHandleSuccess(CommonBean commonBean) {
                        ToastUtils.showToast("置顶成功");
                        getList();
                    }
                });
    }

    /**
     * 取消置顶
     *
     * @param id
     */
    private void cancelTop(String id) {
        RetrofitClient.getInstance().createApi().cancelTop(ReaderApplication.token, id)
                .compose(RxUtils.<HttpResult<CommonBean>>io_main())
                .subscribe(new BaseObjObserver<CommonBean>(getActivity(), "设置中") {
                    @Override
                    protected void onHandleSuccess(CommonBean commonBean) {
                        ToastUtils.showToast("取消置顶成功");
                        getList();
                    }
                });
    }

    /**
     * 移除书架
     */
    private void delNovel(String ids) {
        RetrofitClient.getInstance().createApi().delBook(ReaderApplication.token, ids)
                .compose(RxUtils.<HttpResult<CommonBean>>io_main())
                .subscribe(new BaseObjObserver<CommonBean>(getContext(), "删除中") {
                    @Override
                    protected void onHandleSuccess(CommonBean commonBean) {
                        getList();
                    }
                });
    }

    /**
     * 显示批量管理布局
     */
    private void showBatchManagementLayout() {
        visible(llBatchManagement);
        mAdapter.setCheckShow(true);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 显示删除本地缓存对话框
     */
    private void showDeleteCacheDialog(final String ids) {
        final boolean selected[] = {true};
        new AlertDialog.Builder(activity)
                .setTitle(activity.getString(R.string.remove_selected_book))
                .setMultiChoiceItems(new String[]{activity.getString(R.string.delete_local_cache)}, selected,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                selected[0] = isChecked;
                            }
                        })
                .setPositiveButton(activity.getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        delNovel(ids);
                        goneBatchManagementAndRefreshUI();

                    }
                })
                .setNegativeButton(activity.getString(R.string.cancel), null)
                .create().show();
    }

    /**
     * 隐藏批量管理布局并刷新页面
     */
    public void goneBatchManagementAndRefreshUI() {
        if (mAdapter == null) return;
        gone(llBatchManagement);
        mAdapter.setCheckShow(false);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 全选／取消全选
     */
    public void selectAll() {
        isSelectAll = !isSelectAll;
        tvSelectAll.setText(isSelectAll ? activity.getString(R.string.cancel_selected_all) : activity.getString(R.string.selected_all));

        for (int i = 0; i < list.size(); i++) {
            list.set(i, isSelectAll);
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 获取章节列表，缓存全本
     *
     * @param bookId
     */
    private void getChapterList(final String bookId) {
        RetrofitClient.getInstance().createApi().getChapterList(bookId)
                .compose(RxUtils.<HttpResult<List<ChapterListBean>>>io_main())
                .subscribe(new BaseObjObserver<List<ChapterListBean>>(mContext) {
                    @Override
                    protected void onHandleSuccess(List<ChapterListBean> listBeen) {
                        if (listBeen == null || listBeen.size() == 0) {
                            return;
                        }
                        DownloadBookService.post(new DownloadQueue(bookId, listBeen, 1, listBeen.size()));

                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showDownProgress(DownloadProgress progress) {

        if (!isVisible(tvTip))
            showTipView(progress.message);
        else
            tvTip.setText(progress.message);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void downloadMessage(final DownloadMessage msg) {
        tvTip.setText(msg.message);
        if (msg.isComplete) {
            hideTipView(2200);
        }
    }

    public void hideTipView(long delayMillis) {
        tvTip.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                        0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                        -1.0f);
                mHiddenAction.setDuration(500);
                tvTip.startAnimation(mHiddenAction);
                tvTip.setVisibility(View.GONE);
            }
        }, delayMillis);
    }

    public void showTipView(String tip) {
        tvTip.setText(tip);
        Animation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(500);
        tvTip.startAnimation(mShowAction);
        tvTip.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRefresh() {
        gone(llBatchManagement);
        getAdList();
        getList();
    }


    @Override
    public void OnBannerClick(int position) {
        CoomonApi.toBrowser(getActivity(), bannerBeens.get(position - 1).getUrl());
    }

    public void fetchAd() {
        /**
         * Step 1. 创建BaiduNative对象，参数分别为： 上下文context，广告位ID, BaiduNativeNetworkListener监听（监听广告请求的成功与失败）
         * 注意：请将YOUR_AD_PALCE_ID替换为自己的广告位ID
         */
        BaiduNative baidu = new BaiduNative(getActivity(), "2058628", new BaiduNative.BaiduNativeNetworkListener() {

            @Override
            public void onNativeFail(NativeErrorCode arg0) {
                Log.e("gy", "onNativeFail:" + arg0.name());
                for (int i = 0; i < mList.size(); i++) {
                    list.add(false);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNativeLoad(List<NativeResponse> arg0) {
                try {

                    if (arg0 != null && arg0.size() > 0) {
                        nrAdList = arg0;
                        if (mList.size() > 0 && mList.size() < 5) {//书架的数量在0-5之间，添加一个广告
                            LogUtils.e("书架的数量在0-5之间，添加一个广告");
                            Random rand = new Random();
                            int num = rand.nextInt(arg0.size());
                            BookShelfBean.DataBean bean = new BookShelfBean.DataBean();
                            bean.setType("ad");
                            bean.setDesc(arg0.get(num).getDesc());
                            bean.setPic(arg0.get(num).getIconUrl());
                            bean.setTitle(arg0.get(num).getTitle());
                            bean.setAd_position(num);
                            if (mList.size() == 1) {
                                mList.add(bean);
                            } else {
                                mList.add(1, bean);
                            }
                        } else if (mList.size() >= 5) {//书架的数量大于5，添加2个广告
                            LogUtils.e("书架的数量大于5，添加2个广告");
                            if (arg0.size() > 1) {//两个广告
                                Random rand = new Random();
                                int num1 = rand.nextInt(arg0.size());
                                int num2 = rand.nextInt(arg0.size());

                                while (num1 == num2) {
                                    num1 = rand.nextInt(arg0.size());
                                    num2 = rand.nextInt(arg0.size());
                                }

                                LogUtils.e("num1：" + num1 + "\n num2：" + num2);

                                BookShelfBean.DataBean bean = new BookShelfBean.DataBean();
                                bean.setType("ad");
                                bean.setDesc(arg0.get(num1).getDesc());
                                bean.setPic(arg0.get(num1).getIconUrl());
                                bean.setTitle(arg0.get(num1).getTitle());
                                bean.setAd_position(num1);
                                mList.add(1, bean);

                                BookShelfBean.DataBean bean2 = new BookShelfBean.DataBean();
                                bean2.setType("ad");
                                bean2.setDesc(arg0.get(num2).getDesc());
                                bean2.setPic(arg0.get(num2).getIconUrl());
                                bean2.setTitle(arg0.get(num2).getTitle());
                                bean2.setAd_position(num2);
                                mList.add(3, bean2);
                            } else {
                                Random rand = new Random();
                                int num1 = rand.nextInt(arg0.size());
                                BookShelfBean.DataBean bean = new BookShelfBean.DataBean();
                                bean.setType("ad");
                                bean.setDesc(arg0.get(num1).getDesc());
                                bean.setPic(arg0.get(num1).getIconUrl());
                                bean.setTitle(arg0.get(num1).getTitle());

                                mList.add(0, bean);
                            }

                        }

                        for (int i = 0; i < mList.size(); i++) {
                            list.add(false);
                        }
                        mAdapter.notifyDataSetChanged();

                        LogUtils.e("集合长度：" + mList.size());
                    }

                } catch (Exception e) {
                    for (int i = 0; i < mList.size(); i++) {
                        list.add(false);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

        /**
         * Step 2. 创建requestParameters对象，并将其传给baidu.makeRequest来请求广告
         */
        // 用户点击下载类广告时，是否弹出提示框让用户选择下载与否
        if (requestParameters == null) {
            requestParameters = new RequestParameters.Builder()
                    .downloadAppConfirmPolicy(
                            RequestParameters.DOWNLOAD_APP_CONFIRM_ONLY_MOBILE).build();
        }

        baidu.makeRequest(requestParameters);
    }
}
