package com.justwayward.book.ui.activity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
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
import com.justwayward.book.utils.ToastUtils;

import butterknife.Bind;
import butterknife.OnClick;

public class EditReviewActivity extends BaseActivity implements RatingBar.OnRatingBarChangeListener {

    @Bind(R.id.ratingBar)
    RatingBar ratingBar;
    @Bind(R.id.et_edit_title)
    EditText etEditTitle;
    @Bind(R.id.et_edit_coment)
    EditText etEditComent;
    @Bind(R.id.tv_edit)
    TextView tvEdit;

    private String id;
    private float rating;


    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_review;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        id = getIntent().getStringExtra("id");
        if (TextUtils.isEmpty(id)) {
            finish();
        }
    }

    @Override
    public void initToolBar() {
        mCommonToolbar.setNavigationIcon(R.drawable.ab_back);
        mCommonToolbar.setTitle("我要评论");
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews() {
        ratingBar.setMax(5);
        ratingBar.setOnRatingBarChangeListener(this);
    }

    @OnClick({R.id.ratingBar, R.id.tv_edit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ratingBar:

                break;
            case R.id.tv_edit:
                editReview();
                break;
        }
    }


    @Override
    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
        rating = v;

        Log.e("gy", "评分：" + rating);
    }

    /**
     * 写书评
     */
    private void editReview() {

        String title = etEditTitle.getText().toString();
        String comment = etEditComent.getText().toString();

        if (rating <= 0) {
            showToastMsg("请为该书籍评分");
            return;
        }

        if (TextUtils.isEmpty(comment)) {
            showToastMsg("请输入正文");
            return;
        }

        RetrofitClient.getInstance().createApi().addComment(ReaderApplication.token, id, rating + "", title, comment)
                .compose(RxUtils.<HttpResult<CommonBean>>io_main())
                .subscribe(new BaseObjObserver<CommonBean>(this, "发表中") {
                    @Override
                    protected void onHandleSuccess(CommonBean commonBean) {
                        ToastUtils.showToast("书评发表成功");
                        finish();
                    }
                });
    }

}
