package com.justwayward.book.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.justwayward.book.R;
import com.justwayward.book.bean.ReviewDetailBean;
import com.justwayward.book.utils.FormatUtils;
import com.yuyh.easyadapter.glide.GlideCircleTransform;

import java.util.List;

/**
 * Created by gaoyuan on 2017/11/16.
 */

public class ReviewDetailAdapter extends BaseQuickAdapter<ReviewDetailBean.ReplyListBean,BaseViewHolder>{

    public ReviewDetailAdapter(@LayoutRes int layoutResId, @Nullable List<ReviewDetailBean.ReplyListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ReviewDetailBean.ReplyListBean item) {
        ImageView imageView = helper.getView(R.id.ivBookCover);
        Glide.with(mContext)
                .load(item.getAvatar())
                .placeholder(R.drawable.avatar_default)
                .transform(new GlideCircleTransform(mContext))
                .into(imageView);

        helper.setText(R.id.tvBookTitle,item.getUser_nickname())
                .setText(R.id.tvContent,item.getContent())
                .setText(R.id.tvTime, FormatUtils.getDescriptionTimeFromDateString(item.getAdd_time()+"000"));

    }
}
