package com.justwayward.book.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobad.feeds.BaiduNative;
import com.baidu.mobad.feeds.NativeErrorCode;
import com.baidu.mobad.feeds.NativeResponse;
import com.baidu.mobad.feeds.RequestParameters;
import com.bumptech.glide.Glide;
import com.justwayward.book.R;
import com.justwayward.book.ReaderApplication;
import com.justwayward.book.api.CoomonApi;
import com.justwayward.book.base.BaseActivity;
import com.justwayward.book.bean.BookDetailBean;
import com.justwayward.book.bean.BookShelfBean;
import com.justwayward.book.bean.CommonBean;
import com.justwayward.book.bean.HotReview;
import com.justwayward.book.bean.Recommend;
import com.justwayward.book.bean.RecommendBookList;
import com.justwayward.book.bean.support.RefreshCollectionIconEvent;
import com.justwayward.book.common.OnRvItemClickListener;
import com.justwayward.book.component.AppComponent;
import com.justwayward.book.component.DaggerBookComponent;
import com.justwayward.book.retrofit.BaseObjObserver;
import com.justwayward.book.retrofit.HttpResult;
import com.justwayward.book.retrofit.RetrofitClient;
import com.justwayward.book.retrofit.RxUtils;
import com.justwayward.book.ui.activity.login.LoginActivity;
import com.justwayward.book.ui.adapter.HotReviewAdapter;
import com.justwayward.book.ui.adapter.LikeBookListAdapter;
import com.justwayward.book.ui.adapter.RecommendBookListAdapter;
import com.justwayward.book.ui.contract.BookDetailContract;
import com.justwayward.book.ui.presenter.BookDetailPresenter;
import com.justwayward.book.utils.AppUtils;
import com.justwayward.book.utils.FormatUtils;
import com.justwayward.book.utils.LogUtils;
import com.justwayward.book.utils.ToastUtils;
import com.justwayward.book.view.DrawableCenterButton;
import com.justwayward.book.view.TagColor;
import com.justwayward.book.view.TagGroup;
import com.yuyh.easyadapter.glide.GlideRoundTransform;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by lfh on 2016/8/6.
 */
public class BookDetailActivity extends BaseActivity implements BookDetailContract.View, OnRvItemClickListener<Object> {

    public static String INTENT_BOOK_ID = "bookId";
    private BookDetailBean bean;
    private RequestParameters requestParameters;

    public static void startActivity(Context context, String bookId) {
        context.startActivity(new Intent(context, BookDetailActivity.class)
                .putExtra(INTENT_BOOK_ID, bookId));
    }

    @Bind(R.id.ivBookCover)
    ImageView mIvBookCover;
    @Bind(R.id.tvBookListTitle)
    TextView mTvBookTitle;
    @Bind(R.id.tvBookListAuthor)
    TextView mTvAuthor;
    @Bind(R.id.tvCatgory)
    TextView mTvCatgory;
    @Bind(R.id.tvWordCount)
    TextView mTvWordCount;
    @Bind(R.id.tvLatelyUpdate)
    TextView mTvLatelyUpdate;
    @Bind(R.id.btnRead)
    DrawableCenterButton mBtnRead;
    @Bind(R.id.btnJoinCollection)
    DrawableCenterButton mBtnJoinCollection;
    @Bind(R.id.tvLatelyFollower)
    TextView mTvLatelyFollower;
    @Bind(R.id.tvRetentionRatio)
    TextView mTvRetentionRatio;
    @Bind(R.id.tvSerializeWordCount)
    TextView mTvSerializeWordCount;
    @Bind(R.id.tag_group)
    TagGroup mTagGroup;
    @Bind(R.id.tvlongIntro)
    TextView mTvlongIntro;
    @Bind(R.id.tvMoreReview)
    TextView mTvMoreReview;
    @Bind(R.id.rvHotReview)
    RecyclerView mRvHotReview;
    @Bind(R.id.rlCommunity)
    RelativeLayout mRlCommunity;
    @Bind(R.id.tvCommunity)
    TextView mTvCommunity;
    @Bind(R.id.tvHelpfulYes)
    TextView mTvPostCount;
    @Bind(R.id.tvRecommendBookList)
    TextView mTvRecommendBookList;
    @Bind(R.id.tvLikeBookList)
    TextView tvLikeBookList;
    @Bind(R.id.rvLikeBoookList)
    RecyclerView rvLikeBoookList;
    @Bind(R.id.tvAdTitle)
    TextView tvAdTitle;
    @Bind(R.id.tvAdDes)
    TextView tvAdDes;
    @Bind(R.id.imgAd)
    ImageView imgAd;
    @Bind(R.id.rlAd)
    RelativeLayout rlAd;

