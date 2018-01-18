package com.justwayward.book.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.justwayward.book.R;
import com.justwayward.book.bean.ReviewBean;
import com.justwayward.book.utils.FormatUtils;
import com.justwayward.book.view.XLHRatingBar;

import java.util.List;

/**
 * Created by gaoyuan on 2017/11/16.
 */

public class AllReviewAdapter extends BaseQuickAdapter<ReviewBean.DataBean, BaseViewHolder> {

    public AllReviewAdapter(@LayoutRes int layoutResId, @Nullable List<ReviewBean.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ReviewBean.DataBean item) {

        ImageView imageView = helper.getView(R.id.ivBookCover);
        Glide.with(mContext).load(item.getAvatar()).error(R.drawable.avatar_default).into(imageView);

        helper.setText(R.id.tvBookTitle, item.getUser_nickname())
                .setText(R.id.tvTime, FormatUtils.getDescriptionTimeFromDateString(item.getAdd_time() + "000"))
                .setText(R.id.tvTitle, item.getTitle())
                .setText(R.id.tvContent, String.valueOf(item.getComment()))
                .setText(R.id.tvHelpfulYes, String.valueOf(item.getPraise_num()));
        helper.setVisible(R.id.tvTime, true);
        XLHRatingBar ratingBar = helper.getView(R.id.rating);
        ratingBar.setCountSelected(item.getScore());
    }
}
