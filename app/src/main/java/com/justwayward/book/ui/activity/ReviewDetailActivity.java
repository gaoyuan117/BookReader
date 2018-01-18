package com.justwayward.book.ui.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.justwayward.book.R;
import com.justwayward.book.ReaderApplication;
import com.justwayward.book.api.CoomonApi;
import com.justwayward.book.base.BaseActivity;
import com.justwayward.book.bean.CommonBean;
import com.justwayward.book.bean.ReviewDetailBean;
import com.justwayward.book.component.AppComponent;
import com.justwayward.book.retrofit.BaseObjObserver;
import com.justwayward.book.retrofit.HttpResult;
import com.justwayward.book.retrofit.RetrofitClient;
import com.justwayward.book.retrofit.RxUtils;
import com.justwayward.book.ui.adapter.ReviewDetailAdapter;
import com.justwayward.book.utils.FormatUtils;
import com.justwayward.book.utils.ToastUtils;
import com.justwayward.book.view.recyclerview.decoration.DividerDecoration;
import com.yuyh.easyadapter.glide.GlideCircleTransform;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class ReviewDetailActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;

    private View headView;
    private ImageView imgAvatar, imgClick;
    private ImageView imgMakeComment;
    private TextView tvAuthor;
    private TextView tvTime;
    private TextView tvTitle;
    private TextView tvContent;
    private TextView tvCommentCount;

    private String id;
    private String novelId;
    private ReviewDetailAdapter mAdapter;
    private List<ReviewDetailBean.ReplyListBean> mList = new ArrayList<>();
    private int page = 1;
    ReviewDetailBean bean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_review_detail;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        id = getIntent().getStringExtra("id");
        novelId = getIntent().getStringExtra("novelId");
        Log.e("gy", "小说ID：" + novelId);
        if (TextUtils.isEmpty(id)) {
            finish();
            return;
        }

        headView = View.inflate(this, R.layout.head_review_detail, null);
        imgAvatar = (ImageView) headView.findViewById(R.id.ivAuthorAvatar);
        tvAuthor = (TextView) headView.findViewById(R.id.tvBookAuthor);
        tvTime = (TextView) headView.findViewById(R.id.tvTime);
        tvTitle = (TextView) headView.findViewById(R.id.tvTitle);
        tvContent = (TextView) headView.findViewById(R.id.tvContent);
        tvCommentCount = (TextView) headView.findViewById(R.id.tvCommentCount);
        imgMakeComment = (ImageView) headView.findViewById(R.id.img_make_comment);
        imgClick = (ImageView) headView.findViewById(R.id.img_click);
        imgMakeComment.setOnClickListener(this);
        imgClick.setOnClickListener(this);

        mAdapter = new ReviewDetailAdapter(R.layout.item_review_list, mList);
        mAdapter.addHeaderView(headView);
        mAdapter.setEnableLoadMore(true);
        mAdapter.setOnLoadMoreListener(this, recyclerview);
        mAdapter.disableLoadMoreIfNotFullPage();
        refresh.setOnRefreshListener(this);
        DividerDecoration itemDecoration = new DividerDecoration(ContextCompat.getColor(this, R.color.common_divider_narrow), 1, 0, 0);
        itemDecoration.setDrawLastItem(false);
        recyclerview.addItemDecoration(itemDecoration);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(mAdapter);
    }

    @Override
    public void initToolBar() {
        mCommonToolbar.setTitle("书评详情");
        mCommonToolbar.setNavigationIcon(R.drawable.ab_back);

    }

    @Override
    public void initDatas() {
        getReviewDetail();
    }

    @Override
    public void configViews() {

    }

    /**
     * 获取书评详情
     */
    private void getReviewDetail() {
        RetrofitClient.getInstance().createApi().getCommentInfo(ReaderApplication.token, id)
                .compose(RxUtils.<HttpResult<ReviewDetailBean>>io_main())
                .subscribe(new BaseObjObserver<ReviewDetailBean>(this, refresh) {
                    @Override
                    protected void onHandleSuccess(ReviewDetailBean reviewDetailBean) {
                        bean = reviewDetailBean;
                        setData();

                    }

                });
    }

    private void setData() {
        Glide.with(mContext)
                .load(bean.getAvatar())
                .error(R.drawable.avatar_default)
                .transform(new GlideCircleTransform(mContext))
                .into(imgAvatar);

        tvAuthor.setText(bean.getUser_nickname());
        tvTime.setText(FormatUtils.getDescriptionTimeFromDateString(bean.getAdd_time() + "000"));
        tvTitle.setText(bean.getTitle());
        tvContent.setText(bean.getComment());

        if (page == 1) {
            mList.clear();
        }
        if (bean.getReply_list() == null || bean.getReply_list().size() == 0) {
            mAdapter.loadMoreEnd();
            mAdapter.notifyDataSetChanged();
            return;
        }
        tvCommentCount.setText(String.format(mContext.getString(R.string.comment_comment_count), bean.getReply_list().size()));

        mList.addAll(bean.getReply_list());
        mAdapter.notifyDataSetChanged();

        if (bean.getIs_praise() == 1) {//点赞过
            imgClick.setImageResource(R.mipmap.zan_true);
        } else {
            imgClick.setImageResource(R.mipmap.zan_false);
        }
    }


    @Override
    public void onLoadMoreRequested() {
        page++;
        getReviewDetail();
    }

    @Override
    public void onRefresh() {
        page = 1;
        getReviewDetail();
    }

    @Override
    public void onClick(View view) {
        if (!CoomonApi.isLogin(this)) return;

        switch (view.getId()) {
            case R.id.img_make_comment:
                Intent intent = new Intent(this, MakeCommentActivity.class);
                intent.putExtra("novelId", novelId);
                intent.putExtra("id", id);
                startActivity(intent);
                break;

            case R.id.img_click:
                click();
                break;
        }
    }

    /**
     * 点赞
     */
    private void click() {
        RetrofitClient.getInstance().createApi().praise(ReaderApplication.token, novelId, id)
                .compose(RxUtils.<HttpResult<CommonBean>>io_main())
                .subscribe(new BaseObjObserver<CommonBean>(this) {
                    @Override
                    protected void onHandleSuccess(CommonBean commonBean) {
                        if (bean.getIs_praise() == 1) {//点赞过
                            imgClick.setImageResource(R.mipmap.zan_false);
                            showToastMsg("取消点赞");
                            bean.setIs_praise(0);
                        } else {
                            imgClick.setImageResource(R.mipmap.zan_true);
                            showToastMsg("点赞成功");
                            bean.setIs_praise(1);
                        }
                    }
                });
    }
}