    @Bind(R.id.rvRecommendBoookList)
    RecyclerView mRvRecommendBoookList;

    @Inject
    BookDetailPresenter mPresenter;

    private List<String> tagList = new ArrayList<>();
    private int times = 0;

    private HotReviewAdapter mHotReviewAdapter;
    private List<BookDetailBean.CommentListBean> mHotReviewList = new ArrayList<>();
    private RecommendBookListAdapter mRecommendBookListAdapter;
    private LikeBookListAdapter mLikeBookListAdapter;
    private List<BookDetailBean.RecommendListBean> mRecommendBookList = new ArrayList<>();
    private List<BookDetailBean.GuestListBean> mLikeBookList = new ArrayList<>();
    private String bookId;

    private boolean collapseLongIntro = true;
    private Recommend.RecommendBooks recommendBooks;
    private boolean isJoinedCollections = false;
    private int is_bookshelf = 0;

    @Override
    public int getLayoutId() {
        return R.layout.activity_book_detail;
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
        mCommonToolbar.setNavigationIcon(R.drawable.ab_back);
        mCommonToolbar.setTitle(R.string.book_detail);
    }

    @Override
    public void initDatas() {
        bookId = getIntent().getStringExtra(INTENT_BOOK_ID);
        Log.e("gy", "book_id：" + bookId);
        EventBus.getDefault().register(this);
        fetchAd();

    }

