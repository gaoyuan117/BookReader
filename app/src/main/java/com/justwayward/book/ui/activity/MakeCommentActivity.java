package com.justwayward.book.ui.activity;

import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.justwayward.book.R;
import com.justwayward.book.ReaderApplication;
import com.justwayward.book.base.BaseActivity;
import com.justwayward.book.bean.CommonBean;
import com.justwayward.book.component.AppComponent;
import com.justwayward.book.retrofit.BaseObjObserver;
import com.justwayward.book.retrofit.HttpResult;
import com.justwayward.book.retrofit.RetrofitClient;
import com.justwayward.book.retrofit.RxUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 发表评论
 */
public class MakeCommentActivity extends BaseActivity {

    @Bind(R.id.et_comment)
    EditText etComment;
    @Bind(R.id.tv_all_review_edit)
    TextView tvAllReviewEdit;

    private String novelId;
    private String id;

    @Override
    public int getLayoutId() {
        return R.layout.activity_make_comment;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        novelId = getIntent().getStringExtra("novelId");
        id = getIntent().getStringExtra("id");
        Log.e("gy", "小说ID：" + novelId);
    }

    @Override
    public void initToolBar() {
        mCommonToolbar.setNavigationIcon(R.drawable.ab_back);
        mCommonToolbar.setTitle("回复");
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews() {

    }

    @OnClick(R.id.tv_all_review_edit)
    public void onViewClicked() {
        addComment();
    }

    private void addComment() {
        String comment = etComment.getText().toString();
        if (TextUtils.isEmpty(comment)) {
            showToastMsg("请输入回复内容");
            return;
        }
        RetrofitClient.getInstance().createApi().addReply(ReaderApplication.token, id, novelId, comment)
                .compose(RxUtils.<HttpResult<CommonBean>>io_main())
                .subscribe(new BaseObjObserver<CommonBean>(this, "评论中") {
                    @Override
                    protected void onHandleSuccess(CommonBean commonBean) {
                        showToastMsg("评论成功");
                        finish();
                    }
                });
    }


}
