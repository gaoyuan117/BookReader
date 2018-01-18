package com.justwayward.book.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.justwayward.book.R;
import com.justwayward.book.bean.TopDetailBean;

import java.util.List;

/**
 * Created by gaoyuan on 2017/11/30.
 */

public class TopDetailAdapter extends BaseQuickAdapter<TopDetailBean, BaseViewHolder> {

    public TopDetailAdapter(@LayoutRes int layoutResId, @Nullable List<TopDetailBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TopDetailBean item) {

        ImageView imgCover = helper.getView(R.id.ivSubCateCover);
        Glide.with(mContext).load(item.getPic()).error(R.drawable.cover_default).into(imgCover);

        helper.setText(R.id.tvSubCateTitle, item.getTitle())
                .setText(R.id.tvSubCateAuthor, (item.getAuthor() == null ? "未知" : item.getAuthor()) + " | " + (item.getLabels() == null ? "未知" : item.getLabels()))
                .setText(R.id.tvSubCateShort, item.getDesc())
                .setText(R.id.tvSubCateMsg, String.format(mContext.getResources().getString(R.string.category_book_msg),
                        item.getCollect_num(),
                        TextUtils.isEmpty(item.getView_num() + "") ? "0" : item.getView_num() + ""));
    }
}