    @Override
    public void configViews() {
        mRvHotReview.setHasFixedSize(true);
        mRvHotReview.setLayoutManager(new LinearLayoutManager(this));
        mHotReviewAdapter = new HotReviewAdapter(mContext, mHotReviewList, this);
        mRvHotReview.setAdapter(mHotReviewAdapter);

        mRvRecommendBoookList.setHasFixedSize(true);
        mRvRecommendBoookList.setLayoutManager(new LinearLayoutManager(this));
        GridLayoutManager manager = new GridLayoutManager(this, 4);

        rvLikeBoookList.setLayoutManager(manager);
        mRecommendBookListAdapter = new RecommendBookListAdapter(mContext, mRecommendBookList, this);
        mLikeBookListAdapter = new LikeBookListAdapter(mContext, mLikeBookList, this);
        mRvRecommendBoookList.setAdapter(mRecommendBookListAdapter);
        rvLikeBoookList.setAdapter(mLikeBookListAdapter);

//        mTagGroup.setOnTagClickListener(new TagGroup.OnTagClickListener() {
//            @Override
//            public void onTagClick(String tag) {
//                startActivity(new Intent(BookDetailActivity.this, BooksByTagActivity.class)
//                        .putExtra("tag", tag));
//            }
//        });

        mPresenter.attachView(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getBookDetail(bookId);
    }

    @Override
    public void showBookDetail(BookDetailBean data) {
        bean = data;
        Glide.with(mContext)
                .load(data.getPic())
                .placeholder(R.drawable.cover_default)
                .transform(new GlideRoundTransform(mContext))
                .into(mIvBookCover);

        String percent = "0";
        if (bean.getCollect_num() != 0 && bean.getView_num() != 0) {
            double d = (bean.getCollect_num() * 100 / bean.getView_num());
            percent = String.format("%.0f", d);
        }


        mTvBookTitle.setText(data.getTitle());
        mTvAuthor.setText(String.format(getString(R.string.book_detail_author), data.getAuthor()));
        mTvCatgory.setText(String.format(getString(R.string.book_detail_category), data.getCategory_name()));
        mTvWordCount.setText(data.getWord_num());
        mTvLatelyUpdate.setText(FormatUtils.getDescriptionTimeFromDateString(data.getRead_time() + "000"));
        mTvLatelyFollower.setText(String.valueOf(data.getView_num()));
        mTvRetentionRatio.setText(TextUtils.isEmpty(data.getCollect_num() + "") ?
                "-" : String.format(getString(R.string.book_detail_retention_ratio),
                percent + ""));
//        mTvSerializeWordCount.setText(data.getDay_num() < 0 ? "-" :
//                String.valueOf(data.getDay_num()));

        mTvSerializeWordCount.setText(data.getDay_num());
        tagList.clear();
        String labels = data.getLabels();
        if (!TextUtils.isEmpty(labels)) {
            String[] split = labels.split(",");
            for (int i = 0; i < split.length; i++) {
                tagList.add(split[i]);
                showHotWord();
            }
        }
        times = 0;

        mTvlongIntro.setText(data.getDesc());
//        mTvCommunity.setText(String.format(getString(R.string.book_detail_community), data.title));
//        mTvPostCount.setText(String.format(getString(R.string.book_detail_post_count), data.postCount));

        recommendBooks = new Recommend.RecommendBooks();
        recommendBooks.title = data.getTitle();
        recommendBooks._id = data.getId() + "";
        recommendBooks.cover = data.getPic();
//        recommendBooks.lastChapter = data.lastChapter;
//        recommendBooks.updated = data.updated;

        is_bookshelf = data.getIs_bookshelf();

        refreshCollectionIcon();

        if (data.getComment_list() != null) {
            mHotReviewList.clear();
            mHotReviewList.addAll(data.getComment_list());
            mHotReviewAdapter.notifyDataSetChanged();
        }

        if (!data.getRecommend_list().isEmpty()) {
            mTvRecommendBookList.setVisibility(View.VISIBLE);
            mRecommendBookList.clear();
            mRecommendBookList.addAll(data.getRecommend_list());
            mRecommendBookListAdapter.notifyDataSetChanged();
        }

        if (!data.getGuest_list().isEmpty()) {
            tvLikeBookList.setVisibility(View.VISIBLE);
            mLikeBookList.clear();
            if (data.getGuest_list().size() > 4) {
                mLikeBookList.add(data.getGuest_list().get(0));
                mLikeBookList.add(data.getGuest_list().get(1));
                mLikeBookList.add(data.getGuest_list().get(2));
                mLikeBookList.add(data.getGuest_list().get(3));
            } else {
                mLikeBookList.addAll(data.getGuest_list());
            }
            mLikeBookListAdapter.notifyDataSetChanged();
        }

    }

    /**
     * 刷新收藏图标
     */
    private void refreshCollectionIcon() {
        if (is_bookshelf == 0) {
            initCollection(true);
        } else {
            initCollection(false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RefreshCollectionIcon(RefreshCollectionIconEvent event) {
        refreshCollectionIcon();
    }

    /**
     * 每次显示8个
     */
    private void showHotWord() {
        int start, end;
        if (times < tagList.size() && times + 8 <= tagList.size()) {
            start = times;
            end = times + 8;
        } else if (times < tagList.size() - 1 && times + 8 > tagList.size()) {
            start = times;
            end = tagList.size() - 1;
        } else {
            start = 0;
            end = tagList.size() > 8 ? 8 : tagList.size();
        }
        times = end;
        if (end - start > 0) {
            List<String> batch = tagList.subList(start, end);
            List<TagColor> colors = TagColor.getRandomColors(batch.size());
            mTagGroup.setTags(colors, (String[]) batch.toArray(new String[batch.size()]));
        }
    }

    @Override
    public void showHotReview(List<HotReview.Reviews> list) {
//        mHotReviewList.clear();
//        mHotReviewList.addAll(list);
//        mHotReviewAdapter.notifyDataSetChanged();
    }

    @Override
    public void showRecommendBookList(List<RecommendBookList.RecommendBook> list) {
//        if (!list.isEmpty()) {
//            mTvRecommendBookList.setVisibility(View.VISIBLE);
//            mRecommendBookList.clear();
//            mRecommendBookList.addAll(list);
//            mRecommendBookListAdapter.notifyDataSetChanged();
//        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onItemClick(View view, int position, Object data) {
        if (data instanceof BookDetailBean.CommentListBean) {
            Intent intent = new Intent(this, ReviewDetailActivity.class);
            intent.putExtra("id", ((BookDetailBean.CommentListBean) data).getId() + "");
            intent.putExtra("novelId", bookId);
            startActivity(intent);
        } else if (data instanceof BookDetailBean.GuestListBean) {
            BookDetailBean.GuestListBean bean = (BookDetailBean.GuestListBean) data;
            Intent intent = new Intent(this, BookDetailActivity.class);
            intent.putExtra(INTENT_BOOK_ID, bean.getId() + "");
            startActivity(intent);
        } else if (data instanceof BookDetailBean.RecommendListBean) {
            BookDetailBean.RecommendListBean bean = (BookDetailBean.RecommendListBean) data;
            Intent intent = new Intent(this, BookDetailActivity.class);
            intent.putExtra(INTENT_BOOK_ID, bean.getId() + "");
            startActivity(intent);
        }
    }

    @OnClick(R.id.btnJoinCollection)
    public void onClickJoinCollection() {
        if (!isJoinedCollections) {
            if (recommendBooks != null) {
                addNovel();
            }
        } else {
            delNovel();
        }
    }

    private void initCollection(boolean coll) {
        if (coll) {
            mBtnJoinCollection.setText(R.string.book_detail_join_collection);
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.book_detail_info_add_img);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mBtnJoinCollection.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.shape_common_btn_solid_normal));
            mBtnJoinCollection.setCompoundDrawables(drawable, null, null, null);
            mBtnJoinCollection.postInvalidate();
            isJoinedCollections = false;
        } else {
            mBtnJoinCollection.setText(R.string.book_detail_remove_collection);
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.book_detail_info_del_img);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mBtnJoinCollection.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.btn_join_collection_pressed));
            mBtnJoinCollection.setCompoundDrawables(drawable, null, null, null);
            mBtnJoinCollection.postInvalidate();
            isJoinedCollections = true;
        }
    }

    @OnClick(R.id.btnRead)
    public void onClickRead() {
        if (TextUtils.isEmpty(ReaderApplication.token)) {
            CoomonApi.showLoginDialog(mContext);
            return;
        }

        if (bean == null) return;

        if (bean.isMember_switch()) {//后台开启了会员模式
            if (bean.isUser_member()) {//用户是会员
                ReadActivity.startActivity(getActivity(), bean.getTitle(), bookId, AppUtils.isTrue(bean.getIs_bookshelf()), bean.getPic(), bean.getAuthor(), bean.getDesc());
            } else {//不是会员
                CoomonApi.showBuyVipDialog(getActivity());
            }
        } else {//没有开启会员开关
            ReadActivity.startActivity(getActivity(), bean.getTitle(), bookId, AppUtils.isTrue(bean.getIs_bookshelf()), bean.getPic(), bean.getAuthor(), bean.getDesc());
        }
    }

    @OnClick(R.id.tvBookListAuthor)
    public void searchByAuthor() {
        String author = mTvAuthor.getText().toString().replaceAll(" ", "");
        SearchByAuthorActivity.startActivity(this, author);
    }

    @OnClick(R.id.tvlongIntro)
    public void collapseLongIntro() {
        if (collapseLongIntro) {
            mTvlongIntro.setMaxLines(20);
            collapseLongIntro = false;
        } else {
            mTvlongIntro.setMaxLines(4);
            collapseLongIntro = true;
        }
    }

    @OnClick(R.id.tvMoreReview)
    public void onClickMoreReview() {
//        BookDetailCommunityActivity.startActivity(this, bookId, mTvBookTitle.getText().toString(), 1);

        Intent intent = new Intent(this, AllReviewActivity.class);
        intent.putExtra("id", bookId);
        startActivity(intent);
    }

    @OnClick(R.id.rlCommunity)
    public void onClickCommunity() {
        BookDetailCommunityActivity.startActivity(this, bookId, mTvBookTitle.getText().toString(), 0);
    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    /**
     * 添加书架
     */
    private void addNovel() {
        RetrofitClient.getInstance().createApi().addNovel(ReaderApplication.token, bookId)
                .compose(RxUtils.<HttpResult<CommonBean>>io_main())
                .subscribe(new BaseObjObserver<CommonBean>(this, "添加中") {
                    @Override
                    protected void onHandleSuccess(CommonBean commonBean) {
                        ToastUtils.showToast(String.format(getString(
                                R.string.book_detail_has_joined_the_book_shelf), recommendBooks.title));
                        initCollection(false);
                        bean.setIs_bookshelf(1);
                    }
                });

    }

    /**
     * 移除书架
     */
    private void delNovel() {
        RetrofitClient.getInstance().createApi().delBook(ReaderApplication.token, bookId)
                .compose(RxUtils.<HttpResult<CommonBean>>io_main())
                .subscribe(new BaseObjObserver<CommonBean>(this, "删除中") {
                    @Override
                    protected void onHandleSuccess(CommonBean commonBean) {
                        ToastUtils.showToast(String.format(getString(
                                R.string.book_detail_has_remove_the_book_shelf), recommendBooks.title));
                        initCollection(true);
                        bean.setIs_bookshelf(0);
                    }
                });

    }

    public void fetchAd() {

        BaiduNative baidu = new BaiduNative(getActivity(), "2058628", new BaiduNative.BaiduNativeNetworkListener() {
            @Override
            public void onNativeFail(NativeErrorCode arg0) {
                Log.e("gy", "onNativeFail:" + arg0.name());
                rlAd.setVisibility(View.GONE);
            }

            @Override
            public void onNativeLoad(final List<NativeResponse> arg0) {
                try {

                    if (arg0 != null && arg0.size() > 0) {
                        rlAd.setVisibility(View.VISIBLE);
                        Random rand = new Random();
                        final int num = rand.nextInt(arg0.size());
                        tvAdDes.setText(arg0.get(num).getDesc());
                        tvAdTitle.setText(arg0.get(num).getTitle());
                        Glide.with(BookDetailActivity.this).load(arg0.get(num).getIconUrl()).error(R.drawable.cover_default).into(imgAd);

                        rlAd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                arg0.get(num).handleClick(v);
                            }
                        });
                    }

                } catch (Exception e) {

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
