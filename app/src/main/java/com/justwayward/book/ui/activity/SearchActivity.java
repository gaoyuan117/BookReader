package com.justwayward.book.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.justwayward.book.R;
import com.justwayward.book.base.BaseRVActivity;
import com.justwayward.book.bean.HotSearchBean;
import com.justwayward.book.bean.SearchBean;
import com.justwayward.book.bean.SearchDetail;
import com.justwayward.book.component.AppComponent;
import com.justwayward.book.component.DaggerBookComponent;
import com.justwayward.book.manager.CacheManager;
import com.justwayward.book.retrofit.BaseObjObserver;
import com.justwayward.book.retrofit.HttpResult;
import com.justwayward.book.retrofit.RetrofitClient;
import com.justwayward.book.retrofit.RxUtils;
import com.justwayward.book.ui.adapter.AutoCompleteAdapter;
import com.justwayward.book.ui.adapter.SearchHistoryAdapter;
import com.justwayward.book.ui.contract.SearchContract;
import com.justwayward.book.ui.easyadapter.SearchAdapter;
import com.justwayward.book.ui.presenter.SearchPresenter;
import com.justwayward.book.view.TagColor;
import com.justwayward.book.view.TagGroup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/8/6.
 */
public class SearchActivity extends BaseRVActivity<SearchBean.DataBean> implements SearchContract.View {

    public static final String INTENT_QUERY = "query";

    public static void startActivity(Context context, String query) {
        context.startActivity(new Intent(context, SearchActivity.class)
                .putExtra(INTENT_QUERY, query));
    }

    @Bind(R.id.tvChangeWords)
    TextView mTvChangeWords;
    @Bind(R.id.tvChangeWords2)
    TextView mTvChangeWords2;
    @Bind(R.id.tag_group)
    TagGroup mTagGroup;
    @Bind(R.id.tag_group_tj)
    TagGroup mTagGroup2;
    @Bind(R.id.rootLayout)
    LinearLayout mRootLayout;
    @Bind(R.id.layoutHotWord)
    RelativeLayout mLayoutHotWord;
    @Bind(R.id.layoutHotWord2)
    RelativeLayout mLayoutHotWord2;
    @Bind(R.id.rlHistory)
    RelativeLayout rlHistory;
    @Bind(R.id.tvClear)
    TextView tvClear;
    @Bind(R.id.lvSearchHistory)
    ListView lvSearchHistory;

    @Inject
    SearchPresenter mPresenter;

    private List<String> tagList = new ArrayList<>();
    private List<String> tagList2 = new ArrayList<>();
    private int times = 0;

    private AutoCompleteAdapter mAutoAdapter;
    private List<String> mAutoList = new ArrayList<>();

    private SearchHistoryAdapter mHisAdapter;
    private List<String> mHisList = new ArrayList<>();
    private String key;
    private MenuItem searchMenuItem;
    private SearchView searchView;

    private ListPopupWindow mListPopupWindow;

    int hotIndex = 0;
    int page1 = 1;
    int page2 = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerBookComponent.builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    public void initToolBar() {
        mCommonToolbar.setTitle("");
        mCommonToolbar.setNavigationIcon(R.drawable.ab_back);
    }

