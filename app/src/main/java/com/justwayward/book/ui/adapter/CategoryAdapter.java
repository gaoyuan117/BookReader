package com.justwayward.book.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.justwayward.book.R;
import com.justwayward.book.bean.CategoryListBean;
import com.justwayward.book.utils.LogUtils;

import java.util.List;

/**
 * Created by gaoyuan on 2017/11/20.
 */

public class CategoryAdapter extends BaseQuickAdapter<CategoryListBean.DataBean, BaseViewHolder> {

    public CategoryAdapter(@LayoutRes int layoutResId, @Nullable List<CategoryListBean.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CategoryListBean.DataBean item) {

        ImageView imgCover = helper.getView(R.id.ivSubCateCover);
        Glide.with(mContext).load(item.getPic()).error(R.drawable.cover_default).into(imgCover);
        String percent = "0";
        if (item.getCollect_num() != 0 && item.getView_num() != 0) {
            double d = (item.getCollect_num() * 100 / item.getView_num());
            percent = String.format("%.0f", d);
        }


        helper.setText(R.id.tvSubCateTitle, item.getTitle())
                .setText(R.id.tvSubCateAuthor, (item.getAuthor() == null ? "未知" : item.getAuthor()) + " | " + (item.getLabels() == null ? "未知" : item.getLabels()))
                .setText(R.id.tvSubCateShort, item.getDesc())
                .setText(R.id.tvSubCateMsg, String.format(mContext.getResources().getString(R.string.category_book_msg),
                        item.getView_num(),
                        TextUtils.isEmpty(item.getCollect_num() + "") ? "0" : percent));
    }
}
