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

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.justwayward.book.R;
import com.justwayward.book.ReaderApplication;
import com.justwayward.book.base.BaseRVFragment;
import com.justwayward.book.bean.BannerBean;
import com.justwayward.book.bean.BookMixAToc;
import com.justwayward.book.bean.BookShelfBean;
import com.justwayward.book.bean.Recommend;
import com.justwayward.book.bean.support.DownloadMessage;
import com.justwayward.book.bean.support.DownloadProgress;
import com.justwayward.book.bean.support.DownloadQueue;
import com.justwayward.book.bean.support.RefreshCollectionListEvent;
import com.justwayward.book.bean.support.UserSexChooseFinishedEvent;
import com.justwayward.book.component.AppComponent;
import com.justwayward.book.component.DaggerMainComponent;
import com.justwayward.book.manager.CollectionsManager;
import com.justwayward.book.retrofit.BaseObjObserver;
import com.justwayward.book.retrofit.HttpResult;
import com.justwayward.book.retrofit.RetrofitClient;
import com.justwayward.book.retrofit.RxUtils;
import com.justwayward.book.service.DownloadBookService;
import com.justwayward.book.ui.activity.BookDetailActivity;
import com.justwayward.book.ui.activity.MainActivity;
import com.justwayward.book.ui.activity.SearchActivity;
import com.justwayward.book.ui.contract.RecommendContract;
import com.justwayward.book.ui.easyadapter.RecommendAdapter;
import com.justwayward.book.ui.presenter.RecommendPresenter;
import com.justwayward.book.utils.MyImageLoader;
import com.justwayward.book.view.recyclerview.adapter.RecyclerArrayAdapter;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class RecommendFragment extends BaseRVFragment<RecommendPresenter, BookShelfBean.DataBean> implements RecommendContract.View, RecyclerArrayAdapter.OnItemLongClickListener {

    @Bind(R.id.llBatchManagement)
    LinearLayout llBatchManagement;
    @Bind(R.id.tvSelectAll)
    TextView tvSelectAll;
    @Bind(R.id.tvDelete)
    TextView tvDelete;
    @Bind(R.id.img_recommend_search)
    ImageView imgSearch;

    Banner mBanner;

    private List<String> bannerList;
    private boolean isSelectAll = false;

    private List<BookMixAToc.mixToc.Chapters> chaptersList = new ArrayList<>();
    private View headView;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_recommend;
    }

    @Override
    public void initDatas() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void configViews() {
        bannerList = new ArrayList<>();
        initAdapter(RecommendAdapter.class, true, false);
        mAdapter.setOnItemLongClickListener(this);
        mAdapter.addFooter(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                View headerView = LayoutInflater.from(activity).inflate(R.layout.foot_view_shelf, parent, false);
                return headerView;
            }

            @Override
            public void onBindView(View headerView) {
                headerView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MainActivity) activity).setCurrentItem(2);
                    }
                });
            }
        });
        mRecyclerView.getEmptyView().findViewById(R.id.btnToAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) activity).setCurrentItem(2);
            }
        });


        mAdapter.addHeader(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                headView = LayoutInflater.from(activity).inflate(R.layout.layout_banner, null);
                mBanner = (Banner) headView.findViewById(R.id.banner);
                getAdList();
                return headView;
            }

            @Override
            public void onBindView(View headerView) {

            }
        });
        onRefresh();
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerMainComponent.builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    public void showRecommendList(List<Recommend.RecommendBooks> list) {
//        mAdapter.clear();
//        mAdapter.addAll(list);
//        //推荐列表默认加入收藏
//        for (Recommend.RecommendBooks bean : list) {
//            //TODO 此处可优化：批量加入收藏->加入前需先判断是否收藏过
//            CollectionsManager.getInstance().add(bean);
//        }
    }

    @Override
    public void showBookToc(String bookId, List<BookMixAToc.mixToc.Chapters> list) {
        chaptersList.clear();
        chaptersList.addAll(list);
//        DownloadBookService.post(new DownloadQueue(bookId, list, 1, list.size()));
        dismissDialog();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void downloadMessage(final DownloadMessage msg) {
        mRecyclerView.setTipViewText(msg.message);
        if (msg.isComplete) {
            mRecyclerView.hideTipView(2200);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showDownProgress(DownloadProgress progress) {
        mRecyclerView.setTipViewText(progress.message);
    }

    @Override
    public void onItemClick(int position) {
        if (isVisible(llBatchManagement)) //批量管理时，屏蔽点击事件
            return;
//        ReadActivity.startActivity(activity, mAdapter.getItem(position), mAdapter.getItem(position).getNovel_id()+"");
    }

    @Override
    public boolean onItemLongClick(int position) {
        //批量管理时，屏蔽长按事件
        if (isVisible(llBatchManagement)) return false;
        showLongClickDialog(position);
        return false;
    }



    /**
     * 显示长按对话框
     *
     * @param position
     */
    private void showLongClickDialog(final int position) {
//        final boolean isTop = CollectionsManager.getInstance().isTop(mAdapter.getItem(position).getId());
        String[] items;
        DialogInterface.OnClickListener listener;
//        if (mAdapter.getItem(position).isFromSD) {
        if (false) {
//            items = getResources().getStringArray(R.array.recommend_item_long_click_choice_local);
//            listener = new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    switch (which) {
//                        case 0:
//                            //置顶、取消置顶
//                            CollectionsManager.getInstance().top(mAdapter.getItem(position)._id, !isTop);
//                            break;
//                        case 1:
//                            //删除
//                            List<Recommend.RecommendBooks> removeList = new ArrayList<>();
//                            removeList.add(mAdapter.getItem(position));
//                            showDeleteCacheDialog(removeList);
//                            break;
//                        case 2:
//                            //批量管理
//                            showBatchManagementLayout();
//                            break;
//                        default:
//                            break;
//                    }
//                    dialog.dismiss();
//                }
//            };
        } else {
            items = getResources().getStringArray(R.array.recommend_item_long_click_choice);
            listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            //置顶、取消置顶
//                            CollectionsManager.getInstance().top(mAdapter.getItem(position)._id, !isTop);
                            break;
                        case 1:
                            //书籍详情
                            BookDetailActivity.startActivity(activity,
                                    mAdapter.getItem(position).getNovel_id()+"");
                            break;
                        case 2:
                            //移入养肥区
                            mRecyclerView.showTipViewAndDelayClose("正在拼命开发中...");
                            break;
                        case 3:
                            //缓存全本
//                            if (mAdapter.getItem(position).isFromSD) {
//                                mRecyclerView.showTipViewAndDelayClose("本地文件不支持该选项哦");
//                            } else {
//                                showDialog();
                                mPresenter.getTocList(mAdapter.getItem(position).getNovel_id()+"");
//                            }
                            break;
                        case 4:
                            //删除
                            List<Recommend.RecommendBooks> removeList = new ArrayList<>();
//                            removeList.add(mAdapter.getItem(position).getNovel_id()+"");
                            showDeleteCacheDialog(removeList);
                            break;
                        case 5:
                            //批量管理
                            showBatchManagementLayout();
                            break;
                        default:
                            break;
                    }
                    dialog.dismiss();
                }
            };
        }
//        if (isTop) items[0] = getString(R.string.cancle_top);
//        new AlertDialog.Builder(activity)
//                .setTitle(mAdapter.getItem(position).title)
//                .setItems(items, listener)
//                .setNegativeButton(null, null)
//                .create().show();
    }

    /**
     * 显示删除本地缓存对话框
     *
     * @param removeList
     */
    private void showDeleteCacheDialog(final List<Recommend.RecommendBooks> removeList) {
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
                        new AsyncTask<String, String, String>() {
                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                showDialog();
                            }

                            @Override
                            protected String doInBackground(String... params) {
                                CollectionsManager.getInstance().removeSome(removeList, selected[0]);
                                return null;
                            }

                            @Override
                            protected void onPostExecute(String s) {
                                super.onPostExecute(s);
                                mRecyclerView.showTipViewAndDelayClose("成功移除书籍");
                                for (Recommend.RecommendBooks bean : removeList) {
//                                    mAdapter.remove(bean);
                                }
                                if (isVisible(llBatchManagement)) {
                                    //批量管理完成后，隐藏批量管理布局并刷新页面
                                    goneBatchManagementAndRefreshUI();
                                }
                                hideDialog();
                            }
                        }.execute();

                    }
                })
                .setNegativeButton(activity.getString(R.string.cancel), null)
                .create().show();
    }

    /**
     * 隐藏批量管理布局并刷新页面
     */
    public void goneBatchManagementAndRefreshUI() {
//        if (mAdapter == null) return;
//        gone(llBatchManagement);
//        for (Recommend.RecommendBooks bean :
//                mAdapter.getAllData()) {
//            bean.showCheckBox = false;
//        }
//        mAdapter.notifyDataSetChanged();
    }

    /**
     * 显示批量管理布局
     */
    private void showBatchManagementLayout() {
//        visible(llBatchManagement);
//        for (Recommend.RecommendBooks bean : mAdapter.getAllData()) {
//            bean.showCheckBox = true;
//        }
//        mAdapter.notifyDataSetChanged();
    }


    @OnClick(R.id.tvSelectAll)
    public void selectAll() {
//        isSelectAll = !isSelectAll;
//        tvSelectAll.setText(isSelectAll ? activity.getString(R.string.cancel_selected_all) : activity.getString(R.string.selected_all));
//        for (Recommend.RecommendBooks bean : mAdapter.getAllData()) {
//            bean.isSeleted = isSelectAll;
//        }
//        mAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.tvDelete})
    public void delete() {
//        List<Recommend.RecommendBooks> removeList = new ArrayList<>();
//        for (Recommend.RecommendBooks bean : mAdapter.getAllData()) {
//            if (bean.isSeleted) removeList.add(bean);
//        }
//        if (removeList.isEmpty()) {
//            mRecyclerView.showTipViewAndDelayClose(activity.getString(R.string.has_not_selected_delete_book));
//        } else {
//            showDeleteCacheDialog(removeList);
//        }
    }

    @OnClick({R.id.img_recommend_search})
    public void search() {
        startActivity(new Intent(getActivity(), SearchActivity.class));
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        StackTraceElement stack[] = (new Throwable()).getStackTrace();

        boolean hasRefBookShelfInCallStack = false;
        boolean isMRefresh = false;
        for (int i = 0; i < stack.length; i++) {
            StackTraceElement ste = stack[i];
            if (ste.getMethodName().equals("pullSyncBookShelf")) {
                hasRefBookShelfInCallStack = true;
            }
            if (ste.getMethodName().equals("onAnimationEnd") && ste.getFileName().equals("SwipeRefreshLayout.java")) {
                isMRefresh = true;
            }
        }

        if (!hasRefBookShelfInCallStack && isMRefresh) {
//            ((Main2Activity) activity).pullSyncBookShelf();
            return;
        }


        gone(llBatchManagement);
        List<Recommend.RecommendBooks> data = CollectionsManager.getInstance().getCollectionListBySort();
        mAdapter.clear();
//        mAdapter.addAll(data);
        //不加下面这句代码会导致，添加本地书籍的时候，部分书籍添加后直接崩溃
        //报错：Scrapped or attached views may not be recycled. isScrap:false isAttached:true
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setRefreshing(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RefreshCollectionList(RefreshCollectionListEvent event) {
        mRecyclerView.setRefreshing(true);
        onRefresh();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void UserSexChooseFinished(UserSexChooseFinishedEvent event) {
        //首次进入APP，选择性别后，获取推荐列表
        mPresenter.getRecommendList();
    }

    @Override
    public void showError() {
        loaddingError();
        dismissDialog();
    }

    @Override
    public void complete() {
        mRecyclerView.setRefreshing(false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!getUserVisibleHint()) {
            goneBatchManagementAndRefreshUI();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
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
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    private boolean isForeground() {
        ActivityManager am = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (MainActivity.class.getName().contains(cpn.getClassName())) {
                return true;
            }
        }

        return false;
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

    private void getList(){
        RetrofitClient.getInstance().createApi().getList(ReaderApplication.token)
                .compose(RxUtils.<HttpResult<BookShelfBean>>io_main())
                .subscribe(new BaseObjObserver<BookShelfBean>(getActivity()) {
                    @Override
                    protected void onHandleSuccess(BookShelfBean bookShelfBean) {
                        mAdapter.clear();
                        mAdapter.addAll(bookShelfBean.getData());
                        //不加下面这句代码会导致，添加本地书籍的时候，部分书籍添加后直接崩溃
                        //报错：Scrapped or attached views may not be recycled. isScrap:false isAttached:true
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.setRefreshing(false);
                    }
                });
    }

}