    @Override
    public void initDatas() {
        key = getIntent().getStringExtra(INTENT_QUERY);

        mHisAdapter = new SearchHistoryAdapter(this, mHisList);
        lvSearchHistory.setAdapter(mHisAdapter);
        lvSearchHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                search(mHisList.get(position));
            }
        });

        lvSearchHistory.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteDialog(mHisList.get(position));
                return true;
            }
        });

        initSearchHistory();
        getSearchHot();
        getSearchRecommend();
    }

    @Override
    public void configViews() {
        initAdapter(SearchAdapter.class, false, false);

        initAutoList();

        mTagGroup.setOnTagClickListener(new TagGroup.OnTagClickListener() {
            @Override
            public void onTagClick(String tag) {
                search(tag);
            }
        });

        mTagGroup2.setOnTagClickListener(new TagGroup.OnTagClickListener() {
            @Override
            public void onTagClick(String tag) {
                search(tag);
            }
        });

        mTvChangeWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page1++;
                getSearchHot();
            }
        });

        mTvChangeWords2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page2++;
                getSearchRecommend();
            }
        });

        mPresenter.attachView(this);

    }

    private void initAutoList() {
        mAutoAdapter = new AutoCompleteAdapter(this, mAutoList);
        mListPopupWindow = new ListPopupWindow(this);
        mListPopupWindow.setAdapter(mAutoAdapter);
        mListPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mListPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mListPopupWindow.setAnchorView(mCommonToolbar);
        mListPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListPopupWindow.dismiss();
                TextView tv = (TextView) view.findViewById(R.id.tvAutoCompleteItem);
                String str = tv.getText().toString();
                search(str);
            }
        });
    }

    @Override
    public synchronized void showHotWordList(List<String> list) {
        visible(mTvChangeWords);
        tagList.clear();
        tagList.addAll(list);
        times = 0;

    }

    /**
     * 每次最多显示8个热搜词
     */
    private synchronized void showHotWord(int type) {

        int tagSize = 0;
        if (type == 1) {

            if (tagList.size() > 8) {
                tagSize = 8;
            } else {
                tagSize = tagList.size();
            }

            if (tagSize == 8) {
                visible(mTvChangeWords);
            }

            String[] tags = new String[tagSize];
            for (int j = 0; j < tagSize && j < tagList.size(); hotIndex++, j++) {
                tags[j] = tagList.get(hotIndex % tagList.size());
            }
            List<TagColor> colors = TagColor.getRandomColors(tagSize);

            mTagGroup.setTags(colors, tags);
        } else {
            if (tagList2.size() > 8) {
                tagSize = 8;
            } else {
                tagSize = tagList2.size();
            }

            if (tagSize == 8) {
                visible(mTvChangeWords2);
            }

            String[] tags = new String[tagSize];
            for (int j = 0; j < tagSize && j < tagList2.size(); hotIndex++, j++) {
                tags[j] = tagList2.get(hotIndex % tagList2.size());
            }
            List<TagColor> colors = TagColor.getRandomColors(tagSize);

            mTagGroup2.setTags(colors, tags);
        }

    }

    @Override
    public void showAutoCompleteList(List<String> list) {
        mAutoList.clear();
        mAutoList.addAll(list);

        if (!mListPopupWindow.isShowing()) {
            mListPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            mListPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            mListPopupWindow.show();
        }
        mAutoAdapter.notifyDataSetChanged();

    }

    @Override
    public void showSearchResultList(List<SearchDetail.SearchBooks> list) {
//        mAdapter.clear();
//        mAdapter.addAll(list);
//        mAdapter.notifyDataSetChanged();
        initSearchResult();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        searchMenuItem = menu.findItem(R.id.action_search);//在菜单中找到对应控件的item
        searchMenuItem.expandActionView();
        searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        searchView.setQueryHint("输入书名或作者名");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                key = query;
//                mPresenter.getSearchResultList(query);
                searchList(key);
                saveSearchHistory(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                if (TextUtils.isEmpty(newText)) {
//                    if (mListPopupWindow.isShowing())
//                        mListPopupWindow.dismiss();
//                    initTagGroup();
//                } else {
//                    mPresenter.getAutoCompleteList(newText);
//                }
                return false;
            }
        });
        search(key); // 外部调用搜索，则打开页面立即进行搜索
        MenuItemCompat.setOnActionExpandListener(searchMenuItem,
                new MenuItemCompat.OnActionExpandListener() {//设置打开关闭动作监听
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        initTagGroup();
                        return true;
                    }
                });
        return true;
    }

    /**
     * 保存搜索记录.不重复，最多保存20条
     *
     * @param query
     */
    private void saveSearchHistory(String query) {
        List<String> list = CacheManager.getInstance().getSearchHistory();
        if (list == null) {
            list = new ArrayList<>();
            list.add(query);
        } else {
            Iterator<String> iterator = list.iterator();
            while (iterator.hasNext()) {
                String item = iterator.next();
                if (TextUtils.equals(query, item)) {
                    iterator.remove();
                }
            }
            list.add(0, query);
        }
        int size = list.size();
        if (size > 20) { // 最多保存20条
            for (int i = size - 1; i >= 20; i--) {
                list.remove(i);
            }
        }
        CacheManager.getInstance().saveSearchHistory(list);
        initSearchHistory();
    }

    private void deleteHistory(String query){
        List<String> list = CacheManager.getInstance().getSearchHistory();

        list.remove(query);
        CacheManager.getInstance().saveSearchHistory(list);
        initSearchHistory();
    }

    private void initSearchHistory() {
        List<String> list = CacheManager.getInstance().getSearchHistory();
        mHisAdapter.clear();
        if (list != null && list.size() > 0) {
            tvClear.setEnabled(true);
            mHisAdapter.addAll(list);
        } else {
            tvClear.setEnabled(false);
        }
        mHisAdapter.notifyDataSetChanged();
    }

    /**
     * 展开SearchView进行查询
     *
     * @param key
     */
    private void search(String key) {
        if (!TextUtils.isEmpty(key)) {
            searchList(key);
            saveSearchHistory(key);
        }
    }

    private void initSearchResult() {
        gone(mTagGroup, mTagGroup2, mLayoutHotWord, mLayoutHotWord, rlHistory);
        visible(mRecyclerView);
        if (mListPopupWindow.isShowing())
            mListPopupWindow.dismiss();
    }

    private void initTagGroup() {
        visible(mTagGroup, mTagGroup2, mLayoutHotWord, mLayoutHotWord2, rlHistory);
        gone(mRecyclerView);
        if (mListPopupWindow.isShowing())
            mListPopupWindow.dismiss();
    }

    @Override
    public void onItemClick(int position) {
        SearchBean.DataBean data = mAdapter.getItem(position);
        BookDetailActivity.startActivity(this, data.getId() + "");
    }

    @OnClick(R.id.tvClear)
    public void clearSearchHistory() {
        CacheManager.getInstance().saveSearchHistory(null);
        initSearchHistory();
    }

    @Override
    public void showError() {
        loaddingError();
    }

    @Override
    public void complete() {
        mRecyclerView.setRefreshing(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    /**
     * 获取热搜
     */
    private void getSearchHot() {
        RetrofitClient.getInstance().createApi().getSearchHot(page1)
                .compose(RxUtils.<HttpResult<HotSearchBean>>io_main())
                .subscribe(new BaseObjObserver<HotSearchBean>(this) {
                    @Override
                    protected void onHandleSuccess(HotSearchBean hotSearchBean) {
                        tagList.clear();
                        if (hotSearchBean == null || hotSearchBean.getData().size() == 0) {
                            showToastMsg("暂时没有搜索热词了");
                            return;
                        }

                        for (int i = 0; i < hotSearchBean.getData().size(); i++) {
                            tagList.add(hotSearchBean.getData().get(i).getSearchword());
                        }
                        showHotWord(1);
                    }
                });
    }

    /**
     * 获取热门推荐
     */
    private void getSearchRecommend() {
        RetrofitClient.getInstance().createApi().getSearchRecommend(page2)
                .compose(RxUtils.<HttpResult<HotSearchBean>>io_main())
                .subscribe(new BaseObjObserver<HotSearchBean>(this) {
                    @Override
                    protected void onHandleSuccess(HotSearchBean hotSearchBean) {
                        tagList2.clear();
                        if (hotSearchBean == null || hotSearchBean.getData().size() == 0) {
                            showToastMsg("暂时没有热门推荐了");
                            return;
                        }

                        for (int i = 0; i < hotSearchBean.getData().size(); i++) {
                            tagList2.add(hotSearchBean.getData().get(i).getSearchword());
                        }
                        showHotWord(2);
                    }
                });
    }

    /**
     * 搜索
     *
     * @param world 关键词
     */
    private void searchList(String world) {
        RetrofitClient.getInstance().createApi().searchList(world)
                .compose(RxUtils.<HttpResult<SearchBean>>io_main())
                .subscribe(new BaseObjObserver<SearchBean>(this, "搜索中") {
                    @Override
                    protected void onHandleSuccess(SearchBean searchBean) {
                        mAdapter.clear();
                        if (searchBean.getData() == null || searchBean.getData().size() == 0) {
                            mAdapter.notifyDataSetChanged();
                            return;
                        }
                        mAdapter.addAll(searchBean.getData());
                        mAdapter.notifyDataSetChanged();
                        initSearchResult();
                    }
                });
    }

    /**
     * 显示删除对话框
     */
    private void showDeleteDialog(final String query){
        new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("是否删除该条搜索记录")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteHistory(query);
                    }
                }).create().show();
    }
}
